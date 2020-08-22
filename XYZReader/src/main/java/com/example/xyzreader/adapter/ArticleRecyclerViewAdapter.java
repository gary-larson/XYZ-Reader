package com.example.xyzreader.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.Article;
import com.example.xyzreader.databinding.ListItemArticleBinding;
import com.example.xyzreader.utilities.GlideApp;

import java.util.List;

/**
 * Class to deal with article list recycler view
 */
public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder> {
    // Declare member variables
    private int mWidth;
    private ListItemArticleBinding mBinding;
    private Context mContext;
    private List<Article> mArticles;
    // Variable for listener
    private final OnListActivityInteractionListener mListener;

    /**
     * Constructor for adapter
     * @param listener to process taps
     */
    public ArticleRecyclerViewAdapter(OnListActivityInteractionListener listener) {
        mListener = listener;
    }

    /**
     * Method to create individual view holders
     * @param parent of the view holders MovieItemFragment
     * @param viewType to use
     * @return the completed view holder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        // get visible width and height of the recyclerview
        mWidth  = parent.getMeasuredWidth();
        // Inflate layout
        mBinding = ListItemArticleBinding.inflate(LayoutInflater.from(mContext), parent,
                false);
        View view = mBinding.getRoot();

        return new ViewHolder(view);
    }

    /**
     * Method to bind data to the view holder
     * @param holder to bind the data to
     * @param position of the item in the holder
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // get article at position
        Article article = mArticles.get(position);
        // set title text
        holder.titleView.setText(article.getTitle());
        // set subtitle text
        holder.subtitleView.setText(Html.fromHtml(article.getByline()));

        // Utilize Glide to load the poster into the image view
        // resize images based on height, width and orientation of phone
        String urlString = article.getThumbnailUrl();
        if (urlString != null) {
            // calculate width and height
            int width = mWidth / mContext.getResources().getInteger(R.integer.list_column_count);
            int height = (int) (width * article.getAspectRatio());
            // display image
            GlideApp.with(mContext)
                    .load(urlString)
                    .override(width, height)
                    .into(mBinding.ivArticleImage);
        }


        // set up on click listener
        holder.cardView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListActivityInteraction(position);
            }
        });
    }

    /**
     * Method to get the number of items in the list
     * @return the number of items in the list
     */
    @Override
    public int getItemCount() {
        if (mArticles == null) {
            return 0;
        }
        return mArticles.size();
    }

    /**
     * Method to set list data and notify adapter
     * @param list to set
     */
    public void setList(List<Article> list) {
        mArticles = list;
        notifyDataSetChanged();
    }

    /**
     * Getter for the item id
     * @param position of item
     * @return item id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * method to get the item view type
     * @param position of the item
     * @return item view type
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * Class for the view holders
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        // Declare variables
        public CardView cardView;
        public ImageView imageView;
        public TextView titleView;
        public TextView subtitleView;

        /**
         * Constructor for the view holder class
         * @param view to use
         */
        public ViewHolder(View view) {
            super(view);
            cardView = mBinding.cardView;
            imageView = mBinding.ivArticleImage;
            titleView = mBinding.articleTitle;
            subtitleView = mBinding.articleSubtitle;
        }
    }

    /**
     * Interface for the click listener in the activity containing the fragment
     */
    public interface OnListActivityInteractionListener {
        // set arguments type and name
        void onListActivityInteraction(int itemId);
    }

}
