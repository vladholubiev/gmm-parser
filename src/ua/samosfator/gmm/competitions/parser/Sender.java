package ua.samosfator.gmm.competitions.parser;

import com.google.common.base.CaseFormat;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Sender {
    private SpreadsheetService service;
    private URL listFeedUrl;

    public Sender prepare() {
        try {
            service = new SpreadsheetService("Google Map Maker Competitions");
            service.setUserCredentials(Config.GOOGLE_ACCOUNT_USERNAME, Config.GOOGLE_ACCOUNT_PASSWORD);
            URL metaFeedUrl = new URL(Config.SPREADSHEET_URL);
            SpreadsheetEntry spreadsheet = service.getEntry(metaFeedUrl, SpreadsheetEntry.class);
            listFeedUrl = spreadsheet.getWorksheets().get(Config.SPREADSHEET_WORKSHEET).getListFeedUrl();
        } catch (IOException | ServiceException e) {
            e.printStackTrace();
        }
        return this;
    }

    public HashSet<String> read(String column) throws ServiceException, IOException {
        ListFeed feed = service.getFeed(listFeedUrl, ListFeed.class);
        return (HashSet<String>) feed.getEntries().stream().map(entry -> entry.getCustomElements().getValue(column)).collect(Collectors.toSet());
    }

    public void write(HashSet<Edit> edits) {
        ListEntry row = new ListEntry();
        for (Edit edit : edits) {
            for (Method method : Edit.class.getMethods()) {
                String methodName = method.getName();

                if (methodName.startsWith("get")) {
                    String columnName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, methodName.replace("get", ""));

                    try {
                        String value = String.valueOf(method.invoke(edit));

                        if (columnName.equals("authorUID")) {
                            value += "'" + value;
                        }
                        row.getCustomElements().setValueLocal(columnName, value);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

            tryInsertRow(row);

            System.out.println(edit.getAuthorName() + ":" + edit.getDate());
        }
    }

    public void write(User user) {
        ListEntry row = new ListEntry();

        for (Method method : User.class.getMethods()) {
            String methodName = method.getName();

            if (!methodName.equals("getBadges") && methodName.startsWith("get")) {
                String columnName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, methodName.replace("get", ""));

                try {
                    String value = String.valueOf(method.invoke(user));

                    if (columnName.equals("uid")) {
                        value += "'" + value;
                    }
                    row.getCustomElements().setValueLocal(columnName, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        tryInsertRow(row);

        System.out.println("user: " + user.getName());
    }

    private void tryInsertRow(ListEntry row) {
        try {
            service.insert(listFeedUrl, row);
        } catch (IOException | ServiceException e) {
            e.printStackTrace();
        }
    }
}
