package fastrackm.nanodegree.udacity.abhis.popularmovies_s2;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.data.MovieContract;
import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models.Movie;
import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models.Reviews;
import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.utilities.NetworkUtils;
import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.utilities.themoviedbJsonUtils;

/**
 * Created by abhis on 6/13/2017.
 */

public class MovieDetailActivity extends AppCompatActivity
{
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private ImageView mMovieBackDrop;
    private TextView mMoviePlot;
    private TextView mMovieRating;
    private TextView mMoviedate;
    private Button mFavButton;
    private ImageView mTrailers;
    private TextView mAuthors;
    private TextView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        Intent currentMovie = getIntent();
        Movie currentMovieObj = (Movie) currentMovie.getSerializableExtra("movieObject");

        String movieTitle = currentMovieObj.getMtitle();
        String movieBackDrop = currentMovieObj.getmPoster();
        String moviePlot = currentMovieObj.getmPlot();
        double movieRating = currentMovieObj.getmUserRating();
        String movieDate = currentMovieObj.getmReleaseDate();
        final String movieDBID = currentMovieObj.getmDBID();
        final ArrayList<String> movieTrailers = currentMovieObj.getmMovieTrailers();
        //ArrayList<Reviews> movieReviews = currentMovieObj.getmReviews();

        getSupportActionBar().setTitle(movieTitle);

        mMovieBackDrop = (ImageView) findViewById(R.id.main_backdrop);
        mMoviePlot = (TextView) findViewById(R.id.plot_summary);
        mMovieRating = (TextView) findViewById(R.id.user_rating);
        mMoviedate = (TextView) findViewById(R.id.release_date);
        mFavButton = (Button) findViewById(R.id.favorite_button);
        mTrailers = (ImageView) findViewById(R.id.movieTrailer1);
        mAuthors = (TextView) findViewById(R.id.movieReviewAuthor1);
        mContent = (TextView) findViewById(R.id.movieReviewReview);

        Picasso.with(this).load(movieBackDrop).fit().into(mMovieBackDrop);
        mMoviePlot.setText(String.format(getApplicationContext().
                getString(R.string.Plot_Synopsis), moviePlot));
        mMovieRating.setText(String.format(getApplicationContext().
                getString(R.string.Rating), Double.toString(movieRating)));
        mMoviedate.setText(String.format(getApplicationContext().
                getString(R.string.Release_Date), movieDate));

        mFavButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ContentValues contentValues = new ContentValues();

                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieDBID);
                Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

                if (uri != null)
                {
                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });

        mTrailers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (movieTrailers.get(0) != null)
                {
                    String key = movieTrailers.get(0);
                    String video_path = "http://www.youtube.com/watch?v=";
                    String appendedVideoPath = video_path.concat(key);
                    Uri uri = Uri.parse(appendedVideoPath);

                    // With this line the Youtube application, if installed, will launch immediately.
                    // Without it you will be prompted with a list of the application to choose.
                    uri = Uri.parse("vnd.youtube:"  + uri.getQueryParameter("v"));

                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }

            }
        });


       /* if (movieReviews.get(0) != null)
        {
            Reviews mReview = movieReviews.get(0);
            mAuthors.setText(mReview.getmAuthor());
            mContent.setText(mReview.getmContent());
        }*/
        new FetchMovieTask().execute(currentMovieObj.getmDBID());

    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Reviews>>
    {

        @Override
        protected ArrayList<Reviews> doInBackground(String... params)
        {
            if (params.length == 0) {
                return null;
            }

            String id = params[0];
            URL movieRequestURLReviews = NetworkUtils.buildUrl(getApplicationContext(), "reviews", id);

            try {
                    String jsonMOvieResponseReview = NetworkUtils.getResponseFromHttpUrl(movieRequestURLReviews);
                    return themoviedbJsonUtils.getMovieReviews(jsonMOvieResponseReview);



            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Reviews> movieData)
        {
            if (movieData != null)
            {
                if (!movieData.isEmpty())
                {
                    if (movieData.get(0) != null )
                    {
                        Reviews mReview = movieData.get(0);
                        mAuthors.setText(mReview.getmAuthor());
                        mContent.setText(mReview.getmContent());
                    }

                }
            }
            else
            {
                Log.d(TAG, "this failed terribly");
            }
        }

    }

}
