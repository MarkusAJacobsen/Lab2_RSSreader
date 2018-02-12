package com.ntnu.mobiledev.mj.lab2rssreader;

/**
 * Created by markusja on 2/12/18.
 */

public class FeedItem {
    String title;
    String description;
    String link;
    String author;
    String pubDate;

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setLink(String link) {
        this.link = link;
    }

    void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString(){
        return title + "\nDescription=" + description
                + "\nLink=" + link + "\nAuthor=" + author + " Pub: " + pubDate;
    }
}

