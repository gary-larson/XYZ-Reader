package com.example.xyzreader.ui;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.xyzreader.data.Article;
import com.example.xyzreader.data.ArticleDetailViewModel;
import com.example.xyzreader.databinding.ActivityArticleDetailBinding;

import java.util.List;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends FragmentActivity {
    // Declare constants
    private static final String PHOTO_URL = "PhotoUrl";
    private static final String ASPECT_RATIO = "AspectRatio";
    private static final String AUTHOR = "Author";
    private static final String TITLE = "Title";
    private static final String PUBLISHED_DATE = "PublishedDate";
    private static final String BODY = "Body";
    // Declare variables
    private ActivityArticleDetailBinding mBinding;
    private int mStartPosition;
    private int mPosition;
    private List<Article> mArticles;
    private FragmentStateAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityArticleDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        ArticleDetailViewModel mArticleDetailViewModel = new ViewModelProvider(this)
                .get(ArticleDetailViewModel.class);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent.hasExtra("Position")) {
                mPosition = intent.getIntExtra("Position", 0);
                mStartPosition = mPosition;
            }
        } else {
            mPosition = savedInstanceState.getInt("position");
            mStartPosition = savedInstanceState.getInt("StartPosition");
        }
//        ActionBar actionBar = getActionBar();
//        actionBar.show();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        mArticleDetailViewModel.getArticles().observe(this, newArticles -> {
            if (newArticles != null) {
                mArticles = newArticles;
                mPagerAdapter = new MyPagerAdapter(this);

                mBinding.pager.setAdapter(mPagerAdapter);
                mBinding.pager.setCurrentItem(mPosition);
                mBinding.pager.setOffscreenPageLimit(1);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Position", mPosition);
        outState.putInt("StartPosition", mStartPosition);
    }

    private class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = new ArticleDetailFragment();
            Bundle args = new Bundle();
            // Add all data needed by fragment
            args.putString(PHOTO_URL, mArticles.get(position).getPhotoUrl());
            args.putDouble(ASPECT_RATIO, mArticles.get(position).getAspectRatio());
            args.putString(AUTHOR, mArticles.get(position).getAuthor());
            args.putString(TITLE, mArticles.get(position).getTitle());
            args.putLong(PUBLISHED_DATE, mArticles.get(position).getPublishedDate().getTime());
            args.putString(BODY, mArticles.get(position).getBody());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return mArticles.size();
        }
    }
}
