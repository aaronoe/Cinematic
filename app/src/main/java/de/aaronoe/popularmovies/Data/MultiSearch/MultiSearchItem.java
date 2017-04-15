package de.aaronoe.popularmovies.Data.MultiSearch;

import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by aaron on 15.04.17.
 */

public class MultiSearchItem {

    @SerializedName("media_type")
    private
    String mediaType;

    @SerializedName("id")
    private
    Integer id;


    //Person
    @SerializedName("profile_path")
    private
    String profilePath;

    @SerializedName("name")
    private
    String name;


    //Movie
    @SerializedName("title")
    private
    String title;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private
    String posterPath;


    //TV
    @SerializedName("first_air_date")
    private
    String firstAirDate;


    public Integer getId() {
        return id;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getName() {
        return name;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

}
