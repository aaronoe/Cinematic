package de.aaronoe.cinematic.model.auth

import com.google.gson.annotations.SerializedName

/**
 * Created by private on 7/9/17.
 *
 */
data class RequestToken(@SerializedName("status_message") val statusMessage : String,
                        @SerializedName("request_token") val requestToken : String,
                        @SerializedName("status_code") val statusCode : Int,
                        val success : Boolean)
