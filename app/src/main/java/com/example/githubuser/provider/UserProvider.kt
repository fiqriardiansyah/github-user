package com.example.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.githubuser.db.DbContract.UserColumns.Companion.AUTHORITY
import com.example.githubuser.db.DbContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser.db.DbContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuser.db.DbHelper
import cz.msebera.android.httpclient.auth.AUTH

class UserProvider : ContentProvider() {

    private lateinit var db: DbHelper

    companion object {
        const val USER = 1
        const val USER_USERNAME = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME,USER)
            uriMatcher.addURI(AUTHORITY,"$TABLE_NAME/*",USER_USERNAME)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted:Int = when(USER_USERNAME){
            uriMatcher.match(uri) -> db.delete(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when(USER){
            uriMatcher.match(uri) -> db.insert(values!!)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        db = DbHelper(context as Context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(uriMatcher.match(uri)){
            USER -> {
                Log.e("URI","1")
                db.query()
            }
            USER_USERNAME -> {
                Log.e("URI","2")
                val cursor = db.queryByUsername(uri.lastPathSegment.toString())
                return cursor
            }
            else -> null
        }

    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}