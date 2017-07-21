package fastrackm.nanodegree.udacity.abhis.popularmovies_s2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailTrailerAdapter.TrailerAdapterOnClickHandler
{
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private RecyclerView mReviewRecyclerView;
    private RecyclerView mTrailerRecyclerView;
    private GridLayoutManager mTrailerLayoutManager;
    private LinearLayoutManager mReviewLayoutManager;
    private MovieDetailReviewAdapter mReviewAdapter;
    private MovieDetailTrailerAdapter mTrailerAdapter;
    private Context mContext;

    private ImageView mMovieBackDrop;
    private TextView mMoviePlot;
    private TextView mMovieRating;
    private TextView mMoviedate;
    private Button mFavButton;
    private ImageView mTrailers;
    private TextView mAuthors;
    private TextView mContent;

    public static final String[] FAVORITE_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID
    };

    public static final int INDEX_MOVIE_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        mContext = getApplicationContext();

        Intent currentMovie = getIntent();
        Movie currentMovieObj = currentMovie.getParcelableExtra("movieObject");
        //Movie currentMovieObj = (Movie) currentMovie.getSerializableExtra("movieObject");

        final String movieTitle = currentMovieObj.getMtitle();
        String movieBackDrop = currentMovieObj.getmPoster();
        String moviePlot = currentMovieObj.getmPlot();
        double movieRating = currentMovieObj.getmUserRating();
        String movieDate = currentMovieObj.getmReleaseDate();
        final String movieDBID = currentMovieObj.getmDBID();
        ArrayList<String> movieTrailers = currentMovieObj.getmMovieTrailers();
        //ArrayList<Reviews> movieReviews = currentMovieObj.getmReviews();

        getSupportActionBar().setTitle(movieTitle);

        mMovieBackDrop = (ImageView) findViewById(R.id.main_backdrop);
        Picasso.with(this).load(movieBackDrop).fit().into(mMovieBackDrop);
        mMoviePlot = (TextView) findViewById(R.id.plot_summary);
        mMovieRating = (TextView) findViewById(R.id.user_rating);
        mMoviedate = (TextView) findViewById(R.id.release_date);
        mFavButton = (Button) findViewById(R.id.favorite_button);

        mMoviePlot.setText(String.format(getApplicationContext().getString(R.string.Plot_Synopsis), moviePlot));
        mMovieRating.setText(String.format(getApplicationContext().getString(R.string.Rating), Double.toString(movieRating)));
        mMoviedate.setText(String.format(getApplicationContext().getString(R.string.Release_Date), movieDate));
        boolean isClicked = CheckIsDataAlreadyInDBorNot(movieDBID);

        mReviewRecyclerView = (RecyclerView) findViewById(R.id.reviewRecycler);
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.trailerRecycler);

        mReviewLayoutManager = new LinearLayoutManager(this);
        mTrailerLayoutManager = new GridLayoutManager(this, 2);

        mReviewRecyclerView.setLayoutManager(mReviewLayoutManager);
        mTrailerRecyclerView.setLayoutManager(mTrailerLayoutManager);

        mReviewAdapter = new MovieDetailReviewAdapter(mContext);
        mTrailerAdapter = new MovieDetailTrailerAdapter(mContext, this, movieTrailers);

        mReviewRecyclerView.setAdapter(mReviewAdapter);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        mReviewRecyclerView.setHasFixedSize(true);
        mTrailerRecyclerView.setHasFixedSize(true);

        mFavButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (CheckIsDataAlreadyInDBorNot(movieDBID))
                {
                    Log.d(TAG, "does it get here");
                    String stringId = movieDBID;
                    Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(stringId).build();
                    getContentResolver().delete(uri, null, null);

                }
                else
                {
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movieTitle);
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieDBID);
                    Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

                    if (uri != null)
                    {
                        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    }

                }

            }
        });



        new FetchMovieTask().execute(currentMovieObj.getmDBID());

/*

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
*/


    }

    public boolean CheckIsDataAlreadyInDBorNot(String id) {

           Cursor mCursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{id},
                    null);
        if(mCursor.getCount() <= 0){
            mCursor.close();
            return false;
        }
        mCursor.close();
        return true;
    }

    @Override
    public void onClick() {

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

          /*  Cursor mCursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{id},
                    null);*/

        }

        @Override
        protected void onPostExecute(ArrayList<Reviews> reviewData)
        {
            if (!reviewData.isEmpty())
            {
                mReviewAdapter.setMovieReviewData(reviewData);
            }

            else
            {
                Log.d(TAG, "this failed terribly");
            }
        }

    }

}
