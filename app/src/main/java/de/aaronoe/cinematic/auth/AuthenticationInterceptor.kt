package de.aaronoe.cinematic.auth

import android.util.Log
import de.aaronoe.cinematic.CinematicApp
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Created by private on 7/9/17.
 *
 */
class AuthenticationInterceptor : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.e("AuthIntercept", "Logged in: " + CinematicApp.getInstance().mAuthManager.loggedIn)
        val request: Request
        if (CinematicApp.getInstance().mAuthManager.loggedIn) {
            // Private
            request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + CinematicApp.getInstance().mAuthManager.mAccessToken)
                    .addHeader("Content-Type" ,"application/json;charset=utf-8")
                    .build()
        } else {
            // Public
            request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + CinematicApp.TMDB_PUBLIC_ACCESS_TOKEN)
                    .addHeader("Content-Type" ,"application/json;charset=utf-8")
                    .build()
        }
        return chain.proceed(request)
    }
}