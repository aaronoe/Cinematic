package de.aaronoe.popularmovies.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class specifies the table information for the favorite movies database
 * Created by aaron on 20.02.17.
 */

public class MoviesContract {

    // Content Authority and Base URI to query data
    static final String CONTENT_AUTHORITY = "de.aaronoe.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_FAVORITE = "faves";
    static final String PATH_FAVORITE_SHOWS = "shows_faves";


    /**
     * This class holds constants for the favored movies table
     */
    public static final class MovieEntry implements BaseColumns {

        // The content uri is the base uri with the path appended
        // This will be used to query the favorite movies
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();



        static final String TABLE_NAME = "fave_movies";

        // _ID is already implemented by extending the BaseColumns class
        /*
        We need columns to hold the following information for a movie:

        - Posterpath (String)
        - Description (String)
        - Title (String)
        - Movie ID (int)
        - Release Date (String)
        - Vote Average (Double)

         */

        static final String COLUMN_POSTER_PATH = "posterpath";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIE_ID = "movieid";
        static final String COLUMN_RELEASE_DATE = "releasedate";
        static final String COLUMN_VOTE_AVERAGE = "voteaverage";
        static final String COLUMN_BACKDROP_PATH = "backdroppath";


    }

    public static final class ShowEntry implements BaseColumns {

        // The content uri is the base uri with the path appended
        // This will be used to query the favorite shows
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_SHOWS).build();

        static final String TABLE_NAME = "fave_shows";

        static final String COLUMN_ID = "show_id";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_VOTE_AVERAGE = "vote_average";
        static final String COLUMN_FIRST_AIR_DATE = "first_air_date";
        static final String COLUMN_GENRES = "show_genres";
        static final String COLUMN_BACKDROP_PATH = "backdrop_path";

    }

}
