package com.example.xyzreader.ui;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;

import com.example.xyzreader.R;
import com.example.xyzreader.databinding.FragmentArticleDetailBinding;
import com.example.xyzreader.utilities.GlideApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Class to manage detail fragments
 */
public class ArticleDetailFragment extends Fragment {
    // Declare constants
    private static final String PHOTO_URL = "PhotoUrl";
    private static final String ASPECT_RATIO = "AspectRatio";
    private static final String AUTHOR = "Author";
    private static final String TITLE = "Title";
    private static final String PUBLISHED_DATE = "PublishedDate";
    private static final String BODY = "Body";
    // Declare variables
    private String mPhotoUrl;
    private double mAspectRatio;
    private String mAuthor;
    private String mTitle;
    private Date mPublishedDate;
    private String mBody;

    private FragmentArticleDetailBinding mBinding;


    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat("", Locale.getDefault());
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

//    public static ArticleDetailFragment newInstance(int position) {
//        Bundle arguments = new Bundle();
//        arguments.putInt(ARG_ITEM_ID, position);
//        ArticleDetailFragment fragment = new ArticleDetailFragment();
//        fragment.setArguments(arguments);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(PHOTO_URL)) {
            mPhotoUrl = getArguments().getString(PHOTO_URL);
            mAspectRatio = getArguments().getDouble(ASPECT_RATIO);
            mAuthor = getArguments().getString(AUTHOR);
            mTitle = getArguments().getString(TITLE);
            mPublishedDate = new Date(getArguments().getLong(PUBLISHED_DATE));
            mBody = getArguments().getString(BODY);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentArticleDetailBinding.inflate(inflater, container, false);



        if (getActivity() != null) {
            mBinding.shareFab.setOnClickListener(view -> startActivity(Intent
                    .createChooser(ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .setText("Some sample text")
                    .getIntent(), getString(R.string.action_share))));
        }

        bindViews();
        //updateStatusBar();
        return mBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            getActivity().setTitle(mTitle);
        }
    }

    private Date parsePublishedDate() {
        return mPublishedDate;
    }

    private void bindViews() {
        //mBinding.articleByline.setMovementMethod(new LinkMovementMethod());
        mBinding.articleBody.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "Rosario-Regular.ttf"));

        if (mTitle != null) {
            mBinding.getRoot().setVisibility(View.VISIBLE);
            //mBinding.toolbar.setTitle(mTitle + "\n" + );
            Date publishedDate = parsePublishedDate();
            mBinding.toolbar.setTitle(mTitle);
            mBinding.toolbar.setSubtitleTextColor(getResources().getColor(R.color.detail_article_byline));
            if (!publishedDate.before(START_OF_EPOCH.getTime())) {

                mBinding.toolbar.setSubtitle(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + " by <font color='#ffffff'>"
                                + mAuthor
                                + "</font>"));

            } else {
                // If date is before 1902, just show the string
                mBinding.toolbar.setSubtitle(Html.fromHtml(
                        outputFormat.format(publishedDate) + " by <font color='#ffffff'>"
                        + mAuthor
                                + "</font>"));

            }
            mBinding.articleBody.setText(Html.fromHtml(mBody.replaceAll("(\r\n|\n)",
                    "<br />")));
            String urlString = mPhotoUrl;
            if (urlString != null) {

                double height = getResources().getDimension(R.dimen.detail_photo_height);
                double width = height * mAspectRatio;
                GlideApp.with(this)
                        .load(urlString)
                        .override((int) width, (int) height)
                        .into(mBinding.photo);
//                Picasso.get().load(urlString)
//                        //.error(R.mipmap.error)
//                        .noPlaceholder()
//                        .centerInside()
//                        .resize( (int) width, (int) height)
//                        .into(mBinding.photo);
            }
//            ImageLoaderHelper.getInstance(getActivity()).getImageLoader()
//                    .get(mArticle.getPhotoUrl(), new ImageLoader.ImageListener() {
//                        @Override
//                        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
//                            Bitmap bitmap = imageContainer.getBitmap();
//                            if (bitmap != null) {
//                                Palette p = Palette.generate(bitmap, 12);
//                                mMutedColor = p.getDarkMutedColor(0xFF333333);
//                                mBinding.photo.setImageBitmap(imageContainer.getBitmap());
//                                mBinding.metaBar.setBackgroundColor(mMutedColor);
//                                //updateStatusBar();
//                            }
//                        }
//
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//
//                        }
//                    });
        } else {
            mBinding.getRoot().setVisibility(View.GONE);
            // mBinding.articleTitle.setText("N/A");
            // mBinding.articleByline.setText("N/A" );
            mBinding.articleBody.setText("N/A");
        }
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull androidx.loader.content.Loader<Cursor> loader, Cursor cursor) {
//        if (!isAdded()) {
//            if (cursor != null) {
//                cursor.close();
//            }
//            return;
//        }
//
//        mCursor = cursor;
//        if (mCursor != null && !mCursor.moveToFirst()) {
//            Log.e(TAG, "Error reading item detail cursor");
//            mCursor.close();
//            mCursor = null;
//        }
//
//        bindViews();
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull androidx.loader.content.Loader<Cursor> loader) {
//        mCursor = null;
//        bindViews();
//    }



//    public int getUpButtonFloor() {
//        if (mPhotoContainerView == null || mPhotoView.getHeight() == 0) {
//            return Integer.MAX_VALUE;
//        }
//
//        // account for parallax
//        return mIsCard
//                ? (int) mPhotoContainerView.getTranslationY() + mPhotoView.getHeight() - mScrollY
//                : mPhotoView.getHeight() - mScrollY;
//    }
}
