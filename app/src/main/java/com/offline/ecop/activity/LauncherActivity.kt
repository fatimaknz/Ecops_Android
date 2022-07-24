package com.offline.ecop.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.offline.ecop.R
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.constant.SharedPreferenceConstant
import com.offline.ecop.database.AdminDAO
import com.offline.ecop.util.SharedPreferenceUtil

class LauncherActivity : AppCompatActivity() {

    private lateinit var mIvPoliceImage: ImageView

    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        val sharedPreferences = SharedPreferenceUtil(mContext!!)

        if (sharedPreferences.getBoolean(SharedPreferenceConstant.LOGIN_STATUS, false)) {
            if (AppConstant.UserRole.ROLE_ADMIN.equals(sharedPreferences.getString(SharedPreferenceConstant.ROLE, null), ignoreCase = true)) {
                val intent = Intent(mContext, AdministratorActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(mContext, PoliceActivity::class.java)
                startActivity(intent)
            }
            finish()
        } else {
            setContentView(R.layout.activity_launcher)
            mIvPoliceImage = findViewById(R.id.iv_police_space)

            val adminDAO = AdminDAO(mContext!!)
            adminDAO.addAdmin("admin", "admin")
        }

    }

    fun navigateToAdminLogin(view: View) {
        val intent = Intent(mContext, LoginActivity::class.java)
        intent.putExtra(AppConstant.USER_ROLE, AppConstant.UserRole.ROLE_ADMIN)
        startActivity(intent)
    }

    fun navigateToAddComplaint(view: View) {
        val intent = Intent(mContext, AddComplaintActivity::class.java)
        startActivity(intent)
    }

    fun navigateToPoliceLogin(view: View) {
        val intent = Intent(mContext, LoginActivity::class.java)
        intent.putExtra(AppConstant.USER_ROLE, AppConstant.UserRole.ROLE_POLICE)
        startActivity(intent)
    }
}
