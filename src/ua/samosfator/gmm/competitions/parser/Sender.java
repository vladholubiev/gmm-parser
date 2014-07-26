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

    public void prepare() throws ServiceException, IOException {
        service = new SpreadsheetService("Print Google Spreadsheet Demo");
        service.setUserCredentials(Config.GOOGLE_ACCOUNT_USERNAME, Config.GOOGLE_ACCOUNT_PASSWORD);
        URL metaFeedUrl = new URL(Config.SPREADSHEET_URL);
        SpreadsheetEntry spreadsheet = service.getEntry(metaFeedUrl, SpreadsheetEntry.class);
        listFeedUrl = spreadsheet.getWorksheets().get(0).getListFeedUrl();
    }

    public HashSet<String> read(String column) throws ServiceException, IOException {
        ListFeed feed = service.getFeed(listFeedUrl, ListFeed.class);
        return (HashSet<String>) feed.getEntries().stream().map(entry -> entry.getCustomElements().getValue(column)).collect(Collectors.toSet());
    }

    public void write(HashSet<Edit> edits) throws ServiceException, IOException {
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

            service.insert(listFeedUrl, row);
            System.out.println("edit: " + date);
        }
    }
}
