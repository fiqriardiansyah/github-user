package com.example.consumerapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.consumerapp.db.DbContract.UserColumns.Companion.COLUMN_AVATAR
import com.example.consumerapp.db.DbContract.UserColumns.Companion.COLUMN_COMPANY
import com.example.consumerapp.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWERS
import com.example.consumerapp.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWER_URL
import com.example.consumerapp.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWINGS
import com.example.consumerapp.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWING_URL
import com.example.consumerapp.db.DbContract.UserColumns.Companion.COLUMN_ID
import com.example.consumerapp.db.DbContract.UserColumns.Companion.COLUMN_LOCATION
import com.example.consumerapp.db.DbContract.UserColumns.Companion.COLUMN_NAME
import com.example.consumerapp.db.DbContract.UserColumns.Companion.COLUMN_REPOSITORY
import com.example.consumerapp.db.DbContract.UserColumns.Companion.COLUMN_USERNAME
import com.example.consumerapp.db.DbContract.UserColumns.Companion.TABLE_NAME
import com.example.consumerapp.model.User

class DbHelper{

    fun parseCursorToArrayList(cursor: Cursor): ArrayList<User>{
        var users = ArrayList<User>()

        with(cursor){
            while (moveToNext()){
                val user = User()
                user.id = getInt(getColumnIndex(COLUMN_ID))
                user.name = getString(getColumnIndex(COLUMN_NAME))
                user.username = getString(getColumnIndex(COLUMN_USERNAME))
                user.followerUrl = getString(getColumnIndex(COLUMN_FOLLOWER_URL))
                user.followingUrl = getString(getColumnIndex(COLUMN_FOLLOWING_URL))
                user.location = getString(getColumnIndex(COLUMN_LOCATION))
                user.company = getString(getColumnIndex(COLUMN_COMPANY))
                user.avatar = getString(getColumnIndex(COLUMN_AVATAR))
                user.followers = getInt(getColumnIndex(COLUMN_FOLLOWERS))
                user.following = getInt(getColumnIndex(COLUMN_FOLLOWINGS))
                user.repository = getInt(getColumnIndex(COLUMN_REPOSITORY))
                users.add(user)
            }
        }

        return users
    }

}