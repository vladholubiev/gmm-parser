package ua.samosfator.gmm.competitions.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
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
            doc = Jsoup.connect(new URL(URL.Tabs.MAIN, uid).getUrl()).get();
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
        Elements stat1 = doc.select("#panel0").select(".time_period_large");
        Elements stat2 = doc.select("#div-mini-dashboard-expanded").select("tr");
        String rawString = stat1.text().substring(1);
        int[] stat = stringArrToInt(rawString.replaceAll("\\D+", ",").split(","));

        setDays(stat[0]);
        setTotalEdits(stat[1]);
        setApproved(stat[2]);
        setReviews(stat[3]);

        for (Element e : stat2) {
            if (e.text().contains("Road length")) setRoadLength(Double.parseDouble(e.select("td:eq(1)").text()));
            if (e.text().contains("Points of interest")) setPoi(Integer.parseInt(e.select("td:eq(1)").text()));
            if (e.text().contains("Business listings")) setBusinessListings(Integer.parseInt(e.select("td:eq(1)").text()));
            if (e.text().contains("Regions")) setRegions(Double.parseDouble(e.select("td:eq(1)").text()));
            if (e.text().contains("Feature edits")) setFeatureEdits(Integer.parseInt(e.select("td:eq(1)").text()));
        }
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
