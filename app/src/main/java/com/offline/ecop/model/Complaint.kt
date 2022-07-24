package com.offline.ecop.model

class Complaint {

    var id: Long? = null

    var uid: Long? = null

    var name: String? = null

    var address: String? = null

    var contactNumber: Long? = null

    var dateOfOffence: Long? = null

    var district: String? = null

    var policeStation: String? = null

    var complaint: String? = null

    var status: String? = null

    var assignee: Long? = null

    companion object {
        const val STATUS_PENDING = "Pending"

        const val STATUS_APPROVED = "Approved"

        const val STATUS_PROCESSED = "Processed"
    }
}