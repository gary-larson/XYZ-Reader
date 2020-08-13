package com.example.xyzreader.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.databinding.ListItemArticleBinding;
import com.example.xyzreader.ui.ArticleListActivity;
import com.example.xyzreader.ui.DynamicHeightNetworkImageView;
import com.example.xyzreader.ui.ImageLoaderHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder> {
    // Declare constant
    private static final String TAG = ArticleListActivity.class.getSimpleName();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss",
            Locale.getDefault());
    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat("", Locale.getDefault());
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);
    // Declare member variables
    private int mWidth;
    private int mHeight;
    private ListItemArticleBinding mBinding;
    private Context mContext;
    private Cursor mCursor;
    // Variable for listener
    private final OnListActivityInteractionListener mListener;

    /**
     * Constructor for adapter
     * @param listener to process taps
     */
    public ArticleRecyclerViewAdapter(OnListActivityInteractionListener listener, Cursor cursor) {
        mListener = listener;
        mCursor = cursor;
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
        mHeight = parent.getMeasuredHeight();
        // Inflate layout
        mBinding = ListItemArticleBinding.inflate(LayoutInflater.from(mContext), parent,
                false);
        View view = mBinding.getRoot();

        return new ViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull ArticleRecyclerViewAdapter.ViewHolder holder, int position) {
//
//    }

    /**
     * Method to bind data to the view holder
     * @param holder to bind the data to
     * @param position of the item in the holder
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        mCursor.moveToPosition(position);
        holder.titleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
        Date publishedDate = parsePublishedDate();
        if (!publishedDate.before(START_OF_EPOCH.getTime())) {

            holder.subtitleView.setText(Html.fromHtml(
                    DateUtils.getRelativeTimeSpanString(
                            publishedDate.getTime(),
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString()
                            + "<br/>" + " by "
                            + mCursor.getString(ArticleLoader.Query.AUTHOR)));
        } else {
            holder.subtitleView.setText(Html.fromHtml(
                    outputFormat.format(publishedDate)
                            + "<br/>" + " by "
                            + mCursor.getString(ArticleLoader.Query.AUTHOR)));
        }
//        holder.thumbnailView.setImageUrl(
//                mCursor.getString(ArticleLoader.Query.THUMB_URL),
//                ImageLoaderHelper.getInstance(ArticleListActivity.this).getImageLoader());
//        holder.thumbnailView.setAspectRatio(mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO));
        // Utilize Picasso to load the poster into the image view
        // resize images based on height, width and orientation of phone
        String urlString = mCursor.getString(ArticleLoader.Query.THUMB_URL);
        if (urlString != null) {
            int width = mWidth / mContext.getResources().getInteger(R.integer.list_column_count);
            Picasso.get().load(urlString)
                    //.error(R.mipmap.error)
                    .noPlaceholder()
                    .resize(width,
                            (int) (width * mCursor.getDouble(ArticleLoader.Query.ASPECT_RATIO) ))
                    .into(holder.imageView);
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


//    /**
//     * Method to set list data and notify adapter
//     * @param list to set
//     */
//    public void setList(List<MovieResult> list) {
//        mMovieData = list;
//        notifyDataSetChanged();
//    }

    private Date parsePublishedDate() {
        try {
            String date = mCursor.getString(ArticleLoader.Query.PUBLISHED_DATE);
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }

    /**
     * Method to get the number of items in the list
     * @return the number of items in the list
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
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

//        final View mView;
//        public MovieResult mMovieResult;
//        public ImageView mImageView;

//        /**
//         * Constructor for the view holder class
//         * @param view to use
//         */
//        ViewHolder(View view) {
//            super(view);
//            mView = view;
//            mImageView = binding.ivListItem;
//        }
    }

    /**
     * Interface for the click listener in the activity containing the fragment
     */
    public interface OnListActivityInteractionListener {
        // set arguments type and name
        void onListActivityInteraction(int itemId);
    }

}
