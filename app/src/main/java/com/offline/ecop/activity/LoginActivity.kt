package com.offline.ecop.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.offline.ecop.R
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.constant.SharedPreferenceConstant
import com.offline.ecop.database.AdminDAO
import com.offline.ecop.database.PoliceDAO
import com.offline.ecop.util.SharedPreferenceUtil

class LoginActivity : AppCompatActivity() {

    private var mToolbar: Toolbar? = null
    private var mTvErrorMessage: TextView? = null
    private var mTilUserName: TextInputLayout? = null
    private var mTilPassword: TextInputLayout? = null
    private var mEtUsername: TextInputEditText? = null
    private var mEtPassword: TextInputEditText? = null

    private var mRole: String? = null

    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mToolbar = findViewById(R.id.login_toolbar)
        mTvErrorMessage = findViewById(R.id.textView_login_error_message)
        mTilUserName = findViewById(R.id.textInputLayout_login_userName)
        mTilPassword = findViewById(R.id.textInputLayout_login_password)
        mEtUsername = findViewById(R.id.textInputEditText_login_userName)
        mEtPassword = findViewById(R.id.textInputEditText_login_password)

        mRole = intent.getStringExtra(AppConstant.USER_ROLE)
        mContext = this

        setupToolbar()

    }

    private fun setupToolbar() {
        mToolbar?.title = "Sign In"
        setSupportActionBar(mToolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun signIn(view: View) {
        if (validate()) {
            if (AppConstant.UserRole.ROLE_ADMIN.equals(mRole, ignoreCase = true)) {
                val adminDAO = AdminDAO(mContext!!)
                if (adminDAO.adminLogin(
                        mEtUsername?.text.toString(),
                        mEtPassword?.text.toString()
                    )
                ) {
                    mTvErrorMessage?.visibility = View.GONE

                    val preferenceUtil = SharedPreferenceUtil(mContext!!)
                    preferenceUtil.putBoolean(SharedPreferenceConstant.LOGIN_STATUS, true)
                    preferenceUtil.putString(SharedPreferenceConstant.ROLE, mRole)
                    preferenceUtil.putString(
                        SharedPreferenceConstant.LOGGED_IN_USER_USERNAME,
                        mEtUsername?.text.toString()
                    )
                    preferenceUtil.putString(
                        SharedPreferenceConstant.LOGGED_IN_USER_PASSWORD,
                        mEtPassword?.text.toString()
                    )

                    val intent = Intent(mContext, AdministratorActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    mTvErrorMessage?.visibility = View.VISIBLE
                }
            } else {
                val policeDAO = PoliceDAO(mContext!!)
                if (policeDAO.policeLogin(
                        mEtUsername?.text.toString(),
                        mEtPassword?.text.toString()
                    )
                ) {
                    mTvErrorMessage?.visibility = View.GONE

                    val preferenceUtil = SharedPreferenceUtil(mContext!!)
                    preferenceUtil.putBoolean(SharedPreferenceConstant.LOGIN_STATUS, true)
                    preferenceUtil.putString(SharedPreferenceConstant.ROLE, mRole)
                    preferenceUtil.putString(
                        SharedPreferenceConstant.LOGGED_IN_USER_USERNAME,
                        mEtUsername?.text.toString()
                    )
                    preferenceUtil.putString(
                        SharedPreferenceConstant.LOGGED_IN_USER_PASSWORD,
                        mEtPassword?.text.toString()
                    )

                    val intent = Intent(mContext, PoliceActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    mTvErrorMessage?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (mEtUsername?.text?.isEmpty() == true) {
            mTilUserName?.error = "Username should not be empty"
            return false
        } else {
            mTilUserName?.error = null
        }
        if (mEtPassword?.text?.isEmpty() == true) {
            mTilPassword?.error = "Password should not be empty"
            return false
        } else {
            mTilPassword?.error = null
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
