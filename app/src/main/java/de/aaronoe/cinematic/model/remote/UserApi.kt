package de.aaronoe.cinematic.model.remote

import de.aaronoe.cinematic.model.auth.AccessToken
import de.aaronoe.cinematic.model.auth.RequestToken
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

/**
 * Created by private on 7/9/17.
 *
 */
interface UserApi {

    data class BodyRedirectUri(val redirect_to : String)
    data class BodyRequestToken(val request_token : String)
    data class BodyAccessToken(val access_token : String)

    @POST("auth/request_token")
    fun getRequestToken(@Body bodyRedirectUri: BodyRedirectUri) : Call<RequestToken>

    @POST("auth/access_token")
    fun getAccessToken(@Body bodyRequestToken: BodyRequestToken) : Call<AccessToken>

    @DELETE("auth/access_token")
    fun deleteAccessToken(@Body bodyAccessToken: BodyAccessToken) : Call<AccessToken>

}