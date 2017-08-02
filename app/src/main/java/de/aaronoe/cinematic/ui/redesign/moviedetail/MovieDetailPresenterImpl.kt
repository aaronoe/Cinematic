package de.aaronoe.cinematic.ui.redesign.moviedetail

import android.app.Activity
import android.os.Build
import de.aaronoe.cinematic.BuildConfig
import de.aaronoe.cinematic.CinematicApp
import de.aaronoe.cinematic.model.Crew.Credits
import de.aaronoe.cinematic.model.FullMovie.FullMovie
import de.aaronoe.cinematic.model.remote.ApiInterface
import de.aaronoe.cinematic.movies.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by private on 8/2/17.
 *
 */
class MovieDetailPresenterImpl(val movieId : Int, val view : MovieDetailContract.View) : MovieDetailContract.Presenter {

    @Inject
    lateinit var apiService : ApiInterface

    init {
        ((view as Activity).application as CinematicApp).netComponent.inject(this)
    }

    override fun downloadDetails() {

        val call = apiService.getMovieDetails(movieId, BuildConfig.MOVIE_DB_API_KEY)

        call.enqueue(object : Callback<FullMovie> {
            override fun onResponse(p0: Call<FullMovie>?, response: Response<FullMovie>?) {
                if (response == null || !response.isSuccessful || response.body() == null) {
                    view.showErrorLoading()
                    return
                }
                view.showDetails(response.body())
            }

            override fun onFailure(p0: Call<FullMovie>?, p1: Throwable?) {
                view.showErrorLoading()
            }
        })

    }

    override fun downloadCast() {

        val call = apiService.getCredits(movieId, BuildConfig.MOVIE_DB_API_KEY)

        call.enqueue(object : Callback<Credits> {
            override fun onResponse(p0: Call<Credits>?, response: Response<Credits>?) {
                if (response == null || !response.isSuccessful || response.body() == null) {
                    view.showErrorLoading()
                    return
                }
                view.showCast(response.body())
            }

            override fun onFailure(p0: Call<Credits>?, p1: Throwable?) {
                view.showErrorLoading()
            }
        })

    }

    override fun downloadSimilar() {

        val call = apiService.getRecommendations(movieId, BuildConfig.MOVIE_DB_API_KEY)

        call.enqueue(object : Callback<MovieResponse> {

            override fun onResponse(p0: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                if (response == null || !response.isSuccessful || response.body() == null) {
                    view.showErrorLoading()
                    return
                }
                view.showSimilar(response.body().results)
            }

            override fun onFailure(p0: Call<MovieResponse>?, p1: Throwable?) {
                view.showErrorLoading()
            }

        })

    }
}