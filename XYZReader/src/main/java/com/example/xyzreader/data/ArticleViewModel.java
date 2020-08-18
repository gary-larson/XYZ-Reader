package com.example.xyzreader.data;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.xyzreader.utilities.ArticleResult;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {
    // Declare variables
    LiveData<ArticleResult<List<Article>>> mArticles;
    ArticleRepository mArticleRepository;

    public ArticleViewModel(Application application) {
        super(application);
        mArticleRepository = new ArticleRepository(application);
    }

    public LiveData<ArticleResult<List<Article>>> getAllArticles() {
        if (mArticles == null || mArticles.getValue() instanceof ArticleResult.Error) {
            mArticles = mArticleRepository.getAllArticles();
        }
        return mArticles;
    }

    public void refreshArticles() {
        mArticleRepository.refreshArticles();
    }

}
