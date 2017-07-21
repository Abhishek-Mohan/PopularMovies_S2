package fastrackm.nanodegree.udacity.abhis.popularmovies_s2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models.Movie;

/**
 * Created by abhis on 6/11/2017.
 */

public class MovieDetailTrailerAdapter extends RecyclerView.Adapter<MovieDetailTrailerAdapter.MovieAdapterViewHolder>
{
    private ArrayList<String> mMovieTrailers;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final TrailerAdapterOnClickHandler mClickHandler;
    private Context mContext;



    /**
     * The interface that receives onClick messages.
     */
    public interface TrailerAdapterOnClickHandler
    {
        // Need to pass in information like title (string),
        // image thumbnail (url), plot (string), user rating (string), and release date (string)
        void onClick();
    }

    MovieDetailTrailerAdapter(Context context, TrailerAdapterOnClickHandler clickHandler, ArrayList<String> movieTrailers)
    {
        this.mContext = context;
        this.mClickHandler = clickHandler;
        this.mMovieTrailers = movieTrailers;
    }

    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_grid, parent, false);
        return new MovieAdapterViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int i)
    {
        //Picasso.with(mContext).load(R.drawable.ic_ondemand_video_black_36px).into(movieAdapterViewHolder.mPlayTrailer);
    }

    public ArrayList<String> getMovieList()
    {
        return mMovieTrailers;
    }

    @Override
    public int getItemCount()
    {
        if (mMovieTrailers == null)
        {
            return 0;
        }
        return mMovieTrailers.size();
    }

    void setMovieData(ArrayList<String> movieTrailers)
    {
        mMovieTrailers = movieTrailers;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        ImageView mPlayTrailer;

        MovieAdapterViewHolder(View itemView)
        {
            super(itemView);
             mPlayTrailer = (ImageView) itemView.findViewById(R.id.playTrailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            int adapterPosition = getAdapterPosition();
            String currentTrailer = mMovieTrailers.get(adapterPosition);
            mClickHandler.onClick();

            String video_path = "http://www.youtube.com/watch?v=";
            String appendedVideoPath = video_path.concat(currentTrailer);
            Uri uri = Uri.parse(appendedVideoPath);

            // With this line the Youtube application, if installed, will launch immediately.
            // Without it you will be prompted with a list of the application to choose.
            uri = Uri.parse("vnd.youtube:"  + uri.getQueryParameter("v"));

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        }
    }

}
