package com.uzlov.developerslife.repository.model

import android.os.Parcel
import android.os.Parcelable


data class Post(
    val author: String = "",
    val canVote: Boolean = false,
    val commentsCount: Int = 0,
    val date: String = "",
    val description: String = "",
    val fileSize: Int = 0,
    val gifSize: Int = 0,
    val gifURL: String = "",
    val height: String = "",
    val id: Int = 0,
    val previewURL: String = "",
    val type: String = "",
    val videoPath: String = "",
    val videoSize: Int = 0,
    val videoURL: String = "",
    val votes: Int = 0,
    val width: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(author)
        parcel.writeByte(if (canVote) 1 else 0)
        parcel.writeInt(commentsCount)
        parcel.writeString(date)
        parcel.writeString(description)
        parcel.writeInt(fileSize)
        parcel.writeInt(gifSize)
        parcel.writeString(gifURL)
        parcel.writeString(height)
        parcel.writeInt(id)
        parcel.writeString(previewURL)
        parcel.writeString(type)
        parcel.writeString(videoPath)
        parcel.writeInt(videoSize)
        parcel.writeString(videoURL)
        parcel.writeInt(votes)
        parcel.writeString(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}