package ua.samosfator.gmm.competitions.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

public class EditDate {
    private LocalDateTime date;

    public EditDate(String unparsedString) {
        parseDate(trimToDate(unparsedString));
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    private void parseDate(String unparsedString) {
        if (unparsedString.contains("ago")) {
            setDate(parseDateAgo(unparsedString));
        } else setDate(parseDateRegular(unparsedString));
    }

    private LocalDateTime parseDateRegular(String s) {
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.US);
        java.util.Date parsed = null;
        try {
            parsed = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return LocalDateTime.ofInstant(parsed.toInstant(), ZoneId.systemDefault());
    }

    private LocalDateTime parseDateAgo(String s) {
        String[] str = s.split(" ");
        int hours = 0;
        int min = 0;
        int sec = 0;
        if (s.contains("hours")) {
            hours = Integer.parseInt(str[0]);
            min = Integer.parseInt(str[2]);
        } else {
            min = Integer.parseInt(str[0]);
            sec = Integer.parseInt(str[2]);
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.minusHours(hours).minusMinutes(min).minusSeconds(sec);
    }

    private String trimToDate(String raw) {
        return raw.replace("Added ", "").replace("Changed ", "").replace("Deleted ", "").replace("on ", "")
                .replace("pm", " pm").replace("am", " am");
    }
}
