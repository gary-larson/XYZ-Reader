package com.example.xyzreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.xyzreader.R;
import com.example.xyzreader.adapter.ArticleRecyclerViewAdapter;
import com.example.xyzreader.data.Article;
import com.example.xyzreader.data.ArticleViewModel;
import com.example.xyzreader.databinding.ActivityArticleListBinding;
import com.example.xyzreader.utilities.ArticleResult;

import java.util.List;

/**
 * Class to handle a list of articles
 */
public class ArticleListActivity extends AppCompatActivity implements
        ArticleRecyclerViewAdapter.OnListActivityInteractionListener, SwipeRefreshLayout.OnRefreshListener {
    // Declare variables
    private ArticleViewModel mViewModel;


    /**
     * Method to create and inflate view
     * @param savedInstanceState for state information
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up binding
        com.example.xyzreader.databinding.ActivityArticleListBinding mBinding =
                ActivityArticleListBinding.inflate(getLayoutInflater());
        com.example.xyzreader.databinding.ArticleContentBinding mContentBinding = mBinding.content;
        com.example.xyzreader.databinding.ArticleErrorMessageBinding mErrorMessageBinding =
                mBinding.errorMessage;

        // get view
        View view = mBinding.getRoot();
        setContentView(view);

        // Declare and initialize variables
        mViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        ArticleRecyclerViewAdapter mAdapter;
        mContentBinding.swipeRefreshLayout.setOnRefreshListener(this);

        // set recyclerView properties
        RecyclerView mRecyclerView = mContentBinding.recyclerView;
        mAdapter = new ArticleRecyclerViewAdapter(this);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);

        // Setup live data observer
        mViewModel.getAllArticles().observe(this, newArticles -> {

            if (newArticles instanceof ArticleResult.Error) {
                // display error message
                String errorMessage = ((ArticleResult.Error<List<Article>>) newArticles)
                        .mErrorMessage;
                mErrorMessageBinding.tvErrorMessage.setText(errorMessage);
                mBinding.content.getRoot().setVisibility(View.GONE);
                mBinding.errorMessage.getRoot().setVisibility(View.VISIBLE);
                mErrorMessageBinding.pbLoadingIndicator.setVisibility(View.GONE);
                mErrorMessageBinding.tvErrorMessage.setVisibility(View.VISIBLE);
            } else {
                // display recycler view
                mBinding.content.getRoot().setVisibility(View.VISIBLE);
                mBinding.errorMessage.getRoot().setVisibility(View.GONE);
                ArticleResult.Success<List<Article>> result =
                        (ArticleResult.Success<List<Article>>) newArticles;
                mAdapter.setList(result.data);
            }
            // turn off refresh loading indicator
            mContentBinding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    /**
     * Listener for article list adapter
     * @param position of click
     */
    @Override
    public void onListActivityInteraction(int position) {
        // create intent
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        // put in position of click
        intent.putExtra("Position", position);
        // start activity
        startActivity(intent);
    }

    /**
     * Listener for swipe refresh
     */
    @Override
    public void onRefresh() {
        // force refresh from internet
        mViewModel.refreshArticles();
    }
}
