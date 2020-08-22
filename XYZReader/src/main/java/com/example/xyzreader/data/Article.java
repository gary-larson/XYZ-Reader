package com.example.xyzreader.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "articles")
public class Article {
    @PrimaryKey()
    @ColumnInfo(name = "article_id")
    private int mId;
    @ColumnInfo(name = "photo_url")
    private String mPhotoUrl;
    @ColumnInfo(name = "thumbnail_url")
    private String mThumbnailUrl;
    @ColumnInfo(name = "aspect_ratio")
    private double mAspectRatio;
    @ColumnInfo(name = "byline")
    private String mByline;
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "body")
    private String mBody;

    /**
     * Default constructor
     */
    @Ignore
    public Article() {}

    /**
     * All variable constructor
     * @param id to set
     * @param photoUrl to set
     * @param thumbnailUrl to set
     * @param aspectRatio to set
     * @param byline to set
     * @param title to set
     * @param body to set
     */
    public Article(int id, String photoUrl, String thumbnailUrl, double aspectRatio, String byline,
                   String title, String body) {
        mId = id;
        mPhotoUrl = photoUrl;
        mThumbnailUrl = thumbnailUrl;
        mAspectRatio = aspectRatio;
        mByline = byline;
        mTitle = title;
        mBody = body;
    }

    /**
     * Getter for id
     * @return id
     */
    public int getId() {
        return mId;
    }

    /**
     * Setter for id
     * @param id to set
     */
    public void setId(int id) {
        this.mId = id;
    }

    /**
     * Getter for photo url
     * @return photo url
     */
    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    /**
     * Setter for photo url
     * @param photoUrl to set
     */
    public void setPhotoUrl(String photoUrl) {
        this.mPhotoUrl = photoUrl;
    }

    /**
     * Getter for thumbnail url
     * @return thumbnail url
     */
    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    /**
     * Setter for thumbnail url
     * @param thumbnailUrl to set
     */
    public void setThumbnailUrl(String thumbnailUrl) {
        this.mThumbnailUrl = thumbnailUrl;
    }

    /**
     * Getter for aspect ratio
     * @return aspect ratio
     */
    public double getAspectRatio() {
        return mAspectRatio;
    }

    /**
     * Setter for aspect ratio
     * @param aspectRatio to set
     */
    public void setAspectRatio(double aspectRatio) {
        this.mAspectRatio = aspectRatio;
    }

    /**
     * Getter for byline
     * @return byline
     */
    public String getByline() {
        return mByline;
    }

    /**
     * Setter for byline
     * @param byline to set
     */
    public void setByline(String byline) {
        this.mByline = byline;
    }

    /**
     * Getter for title
     * @return title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Setter for title
     * @param title to set
     */
    public void setTitle(String title) {
        this.mTitle = title;
    }

    /**
     * Getter for body
     * @return body
     */
    public String getBody() {
        return mBody;
    }

    /**
     * Setter for body
     * @param body to set
     */
    public void setBody(String body) {
        this.mBody = body;
    }
}
