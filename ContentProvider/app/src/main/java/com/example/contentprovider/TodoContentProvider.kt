package com.example.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.content.UriMatcher
import android.util.Log
import androidx.core.net.toUri

class TodoContentProvider : ContentProvider() {

    private val TAG = "TodoContentProvider"
    private lateinit var dbHelper: TodosDatabaseHelper

    companion object {
        const val AUTHORITY = "com.example.contentproviderdemo.provider"
        val BASE_URI = "content://$AUTHORITY".toUri()
        const val TODOS = 1
        const val TODO_ID = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "todos", TODOS)
            addURI(AUTHORITY, "todos/#", TODO_ID)
        }
    }

    override fun onCreate(): Boolean {
        dbHelper = TodosDatabaseHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        return when (uriMatcher.match(uri)) {
            TODOS -> {
                val cursor = db.query(
                    TodosDatabaseHelper.TABLE_TODOS,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
                cursor.setNotificationUri(context!!.contentResolver, uri)
                cursor
            }
            TODO_ID -> {
                val id = uri.lastPathSegment
                val cursor = db.query(
                    TodosDatabaseHelper.TABLE_TODOS,
                    projection,
                    "${TodosDatabaseHelper.COLUMN_ID} = ?",
                    arrayOf(id),
                    null,
                    null,
                    sortOrder
                )
                cursor.setNotificationUri(context!!.contentResolver, uri)
                cursor
            }
            else -> {
                Log.e(TAG, "Invalid URI for query: $uri")
                null
            }
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        return when (uriMatcher.match(uri)) {
            TODOS -> {
                val id = db.insert(TodosDatabaseHelper.TABLE_TODOS, null, values)
                if (id == -1L) {
                    Log.e(TAG, "Failed to insert into todos")
                    return null
                }
                val newUri = Uri.withAppendedPath(BASE_URI, "todos/$id")
                context!!.contentResolver.notifyChange(newUri, null)
                newUri
            }
            else -> {
                Log.e(TAG, "Invalid URI for insert: $uri")
                null
            }
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val db = dbHelper.writableDatabase
        return when (uriMatcher.match(uri)) {
            TODO_ID -> {
                val id = uri.lastPathSegment
                val count = db.update(
                    TodosDatabaseHelper.TABLE_TODOS,
                    values,
                    "${TodosDatabaseHelper.COLUMN_ID} = ?",
                    arrayOf(id)
                )
                if (count > 0) context!!.contentResolver.notifyChange(uri, null)
                count
            }
            else -> {
                Log.e(TAG, "Invalid URI for update: $uri")
                0
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        return when (uriMatcher.match(uri)) {
            TODO_ID -> {
                val id = uri.lastPathSegment
                val count = db.delete(
                    TodosDatabaseHelper.TABLE_TODOS,
                    "${TodosDatabaseHelper.COLUMN_ID} = ?",
                    arrayOf(id)
                )
                if (count > 0) context!!.contentResolver.notifyChange(uri, null)
                count
            }
            else -> {
                Log.e(TAG, "Invalid URI for delete: $uri")
                0
            }
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            TODOS -> "vnd.android.cursor.dir/vnd.$AUTHORITY.todos"
            TODO_ID -> "vnd.android.cursor.item/vnd.$AUTHORITY.todos"
            else -> null
        }
    }
}