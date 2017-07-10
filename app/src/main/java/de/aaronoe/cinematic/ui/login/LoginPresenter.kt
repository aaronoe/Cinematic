package de.aaronoe.cinematic.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import de.aaronoe.cinematic.BuildConfig
import de.aaronoe.cinematic.CinematicApp
import de.aaronoe.cinematic.model.auth.AccessToken
import de.aaronoe.cinematic.model.auth.RequestToken
import de.aaronoe.cinematic.model.remote.UserApi
import org.jetbrains.anko.indeterminateProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by private on 7/9/17.
 *
 */
class LoginPresenter(val context: Context,
                     val view : LoginContract.View,
                     val apiService : UserApi) : LoginContract.Presenter {

    val api_key = BuildConfig.MOVIE_DB_API_KEY
    var requestToken : RequestToken? = null

    override fun getRequestToken() {
        val dialog = context.indeterminateProgressDialog(
                message = "Logging you in",
                title = "Please wait")
        dialog.show()

        val call = apiService.getRequestToken(UserApi.BodyRedirectUri(CinematicApp.TMDB_REDIRECT_URI))

        call.enqueue(object : Callback<RequestToken> {
            override fun onResponse(p0: Call<RequestToken>?, response: Response<RequestToken>?) {
                dialog.cancel()

                if (response == null || response.body() == null) {
                    view.showMessage("We could not log you in")
                    return
                }

                requestToken = response.body()
                val uri = Uri.parse("https://www.themoviedb.org/auth/access?request_token=${response.body().requestToken}")
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                Log.e("Login - RequestToken: ", requestToken?.requestToken)
            }

            override fun onFailure(p0: Call<RequestToken>?, p1: Throwable?) {
                dialog.cancel()
                view.showMessage("We could not log you in")
            }
        })
    }

    override fun getAccessToken() {

        if (requestToken == null) {
            view.showMessage("We could not log you in")
            return
        }

        val dialog = context.indeterminateProgressDialog(
                message = "Getting your profile",
                title = "Please wait")
        dialog.show()

        val call = apiService.getAccessToken(UserApi.BodyRequestToken((requestToken as RequestToken).requestToken))
        call.enqueue(object : Callback<AccessToken> {
            override fun onResponse(p0: Call<AccessToken>?, response: Response<AccessToken>?) {
                dialog.cancel()
                if (response == null || response.body() == null) {
                    view.showMessage("Could not get your profile")
                    return
                }

                val accessToken = response.body()
                Log.e("Login - Access Token: ", accessToken.accessToken)

            }

            override fun onFailure(p0: Call<AccessToken>?, p1: Throwable?) {
                dialog.cancel()
                view.showMessage("Could not get your profile")
            }
        })

    }
}