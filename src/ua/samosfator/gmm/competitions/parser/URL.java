package ua.samosfator.gmm.competitions.parser;

public class URL {
    private final String url;

    public static enum Tabs {
        MAIN(0),
        EDITS(1),
        REVIEWS(3);

        private int id;

        Tabs(int id) {
            this.id = id;
        }
    }

    public enum Language {
        English("en"),
        Russian("ru"),
        Ukrainian("uk");

        private String code;

        Language(String code) {
            this.code = code;
        }
    }

    //Parameters list keeps to the original MapMaker order
    public URL(Tabs tab, String uid, int startPos, Language l) {
        this.url = "http://www.google.com/mapmaker?gw=66" +
                "&ptab=" + tab.id +
                "&uid=" + uid +
                "&start=" + startPos +
                "&sort=" +
                "&hl=" + l.code;
    }
    public URL(Tabs tab, String uid, int startPos) {
        this(tab, uid, startPos, Language.English);
    }

    public URL(Tabs tab, String uid) {
        this(tab, uid, 0, Language.English);
    }

    public String getUrl() {
        return this.url;
    }
}
