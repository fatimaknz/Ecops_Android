package com.offline.ecop.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.offline.ecop.R
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.constant.SharedPreferenceConstant
import com.offline.ecop.database.ComplaintDAO
import com.offline.ecop.util.SharedPreferenceUtil

class SearchComplaintActivity : AppCompatActivity() {

    @BindView(R.id.searchComplaint_toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.textInputLayout_searchComplaint_aadhaarNumber)
    lateinit var mTilSearchComplaint: TextInputLayout

    @BindView(R.id.textInputEditText_searchComplaint_aadhaarNumber)
    lateinit var mEtSearchComplaint: TextInputEditText

    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_complaint)

        ButterKnife.bind(this)

        mContext = this
        setupToolbar()
    }

    private fun setupToolbar() {
        mToolbar.title = "Search Complaint"
        setSupportActionBar(mToolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun searchComplaint(view: View) {
        if (validate()) {
            val complaintId = mEtSearchComplaint.text.toString().toLong()
            val complaintDAO = ComplaintDAO(mContext!!)
            if (complaintDAO.searchComplaint(complaintId)) {
                SharedPreferenceUtil(mContext!!).putString(
                    SharedPreferenceConstant.ROLE,
                    AppConstant.UserRole.ROLE_CITIZEN
                )
                val intent = Intent(mContext, ComplaintDetailActivity::class.java)
                intent.putExtra(AppConstant.BundleKey.COMPLAINT_ID, complaintId)
                startActivity(intent)
            } else {
                Toast.makeText(mContext!!, "Complaint Record not found", Toast.LENGTH_LONG).show()
                mEtSearchComplaint.setText("")
            }
        }
    }

    private fun validate(): Boolean {
        if (mEtSearchComplaint.text.isNullOrEmpty()) {
            mTilSearchComplaint.error = "Complaint Number must not be empty"
            return false
        } else {
            mTilSearchComplaint.error = null
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
