package fastrackm.nanodegree.udacity.abhis.popularmovies_s2.data;

import android.net.Uri;
import android.provider.BaseColumns;

import fastrackm.nanodegree.udacity.abhis.popularmovies_s2.models.Movie;

/**
 * Created by abhis on 7/17/2017.
 */

public class MovieContract
{
    public static final String CONTENT_AUTHORITY = "com.fastrackm.nanodegree.udacity.abhis.popularmovies_s2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_TITLE = "movie_title";
    }
}
