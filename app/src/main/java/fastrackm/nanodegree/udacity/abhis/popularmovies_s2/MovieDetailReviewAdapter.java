package fastrackm.nanodegree.udacity.abhis.popularmovies_s2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models.Reviews;

/**
 * Created by abhis on 7/20/2017.
 */

public class MovieDetailReviewAdapter extends RecyclerView.Adapter<MovieDetailReviewAdapter.MovieDetailReviewViewHolder>
{
    private ArrayList<Reviews> mMovieReviews;

    private Context mContext;


    MovieDetailReviewAdapter(Context context)
    {
        mContext = context;
    }

    @Override
    public MovieDetailReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_layout, parent, false);
        return new MovieDetailReviewViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(MovieDetailReviewViewHolder holder, int position)
    {
        Reviews currentReview = mMovieReviews.get(position);
        holder.mReviewAuthor.setText(currentReview.getmAuthor());
        holder.mReviewContent.setText(currentReview.getmContent());
    }

    @Override
    public int getItemCount()
    {
        if (mMovieReviews == null)
        {
            return 0;
        }
        return mMovieReviews.size();
    }

    void setMovieReviewData(ArrayList<Reviews> reviewData)
    {
        mMovieReviews = reviewData;
        notifyDataSetChanged();
    }

    public class MovieDetailReviewViewHolder extends RecyclerView.ViewHolder
    {
        TextView mReviewAuthor;
        TextView mReviewContent;

        public MovieDetailReviewViewHolder(View itemView)
        {
            super(itemView);
            mReviewAuthor = (TextView) itemView.findViewById(R.id.review_author);
            mReviewContent = (TextView) itemView.findViewById(R.id.review_content);
        }

    }
}
