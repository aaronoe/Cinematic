package de.aaronoe.cinematic.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import de.aaronoe.cinematic.BuildConfig
import de.aaronoe.cinematic.CinematicApp
import de.aaronoe.cinematic.model.remote.ApiInterface
import de.aaronoe.cinematic.model.oldAuth.RequestTokenOld
import de.aaronoe.cinematic.model.oldAuth.SessionId
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
                     val apiService : ApiInterface) : LoginContract.Presenter {

    val api_key = BuildConfig.MOVIE_DB_API_KEY
    var requestToken : RequestTokenOld? = null

    override fun getRequestToken() {
        val dialog = context.indeterminateProgressDialog(
                message = "Logging you in",
                title = "Please wait")
        dialog.show()

        val call = apiService.getRequestToken(CinematicApp.TMDB_API_KEY)

        call.enqueue(object : Callback<RequestTokenOld> {
            override fun onResponse(p0: Call<RequestTokenOld>?, response: Response<RequestTokenOld>?) {
                dialog.cancel()

                requestToken = response?.body()
                if (response == null || requestToken == null) {
                    view.showMessage("We could not log you in")
                    return
                }

                val uri = Uri.parse("https://www.themoviedb.org/authenticate/${(requestToken as RequestTokenOld).requestToken}?redirect_to=${CinematicApp.TMDB_REDIRECT_URI}")
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            }

            override fun onFailure(p0: Call<RequestTokenOld>?, p1: Throwable?) {
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

        val call = apiService.getSessionId(CinematicApp.TMDB_API_KEY, (requestToken as RequestTokenOld).requestToken)
        call.enqueue(object : Callback<SessionId> {
            override fun onResponse(p0: Call<SessionId>?, response: Response<SessionId>?) {
                dialog.cancel()
                if (response == null || response.body() == null) {
                    view.showMessage("Could not get your profile")
                    return
                }

                val sessionId = response.body()
                Log.e("Login - Access Token: ", sessionId.sessionId)

            }

            override fun onFailure(p0: Call<SessionId>?, p1: Throwable?) {
                dialog.cancel()
                view.showMessage("Could not get your profile")
            }
        })

    }
}