package de.aaronoe.cinematic.model.oldAuth

import com.google.gson.annotations.SerializedName

/**
 * Created by aaron on 10.07.17.
 *
 */
data class SessionId(@SerializedName("session_id") val sessionId : String,
                     val success : Boolean)