package com.offline.ecop.constant

object AppConstant {

    const val USER_ROLE = "user_role"

    object UserRole {
        const val ROLE_CITIZEN = "citizen"

        const val ROLE_ADMIN = "admin"

        const val ROLE_POLICE = "police"
    }

    object BroadcastReceiverAction {
        const val ACTION_ADD_POLICE = "action_add_police"
        const val ACTION_ADD_CRIMINAL = "action_add_criminal"
        const val ACTION_UPDATE_COMPLAINT = "action_update_complaint"
    }

    object BundleKey {
        const val COMPLAINT_ID = "complaint_id"
    }
}