package com.example.andriod.redditreaderapp.model;

public class Detail {

    private String comment;
    private String author;
    private String updated;
    private String id;

    public Detail(String comment, String author, String updated, String id) {
        this.comment = comment;
        this.author = author;
        this.updated = updated;
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "comment='" + comment + '\'' +
                ", author='" + author + '\'' +
                ", updated='" + updated + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
