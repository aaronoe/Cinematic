package de.aaronoe.cinematic.model.auth

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by private on 7/9/17.
 *
 */
data class RequestToken(val success : Boolean,
                        @SerializedName("expires_at") val expiresAt : String,
                        @SerializedName("request_token") val requestToken : String) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<RequestToken> = object : Parcelable.Creator<RequestToken> {
            override fun createFromParcel(source: Parcel): RequestToken = RequestToken(source)
            override fun newArray(size: Int): Array<RequestToken?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    1 == source.readInt(),
    source.readString(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt((if (success) 1 else 0))
        dest.writeString(expiresAt)
        dest.writeString(requestToken)
    }
}