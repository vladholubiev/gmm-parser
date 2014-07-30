package ua.samosfator.gmm.competitions.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

public class Config {
    public static String GOOGLE_ACCOUNT_USERNAME = "";
    public static String GOOGLE_ACCOUNT_PASSWORD = "";
    public static String SPREADSHEET_URL = "";
    public static int SPREADSHEET_WORKSHEET;
    public static int START_POS = 0;
    public static String PARSED_USERS = "";
    public static LocalDateTime FROM;
    public static LocalDateTime TO;

    public static void setConfig() {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            GOOGLE_ACCOUNT_USERNAME = prop.getProperty("GOOGLE_ACCOUNT_USERNAME");
            GOOGLE_ACCOUNT_PASSWORD = prop.getProperty("GOOGLE_ACCOUNT_PASSWORD");
            SPREADSHEET_URL = prop.getProperty("SPREADSHEET_URL");
            SPREADSHEET_WORKSHEET = Integer.parseInt(prop.getProperty("SPREADSHEET_WORKSHEET"));
            FROM = EditDate.parseConfigDate(prop.getProperty("FROM"));
            TO = EditDate.parseConfigDate(prop.getProperty("TO"));
            try {
                START_POS = Integer.parseInt(prop.getProperty("START_POS"));
            } catch (NumberFormatException e) { START_POS = 0; }
            PARSED_USERS = prop.getProperty("PARSED_USERS") == null ? "" : prop.getProperty("PARSED_USERS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
