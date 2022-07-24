package com.offline.ecop.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.offline.ecop.R
import com.offline.ecop.database.AdminDAO

class AddAdminActivity : AppCompatActivity() {

    private var mToolbar: Toolbar? = null
    private var mTilUsername: TextInputLayout? = null
    private var mTilPassword: TextInputLayout? = null
    private var mEtUsername: TextInputEditText? = null
    private var mEtPassword: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_admin)
        mToolbar = findViewById(R.id.addAdmin_toolbar)
        mTilUsername = findViewById(R.id.textInputLayout_addAdmin_userName)
        mTilPassword = findViewById(R.id.textInputLayout_addAdmin_password)
        mEtUsername = findViewById(R.id.textInputEditText_addAdmin_userName)
        mEtPassword = findViewById(R.id.textInputEditText_addAdmin_password)

        setupToolbar()

    }

    private fun setupToolbar() {
        mToolbar?.title = "Add Admin"
        setSupportActionBar(mToolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun addNewAdmin(view: View) {
        if (validate()) {
            val adminDao = AdminDAO(this)
            if (adminDao.addAdmin(mEtUsername?.text.toString(), mEtPassword?.text.toString())) {
                Toast.makeText(this, "Admin Added Successfully", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to Add Admin", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validate(): Boolean {
        if (mEtUsername?.text?.isEmpty() == true) {
            mTilUsername?.error = "Username should not be empty"
            return false
        }
        if (mEtPassword?.text?.isEmpty() == true) {
            mTilPassword?.error = "Password should not be empty"
            return false
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
