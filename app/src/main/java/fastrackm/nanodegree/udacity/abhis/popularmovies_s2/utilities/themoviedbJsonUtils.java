package fastrackm.nanodegree.udacity.abhis.popularmovies_s2.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models.Movie;
import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models.Reviews;

import static android.content.ContentValues.TAG;

/**
 * Created by abhis on 6/12/2017.
 */

public final class themoviedbJsonUtils
{
    public static ArrayList<Movie> getMovieTitles(String moviesJsonStr) throws JSONException {
        ArrayList<Movie> parsedMovieData = new ArrayList<>();

        final String MOVIE_ID = "id";
        final String MOVIE_LIST = "results";
        final String POSTER_PATH = "poster_path";
        final String BACKDROP_PATH = "backdrop_path";
        final String PLOT_SYNOPSIS = "overview";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_TITLE = "original_title";
        final String USER_RATING = "vote_average";
        final String SUCCESS_STATUS = "false";
        final String baseURL = "http://image.tmdb.org/t/p/w342/";

        JSONObject movieJson = new JSONObject(moviesJsonStr);
        Log.d(TAG, moviesJsonStr);
        Log.d(TAG, String.valueOf(movieJson));

        // Check if there is an error
        if (movieJson.has(SUCCESS_STATUS)) {
            if (movieJson.getString(SUCCESS_STATUS).equals("false")) {
                Log.d(TAG, String.valueOf(movieJson));
                return null;
            }
        }

        JSONArray movieArray = movieJson.getJSONArray(MOVIE_LIST);
        Log.d(TAG, String.valueOf(movieArray));

        //parsedMovieData = new Movie[movieArray.length()];

        for (int i = 0; i <movieArray.length(); i++)
        {
            String id;
            String title;
            String posterPath;
            String backDropPath;
            String plot;
            double rating;
            String date;


            JSONObject currentMovie = movieArray.getJSONObject(i);
            //Log.d(TAG, String.valueOf(currentMovie));

            id = currentMovie.getString(MOVIE_ID);

            title = currentMovie.getString(MOVIE_TITLE);

            posterPath = baseURL.concat(currentMovie.getString(POSTER_PATH).substring(1));

            backDropPath = baseURL.concat(currentMovie.getString(BACKDROP_PATH).substring(1));

            plot = currentMovie.getString(PLOT_SYNOPSIS);

            rating = currentMovie.getDouble(USER_RATING);

            date = currentMovie.getString(RELEASE_DATE);


            Movie currentMovieObject = new Movie(id, title, posterPath, backDropPath, plot, rating, date);
            parsedMovieData.add(currentMovieObject);
        }

        Log.d(TAG, String.valueOf(parsedMovieData));

        return parsedMovieData;
    }

    public static Movie getMovieDetail(String moviesJsonStr) throws JSONException
    {
        final String MOVIE_ID = "id";
        final String POSTER_PATH = "poster_path";
        final String BACKDROP_PATH = "backdrop_path";
        final String PLOT_SYNOPSIS = "overview";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_TITLE = "original_title";
        final String USER_RATING = "vote_average";
        final String SUCCESS_STATUS = "false";
        final String baseURL = "http://image.tmdb.org/t/p/w342/";

        JSONObject movieJson = new JSONObject(moviesJsonStr);
        Log.d(TAG, moviesJsonStr);
        Log.d(TAG, String.valueOf(movieJson));

        // Check if there is an error
        if (movieJson.has(SUCCESS_STATUS)) {
            if (movieJson.getString(SUCCESS_STATUS).equals("false")) {
                Log.d(TAG, String.valueOf(movieJson));
                return null;
            }
        }

        String id;
        String title;
        String posterPath;
        String backDropPath;
        String plot;
        double rating;
        String date;

        //Log.d(TAG, String.valueOf(currentMovie));

        id = movieJson.getString(MOVIE_ID);

        title = movieJson.getString(MOVIE_TITLE);

        posterPath = baseURL.concat(movieJson.getString(POSTER_PATH).substring(1));

        backDropPath = baseURL.concat(movieJson.getString(BACKDROP_PATH).substring(1));

        plot = movieJson.getString(PLOT_SYNOPSIS);

        rating = movieJson.getDouble(USER_RATING);

        date = movieJson.getString(RELEASE_DATE);

        return new Movie(id, title, posterPath, backDropPath, plot, rating, date);
    }

    public static ArrayList<String> getMovieTrailer(String moviesJsonStr) throws JSONException
    {
        ArrayList<String> movieTrailers = new ArrayList<>();

        final String KEY = "key";
        final String SUCCESS_STATUS = "false";

        final String MOVIE_LIST = "results";

        JSONObject movieJson = new JSONObject(moviesJsonStr);

        // Check if there is an error
        if (movieJson.has(SUCCESS_STATUS)) {
            if (movieJson.getString(SUCCESS_STATUS).equals("false")) {
                Log.d(TAG, String.valueOf(movieJson));
                return null;
            }
        }


        JSONArray movieArray = movieJson.getJSONArray(MOVIE_LIST);
        Log.d(TAG, String.valueOf(movieArray));

        //parsedMovieData = new Movie[movieArray.length()];

        for (int i = 0; i <movieArray.length(); i++)
        {
            String key;


            JSONObject currentMovie = movieArray.getJSONObject(i);

            key = currentMovie.getString(KEY);

            movieTrailers.add(key);

        }

        return movieTrailers;
    }

    public static ArrayList<Reviews> getMovieReviews(String moviesJsonStr) throws JSONException
    {
        ArrayList<Reviews> movieReviews = new ArrayList<>();

        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String SUCCESS_STATUS = "false";

        final String MOVIE_LIST = "results";

        JSONObject movieJson = new JSONObject(moviesJsonStr);

        // Check if there is an error
        if (movieJson.has(SUCCESS_STATUS)) {
            if (movieJson.getString(SUCCESS_STATUS).equals("false")) {
                Log.d(TAG, String.valueOf(movieJson));
                return null;
            }
        }


        JSONArray movieArray = movieJson.getJSONArray(MOVIE_LIST);
        Log.d(TAG, String.valueOf(movieArray));

        if (movieArray != null)
        {
            for (int i = 0; i <movieArray.length(); i++)
            {
                String author;
                String content;


                JSONObject currentMovie = movieArray.getJSONObject(i);

                author = currentMovie.getString(AUTHOR);
                content = currentMovie.getString(CONTENT);

                Reviews currentMovieReview = new Reviews(author, content);
                movieReviews.add(currentMovieReview);

            }

            return movieReviews;
        }
        return null;

    }



}
