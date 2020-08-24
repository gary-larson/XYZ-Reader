package com.example.xyzreader.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xyzreader.adapter.ArticleDetailFragmentStateAdapter;
import com.example.xyzreader.data.ArticleDetailViewModel;
import com.example.xyzreader.databinding.ActivityArticleDetailBinding;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {
    private int mPosition;
    private ArticleDetailFragmentStateAdapter mPagerAdapter;

    /**
     * Method to create activity and inflate pager
     * @param savedInstanceState to use
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup binding
        ActivityArticleDetailBinding mBinding = ActivityArticleDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        // get view model
        ArticleDetailViewModel mArticleDetailViewModel = new ViewModelProvider(this)
                .get(ArticleDetailViewModel.class);
        // check for save instance
        if (savedInstanceState == null) {
            // get intent if no saved instance
            Intent intent = getIntent();
            if (intent.hasExtra("Position")) {
                mPosition = intent.getIntExtra("Position", 0);
            }
        } else {
            mPosition = savedInstanceState.getInt("position");
        }
        // get paging adapter
        mPagerAdapter = new ArticleDetailFragmentStateAdapter(getSupportFragmentManager(), getLifecycle());
        // set paging adapter
        mBinding.pager.setAdapter(mPagerAdapter);
        // setup observer for live data for list of articles
        mArticleDetailViewModel.getArticles().observe(this, newArticles -> {
            if (newArticles != null) {
                // load articles into paging adapter
                mPagerAdapter.setArticles(newArticles);
                // set current position
                mBinding.pager.setCurrentItem(mPosition, false);
            }
        });
    }

    /**
     * Save state variable
     * @param outState to use
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Position", mPosition);
    }
}
