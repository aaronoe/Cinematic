package de.aaronoe.cinematic.Movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by aaronoe on 20.02.17.
 */

public class VideoResponse {


    @SerializedName("id")
    private Integer id;

    @SerializedName("results")
    private List<VideoItem> results;

    public List<VideoItem> getResults() {
        return results;
    }
}
