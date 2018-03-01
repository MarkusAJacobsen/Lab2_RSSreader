package com.ntnu.mobiledev.mj.lab2rssreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markusja on 2/12/18.
 */

/**
 * The RSS feed consisting of feedItems
 */
public class Feed {
    private final String title;
    private final String link;
    private final String description;
    private final String language;
    private final String copyright;
    private final String pubDate;

    private final List<FeedItem> entries = new ArrayList<FeedItem>();

    /**
     * Constructor
     * @param title String
     * @param link String
     * @param description String
     * @param language String
     * @param copyright String
     * @param pubDate String
     */
    public Feed(String title, String link, String description, String language,
                String copyright, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
    }

    public List<FeedItem> getMessages() {
        return entries;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getPubDate() {
        return pubDate;
    }

    /**
     * Override toString to return correct formatting for a list item
     * @return String
     */
    @Override
    public String toString() {
        return "Feed [copyright=" + copyright + ", description=" + description
                + ", language=" + language + ", link=" + link + ", pubDate="
                + pubDate + ", title=" + title + "]";
    }
}
