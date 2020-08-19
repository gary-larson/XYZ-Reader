package com.example.xyzreader.ui;


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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }
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
        mArticleDetailViewModel.getArticles().observe(this, newArticles -> {
            if (newArticles != null) {
                mArticles = newArticles;
                mPagerAdapter = new MyPagerAdapter(this);
                mBinding.pager.setAdapter(mPagerAdapter);
//        mPager.setPageMargin((int) TypedValue
//                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
//        mPager.setPageMarginDrawable(new ColorDrawable(0x22000000));
//
//        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
//                mUpButton.animate()
//                        .alpha((state == ViewPager.SCROLL_STATE_IDLE) ? 1f : 0f)
//                        .setDuration(300);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                int itemId = mArticleDetailViewModel.getItemId(position);
//                if (itemId > -1 && mItemId != itemId) {
//                    mItemId = itemId;
//                }
//                if (mItemId < 0) {
//                    mItemId = 0;
//                }
//                mSelectedItemId = mItemId;
//                updateUpButtonPosition();
//            }
//        });

//                mUpButtonContainer = mBinding.upContainer;
//
//                mUpButton = mBinding.actionUp;
//                mUpButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        int id = view.getId();
//                        if (id == R.id.action_up) {
//                            finish();
//                        }
//                    }
//                });
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mUpButtonContainer.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
//                        @Override
//                        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
//                            view.onApplyWindowInsets(windowInsets);
//                            mTopInset = windowInsets.getSystemWindowInsetTop();
//                            mUpButtonContainer.setTranslationY(mTopInset);
//                            updateUpButtonPosition();
//                            return windowInsets;
//                        }
//                    });
//                }
            }
        });
        //getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Position", mPosition);
        outState.putInt("StartPosition", mStartPosition);
    }

    //    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return ArticleLoader.newAllArticlesInstance(this);
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull androidx.loader.content.Loader<Cursor> loader, Cursor cursor) {
//        mCursor = cursor;
//        mPagerAdapter.notifyDataSetChanged();
//
//        // Select the start ID
//        if (mStartId > 0) {
//            mCursor.moveToFirst();
//            // TODO: optimize
//            while (!mCursor.isAfterLast()) {
//                if (mCursor.getLong(ArticleLoader.Query._ID) == mStartId) {
//                    final int position = mCursor.getPosition();
//                    mPager.setCurrentItem(position, false);
//                    break;
//                }
//                mCursor.moveToNext();
//            }
//            mStartId = 0;
//        }
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull androidx.loader.content.Loader<Cursor> loader) {
//        mCursor = null;
//        mPagerAdapter.notifyDataSetChanged();
//    }

//    public void onUpButtonFloorChanged(int position, ArticleDetailFragment fragment) {
//        if (position == mPosition) {
//            mSelectedItemUpButtonFloor = fragment.getUpButtonFloor();
//            updateUpButtonPosition();
//        }
//    }
//
//    private void updateUpButtonPosition() {
//        int upButtonNormalBottom = mTopInset + mUpButton.getHeight();
//        mUpButton.setTranslationY(Math.min(mSelectedItemUpButtonFloor - upButtonNormalBottom, 0));
//    }

    private class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            super.setPrimaryItem(container, position, object);
//            ArticleDetailFragment fragment = (ArticleDetailFragment) object;
//            if (fragment != null) {
//                mSelectedItemUpButtonFloor = fragment.getUpButtonFloor();
//                updateUpButtonPosition();
//            }
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            int itemId = mArticleDetailViewModel.getItemId(position);
//            if (itemId > -1 && mItemId == itemId) {
//                return ArticleDetailFragment.newInstance(mArticle.getId());
//            } else if (mItemId != itemId) {
//                mItemId = itemId;
//                // TODO setup observer
//                mArticle = mArticleDetailViewModel.getArticle(mItemId);
//                if (mArticle != null) {
//                    return ArticleDetailFragment.newInstance(mArticle.getId());
//                }
//            }
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return (mCursor != null) ? mCursor.getCount() : 0;
//        }


        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            mPosition = position;
            return ArticleDetailFragment.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return mArticles.size();
        }
    }
}
