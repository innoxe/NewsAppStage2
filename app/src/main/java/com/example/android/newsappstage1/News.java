package com.example.android.newsappstage1;

import java.util.Date;

public class News {
    /**
     * Headline of the news
     */
    private final String mNewsHeadline;

    /**
     * Trail of the news
     */
    private final String mNewsTrailText;

    /**
     * By Author line of the news
     */
    private final String mNewsByline;

    /**
     * Section of the news
     */
    private final String mNewsSectionName;

    /**
     * Publication date of the news
     */
    private final Date mNewsPublicationDate;

    /**
     * Page URL of the news
     */
    private final String mNewsUrl;

    /**
     * Image for the news
     */
    private final String mNewsImage;


    /**
     * Constructs a new {@link News} object.
     *
     * @param newsHeadline          is the headline of the news
     * @param newsTrailText         is the lead of the news
     * @param newsByline            is the name of news author
     * @param newsSectionName       is the section of news
     * @param newsPublicationDate   is the date with hour publiction of news
     * @param newsUrl               is the page URL to find the artcle of news
     * @param newsImage             is the image of news
     */
    public News(String newsHeadline, String newsTrailText, String newsByline, String newsSectionName, Date newsPublicationDate, String newsUrl, String newsImage) {
        mNewsHeadline = newsHeadline;
        mNewsTrailText = newsTrailText;
        mNewsByline = newsByline;
        mNewsSectionName = newsSectionName;
        mNewsPublicationDate = newsPublicationDate;
        mNewsUrl = newsUrl;
        mNewsImage= newsImage;
    }

    /**
     * Returns the headline of the news.
     */
    public String getHeadline() {
        return mNewsHeadline;
    }

    /**
     * Returns the lead of the news.
     */
    public String getTrailText() {
        return mNewsTrailText;
    }

    /**
     * Returns the name of the news author.
     */
    public String getByline() {
        return mNewsByline;
    }


    /**
     * Returns the section name of the news.
     */
    public String getSectionName() {
        return mNewsSectionName;
    }


    /**
     * Returns the publication date of the news.
     */
    public Date getPublicationDate() {
        return mNewsPublicationDate;
    }

    /**
     * Returns the page URL to find more information about the news.
     */
    public String getUrl() {
        return mNewsUrl;
    }

    /**
     * Returns the image for the news.
     */
    public String getImage() {
        return mNewsImage;
    }




}
