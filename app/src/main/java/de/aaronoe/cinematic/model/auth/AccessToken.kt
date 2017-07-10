package de.aaronoe.cinematic.model.auth

import com.google.gson.annotations.SerializedName

/**
 * Created by private on 7/9/17.
 *
 */
data class AccessToken(
        @SerializedName("status_message") val statusMessage: String,
        @SerializedName("access_token") val accessToken: String,
        @SerializedName("account_id") val accountId: String,
        @SerializedName("status_code") val statusCode: Int,
        val success : Boolean)