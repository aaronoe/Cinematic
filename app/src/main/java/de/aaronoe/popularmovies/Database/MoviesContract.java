package de.aaronoe.popularmovies.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class specifies the table information for the favorite movies database
 * Created by aaron on 20.02.17.
 */

public class MoviesContract {

    // Content Authority and Base URI to query data
    public static final String CONTENT_AUTHORITY = "de.aaronoe.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE = "faves";

    /**
     * This class holds constants for the favored movies table
     */
    public static final class MovieEntry implements BaseColumns {

        // The content uri is the base uri with the path appended
        // This will be used to query the favorite movies
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final Uri CONTENT_URI_SINGLE =
                Uri.parse(CONTENT_URI + "/#");


        public static final String TABLE_NAME = "fave_movies";

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

        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIE_ID = "movieid";
        public static final String COLUMN_RELEASE_DATE = "releasedate";
        public static final String COLUMN_VOTE_AVERAGE = "voteaverage";


    }

}
