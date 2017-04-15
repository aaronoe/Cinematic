package de.aaronoe.popularmovies.Data;

import de.aaronoe.popularmovies.Data.ActorCredits.Actor;
import de.aaronoe.popularmovies.Data.ActorCredits.ActorCredits;
import de.aaronoe.popularmovies.Data.Crew.Credits;
import de.aaronoe.popularmovies.Data.FullMovie.FullMovie;
import de.aaronoe.popularmovies.Data.MultiSearch.MultiSearchResponse;
import de.aaronoe.popularmovies.Data.TvShow.FullSeason.FullSeason;
import de.aaronoe.popularmovies.Data.TvShow.FullShow.TvShowFull;
import de.aaronoe.popularmovies.Data.TvShow.ShowsResponse;
import de.aaronoe.popularmovies.Movies.MovieResponse;
import de.aaronoe.popularmovies.Movies.ReviewResponse;
import de.aaronoe.popularmovies.Movies.VideoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * Created by aaron on 24.01.17.
 */

public interface ApiInterface {

    @GET("movie/{filter}")
    Call<MovieResponse> getMovies(@Path("filter") String filter,
                                  @Query("api_key") String apiKey);

    @GET("movie/{filter}")
    Call<MovieResponse> getPageOfMovies(@Path("filter") String filter,
                                        @Query("api_key") String apiKey,
                                        @Query("page") int pageNumber);

    @GET("movie/{id}/similar")
    Call<MovieResponse> getRecommendations(@Path("id") int id,
                                           @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") int id,
                                    @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideos(@Path("id") int id,
                                  @Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResponse> searchForMovies(@Query("query") String searchQuery,
                                        @Query("api_key") String apiKey);

    @GET("search/tv")
    Call<ShowsResponse> searchForShows(@Query("query") String searchQuery,
                                       @Query("api_key") String apiKey);


    @GET("search/multi")
    Call<MultiSearchResponse> multiSearch(@Query("query") String searchQuery,
                                          @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<Credits> getCredits(@Path("id") int id,
                             @Query("api_key") String apiKey);

    @GET("person/{person_id}")
    Call<Actor> getActorDetails(@Path("person_id") int id,
                                @Query("api_key") String apiKey);

    @GET("person/{person_id}/combined_credits")
    Call<ActorCredits> getActorCredits(@Path("person_id") int id,
                                       @Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<FullMovie> getMovieDetails(@Path("id") int movieId,
                                    @Query("api_key") String apiKey);

    @GET("tv/{filter}")
    Call<ShowsResponse> getTvShows(@Path("filter") String filter,
                                   @Query("api_key") String apiKey,
                                   @Query("page") int page);

    @GET("tv/{id}")
    Call<TvShowFull> getTvShowDetails(@Path("id") int id,
                                      @Query("api_key") String apiKey);

    @GET("tv/{tv_id}/season/{season_number}")
    Call<FullSeason> getTvSeasonDetails(@Path("tv_id") int id,
                                        @Path("season_number") int seasonNumber,
                                        @Query("api_key") String apiKey);

}