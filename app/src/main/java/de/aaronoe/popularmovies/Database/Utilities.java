package de.aaronoe.popularmovies.Database;

import android.content.ContentValues;

import de.aaronoe.popularmovies.Database.MoviesContract.MovieEntry;
import de.aaronoe.popularmovies.Movies.MovieItem;

/**
 *
 * Created by aaronoe on 20.02.17.
 */

public class Utilities {

    /**
     * This function takes the required fields of a {@link MovieItem} and puts them into
     * a {@link ContentValues} object to use the {@link MoviesContentProvider} to insert a
     * movie into the underlying Database ({@link MoviesDbHelper})
     *
     * @param movieItem a single MovieItem
     * @return a {@link ContentValues} object containing the required information to put the movie into the db
     */
    public static ContentValues getContentValuesForMovie(MovieItem movieItem) {

        ContentValues cv = new ContentValues();

        cv.put(MovieEntry.COLUMN_POSTER_PATH, movieItem.getmPosterPath());
        cv.put(MovieEntry.COLUMN_DESCRIPTION, movieItem.getmMovieDescription());
        cv.put(MovieEntry.COLUMN_TITLE, movieItem.getmTitle());
        cv.put(MovieEntry.COLUMN_MOVIE_ID, movieItem.getmMovieId());
        cv.put(MovieEntry.COLUMN_RELEASE_DATE, movieItem.getmReleaseDate());
        cv.put(MovieEntry.COLUMN_VOTE_AVERAGE, movieItem.getmVoteAverage());

        return cv;

    }

}
