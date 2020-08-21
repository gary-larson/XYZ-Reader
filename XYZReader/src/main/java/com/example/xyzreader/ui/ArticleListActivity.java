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
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
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
                mBinding.content.getRoot().setVisibility(View.VISIBLE);
                mBinding.errorMessage.getRoot().setVisibility(View.GONE);
                ArticleResult.Success<List<Article>> result =
                        (ArticleResult.Success<List<Article>>) newArticles;
                mAdapter.setList(result.data);
            }
            mContentBinding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onListActivityInteraction(int position) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra("Position", position);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mViewModel.refreshArticles();
    }
}
