package ua.samosfator.gmm.competitions.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;

public class User {
    private HashSet<String> badges = new HashSet<>();
    private String name, photoLink, uid;
    private int days, totalEdits, approved, reviews, poi, businessListings, featureEdits;
    private double roadLength, regions;

    public User(String uid) {
        this.uid = uid;
    }

    public HashSet<String> getBadges() {
        return badges;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getTotalEdits() {
        return totalEdits;
    }

    public void setTotalEdits(int totalEdits) {
        this.totalEdits = totalEdits;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public double getRoadLength() {
        return roadLength;
    }

    public void setRoadLength(double roadLength) {
        this.roadLength = roadLength;
    }

    public int getPoi() {
        return poi;
    }

    public void setPoi(int poi) {
        this.poi = poi;
    }

    public int getBusinessListings() {
        return businessListings;
    }

    public void setBusinessListings(int businessListings) {
        this.businessListings = businessListings;
    }

    public double getRegions() {
        return regions;
    }

    public void setRegions(double regions) {
        this.regions = regions;
    }

    public int getFeatureEdits() {
        return featureEdits;
    }

    public void setFeatureEdits(int featureEdits) {
        this.featureEdits = featureEdits;
    }

    public User setFullInfo() {
        Document doc = null;
        try {
            doc = Jsoup.connect(new URL(URL.Tabs.MAIN, uid).getUrl()).timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            setUid(uid);
            setStat(doc);
            setName(doc);
            setBadges(doc);
            setPhoto(doc);
        }
        return this;
    }

    private void setStat(Document doc) {
        Elements statString = doc.select("#panel0").select(".time_period_large");
        Elements statTable = doc.select("#div-mini-dashboard-expanded").select("tr");
        String[][] rawStat = parseStatString(statString.text());

        for (String[] aRawStat : rawStat) {
            if (aRawStat[1].contains("days")) setDays(Integer.parseInt(aRawStat[0]));
            else if (aRawStat[1].contains("total")) setTotalEdits(Integer.parseInt(aRawStat[0]));
            else if (aRawStat[1].contains("approved")) setApproved(Integer.parseInt(aRawStat[0]));
            else if (aRawStat[1].contains("reviews")) setReviews(Integer.parseInt(aRawStat[0]));
        }

        for (Element e : statTable) {
            if (e.text().contains("Road length")) setRoadLength(Double.parseDouble(e.select("td:eq(1)").text()));
            if (e.text().contains("Points of interest")) setPoi(Integer.parseInt(e.select("td:eq(1)").text()));
            if (e.text().contains("Business listings"))
                setBusinessListings(Integer.parseInt(e.select("td:eq(1)").text()));
            if (e.text().contains("Regions")) setRegions(Double.parseDouble(e.select("td:eq(1)").text()));
            if (e.text().contains("Feature edits")) setFeatureEdits(Integer.parseInt(e.select("td:eq(1)").text()));
        }
    }

    private String[][] parseStatString(String raw) {
        raw = raw.substring(1).replaceAll("\u00A0", " ");
        String[] splitByComma = raw.split(",");
        String[][] parsedArr = new String[splitByComma.length][2];

        for (int i = 0; i < splitByComma.length; i++) {
            splitByComma[i] = splitByComma[i].trim();
            String[] splitBySpace = splitByComma[i].split(" ");
            System.arraycopy(splitBySpace, 0, parsedArr[i], 0, 2);
        }

        return parsedArr;
    }

    private void setName(Document doc) {
        Element name = doc.select("#leftpanel").select(".profile-name").first();
        setName(name.text());
    }

    private void setBadges(Document doc) {
        Elements badgesEl = doc.select("#leftpanel").select(".gw-badges-section a img");
        badges.addAll(badgesEl.stream().map(Element::className).collect(Collectors.toList()));
    }

    private void setPhoto(Document doc) {
        Element photo = doc.select("#leftpanel").select("img").first();
        setPhotoLink(photo.attr("src"));
    }

    public static int[] stringArrToInt(String[] arr) {
        int[] newArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            try {
                newArr[i] = Integer.parseInt(arr[i]);
            } catch (NumberFormatException e) {
                newArr[i] = -1;
                e.printStackTrace();
            }
        }
        return newArr;
    }

    @Override
    public String toString() {
        return "User: " + getUid() + "\n" +
                "Name: " + getName() + "\n" +
                "Photo: " + getPhotoLink() + "\n" +
                "Days: " + getDays() + "\n" +
                "Total edits: " + getTotalEdits() + "\n" +
                "Approved edits: " + getApproved() + "\n" +
                "Reviews: " + getReviews() + "\n" +
                "POI: " + getPoi() + "\n" +
                "Business listings: " + getBusinessListings() + "\n" +
                "Feature edits: " + getFeatureEdits() + "\n" +
                "Road length: " + getRoadLength() + "\n" +
                "Regions: " + getRegions() + "\n" +
                "Badges: " + badges;
    }
}
