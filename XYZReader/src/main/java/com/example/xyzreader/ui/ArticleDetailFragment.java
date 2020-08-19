package com.example.xyzreader.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.xyzreader.R;
import com.example.xyzreader.data.Article;
import com.example.xyzreader.data.ArticleDetailViewModel;
import com.example.xyzreader.databinding.ActivityArticleDetailBinding;
import com.example.xyzreader.databinding.ArticleDetailContentBinding;
import com.example.xyzreader.databinding.FragmentArticleDetailBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment {
    // Declare variables
    public static final String ARG_ITEM_ID = "Position";

    private FragmentArticleDetailBinding mBinding;
    private ArticleDetailContentBinding mContentBinding;
    private int mPosition;
    private Article mArticle;
    private int mMutedColor = 0xFF333333;
    private int mTopInset;
    private int mWidth;


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

    public static ArticleDetailFragment newInstance(int position) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_ITEM_ID, position);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
            mPosition = getArguments().getInt(ARG_ITEM_ID);
        }
        setHasOptionsMenu(true);
    }

//    public ArticleDetailActivity getActivityCast() {
//        return (ArticleDetailActivity) getActivity();
//    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
//        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
//        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
//        // we do this in onActivityCreated.
//
//        //getLoaderManager().initLoader(0, null, this);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentArticleDetailBinding.inflate(inflater, container, false);
        mContentBinding = mBinding.content;
        ArticleDetailViewModel mArticleDetailViewModel = new ViewModelProvider(requireActivity())
                .get(ArticleDetailViewModel.class);
        mArticle = mArticleDetailViewModel.getArticle(mPosition);
        // mBinding.drawInsetsFrameLayout.setOnInsetsCallback(insets -> mTopInset = insets.top);


//        mScrollView.setCallbacks(new ObservableScrollView.Callbacks() {
//            @Override
//            public void onScrollChanged() {
//                mScrollY = mScrollView.getScrollY();
//                getActivityCast().onUpButtonFloorChanged(mPosition, ArticleDetailFragment.this);
//                mPhotoContainerView.setTranslationY((int) (mScrollY - mScrollY / PARALLAX_FACTOR));
//                updateStatusBar();
//            }
//        });
        if (getActivity() != null) {
            mBinding.shareFab.setOnClickListener(view -> startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .setText("Some sample text")
                    .getIntent(), getString(R.string.action_share))));
        }

        bindViews();
        //updateStatusBar();
        return mBinding.getRoot();
    }

//    private void updateStatusBar() {
//        int color = 0;
//        if (mPhotoView != null && mTopInset != 0 && mScrollY > 0) {
//            float f = progress(mScrollY,
//                    mStatusBarFullOpacityBottom - mTopInset * 3,
//                    mStatusBarFullOpacityBottom - mTopInset);
//            color = Color.argb((int) (255 * f),
//                    (int) (Color.red(mMutedColor) * 0.9),
//                    (int) (Color.green(mMutedColor) * 0.9),
//                    (int) (Color.blue(mMutedColor) * 0.9));
//        }
//        mStatusBarColorDrawable.setColor(color);
//        mDrawInsetsFrameLayout.setInsetBackground(mStatusBarColorDrawable);
//    }
//
//    static float progress(float v, float min, float max) {
//        return constrain((v - min) / (max - min), 0, 1);
//    }
//
//    static float constrain(float val, float min, float max) {
//        if (val < min) {
//            return min;
//        } else if (val > max) {
//            return max;
//        } else {
//            return val;
//        }
//    }
//
    private Date parsePublishedDate() {
        return mArticle.getPublishedDate();
    }

    private void bindViews() {
        mContentBinding.articleByline.setMovementMethod(new LinkMovementMethod());
        mContentBinding.articleBody.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf"));

        if (mArticle != null) {
            mBinding.getRoot().setAlpha(0);
            mBinding.getRoot().setVisibility(View.VISIBLE);
            mBinding.getRoot().animate().alpha(1);
            mContentBinding.articleTitle.setText(mArticle.getTitle());
            Date publishedDate = parsePublishedDate();
            if (!publishedDate.before(START_OF_EPOCH.getTime())) {
                mContentBinding.articleByline.setText(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + " by <font color='#ffffff'>"
                                + mArticle.getAuthor()
                                + "</font>"));

            } else {
                // If date is before 1902, just show the string
                mContentBinding.articleByline.setText(Html.fromHtml(
                        outputFormat.format(publishedDate) + " by <font color='#ffffff'>"
                        + mArticle.getAuthor()
                                + "</font>"));

            }
            mContentBinding.articleBody.setText(Html.fromHtml(mArticle.getBody().replaceAll("(\r\n|\n)", "<br />")));
            String urlString = mArticle.getPhotoUrl();
            if (urlString != null) {

                double height = getResources().getDimension(R.dimen.detail_photo_height);
                double width = height * mArticle.getAspectRatio();
                Picasso.get().load(urlString)
                        //.error(R.mipmap.error)
                        .noPlaceholder()
                        .resize( (int) width, (int) height)
                        .into(mBinding.photo);
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
            mContentBinding.articleTitle.setText("N/A");
            mContentBinding.articleByline.setText("N/A" );
            mContentBinding.articleBody.setText("N/A");
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
