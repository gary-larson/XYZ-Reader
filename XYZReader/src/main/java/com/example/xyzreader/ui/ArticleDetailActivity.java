package com.example.xyzreader.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xyzreader.adapter.ArticleDetailFragmentStateAdapter;
import com.example.xyzreader.data.ArticleDetailViewModel;
import com.example.xyzreader.databinding.ActivityArticleDetailBinding;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends FragmentActivity {
    private int mPosition;
    private ArticleDetailFragmentStateAdapter mPagerAdapter;
    // TODO Get up button working
    // TODO get orientation change to work
    // TODO test tablets

    /**
     * Method to create activity and inflate pager
     * @param savedInstanceState to use
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup binding
        // Declare variables
        com.example.xyzreader.databinding.ActivityArticleDetailBinding mBinding = ActivityArticleDetailBinding.inflate(getLayoutInflater());
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
        mPagerAdapter = new ArticleDetailFragmentStateAdapter(this);
        // set paging adapter
        mBinding.pager.setAdapter(mPagerAdapter);
        // set current position
        mBinding.pager.setCurrentItem(mPosition);
        // setup observer for live data for list of articles
        mArticleDetailViewModel.getArticles().observe(this, newArticles -> {
            if (newArticles != null) {
                // load articles into paging adapter
                mPagerAdapter.setArticles(newArticles);
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
