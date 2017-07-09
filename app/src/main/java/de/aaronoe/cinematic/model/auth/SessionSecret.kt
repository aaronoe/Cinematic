package de.aaronoe.cinematic.model.auth

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by private on 7/9/17.
 *
 */
data class SessionSecret(val success : Boolean,
                         @SerializedName("session_id") val sessionId: String) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<SessionSecret> = object : Parcelable.Creator<SessionSecret> {
            override fun createFromParcel(source: Parcel): SessionSecret = SessionSecret(source)
            override fun newArray(size: Int): Array<SessionSecret?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    1 == source.readInt(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt((if (success) 1 else 0))
        dest.writeString(sessionId)
    }
}