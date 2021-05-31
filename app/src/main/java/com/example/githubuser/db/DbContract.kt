package com.example.githubuser.db

import android.net.Uri
import android.provider.BaseColumns
import cz.msebera.android.httpclient.auth.AUTH

class DbContract {
    internal class UserColumns: BaseColumns {
        companion object {

            const val AUTHORITY = "com.example.githubuser"
            const val SCHEME = "content"

            const val TABLE_NAME = "favorite"
            const val COLUMN_ID = "_id"
            const val COLUMN_NAME = "name"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_AVATAR = "avatar"
            const val COLUMN_FOLLOWER_URL = "follower_url"
            const val COLUMN_FOLLOWING_URL = "following_url"
            const val COLUMN_COMPANY = "company"
            const val COLUMN_LOCATION = "location"
            const val COLUMN_REPOSITORY = "repository"
            const val COLUMN_FOLLOWERS = "followers"
            const val COLUMN_FOLLOWINGS = "followings"

            val CONTENT_URI: Uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME).build()
        }
    }
}