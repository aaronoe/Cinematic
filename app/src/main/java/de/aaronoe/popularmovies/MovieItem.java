package de.aaronoe.popularmovies;

/**
 * Class to hold information on an individual movie
 * Created by aaron on 21.01.17.
 */

public class MovieItem {

    String mPosterPath;
    String mMovieDescription;
    String mTitle;
    int mMovieId;

    public MovieItem(String PosterPath, String MovieDescription, String Title, int MovieId) {
        mPosterPath = PosterPath;
        mMovieDescription = MovieDescription;
        mTitle = Title;
        mMovieId = MovieId;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }
}
