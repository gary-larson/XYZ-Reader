package com.example.xyzreader.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xyzreader.utilities.ArticleResult;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {
    // Declare variables
    LiveData<ArticleResult<List<Article>>> mArticles;
    ArticleRepository mArticleRepository;

    /**
     * Constructor for article view model
     * @param application to extend to repository
     */
    public ArticleViewModel(Application application) {
        super(application);
        // initialize repository
        mArticleRepository = new ArticleRepository(application);
    }

    /**
     * Method to get all articles for live data
     * @return articles
     */
    public LiveData<ArticleResult<List<Article>>> getAllArticles() {
        if (mArticles == null || mArticles.getValue() instanceof ArticleResult.Error) {
            // get articles from repository
            mArticles = mArticleRepository.getAllArticles();
        }
        return mArticles;
    }

    /**
     * Method to force a refresh from the internet
     */
    public void refreshArticles() {
        // have repository do refresh
        mArticleRepository.refreshArticles();
    }
}
