package com.ntnu.mobiledev.mj.lab2rssreader;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markusja on 2/12/18.
 */

public class RSSHandler extends DefaultHandler {
    private List<FeedItem> rssItems;
    private int limit;
    private FeedItem currentItem;

    RSSHandler(int limit){
        rssItems = new ArrayList();
        this.limit = limit;
    }

    List<FeedItem> getRssItems(){
        rssItems = rssItems.subList(0, limit);
        return this.rssItems;
    }

    private boolean parsingTitle;
    private boolean parsingLink;
    private boolean parsingDescription;
    private boolean parsingPub;



    // The StartElement method creates an empty RssItem object when an item start tag is being processed. When a title or link tag are being processed appropriate indicators are set to true.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("item".equals(qName)) {
            currentItem = new FeedItem();
        } else if ("title".equals(qName)) {
            parsingTitle = true;
        } else if ("link".equals(qName)) {
            parsingLink = true;
        } else if ("description".equals(qName)){
            parsingDescription = true;
        } else if ("pubDate".equals(qName)) {
            parsingPub = true;
        }
    }
    // The EndElement method adds the  current RssItem to the list when a closing item tag is processed. It sets appropriate indicators to false -  when title and link closing tags are processed
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("item".equals(qName)) {
            rssItems.add(currentItem);
            currentItem = null;
        } else if ("title".equals(qName)) {
            parsingTitle = false;
        } else if ("link".equals(qName)) {
            parsingLink = false;
        } else if ("description".equals(qName)) {
            parsingDescription = false;
        } else if ("pubDate".equals(qName)) {
            parsingPub = false;
        }
    }
    // Characters method fills current RssItem object with data when title and link tag content is being processed
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (parsingTitle) {
            if (currentItem != null)
                currentItem.setTitle(new String(ch, start, length));
        } else if (parsingLink) {
            if (currentItem != null) {
                currentItem.setLink(new String(ch, start, length));
                parsingLink = false;
            }
        } else if (parsingDescription) {
            if(currentItem != null) {
                currentItem.setDescription(new String(ch, start, length));
                parsingDescription = false;
            }
        } else if (parsingPub) {
            if(currentItem != null) {
                currentItem.setPubDate(new String(ch, start, length));
                parsingPub = false;
            }
        }
    }
}
