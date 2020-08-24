package com.example.xyzreader.ui;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;

import com.example.xyzreader.R;
import com.example.xyzreader.databinding.FragmentArticleDetailBinding;
import com.example.xyzreader.utilities.GlideApp;

import java.util.Objects;

/**
 * Class to manage detail fragments
 */
public class ArticleDetailFragment extends Fragment {
    // Declare constants
    private static final String PHOTO_URL = "PhotoUrl";
    private static final String ASPECT_RATIO = "AspectRatio";
    private static final String BYLINE = "Byline";
    private static final String TITLE = "Title";
    private static final String BODY = "Body";
    // Declare variables
    private String mPhotoUrl;
    private double mAspectRatio;
    private String mByline;
    private String mTitle;
    private String mBody;

    private FragmentArticleDetailBinding mBinding;

    /**
     * method to create and inflate views
     * @param inflater to use
     * @param container for view
     * @param savedInstanceState to handle state
     * @return view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentArticleDetailBinding.inflate(inflater, container, false);
        if (savedInstanceState != null) {
            // retrieve variables when state was saved
            mPhotoUrl = savedInstanceState.getString(PHOTO_URL);
            mAspectRatio = savedInstanceState.getDouble(ASPECT_RATIO);
            mByline = savedInstanceState.getString(BYLINE);
            mTitle = savedInstanceState.getString(TITLE);
            mBody = savedInstanceState.getString(BODY);
        }
        return mBinding.getRoot();
    }

    /**
     * Method when view has been created
     * @param view to use
     * @param savedInstanceState for saved states
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // get arguments passed to fragment
        Bundle args = getArguments();
        if (args != null) {
            // retrieve arguments and put them in variables
            mPhotoUrl = args.getString(PHOTO_URL);
            mAspectRatio = args.getDouble(ASPECT_RATIO);
            mByline = args.getString(BYLINE);
            mTitle = args.getString(TITLE);
            mBody = args.getString(BODY);
        }
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.toolbar);
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar())
                    .setDisplayHomeAsUpEnabled(true);
        }
        // test photo url
        String urlString = mPhotoUrl;
        if (urlString != null) {
            // use glige to display image
            GlideApp.with(this)
                    .load(urlString)
                    //.override((int) width, (int) height)
                    .into(mBinding.photo);
        }
        // set subtitle color
        mBinding.toolbar.setSubtitleTextColor(getResources().getColor(R.color.detail_article_byline));
        // set subtitle
        //mBinding.toolbar.setSubtitle(Html.fromHtml(mByline));
        mBinding.toolbar.setSubtitle(mByline);
        //set title
        mBinding.toolbar.setTitle(mTitle);
        // set body typeface
        mBinding.articleBody.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "Rosario-Regular.ttf"));
        // set body
        //mBinding.articleBody.setText(Html.fromHtml(mBody));
        mBinding.articleBody.setText(mBody);
        if (getActivity() != null) {
            // set floating action bar listener
            mBinding.shareFab.setOnClickListener(fabView -> startActivity(Intent
                    .createChooser(ShareCompat.IntentBuilder.from(getActivity())
                            .setType("text/plain")
                            .setText("Some sample text")
                            .getIntent(), getString(R.string.action_share))));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * Method to save state variables
     * @param outState to sAve variables in
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PHOTO_URL, mPhotoUrl);
        outState.putDouble(ASPECT_RATIO, mAspectRatio);
        outState.putString(BYLINE, mByline);
        outState.putString(TITLE, mTitle);
        outState.putString(BODY, mBody);
    }
}
