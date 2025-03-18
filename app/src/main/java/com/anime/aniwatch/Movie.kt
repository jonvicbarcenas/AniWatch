package com.anime.aniwatch

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    val rank: Int,
    val name: String,
    val imageResId: Int,
    val duration: String,
    val description: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(), // Read rank first
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(rank) // Write rank first
        parcel.writeString(name)
        parcel.writeInt(imageResId)
        parcel.writeString(duration)
        parcel.writeString(description)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}
