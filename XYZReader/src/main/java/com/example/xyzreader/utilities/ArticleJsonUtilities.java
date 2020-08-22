package com.example.xyzreader.utilities;

import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.xyzreader.data.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ArticleJsonUtilities {
    // Declare constants
    private final static String ID = "id";
    private final static String PHOTO = "photo";
    private final static String THUMB = "thumb";
    private final static String ASPECT_RATIO = "aspect_ratio";
    private final static String AUTHOR = "author";
    private final static String TITLE = "title";
    private final static String PUBLISHED_DATE = "published_date";
    private final static String BODY = "body";
    private final static String TAG = ArticleJsonUtilities.class.getSimpleName();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.sss", Locale.getDefault());
    // Most time functions can only handle 1902 - 2037
    private static GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);
    // Use default locale format
    private static SimpleDateFormat outputFormat = new SimpleDateFormat("",
            Locale.getDefault());

    /**
     * Method to convert a Json string to a list of movie results
     * @param articleJsonStr is the string to be converted
     * @return the list of movie results
     * @throws JSONException in case of error
     */
    public static ArticleResult<List<Article>> getArticleResults (String articleJsonStr) throws JSONException {
        // Declare and initialize variables to return results
        List<Article> articles = new ArrayList<>();

        // Check if there are actual results
        JSONArray articlesJson = new JSONArray(articleJsonStr);

        if (articlesJson.length() < 1) {
            return new ArticleResult.Error<>("No Results Found");
        }
        // Loop through articlesJson and build the list of articles
        for (int i = 0; i < articlesJson.length(); i++) {
            // get the current article
            JSONObject currentArticleJson = articlesJson.getJSONObject(i);

            // declare and initialize variable to hold the current article
            Article currentArticle = new Article();

            // retrieve the individual elements
            // test for null
            if (!currentArticleJson.isNull(ID)) {
                // retrieve id
                currentArticle.setId(currentArticleJson.getInt(ID));
            }
            // Retrieve photo url
            currentArticle.setPhotoUrl(currentArticleJson.getString(PHOTO));

            // Retrieve thumbnail url
            currentArticle.setThumbnailUrl(currentArticleJson.getString(THUMB));

            // Retrieve aspect ratio
            currentArticle.setAspectRatio(currentArticleJson.getDouble(ASPECT_RATIO));

            // Retrieve title
            currentArticle.setTitle(currentArticleJson.getString(TITLE));

            // Retrieve byline combination of date and author
            String temp = currentArticleJson.getString(PUBLISHED_DATE);
            Date publishedDate = parsePublishedDate(temp);
            if (!publishedDate.before(START_OF_EPOCH.getTime())) {
                temp = Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + " by <font color='#ffffff'>"
                                + currentArticleJson.getString(AUTHOR)
                                + "</font>"
                                ).toString();

            } else {
                // If date is before 1902, just show the string
                temp = (Html.fromHtml(
                        outputFormat.format(publishedDate) + " by <font color='#ffffff'>"
                                + currentArticleJson.getString(AUTHOR)
                                + "</font>")).toString();

            }
            currentArticle.setByline(temp);

            // Retrieve body
            String tempBody = currentArticleJson.getString(BODY);
            tempBody = (Html.fromHtml(tempBody.replaceAll("(\r\n|\n)",
                    "<br />"))).toString();
            currentArticle.setBody(tempBody);

            // add this movie to the list
            articles.add(currentArticle);
        }
        // return the movie results
        return new ArticleResult.Success<>(articles);
    }

    /**
     * Method to adjust for epoch dates
     * @param publishedDate to modify
     * @return actual date
     */
    private static Date parsePublishedDate(String publishedDate) {
        try {
            return dateFormat.parse(publishedDate);
        } catch (ParseException ex) {
            if (ex.getMessage() != null) {
                Log.e(TAG, ex.getMessage());
            }
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }
}
