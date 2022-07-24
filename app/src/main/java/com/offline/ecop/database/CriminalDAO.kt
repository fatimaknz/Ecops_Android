package com.offline.ecop.database

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.model.Criminal

class CriminalDAO(private val mContext: Context) : DatabaseHelper(mContext) {

    fun addCriminal(criminal: Criminal): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(UID, criminal.UID)
        contentValues.put(FIR_NO, criminal.FIRNumber)
        contentValues.put(NAME, criminal.name)
        contentValues.put(ADDRESS, criminal.address)
        contentValues.put(DATE_OF_BIRTH, criminal.dateOfBirth)
        contentValues.put(GENDER, criminal.gender)
        contentValues.put(HEIGHT, criminal.height)
        contentValues.put(WEIGHT, criminal.weight)
        contentValues.put(DISTRICT, criminal.district)
        contentValues.put(POLICE_STATION, criminal.policeStation)
        contentValues.put(CRIME_DETAIL, criminal.crimeDetail)
        contentValues.put(DATE_OF_CRIME, criminal.dateOfCrime)
        contentValues.put(PROFILE_IMAGE_DATA, criminal.imageData)


        val row = db.insertWithOnConflict(
            TABLE_NAME,
            null,
            contentValues,
            SQLiteDatabase.CONFLICT_IGNORE
        )

        if (row > -1) {
            sendBroadcast()
            return true
        } else {
            return false
        }
    }

    fun getCriminalList(): List<Criminal> {
        val criminalList = mutableListOf<Criminal>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val criminal = Criminal()
            criminal.UID = cursor.getLong(cursor.getColumnIndex(UID))
            criminal.name = cursor.getString(cursor.getColumnIndex(NAME))
            criminal.imageData = cursor.getString(cursor.getColumnIndex(PROFILE_IMAGE_DATA))
            criminalList.add(criminal)
        }
        cursor.close()
        return criminalList
    }

    fun getCriminalDetail(uid: Long): Criminal {
        val criminal = Criminal()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME " +
                "WHERE $UID = $uid"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            criminal.UID = uid
            criminal.FIRNumber = cursor.getInt(cursor.getColumnIndex(FIR_NO))
            criminal.name = cursor.getString(cursor.getColumnIndex(NAME))
            criminal.address = cursor.getString(cursor.getColumnIndex(ADDRESS))
            criminal.gender = cursor.getString(cursor.getColumnIndex(GENDER))
            criminal.dateOfBirth = cursor.getLong(cursor.getColumnIndex(DATE_OF_BIRTH))
            criminal.height = cursor.getString(cursor.getColumnIndex(HEIGHT))
            criminal.weight = cursor.getString(cursor.getColumnIndex(WEIGHT))
            criminal.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
            criminal.policeStation = cursor.getString(cursor.getColumnIndex(POLICE_STATION))
            criminal.crimeDetail = cursor.getString(cursor.getColumnIndex(CRIME_DETAIL))
            criminal.dateOfCrime = cursor.getLong(cursor.getColumnIndex(DATE_OF_CRIME))
            criminal.imageData = cursor.getString(cursor.getColumnIndex(PROFILE_IMAGE_DATA))
        }
        return criminal
    }

    fun getCriminalCount(): Int {
        var count: Int = 0
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME "

        val cursor = db.rawQuery(query, null)
        count = cursor.count
        cursor.close()

        return count
    }

    private fun sendBroadcast() {
        val intent = Intent(AppConstant.BroadcastReceiverAction.ACTION_ADD_CRIMINAL)
        mContext.sendBroadcast(intent)
    }


    companion object {

        private const val TABLE_NAME = "criminals"

        private const val UID = "uid"

        private const val FIR_NO = "fir_no"

        private const val NAME = "name"

        private const val ADDRESS = "address"

        private const val DATE_OF_BIRTH = "date_of_birth"

        private const val GENDER = "gender"

        private const val HEIGHT = "height"

        private const val WEIGHT = "weight"

        private const val DISTRICT = "district"

        private const val POLICE_STATION = "police_station"

        private const val CRIME_DETAIL = "crime_detail"

        private const val DATE_OF_CRIME = "date_of_crime"

        private const val PROFILE_IMAGE_DATA = "profile_image_data"

        fun getCreateCriminalTableQuery(): String {
            return "CREATE TABLE $TABLE_NAME ( " +
                    "$UID INTEGER PRIMARY KEY, " +
                    "$FIR_NO INTEGER UNIQUE, " +
                    "$NAME TEXT, " +
                    "$ADDRESS TEXT, " +
                    "$DATE_OF_BIRTH INTEGER, " +
                    "$GENDER TEXT, " +
                    "$HEIGHT TEXT, " +
                    "$WEIGHT TEXT, " +
                    "$DISTRICT TEXT, " +
                    "$POLICE_STATION TEXT, " +
                    "$CRIME_DETAIL TEXT, " +
                    "$DATE_OF_CRIME INTEGER, " +
                    "$PROFILE_IMAGE_DATA TEXT);"
        }
    }
}