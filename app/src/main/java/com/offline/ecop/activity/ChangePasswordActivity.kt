package com.offline.ecop.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.offline.ecop.R
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.constant.SharedPreferenceConstant
import com.offline.ecop.database.AdminDAO
import com.offline.ecop.database.PoliceDAO
import com.offline.ecop.util.SharedPreferenceUtil

class ChangePasswordActivity : AppCompatActivity() {

    private var mToolbar: Toolbar? = null
    private var mTilOldPassword: TextInputLayout? = null
    private var mTilNewPassword: TextInputLayout? = null
    private var mEtOldPassword: TextInputEditText? = null
    private var mEtNewPassword: TextInputEditText? = null
    private var mTvErrorMessage: TextView? = null

    private var mContext: Context? = null
    private var mUserName: String? = null
    private var mOldPassword: String? = null
    private var mRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        mContext = this

        mToolbar = findViewById(R.id.changePassword_toolbar)
        mTilOldPassword = findViewById(R.id.textInputLayout_changePassword_oldPassword)
        mTilNewPassword = findViewById(R.id.textInputLayout_changPassword_newPassword)
        mEtOldPassword = findViewById(R.id.textInputEditText_changePassword_oldPassword)
        mEtNewPassword = findViewById(R.id.textInputEditText_changePassword_newPassword)
        mTvErrorMessage = findViewById(R.id.textView_changePassword_errorMessage)

        val sharedPreferenceUtil = SharedPreferenceUtil(mContext!!)
        mUserName =
            sharedPreferenceUtil.getString(SharedPreferenceConstant.LOGGED_IN_USER_USERNAME, null)
        mOldPassword =
            sharedPreferenceUtil.getString(SharedPreferenceConstant.LOGGED_IN_USER_PASSWORD, null)
        mRole = sharedPreferenceUtil.getString(SharedPreferenceConstant.ROLE, null)

        setupToolbar()
    }

    private fun setupToolbar() {
        mToolbar?.title = "Change Password"
        setSupportActionBar(mToolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun changePassword(view: View) {
        if (validate()) {
            mRole?.let { role ->
                if (role.equals(AppConstant.UserRole.ROLE_ADMIN, ignoreCase = true)) {
                    val adminDAO = AdminDAO(mContext!!)
                    if (adminDAO.changePassword(mUserName, mEtNewPassword?.text.toString())) {
                        Toast.makeText(mContext, "Password reset successfully", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(mContext, "Failed to reset Password", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    val policeDAO = PoliceDAO(mContext!!)
                    if (policeDAO.changePassword(mUserName, mEtNewPassword?.text.toString())) {
                        Toast.makeText(mContext, "Password reset successfully", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(mContext, "Failed to reset Password", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (mEtOldPassword?.text?.isEmpty() == true) {
            mTilOldPassword?.error = "Old Password must not be empty"
            return false
        } else {
            mTilOldPassword?.error = null
        }

        mRole?.let { role ->
            if (role.equals(AppConstant.UserRole.ROLE_ADMIN, ignoreCase = true)) {
                val adminDAO = AdminDAO(mContext!!)
                if (mUserName == null || mOldPassword == null || !adminDAO.adminLogin(
                        mUserName!!,
                        mEtOldPassword?.text.toString()
                    )
                ) {
                    mTvErrorMessage?.visibility = View.VISIBLE
                    return false
                } else {
                    mTvErrorMessage?.visibility = View.GONE
                }
            } else {
                val policeDAO = PoliceDAO(mContext!!)
                if (mUserName == null || mOldPassword == null || !policeDAO.policeLogin(
                        mUserName!!,
                        mEtOldPassword?.text.toString()
                    )
                ) {
                    mTvErrorMessage?.visibility = View.VISIBLE
                    return false
                } else {
                    mTvErrorMessage?.visibility = View.GONE
                }
            }
        }

        if (mEtNewPassword?.text?.isEmpty() == true) {
            mTilNewPassword?.error = "New Password must not be empty"
            return false
        } else {
            mTilNewPassword?.error = null
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
