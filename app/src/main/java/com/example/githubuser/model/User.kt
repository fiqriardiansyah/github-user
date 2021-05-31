package com.example.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: Int = 0,
    var username: String = "",
    var avatar: String = "",
    var name: String = "",
    var followerUrl: String = "",
    var followingUrl: String = "",
    var followers: Int = 0,
    var following: Int = 0,
    var company: String = "",
    var location: String = "",
    var repository: Int = 0
) : Parcelable