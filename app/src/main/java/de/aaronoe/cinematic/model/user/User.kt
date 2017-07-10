package de.aaronoe.cinematic.model.user

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by private on 7/9/17.
 *
 */
data class User(val id : Int,
                val username : String,
                val name : String) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readInt(),
    source.readString(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(username)
        dest.writeString(name)
    }
}