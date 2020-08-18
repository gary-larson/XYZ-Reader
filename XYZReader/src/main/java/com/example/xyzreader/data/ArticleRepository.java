package com.example.xyzreader.data;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.xyzreader.R;
import com.example.xyzreader.utilities.ArticleExecutor;
import com.example.xyzreader.utilities.ArticleJsonUtilities;
import com.example.xyzreader.utilities.ArticleNetworkUtilities;
import com.example.xyzreader.utilities.ArticleResult;

import java.util.List;

/**
 * Interface for the callback
 * @param <T>
 */
interface RepositoryCallback<T> {
    void onComplete(ArticleResult<T> result);
}

public class ArticleRepository {
    // Declare variables
    private MutableLiveData<ArticleResult<List<Article>>> mResultArticles;
    private MutableLiveData<List<Article>> mArticles;
    private String mErrorMessage;
    private ArticleExecutor mArticleExecutor;
    private ArticleDao mArticleDao;

    /**
     * Constructor for article repository
     * @param application to use for database
     */
    public ArticleRepository(Application application) {
        // Initialize live data variables
        mResultArticles = new MutableLiveData<>();
        mArticles = new MutableLiveData<>();
        // Initialize error message
        mErrorMessage = application.getString(R.string.error_message);
        // Initialize executor for accessing internet
        mArticleExecutor = new ArticleExecutor();
        // Declar and initialize database variables
        ArticleRoomDatabase db = ArticleRoomDatabase.getDatabase(application);
        mArticleDao = db.articleDao();
    }

    /**
     * Method to retrieve all articles when database data is unknown
     * @return Artical result with data or error message
     */
    public MutableLiveData<ArticleResult<List<Article>>> getAllArticles () {
        // check if data does not exist yet
        if (mResultArticles.getValue() == null ||
                mResultArticles.getValue() instanceof ArticleResult.Error) {
            // start database thread
            ArticleRoomDatabase.databaseWriteExecutor.execute(() -> {
                List<Article> articles = mArticleDao.getAllArticles();
                if (articles.size() > 0) {
                    // package database data
                    ArticleResult<List<Article>> listArticleResult =
                            new ArticleResult.Success<>(articles);
                    // post live data results
                    mResultArticles.postValue(listArticleResult);
                } else {
                    // get data from internet
                    retrieveArticleList(result -> {
                        // post results
                        mResultArticles.postValue(result);
                        if (result instanceof ArticleResult.Success) {
                            // if retrieved data add it to the database
                            mArticleDao.insertAllArticles(((ArticleResult.Success<List<Article>>) result).data);
                        }
                    });
                }
            });
        }
        // Return results
        return mResultArticles;
    }

    /**
     * Method to get all articles from database
     * only used when data is known to be in database
     * @return all articles in live data
     */
    public MutableLiveData<List<Article>> getArticles() {
        // check for data
        if (mArticles.getValue() == null ) {
            // start database executor
            ArticleRoomDatabase.databaseWriteExecutor.execute(() -> {
                // get all articles from database
                List<Article> articleList = mArticleDao.getAllArticles();
                // check for data should always be true
                if (articleList != null) {
                    // return all articles through live data
                    mArticles.postValue(articleList);
                }
            });
        }
        // return all articles or none
        return mArticles;
    }

    /**
     * Method to force retrieval of all articles from the internet (unknown data sections only)
     */
    public void refreshArticles() {
        // get articles from internet
        retrieveArticleList(result -> {
            // post results to live data (unknown
            mResultArticles.postValue(result);
            // check for returned articles
            if (result instanceof ArticleResult.Success) {
                // start database executor
                ArticleRoomDatabase.databaseWriteExecutor.execute(() -> {
                    // delete all articles from database
                    mArticleDao.deleteAllArticles();
                    // insert all articles in database
                    mArticleDao.insertAllArticles(((ArticleResult.Success<List<Article>>)
                            result).data);
                });
            }
        });
    }

    /**
     * Method to retrieve the article list from the internet
     * @param callback to handle results
     */
    public void retrieveArticleList (
            final RepositoryCallback<List<Article>> callback
    ) {
        // start new thread
        mArticleExecutor.execute(() -> {
            try {
                // attempt to get movie information
                String jsonArticleResponse = ArticleNetworkUtilities
                        .getResponseFromHttpUrl();
                // if null cancel task (Unknown error)
                if (jsonArticleResponse == null) {
                    ArticleResult<List<Article>> errorResult = new ArticleResult.Error<>(mErrorMessage);
                    callback.onComplete(errorResult);
                }
                // return Json decoded movie Information
                ArticleResult<List<Article>> result = ArticleJsonUtilities
                        .getArticleResults(jsonArticleResponse);
                callback.onComplete(result);
            } catch (Exception e) {
                e.printStackTrace();
                // in case of an error return null
                ArticleResult<List<Article>> errorResult = new ArticleResult.Error<>(mErrorMessage);
                callback.onComplete(errorResult);
            }
        });
    }
}
