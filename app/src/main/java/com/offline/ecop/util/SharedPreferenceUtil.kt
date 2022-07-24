package com.offline.ecop.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(val context: Context) {

    val mSharedPreferece: SharedPreferences = context.getSharedPreferences("ecop_pref", Context.MODE_PRIVATE)

    fun putString(key: String, value: String?) {
        val editor = mSharedPreferece.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defValue: String?): String? {
        return mSharedPreferece.getString(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = mSharedPreferece.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mSharedPreferece.getBoolean(key, defValue)
    }

    fun putLong(key: String, value: Long) {
        val editor = mSharedPreferece.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLong(key: String, defValue: Long): Long {
        return mSharedPreferece.getLong(key, defValue)
    }

}