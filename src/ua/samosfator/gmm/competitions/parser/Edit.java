package ua.samosfator.gmm.competitions.parser;

import java.time.LocalDateTime;

public class Edit {
    private String name;
    private String address;
    private String category;
    private String status;
    private LocalDateTime date;
    private String link;
    private String thumbnailLink;

    private String authorName;
    private String authorUID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date.toString().replace("T", " ");
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUID() {
        return authorUID;
    }

    public void setAuthorUID(String authorUID) {
        this.authorUID = authorUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edit)) return false;

        Edit edit = (Edit) o;

        if (!address.equals(edit.address)) return false;
        if (!authorName.equals(edit.authorName)) return false;
        if (!authorUID.equals(edit.authorUID)) return false;
        if (!category.equals(edit.category)) return false;
        if (!date.equals(edit.date)) return false;
        if (!link.equals(edit.link)) return false;
        if (!name.equals(edit.name)) return false;
        if (!status.equals(edit.status)) return false;
        if (!thumbnailLink.equals(edit.thumbnailLink)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + thumbnailLink.hashCode();
        result = 31 * result + authorName.hashCode();
        result = 31 * result + authorUID.hashCode();
        return result;
    }
}
