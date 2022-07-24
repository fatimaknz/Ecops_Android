package com.offline.ecop.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.offline.ecop.R
import com.offline.ecop.adapter.AdministratorViewPagerAdapter
import com.offline.ecop.util.Util

class AdministratorActivity : AppCompatActivity() {

    private var mToolbar: Toolbar? = null
    private var mFloatingMenu: FloatingActionMenu? = null
    private var mFloatingMenuContainer: View? = null
    private var mFabAddPolice: FloatingActionButton? = null
    private var mFabAddCriminal: FloatingActionButton? = null
    private var mTabLayout: TabLayout? = null
    private var mViewPager: ViewPager? = null

    private var mContext: Context? = null
    private var mIsExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrator)

        mContext = this

        mToolbar = findViewById(R.id.administrator_toolbar)
        mFloatingMenu = findViewById(R.id.floatingMenu)
        mFabAddPolice = findViewById(R.id.floatingMenuItem_addPolice)
        mFabAddCriminal = findViewById(R.id.floatingMenuItem_addCriminalRecord)
        mFloatingMenuContainer = findViewById(R.id.floatingMenu_overlay_container)
        mTabLayout = findViewById(R.id.tabLayout_administrator)
        mViewPager = findViewById(R.id.viewPager_administrator)

        setupToolbar()
        setFloatingMenuClickListener()
        setupViewPager()
    }

    private fun setupToolbar() {
        mToolbar?.title = "Administrator"
        setSupportActionBar(mToolbar)
    }

    private fun setFloatingMenuClickListener() {
        mFloatingMenu?.setOnMenuToggleListener {
            if (it) {
                mFloatingMenuContainer?.apply {
                    visibility = View.VISIBLE
                    isClickable = true
                    isFocusable = true
                }
            } else {
                mFloatingMenuContainer?.apply {
                    visibility = View.GONE
                    isClickable = false
                    isFocusable = false
                }
            }
            mIsExpanded = it
        }
    }

    private fun setupViewPager() {
        val tabTitleList = arrayListOf("Complaints", "Police", "Criminals")
        val adapter = AdministratorViewPagerAdapter(supportFragmentManager, tabTitleList)
        mViewPager?.adapter = adapter
        mTabLayout?.setupWithViewPager(mViewPager)
    }

    fun addPolice(view: View) {
        val intent = Intent(mContext, AddPoliceActivity::class.java)
        startActivity(intent)
        collapseFloatingMenu(view)
    }

    fun addCriminal(view: View) {
        val intent = Intent(mContext, AddCriminalActivity::class.java)
        startActivity(intent)
        collapseFloatingMenu(view)
    }

    fun addAdmin(view: View) {
        val intent = Intent(mContext, AddAdminActivity::class.java)
        startActivity(intent)
        collapseFloatingMenu(view)
    }

    fun collapseFloatingMenu(view: View) {
        mFloatingMenu?.close(true)
        mFloatingMenuContainer?.apply {
            visibility = View.GONE
            isClickable = false
            isFocusable = false
        }
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
