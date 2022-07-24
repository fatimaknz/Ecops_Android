package com.offline.ecop.database

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.model.Police

class PoliceDAO(private val mContext: Context) : DatabaseHelper(mContext) {

    fun addPolice(police: Police): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(UID, police.UID)
        contentValues.put(NAME, police.name)
        contentValues.put(AGE, police.age)
        contentValues.put(DESIGNATION, police.designation)
        contentValues.put(USER_NAME, police.username)
        contentValues.put(PASSWORD, police.password)
        contentValues.put(DISTRICT, police.district)
        contentValues.put(POLICE_STATION, police.policeStation)

        val row = db.insertWithOnConflict(
            TABLE_NAME,
            null,
            contentValues,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        sendAddPoliceBraodcast()
        return row > -1
    }

    fun policeLogin(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME " +
                " WHERE $USER_NAME = '$username' AND $PASSWORD = '$password'"

        val cursor = db.rawQuery(query, null)
        val rowCount = cursor.count
        cursor.close()

        return rowCount > 0
    }

    fun changePassword(username: String?, newPassword: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(PASSWORD, newPassword)

        val row = db.update(TABLE_NAME, contentValues, "$USER_NAME=?", arrayOf(username))

        return row > 0
    }

    fun getPoliceUIDByUsername(username: String?): Long {
        var uid: Long = 0
        val db = this.readableDatabase
        val query = "SELECT $UID FROM $TABLE_NAME " +
                "WHERE $USER_NAME = '$username'"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            uid = cursor.getLong(cursor.getColumnIndex(UID))
        }
        cursor.close()
        return uid
    }

    fun getPoliceList(): List<Police> {
        val list: MutableList<Police> = mutableListOf()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val police = Police()
            police.name = cursor.getString(cursor.getColumnIndex(NAME))
            police.designation = cursor.getString(cursor.getColumnIndex(DESIGNATION))
            police.username = cursor.getString(cursor.getColumnIndex(USER_NAME))
            list.add(police)
        }
        return list
    }

    fun getPolice(uid: Long): Police {
        val police = Police()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME " +
                "WHERE $UID = $uid"

        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            police.name = cursor.getString(cursor.getColumnIndex(NAME))
            police.UID = uid
            police.username = cursor.getString(cursor.getColumnIndex(USER_NAME))
        }

        cursor.close()

        return police
    }

    fun getPoliceList(policeStation: String): List<Police> {
        val policeList = arrayListOf<Police>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME " +
                "WHERE $POLICE_STATION = '$policeStation'"

        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val police = Police()
            police.name = cursor.getString(cursor.getColumnIndex(NAME))
            police.UID = cursor.getLong(cursor.getColumnIndex(UID))
            police.username = cursor.getString(cursor.getColumnIndex(USER_NAME))
            policeList.add(police)
        }

        cursor.close()

        return policeList
    }


    private fun sendAddPoliceBraodcast() {
        val intent = Intent(AppConstant.BroadcastReceiverAction.ACTION_ADD_POLICE)
        mContext.sendBroadcast(intent)
    }

    companion object {
        private const val TABLE_NAME = "police"

        private const val UID = "uid"

        private const val NAME = "name"

        private const val AGE = "age"

        private const val DESIGNATION = "designation"

        private const val USER_NAME = "user_name"

        private const val PASSWORD = "password"

        private const val DISTRICT = "district"

        private const val POLICE_STATION = "police_station"

        fun getCreatePoliceTableQuery(): String {
            return "CREATE TABLE $TABLE_NAME (" +
                    "$UID INTEGER PRIMARY KEY, " +
                    "$NAME TEXT, " +
                    "$AGE INTEGER, " +
                    "$DESIGNATION TEXT, " +
                    "$USER_NAME TEXT UNIQUE, " +
                    "$PASSWORD TEXT, " +
                    "$DISTRICT TEXT, " +
                    "$POLICE_STATION TEXT);"
        }
    }
}