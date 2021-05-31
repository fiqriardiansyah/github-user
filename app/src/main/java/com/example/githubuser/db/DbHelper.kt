package com.example.githubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_AVATAR
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_COMPANY
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWERS
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWER_URL
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWINGS
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_FOLLOWING_URL
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_ID
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_LOCATION
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_NAME
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_REPOSITORY
import com.example.githubuser.db.DbContract.UserColumns.Companion.COLUMN_USERNAME
import com.example.githubuser.db.DbContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuser.model.User

class DbHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    val context = context

    companion object {
        const val DATABASE_NAME = "github.db"
        const val DATABASE_VERSION = 1
    }

    val query = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY," +
            "$COLUMN_NAME TEXT," +
            "$COLUMN_USERNAME TEXT," +
            "$COLUMN_AVATAR TEXT," +
            "$COLUMN_FOLLOWER_URL TEXT," +
            "$COLUMN_FOLLOWING_URL TEXT," +
            "$COLUMN_COMPANY TEXT," +
            "$COLUMN_LOCATION TEXT," +
            "$COLUMN_REPOSITORY INTEGER," +
            "$COLUMN_FOLLOWERS INTEGER," +
            "$COLUMN_FOLLOWINGS INTEGER" +
            ");"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun insert(values: ContentValues): Long{
        val db = writableDatabase
        return db.insert(TABLE_NAME,null,values)
    }

    fun delete(username: String): Int{
        val db = writableDatabase
        return db.delete(TABLE_NAME,"$COLUMN_USERNAME LIKE ?", arrayOf(username))
    }

    fun query(): Cursor{
        val db = readableDatabase
        return  db.query(TABLE_NAME,null,null,null,null,null,null,null)
    }

    fun queryByUsername(username: String): Cursor{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ?"
        return db.rawQuery(query, arrayOf(username))
    }

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