package de.aaronoe.popularmovies.Movies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Class to hold information on an individual movie
 * Created by aaron on 21.01.17.
 */

public class MovieItem implements Parcelable {

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("overview")
    private String mMovieDescription;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private int mMovieId;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("vote_average")
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

    public int getmMovieId() {
        return mMovieId;
    }

    protected MovieItem(Parcel in) {
        mPosterPath = in.readString();
        mMovieDescription = in.readString();
        mTitle = in.readString();
        mMovieId = in.readInt();
        mReleaseDate = in.readString();
        mVoteAverage = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPosterPath);
        dest.writeString(mMovieDescription);
        dest.writeString(mTitle);
        dest.writeInt(mMovieId);
        dest.writeString(mReleaseDate);
        if (mVoteAverage == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(mVoteAverage);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}