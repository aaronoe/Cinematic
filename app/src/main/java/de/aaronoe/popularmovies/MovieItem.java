package de.aaronoe.popularmovies;

import java.io.Serializable;

/**
 * Class to hold information on an individual movie
 * Created by aaron on 21.01.17.
 */

public class MovieItem implements Serializable {

    private String mPosterPath;
    private String mMovieDescription;
    private String mTitle;
    private int mMovieId;
    private String mReleaseDate;
    private Double mVoteAverage;

    public MovieItem(String PosterPath, String MovieDescription, String Title,
                     int MovieId, String releaseDate, Double voteAverage) {
        mPosterPath = PosterPath;
        mMovieDescription = MovieDescription;
        mTitle = Title;
        mMovieId = MovieId;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmMovieDescription() {
        return mMovieDescription;
    }

    public Double getmVoteAverage() {
        return mVoteAverage;
    }
}
