package com.offline.ecop.database

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.model.Complaint

class ComplaintDAO(private val mContext: Context) : DatabaseHelper(mContext) {

    fun addComplaint(complaint: Complaint): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(UID, complaint.uid)
        contentValues.put(NAME, complaint.name)
        contentValues.put(ADDRESS, complaint.address)
        contentValues.put(CONTACT_NUMBER, complaint.contactNumber)
        contentValues.put(DATE_OF_OFFENCE, complaint.dateOfOffence)
        contentValues.put(DISTRICT, complaint.district)
        contentValues.put(POLICE_STATION, complaint.policeStation)
        contentValues.put(COMPLAINT, complaint.complaint)
        contentValues.put(STATUS, complaint.status)
        contentValues.put(ASSIGNEE, complaint.assignee)

        val row =
            db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE)

        return row
    }

    fun getComplaintList(): MutableList<Complaint> {
        val complaintList = mutableListOf<Complaint>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"

        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val complaint = Complaint()
            complaint.id = cursor.getLong(cursor.getColumnIndex(_ID))
            complaint.name = cursor.getString(cursor.getColumnIndex(NAME))
            complaint.dateOfOffence = cursor.getLong(cursor.getColumnIndex(DATE_OF_OFFENCE))
            complaint.complaint = cursor.getString(cursor.getColumnIndex(COMPLAINT))
            complaint.status = cursor.getString(cursor.getColumnIndex(STATUS))
            complaint.address = cursor.getString(cursor.getColumnIndex(ADDRESS))
            complaintList.add(complaint)
        }
        cursor.close()

        return complaintList
    }

    fun getComplaintListByUID(uid: Long): MutableList<Complaint> {
        val complaintList = mutableListOf<Complaint>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME " +
                "WHERE $ASSIGNEE = $uid"

        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val complaint = Complaint()
            complaint.id = cursor.getLong(cursor.getColumnIndex(_ID))
            complaint.name = cursor.getString(cursor.getColumnIndex(NAME))
            complaint.dateOfOffence = cursor.getLong(cursor.getColumnIndex(DATE_OF_OFFENCE))
            complaint.complaint = cursor.getString(cursor.getColumnIndex(COMPLAINT))
            complaint.status = cursor.getString(cursor.getColumnIndex(STATUS))
            complaint.address = cursor.getString(cursor.getColumnIndex(ADDRESS))
            complaintList.add(complaint)
        }
        cursor.close()

        return complaintList
    }

    fun getComplaintDetail(complaintId: Long): Complaint {
        val complaint = Complaint()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME " +
                "WHERE $_ID = $complaintId"

        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            complaint.name = cursor.getString(cursor.getColumnIndex(NAME))
            complaint.uid = cursor.getLong(cursor.getColumnIndex(UID))
            complaint.dateOfOffence = cursor.getLong(cursor.getColumnIndex(DATE_OF_OFFENCE))
            complaint.complaint = cursor.getString(cursor.getColumnIndex(COMPLAINT))
            complaint.status = cursor.getString(cursor.getColumnIndex(STATUS))
            complaint.address = cursor.getString(cursor.getColumnIndex(ADDRESS))
            complaint.contactNumber = cursor.getLong(cursor.getColumnIndex(CONTACT_NUMBER))
            complaint.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
            complaint.policeStation = cursor.getString(cursor.getColumnIndex(POLICE_STATION))
            complaint.assignee = cursor.getLong(cursor.getColumnIndex(ASSIGNEE))
        }

        cursor.close()

        return complaint
    }

    fun updateComplaint(status: String?, executorId: Long?, complaintId: Long): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        if (status != null) {
            contentValues.put(STATUS, status)
        }
        if (executorId != null && executorId != 0L) {
            contentValues.put(ASSIGNEE, executorId)
        }

        val update =
            db.update(TABLE_NAME, contentValues, "$_ID = ?", arrayOf(complaintId.toString()))

        return if (update > 0) {
            sendBroadcast()
            true
        } else {
            false
        }
    }

    fun searchComplaint(complaintId: Long): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME " +
                "WHERE $_ID = $complaintId"

        val cursor = db.rawQuery(query, null)
        val count = cursor.count
        cursor.close()

        return count > 0
    }

    private fun sendBroadcast() {
        val intent = Intent(AppConstant.BroadcastReceiverAction.ACTION_UPDATE_COMPLAINT)
        mContext.sendBroadcast(intent)
    }

    companion object {

        private const val TABLE_NAME = "complaints"

        private const val _ID = "id"

        private const val UID = "uid"

        private const val NAME = "name"

        private const val ADDRESS = "address"

        private const val CONTACT_NUMBER = "contact_number"

        private const val DATE_OF_OFFENCE = "date_of_offence"

        private const val DISTRICT = "district"

        private const val POLICE_STATION = "police_station"

        private const val COMPLAINT = "complaint"

        private const val STATUS = "status"

        private const val ASSIGNEE = "assignee"

        fun getCreateComplaintTableQuery(): String {
            return "CREATE TABLE $TABLE_NAME ( " +
                    "$_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$UID INTEGER UNIQUE, " +
                    "$NAME TEXT, " +
                    "$ADDRESS TEXT, " +
                    "$CONTACT_NUMBER INTEGER, " +
                    "$DATE_OF_OFFENCE INTEGER, " +
                    "$DISTRICT TEXT, " +
                    "$POLICE_STATION TEXT, " +
                    "$COMPLAINT TEXT, " +
                    "$STATUS TEXT, " +
                    "$ASSIGNEE TEXT);"
        }
    }
}