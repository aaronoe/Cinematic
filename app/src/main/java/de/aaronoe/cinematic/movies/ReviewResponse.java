package de.aaronoe.cinematic.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by aaronoe on 20.02.17.
 */

public class ReviewResponse {

    @SerializedName("id")
    private Integer id;

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private List<ReviewItem> results;

    @SerializedName("total_pages")
    private Integer totalPages;

    @SerializedName("total_results")
    private Integer totalResults;


    public List<ReviewItem> getResults() {
        return results;
    }

}
