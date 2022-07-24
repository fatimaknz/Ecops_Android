package com.offline.ecop.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.offline.ecop.R
import com.offline.ecop.constant.SharedPreferenceConstant
import com.offline.ecop.database.PoliceDAO
import com.offline.ecop.fragment.ComplaintFragment
import com.offline.ecop.util.SharedPreferenceUtil
import com.offline.ecop.util.Util

class PoliceActivity : AppCompatActivity() {

    @BindView(R.id.police_toolbar)
    lateinit var mToolbar: Toolbar

    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police)
        ButterKnife.bind(this)
        mContext = this
        setupToolbar()
        val policeDAO = PoliceDAO(mContext!!)
        val preferenceUtil = SharedPreferenceUtil(mContext!!)
        val uid = policeDAO.getPoliceUIDByUsername(preferenceUtil.getString(SharedPreferenceConstant.LOGGED_IN_USER_USERNAME, null))
        preferenceUtil.putLong(SharedPreferenceConstant.LOGGED_IN_USER_UID, uid)

        val complaintFragment = ComplaintFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.frameLayout, complaintFragment)
            .commit()
    }

    private fun setupToolbar() {
        mToolbar.title = "Police Space"
        setSupportActionBar(mToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout_change_password, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {

            R.id.action_changePassword -> {
                val intent = Intent(mContext, ChangePasswordActivity::class.java)
                startActivity(intent)
            }

            R.id.action_logout -> {
                Util.logout(mContext!!)
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
