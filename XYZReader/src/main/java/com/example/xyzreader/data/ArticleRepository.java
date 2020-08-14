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
    private MutableLiveData<ArticleResult<List<Article>>> mArticles;
    private String mErrorMessage;
    private Application mApplication;
    private ArticleExecutor mArticleExecutor;
    private ArticleDao mArticleDao;

    public ArticleRepository(Application application) {
        mApplication = application;
        mArticles = new MutableLiveData<>();
        mErrorMessage = mApplication.getString(R.string.error_message);
        mArticleExecutor = new ArticleExecutor();
        ArticleRoomDatabase db = ArticleRoomDatabase.getDatabase(mApplication);
        mArticleDao = db.articleDao();
    }

    public MutableLiveData<ArticleResult<List<Article>>> getAllArticles () {
        ArticleRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Article> articles = mArticleDao.getAllArticles();
            if (articles.size() > 0) {
                ArticleResult<List<Article>> listArticleResult = new ArticleResult.Success<>(articles);
                mArticles.postValue(listArticleResult);
            } else {
                retrieveArticleList(result -> {
                    mArticles.postValue(result);
                    if (result instanceof ArticleResult.Success) {
                        mArticleDao.insertAllArticles(((ArticleResult.Success<List<Article>>) result).data);
                    }
                });
            }
        });
        return mArticles;
    }

    public void refreshArticles() {
        if (mArticles.getValue() instanceof ArticleResult.Success) {
            mArticleDao.deleteAllArticles(((ArticleResult.Success<List<Article>>) mArticles.getValue()).data);
        }
        retrieveArticleList(result -> {
            mArticles.postValue(result);
            if (result instanceof ArticleResult.Success) {
                mArticleDao.insertAllArticles(((ArticleResult.Success<List<Article>>) result).data);
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
