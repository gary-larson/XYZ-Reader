package com.example.xyzreader.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.xyzreader.data.Article;
import com.example.xyzreader.ui.ArticleDetailFragment;

import java.util.List;

/**
 * Class to hadle pager adapter
 */
public class ArticleDetailFragmentStateAdapter extends FragmentStateAdapter {
    // Declare constants
    private static final String PHOTO_URL = "PhotoUrl";
    private static final String ASPECT_RATIO = "AspectRatio";
    private static final String BYLINE = "Byline";
    private static final String TITLE = "Title";
    private static final String BODY = "Body";
    // Declare variables
    List<Article> mArticles;

    /**
     * Constructor for adapter
     * @param fragmentManager to reference
     * @param lifecycle to use
     */
    public ArticleDetailFragmentStateAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    /**
     * Method to create fragment
     * @param position od fragment
     * @return completed fragment
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle args = new Bundle();
        // Add all data needed by fragment
        args.putString(PHOTO_URL, mArticles.get(position).getPhotoUrl());
        args.putDouble(ASPECT_RATIO, mArticles.get(position).getAspectRatio());
        args.putString(BYLINE, mArticles.get(position).getByline());
        args.putString(TITLE, mArticles.get(position).getTitle());
        args.putString(BODY, mArticles.get(position).getBody());
        // create fragment
        Fragment fragment = new ArticleDetailFragment();
        // add arguments
        fragment.setArguments(args);
        // return fragment
        return fragment;
    }

    /**
     * Method to get the number of articles
     * @return number of articles
     */
    @Override
    public int getItemCount() {
        if (mArticles == null) {
            return 0;
        }
        return mArticles.size();
    }

    /**
     * Method to set articles
     * @param articles to set
     */
    public void setArticles(List<Article> articles) {
        this.mArticles = articles;
        // alert adapter of data change
        notifyDataSetChanged();
    }
}
