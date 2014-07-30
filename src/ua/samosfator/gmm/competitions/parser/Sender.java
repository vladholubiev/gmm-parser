package ua.samosfator.gmm.competitions.parser;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
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
            String date = edit.getDate().toString().replace("T", " ");
            row.getCustomElements().setValueLocal("date", date);
            row.getCustomElements().setValueLocal("authorName", edit.getAuthorName());
            row.getCustomElements().setValueLocal("authorUID", "'" + edit.getAuthorUID());
            row.getCustomElements().setValueLocal("name", edit.getName());
            row.getCustomElements().setValueLocal("category", edit.getCategory());
            row.getCustomElements().setValueLocal("link", edit.getLink());
            row.getCustomElements().setValueLocal("address", edit.getAddress());
            row.getCustomElements().setValueLocal("status", edit.getStatus());
            row.getCustomElements().setValueLocal("thumbnailLink", edit.getThumbnailLink());

            tryInsertRow(row);

            System.out.println(edit.getAuthorName() + ": " + date);
        }
    }

    public void write(User user) {
        ListEntry row = new ListEntry();
        row.getCustomElements().setValueLocal("name", user.getName());
        row.getCustomElements().setValueLocal("uid", "'" + user.getUid());
        row.getCustomElements().setValueLocal("totalEdits", String.valueOf(user.getTotalEdits()));
        row.getCustomElements().setValueLocal("approved", String.valueOf(user.getApproved()));
        row.getCustomElements().setValueLocal("reviews", String.valueOf(user.getReviews()));
        row.getCustomElements().setValueLocal("days", String.valueOf(user.getDays()));
        row.getCustomElements().setValueLocal("photoLink", String.valueOf(user.getPhotoLink()));
        row.getCustomElements().setValueLocal("roadLength", String.valueOf(user.getRoadLength()));
        row.getCustomElements().setValueLocal("regions", String.valueOf(user.getRegions()));
        row.getCustomElements().setValueLocal("businessListings", String.valueOf(user.getBusinessListings()));
        row.getCustomElements().setValueLocal("poi", String.valueOf(user.getPoi()));
        row.getCustomElements().setValueLocal("featureEdits", String.valueOf(user.getFeatureEdits()));

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
