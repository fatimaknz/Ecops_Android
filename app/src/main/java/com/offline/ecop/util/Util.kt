package com.offline.ecop.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.offline.ecop.activity.LauncherActivity
import com.offline.ecop.constant.SharedPreferenceConstant

object Util {

    const val CAMERA_REQUEST_CODE = 100

    fun logout(context: Context) {
        val preferenceUtil = SharedPreferenceUtil(context)
        preferenceUtil.putBoolean(SharedPreferenceConstant.LOGIN_STATUS, false)
        val intent = Intent(context, LauncherActivity::class.java)
        context.startActivity(intent)
        (context as Activity).finish()
    }

    fun checkCameraPermission(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun requestCameraPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }
}