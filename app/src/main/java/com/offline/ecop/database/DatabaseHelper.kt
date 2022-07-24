package com.offline.ecop.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

abstract class DatabaseHelper(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        Log.d(TAG, "inside onCreate()")

        db?.execSQL(AdminDAO.getCreateAdminTableQuery())
        db?.execSQL(PoliceDAO.getCreatePoliceTableQuery())
        db?.execSQL(CriminalDAO.getCreateCriminalTableQuery())
        db?.execSQL(ComplaintDAO.getCreateComplaintTableQuery())
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    companion object {
        private const val DATABASE_NAME = "ecop.db"

        /**
         * update version whenever there is change in database structure or in table structure
         * update only once per release
         * last updated App version 1.0.0
         */
        private val DATABASE_VERSION = 1

        private const val TAG = "DatabaseHelper"
    }
}