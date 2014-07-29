package ua.samosfator.gmm.competitions.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditDate {
    private LocalDateTime date;
    public static DateTimeFormatter configDateFormat = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

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
        Date parsed = null;
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
        if (s.contains("hours")) {
            hours = Integer.parseInt(str[0]);
            min = Integer.parseInt(str[2]);
        } else {
            min = Integer.parseInt(str[0]);
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        int currSeconds = currentDateTime.getSecond();
        int currNanoSeconds = currentDateTime.getNano();
        return currentDateTime.minusHours(hours).minusMinutes(min).minusSeconds(currSeconds).minusNanos(currNanoSeconds);
    }

    private String trimToDate(String raw) {
        return raw.replace("Added ", "").replace("Changed ", "").replace("Deleted ", "").replace("on ", "")
                .replace("pm", " pm").replace("am", " am");
    }

    public static LocalDateTime parseConfigDate(String s) {
        DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm");
        Date parsed = null;
        try {
            parsed = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return LocalDateTime.ofInstant(parsed.toInstant(), ZoneId.systemDefault());
    }

    public static boolean isInRange(LocalDateTime currDate) {
        return currDate.compareTo(Config.FROM) > 0 && currDate.compareTo(Config.TO) < 0;
    }
}
