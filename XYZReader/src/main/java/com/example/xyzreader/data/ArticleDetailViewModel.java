package com.example.xyzreader.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Class to deal with article detail data
 */
public class ArticleDetailViewModel extends AndroidViewModel {
    // Declare variables
    ArticleRepository mRepository;
    LiveData<List<Article>> mArticles;

    /**
     * Constructor for view model
     * @param application to push to repository
     */
    public ArticleDetailViewModel(Application application) {
        super(application);
        // initialize repository
        mRepository = new ArticleRepository(application);
        mArticles = mRepository.getArticles();
    }

    /**
     * Method to get article from position
     * @return article at position
     */
    public LiveData<List<Article>> getArticles() {
        // test if already here
        if (mArticles == null) {
            // get all articles from repository
            mArticles = mRepository.getArticles();
        }
        // return all articles or null through live data
        return mArticles;
    }

    public Article getArticle(int position) {
        if (mArticles == null || mArticles.getValue() == null) {
            return null;
        }
        return mArticles.getValue().get(position);
    }

    /**
     * Method to get number of articles
     * @return number of articles
     */
    public  int getArticleCount() {
        // check if there is data
        if (mArticles.getValue() == null) {
            // if not return 0
            return 0;
        }
        // return number of articles
        return mArticles.getValue().size();
    }
}
