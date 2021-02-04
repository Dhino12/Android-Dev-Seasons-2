package com.example.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuser.database.DatabaseContract.AUTHORITY
import com.example.githubuser.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser.database.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuser.database.UserHelper

class GithubUserProvider : ContentProvider() {

    companion object{
        private const val GITUSER = 1
        private const val GITUSER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userHelper:UserHelper

        init {
            sUriMatcher.addURI(AUTHORITY,TABLE_NAME, GITUSER)
            sUriMatcher.addURI(AUTHORITY,"$TABLE_NAME/#", GITUSER_ID)
        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val cursor:Cursor? = when(sUriMatcher.match(uri)){
            GITUSER -> userHelper.queryAll()
            GITUSER_ID -> userHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added:Long = when(GITUSER){
            sUriMatcher.match(uri) -> userHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val update = when(GITUSER_ID){
            sUriMatcher.match(uri) -> userHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return update

    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted:Int = when(GITUSER_ID){
            sUriMatcher.match(uri) -> userHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI,null)
        return deleted
    }

}
