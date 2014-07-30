package ua.samosfator.gmm.competitions.parser;

import com.google.gdata.util.ServiceException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;

public class Parser {
    private LinkedHashSet<User> users = new LinkedHashSet<>();
    private LinkedHashSet<Edit> prevEdits = new LinkedHashSet<>(5, 1);
    private final int MAX_VISIBLE_EDITS = 2000;
    private Sender sender;
    private String parsedUsers = "";
    private String url = "";
    private int startPos;

    public Parser(LinkedHashSet<User> users) {
        this.users = users;

        Config.setConfig();

        try {
            sender = new Sender().prepare();
        } catch (ServiceException | IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        for (User user : users) {
            boolean isUserParsed = Config.PARSED_USERS.contains(user.getUid());

            if (!isUserParsed) startPos = 0;
            else if (Config.START_POS != 0) startPos = Config.START_POS;
            else continue;

            while (startPos < MAX_VISIBLE_EDITS) parseNextPage(user);

            saveParsedUsers(user);
        }
    }

    private void parseNextPage(User user) {
        try {
            url = new URL(URL.Tabs.EDITS, user.getUid(), startPos).getUrl();
            LinkedHashSet<Edit> currentEdits = createEdits(Jsoup.connect(url).timeout(0).get(), user);

            if (isSameEdits(currentEdits, prevEdits)) return;
            else {
                prevEdits = new LinkedHashSet<>(currentEdits);
                sender.write(currentEdits);
            }
            startPos += 5;
        } catch (Exception e) {
            saveStartPos(startPos);
            System.out.println(e.getMessage());
        }
    }

    private LinkedHashSet<Edit> createEdits(Document doc, User user) {
        LinkedHashSet<Edit> edits = new LinkedHashSet<>(5, 1);

        Elements names = doc.select("span#sxtitle");
        Elements addresses = doc.select("div.gw-card-address").select("span");
        Elements categories = doc.select("span.gw-ftcard-category");
        Elements status = doc.select("span[class^=stat]");
        Elements dates = doc.select("div.gw-ftcard-links").select("span");
        Elements links = doc.getElementsByAttributeValue("cad", "src:pp-edit-link");
        Elements thumbnailLinks = doc.select("img.map-thumbnail");
        Element authorName = doc.select(".profile-name").first();

        assert (names.size() + addresses.size() + categories.size() + status.size() + dates.size() +
                links.size() + thumbnailLinks.size()) % 7 == 0;

        for (int i = 0; i < names.size(); i++) {
            if (EditDate.isInRange(new EditDate(dates.get(i).text()).getDate())) {
                Edit edit = new Edit();
                edit.setName(names.get(i).text());
                edit.setAddress(addresses.get(i).attr("title"));
                edit.setCategory(categories.get(i).text());
                edit.setStatus(status.get(i).className().substring(5));
                edit.setDate(new EditDate(dates.get(i).text()).getDate());
                edit.setLink(links.get(i).attr("href"));
                edit.setThumbnailLink("http:" + thumbnailLinks.get(i).attr("src"));
                edit.setAuthorName(authorName.text());
                edit.setAuthorUID(user.getUid());
                edits.add(edit);
            }
        }
        return edits;
    }

    private void saveParsedUsers(User user) {
        parsedUsers = Config.PARSED_USERS + parsedUsers + user.getUid() + ", ";

        Properties prop = new Properties();
        try (FileOutputStream output = new FileOutputStream("config.properties")) {
            prop.setProperty("GOOGLE_ACCOUNT_USERNAME", Config.GOOGLE_ACCOUNT_USERNAME);
            prop.setProperty("GOOGLE_ACCOUNT_PASSWORD", Config.GOOGLE_ACCOUNT_PASSWORD);
            prop.setProperty("SPREADSHEET_URL", Config.SPREADSHEET_URL);
            prop.setProperty("SPREADSHEET_WORKSHEET", String.valueOf(Config.SPREADSHEET_WORKSHEET));
            prop.setProperty("FROM", Config.FROM.format(EditDate.configDateFormat));
            prop.setProperty("TO", Config.TO.format(EditDate.configDateFormat));

            prop.setProperty("PARSED_USERS", parsedUsers);
            //Set Start position to 0, because next non-parsed user must be parsed from the beginning
            prop.setProperty("START_POS", "0");
            prop.store(output, "Interrupted position");
            //Refresh configs. Necessary
            Config.setConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveStartPos(int startPos) {
        Properties prop = new Properties();
        try (FileOutputStream output = new FileOutputStream("config.properties")) {
            prop.setProperty("GOOGLE_ACCOUNT_USERNAME", Config.GOOGLE_ACCOUNT_USERNAME);
            prop.setProperty("GOOGLE_ACCOUNT_PASSWORD", Config.GOOGLE_ACCOUNT_PASSWORD);
            prop.setProperty("SPREADSHEET_URL", Config.SPREADSHEET_URL);
            prop.setProperty("SPREADSHEET_WORKSHEET", String.valueOf(Config.SPREADSHEET_WORKSHEET));
            prop.setProperty("PARSED_USERS", Config.PARSED_USERS + parsedUsers);
            prop.setProperty("FROM", Config.FROM.format(EditDate.configDateFormat));
            prop.setProperty("TO", Config.TO.format(EditDate.configDateFormat));

            prop.setProperty("START_POS", String.valueOf(startPos));
            prop.store(output, "Interrupted position");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Map Maker issue: reaching the last page with user edits it keeps on showing same last edits upon
     * page number equals 401. This method checks if last set of edits equals previous.
     */
    private boolean isSameEdits(LinkedHashSet<Edit> current, LinkedHashSet<Edit> prev) {
        if (prev.size() == 0) return false;
        for (Iterator<Edit> currIter = current.iterator(), prevIter = prev.iterator(); currIter.hasNext() && prevIter.hasNext(); ) {
            Edit currEdit = currIter.next();
            Edit prevEdit = prevIter.next();

            if (!currEdit.equals(prevEdit)) return false;
        }
        return true;
    }
}
