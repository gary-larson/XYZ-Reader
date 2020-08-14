package com.example.xyzreader.utilities;

import android.text.Html;
import android.util.Log;

import com.example.xyzreader.data.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private final static String STATUS_MESSAGE = "status_message";
    private final static String TAG = ArticleJsonUtilities.class.getSimpleName();

    /**
     * Method to convert a Json string to a list of movie results
     * @param articleJsonStr is the string to be converted
     * @return the list of movie results
     * @throws JSONException in case of error
     */
    public static ArticleResult<List<Article>> getArticleResults (String articleJsonStr) throws JSONException {
        // Declare and initialize variables to return results
        Article article = new Article();
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

            // Retrieve author
            currentArticle.setAuthor(currentArticleJson.getString(AUTHOR));

            // Retrieve title
            currentArticle.setTitle(currentArticleJson.getString(TITLE));

            // Retrieve published date
            String temp = currentArticleJson.getString(PUBLISHED_DATE);
            Date date;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .parse(temp.substring(0, 9));
            } catch (ParseException e) {
                Log.i(TAG, "Published Date parse Error");
                date = null;
            }
            currentArticle.setPublishedDate(date);

            // Retrieve body
            currentArticle.setBody(currentArticleJson.getString(BODY));

            // add this movie to the list
            articles.add(currentArticle);
        }
        // return the movie results
        return new ArticleResult.Success<>(articles);
    }
}
