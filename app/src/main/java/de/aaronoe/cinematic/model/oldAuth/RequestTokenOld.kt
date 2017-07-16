package de.aaronoe.cinematic.model.oldAuth

import com.google.gson.annotations.SerializedName

/**
 * Created by aaron on 10.07.17.
 */

data class RequestTokenOld(@SerializedName("request_token") val requestToken : String,
                           @SerializedName("expires_at") val expiration : String,
                           val success : Boolean)