package fastrackm.nanodegree.udacity.abhis.popularmovies_s2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.data.MovieContract;
import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models.Movie;
import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models.Reviews;
import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.utilities.NetworkUtils;
import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.utilities.themoviedbJsonUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private MovieAdapter mAdapter;
    private Context mContext;
    private String defaultList = "popular";
    ArrayList<Integer> favMovieIDList;
    public static final String[] FAVORITE_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID
    };

    public static final int INDEX_MOVIE_ID = 0;

    private static final int ID_MOVIE_LOADER = 44;

    private static final String MOVIE_QUERY = "query";

    Cursor mCursor;

    public final static String LIST_STATE_KEY = "recycler_list_state";
    public final static String LIST_VALUE_KEY = "recycler_list_values";

    public final static String CURRENT_QUERY = "movie_query";

    Parcelable listState;
    ArrayList<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Popular Movies");

        mContext = getApplicationContext();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mLayoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MovieAdapter(mContext, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

       if (savedInstanceState != null)
        {
            String currentQuery = savedInstanceState.getString(CURRENT_QUERY);
            Log.d(TAG, currentQuery);

            if (currentQuery.equals("favorite"))
            {
                defaultList = currentQuery;
                loadFavMovieData();
            }
            else
            {
                defaultList = currentQuery;
                loadMovieData(currentQuery);
            }
        }
        else
       {

           loadMovieData(defaultList);
       }
       }


    protected void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        // Save list state
        listState = mLayoutManager.onSaveInstanceState();
        state.putString(CURRENT_QUERY, defaultList);
        state.putParcelable(LIST_STATE_KEY, listState);
        state.putParcelableArrayList(LIST_VALUE_KEY, mAdapter.getMovieList());
    }

    protected void onRestoreInstanceState(Bundle state)
    {
        super.onRestoreInstanceState(state);
       /* if (state != null)
        {
            String currentQuery = state.getString(CURRENT_QUERY);
            Log.d(TAG, currentQuery);

            if (currentQuery.equals("favorite"))
            {
                loadFavMovieData();
            }
            else
            {
                loadMovieData(currentQuery);
            }

            *//*Log.d(TAG, "does it reach onRestore");
            //listState = state.getParcelable(LIST_STATE_KEY);
            movieList = state.getParcelableArrayList(LIST_VALUE_KEY);
            mAdapter.setMovieData(movieList);
            mAdapter.notifyDataSetChanged();*//*
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (mCursor != null)
        {
            mCursor.close();
        }

        if (listState != null)
        {
            Log.d(TAG, "listState is not null");
            if (!defaultList.equals("popular") && !defaultList.equals("top"))
            {
                loadFavMovieData();
            }
            else
            {
                loadMovieData(defaultList);
            }
        }
    }

    private void loadMovieData(String typeOfMovies)
    {
        new FetchMovieTask().execute(typeOfMovies);
    }

    private void loadFavMovieData()
    {

getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);
            //getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);



    }

    private void nextTask(Cursor cursor)
    {
        mCursor = cursor;
        // Query here from cursor.  Give recyclerview the queried data.
        //Log.d(TAG, "does it come here twice");
        favMovieIDList = new ArrayList<Integer>();
        ArrayList<Movie> favMovieList = new ArrayList<Movie>();

        try {
            //Log.d(TAG, "what about here");

            while (mCursor.moveToNext())
            {
                int movieID = mCursor.getInt(INDEX_MOVIE_ID);
                //Log.d(TAG, String.valueOf(movieID));
                favMovieIDList.add(movieID);
            }
        }
        finally {
            //Log.d(TAG, "here?");
            cursor.close();

        }
        new FetchFavTask().execute(favMovieIDList);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {

        switch (id) {

            case ID_MOVIE_LOADER:

                Uri movieQueryUri = MovieContract.MovieEntry.CONTENT_URI;

                return new CursorLoader(this,
                        movieQueryUri,
                        FAVORITE_MOVIE_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        nextTask(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }

    public class FetchFavTask extends AsyncTask< ArrayList<Integer>, Void, ArrayList<Movie>>
    {

        @Override
        protected ArrayList<Movie> doInBackground(ArrayList<Integer>[] params)
        {
            if (params.length == 0)
            {
                return null;
            }

            // Array of Movie IDS from external database
            ArrayList<Integer> mids = params[0];

            ArrayList<Movie> favMovieList = new ArrayList<>();


            for (Integer id : mids)
            {
                // do first query here
                URL movieRequestURL = NetworkUtils.buildUrl(mContext, "details", String.valueOf(id));
                try
                {

                    Movie currentMovie;
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                    currentMovie = themoviedbJsonUtils.getMovieDetail(jsonMovieResponse);
                    favMovieList.add(currentMovie);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // for each of those movies and do two queries trailers and reviews
            for (Movie currentMovie : favMovieList)
            {
                URL movieRequestURLTrailer = NetworkUtils.buildUrl(mContext, "trailers", currentMovie.getmDBID());


                try
                {
                    String jsonMovieResponseTrailer = NetworkUtils.getResponseFromHttpUrl(movieRequestURLTrailer);


                    ArrayList<String> movieTrailers = themoviedbJsonUtils.getMovieTrailer(jsonMovieResponseTrailer);


                    currentMovie.setmMovieTrailers(movieTrailers);


                }
                 catch (JSONException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }


            }




            return favMovieList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieData)
        {
            if (movieData != null)
            {
                mAdapter.setMovieData(movieData);
            }
            else
            {
                //Log.d(TAG, "this failed terribly");
            }
        }


    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>>
    {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String typeOfMovies = params[0];

            URL movieRequestURL = NetworkUtils.buildUrlOrg(mContext, typeOfMovies);

            String jsonMovieResponse = null;
            ArrayList<Movie> movieList = null;
            try {
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                movieList = themoviedbJsonUtils.getMovieTitles(jsonMovieResponse);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {

                // for each of those movies and do two queries trailers and reviews
                for (Movie currentMovie : movieList) {
                    URL movieRequestURLTrailer = NetworkUtils.buildUrl(mContext, "trailers", currentMovie.getmDBID());
                    //URL movieRequestURLReviews = NetworkUtils.buildUrl(mContext, "reviews", currentMovie.getmDBID());

                    String jsonMovieResponseTrailer = NetworkUtils.getResponseFromHttpUrl(movieRequestURLTrailer);
                    //String jsonMOvieResponseReview = NetworkUtils.getResponseFromHttpUrl(movieRequestURLReviews);

                    ArrayList<String> movieTrailers = themoviedbJsonUtils.getMovieTrailer(jsonMovieResponseTrailer);
                    //ArrayList<Reviews> movieReviews = themoviedbJsonUtils.getMovieReviews(jsonMOvieResponseReview);

                    currentMovie.setmMovieTrailers(movieTrailers);
                    //currentMovie.setmReviews(movieReviews);



                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

          /*  try {
                for (Movie currentMovie : movieList) {
                    URL movieRequestURLReviews = NetworkUtils.buildUrl(mContext, "reviews", currentMovie.getmDBID());
                    String jsonMOvieResponseReview = NetworkUtils.getResponseFromHttpUrl(movieRequestURLReviews);
                    ArrayList<Reviews> movieReviews = themoviedbJsonUtils.getMovieReviews(jsonMOvieResponseReview);
                    currentMovie.setmReviews(movieReviews);
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
*/

            return movieList;

        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieData)
        {
            if (movieData != null)
            {
                mAdapter.setMovieData(movieData);
            }
            else
            {
                //Log.d(TAG, "this failed terribly");
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Toast.makeText(mContext, "Work in Progress", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.popular_movies)
        {
            defaultList = "popular";
            loadMovieData("popular");
            return true;
        }
        if (id == R.id.top_rated)
        {
            defaultList = "top";
            loadMovieData("top");
            return true;
        }
        if (id == R.id.favorite_movies)
        {
            defaultList = "favorite";
            loadFavMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick() {

    }
}
