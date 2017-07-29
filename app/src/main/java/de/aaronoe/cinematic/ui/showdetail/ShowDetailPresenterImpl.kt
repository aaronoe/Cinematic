package de.aaronoe.cinematic.ui.showdetail

import android.app.Activity
import de.aaronoe.cinematic.BuildConfig
import de.aaronoe.cinematic.CinematicApp
import de.aaronoe.cinematic.model.Crew.Credits
import de.aaronoe.cinematic.model.TvShow.FullShow.TvShowFull
import de.aaronoe.cinematic.model.TvShow.ShowsResponse
import de.aaronoe.cinematic.model.remote.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by private on 7/22/17.
 *
 *
 */
class ShowDetailPresenterImpl(var view : ShowDetailContract.View) : ShowDetailContract.Presenter {

    @Inject
    lateinit var apiService: ApiInterface
    val API_KEY = BuildConfig.MOVIE_DB_API_KEY

    init {
        ((view as Activity).application as CinematicApp).netComponent.inject(this)
    }

    override fun downloadInfo(showId: Int) {
        val call = apiService.getTvShowDetails(showId, API_KEY)

        call.enqueue(object : Callback<TvShowFull> {
            override fun onResponse(call: Call<TvShowFull>, response: Response<TvShowFull>?) {
                if (response == null || response.body() == null) {
                    view.showErrorInfo()
                    return
                }
                view.showInfo(response.body())
            }

            override fun onFailure(call: Call<TvShowFull>, t: Throwable) {
                view.showErrorInfo()
            }
        })
    }

    override fun downloadCast(showId: Int) {
        val call = apiService.getTvShowCredits(showId, API_KEY)

        call.enqueue(object : Callback<Credits> {
            override fun onResponse(call: Call<Credits>?, response: Response<Credits>?) {
                if (response == null || response.body() == null) {
                    view.showErrorCast()
                    return
                }
                view.showCast(response.body())
            }

            override fun onFailure(call: Call<Credits>?, p1: Throwable?) {
                view.showErrorCast()
            }
        })
    }

    override fun downloadSimilar(showId: Int) {
        val call = apiService.getSimilarTvShows(showId, API_KEY)

        call.enqueue(object : Callback<ShowsResponse> {
            override fun onResponse(call: Call<ShowsResponse>?, response: Response<ShowsResponse>?) {
                if (response == null || response.body() == null || response.body().tvShows == null || response.body().tvShows.size == 0) {
                    view.showErrorSimilar()
                    return
                }
                view.showSimilar(response.body().tvShows)
            }

            override fun onFailure(call: Call<ShowsResponse>?, p1: Throwable?) {
                view.showErrorSimilar()
            }
        })
    }
}