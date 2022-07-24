package com.offline.ecop.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class AdminDAO(context: Context) : DatabaseHelper(context) {


    fun addAdmin(username: String, password: String): Boolean {

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USERNAME, username)
        contentValues.put(PASSWORD, password)

        val row =
            db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE)

        return row > -1
    }

    fun adminLogin(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME " +
                " WHERE $USERNAME = '$username' AND $PASSWORD = '$password'"

        val cursor = db.rawQuery(query, null)
        val rowCount = cursor.count
        cursor.close()

        return rowCount > 0
    }

    fun changePassword(username: String?, newPassword: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(PASSWORD, newPassword)

        val row = db.update(TABLE_NAME, contentValues, "$USERNAME=?", arrayOf(username))

        return row > 0
    }


    companion object {
        const val TABLE_NAME = "admin"

        const val ID = "_id"

        const val USERNAME = "username"

        const val PASSWORD = "password"

        fun getCreateAdminTableQuery(): String {
            return "CREATE TABLE $TABLE_NAME ( " +
                    "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$USERNAME TEXT UNIQUE, " +
                    "$PASSWORD TEXT);"
        }
    }
}