package com.offline.ecop.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.offline.ecop.R
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.constant.SharedPreferenceConstant
import com.offline.ecop.database.ComplaintDAO
import com.offline.ecop.database.PoliceDAO
import com.offline.ecop.model.Complaint
import com.offline.ecop.util.SharedPreferenceUtil
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ComplaintDetailActivity : AppCompatActivity() {

    @BindView(R.id.complaintDetail_toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.textView_complaintDetail_complaintDetail)
    lateinit var mTvComplaintDetail: TextView

    @BindView(R.id.textView_complaintDetail_dateOfOffence)
    lateinit var mTvDateOfOffence: TextView

    @BindView(R.id.textView_complaintDetail_complainantUID)
    lateinit var mTvComplainantUID: TextView

    @BindView(R.id.textView_complaintDetail_complainantName)
    lateinit var mTvComplainantName: TextView

    @BindView(R.id.textView_complaintDetail_address)
    lateinit var mTvComplainantAddress: TextView

    @BindView(R.id.textView_complaintDetail_contactNumber)
    lateinit var mTvContactNumber: TextView

    @BindView(R.id.textView_complaintDetail_district)
    lateinit var mTvDistrict: TextView

    @BindView(R.id.textView_complaintDetail_policeStation)
    lateinit var mTvPoliceStation: TextView

    @BindView(R.id.textView_complaintDetail_status)
    lateinit var mTvComplaintStatus: TextView

    @BindView(R.id.textView_complaintDetail_complaintExecutor)
    lateinit var mTvComplaintExecutorName: TextView

    @BindView(R.id.imageView_complaintDetail_editExecutor)
    lateinit var mEditExecutor: AppCompatImageView

    @BindView(R.id.imageView_complaintDetail_editStatus)
    lateinit var mEditStatus: AppCompatImageView

    private var mContext: Context? = null
    private var mComplaintId: Long? = null
    private var mComplaint: Complaint? = null
    private var mRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_detail)
        ButterKnife.bind(this)
        mContext = this
        mComplaintId = intent.getLongExtra(AppConstant.BundleKey.COMPLAINT_ID, 0)
        val complaintDAO = ComplaintDAO(mContext!!)
        mComplaint = complaintDAO.getComplaintDetail(mComplaintId!!)

        setupToolbar()
        setupData()
        mRole = SharedPreferenceUtil(mContext!!).getString(SharedPreferenceConstant.ROLE, null)
        if (AppConstant.UserRole.ROLE_POLICE == mRole || AppConstant.UserRole.ROLE_CITIZEN == mRole) {
            mEditExecutor.visibility = View.INVISIBLE
        }
        if (AppConstant.UserRole.ROLE_CITIZEN == mRole) {
            mEditStatus.visibility = View.INVISIBLE
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupData() {
        mTvComplaintDetail.text = mComplaint?.complaint
        mTvDateOfOffence.text = convertDate(mComplaint?.dateOfOffence!!)
        mTvComplainantUID.text = mComplaint?.uid.toString()
        mTvComplainantName.text = mComplaint?.name
        mTvComplainantAddress.text = mComplaint?.address
        mTvContactNumber.text = mComplaint?.contactNumber.toString()
        mTvDistrict.text = mComplaint?.district
        mTvPoliceStation.text = mComplaint?.policeStation
        mTvComplaintStatus.text = mComplaint?.status
        mTvComplaintExecutorName.text =
            if (mComplaint?.assignee == null || mComplaint?.assignee!! == 0L) "Not Selected" else {
                val policeDAO = PoliceDAO(mContext!!)
                val police = policeDAO.getPolice(mComplaint?.assignee!!)
                police.name
            }
    }

    @OnClick(R.id.imageView_complaintDetail_editStatus)
    fun editStatus() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(mContext!!)
        builder.setTitle("Change Status")
        val status =
            arrayOf(Complaint.STATUS_PENDING, Complaint.STATUS_APPROVED, Complaint.STATUS_PROCESSED)
        builder.setItems(status, DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                0 -> {
                    mComplaint?.status = Complaint.STATUS_PENDING
                    mTvComplaintStatus.text = Complaint.STATUS_PENDING
                }
                1 -> {
                    mComplaint?.status = Complaint.STATUS_APPROVED
                    mTvComplaintStatus.text = Complaint.STATUS_APPROVED
                }
                2 -> {
                    mComplaint?.status = Complaint.STATUS_PROCESSED
                    mTvComplaintStatus.text = Complaint.STATUS_PROCESSED
                }
            }
        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    @OnClick(R.id.imageView_complaintDetail_editExecutor)
    fun editExecutor() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(mContext!!)
        builder.setTitle("Select")
        val policeDAO = PoliceDAO(mContext!!)
        val policeList = policeDAO.getPoliceList(mComplaint?.policeStation!!)
        if (policeList.isNotEmpty()) {
            var policeArray: Array<String> = Array(policeList.size) { it.toString() }
            for ((index, police) in policeList.withIndex()) {
                policeArray[index] = police.name!!
            }


            builder.setItems(policeArray, DialogInterface.OnClickListener { _, which ->
                val uid = policeList[which].UID
                mComplaint?.assignee = uid
                mTvComplaintExecutorName.text = policeList[which].name
            })
            val dialog: AlertDialog = builder.create()
            dialog.show()
        } else {
            Toast.makeText(
                mContext,
                "There are not Police record available. Please add first",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertDate(dateInMilliSeconds: Long): String {
        try {
            val simple: DateFormat = SimpleDateFormat("dd MMM yyyy")

            val result = Date(dateInMilliSeconds)

            return simple.format(result)
        } catch (e: Exception) {

        }

        return dateInMilliSeconds.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_complaint_detail, menu)
        val menuItem = menu?.findItem(R.id.action_update_complaint)
        if (AppConstant.UserRole.ROLE_CITIZEN == mRole) {
            menuItem?.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.action_update_complaint -> {
                val complaintDAO = ComplaintDAO(mContext!!)
                if (complaintDAO.updateComplaint(
                        mComplaint?.status,
                        mComplaint?.assignee,
                        mComplaintId!!
                    )
                ) {
                    Toast.makeText(mContext, "Complaint updated successfully", Toast.LENGTH_LONG)
                        .show()
                    finish()
                } else {
                    Toast.makeText(mContext, "Failed to update complaint", Toast.LENGTH_LONG).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
