package com.offline.ecop.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.offline.ecop.R
import com.offline.ecop.database.ComplaintDAO
import com.offline.ecop.model.Complaint
import com.offline.ecop.util.DistrictUtil
import java.text.ParseException
import java.text.SimpleDateFormat


class AddComplaintActivity : AppCompatActivity() {

    @BindView(R.id.addComplaint_toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.textInputLayout_addComplaint_aadhaarNumber)
    lateinit var mTilAadhaarNumber: TextInputLayout

    @BindView(R.id.textInputEditText_addComplaint_aadhaarNumber)
    lateinit var mEtAadhaarNumber: TextInputEditText

    @BindView(R.id.textInputLayout_addComplaint_name)
    lateinit var mTilName: TextInputLayout

    @BindView(R.id.textInputEditText_addComplaint_name)
    lateinit var mEtName: TextInputEditText

    @BindView(R.id.textInputLayout_addComplaint_address)
    lateinit var mTilAddress: TextInputLayout

    @BindView(R.id.textInputEditText_addComplaint_address)
    lateinit var mEtAddress: TextInputEditText

    @BindView(R.id.textInputLayout_addComplaint_contactNumber)
    lateinit var mTilContanctNumber: TextInputLayout

    @BindView(R.id.textInputEditText_addComplaint_contactNumber)
    lateinit var mEtContanctNumber: TextInputEditText

    @BindView(R.id.textInputLayout_addComplaint_dateOfOffence)
    lateinit var mTilDateOfOffence: TextInputLayout

    @BindView(R.id.textInputEditText_addComplaint_dateOfOffence)
    lateinit var mEtDateOfOffence: TextInputEditText

    @BindView(R.id.textView_addComplaint_districtError)
    lateinit var mTvDistrictError: TextView

    @BindView(R.id.spinner_addComplaint_district)
    lateinit var mSpinnerDistrict: Spinner

    @BindView(R.id.textView_addComplaint_policeStationError)
    lateinit var mTvPoliceStationError: TextView

    @BindView(R.id.spinner_addComplaint_policeStation)
    lateinit var mSpinnerPoliceStation: Spinner

    @BindView(R.id.textInputLayout_addComplaint_complaintDetail)
    lateinit var mTilComplaintDetail: TextInputLayout

    @BindView(R.id.textInputEditText_addComplaint_complaintDetail)
    lateinit var mEtComplaintDetail: TextInputEditText

    private var mContext: Context? = null
    private var mSelectedDistrict: String = "Select"
    private var mSelectedPoliceStation: String? = "Select"

    private val TAG = "AddComplaintActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_complaint)
        ButterKnife.bind(this)

        mContext = this

        setupToolbar()
        setupSpinners()
    }

    private fun setupToolbar() {
        mToolbar.title = "Add New Complaint"
        setSupportActionBar(mToolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupSpinners() {
        val districtUtil = DistrictUtil()
        val districtList = districtUtil.getDistrictList()
        val districtAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, districtList)
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        //Setting the ArrayAdapter data on the Spinner
        mSpinnerDistrict.adapter = districtAdapter

        mSpinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                mSelectedDistrict = districtList[position]
                setupPoliceStationSpinner(districtUtil, mSelectedDistrict)
            }

        }

        setupPoliceStationSpinner(districtUtil, "Select")

    }

    fun setupPoliceStationSpinner(districtUtil: DistrictUtil, districtName: String) {
        val policeStationList = districtUtil.getPoliceStationList(districtName)
        mSelectedPoliceStation = policeStationList[0]
        val policeAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, policeStationList)
        policeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        //Setting the ArrayAdapter data on the Spinner
        mSpinnerPoliceStation.adapter = policeAdapter

        mSpinnerPoliceStation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                mSelectedPoliceStation = policeStationList[position]
            }

        }
    }

    @OnClick(R.id.btn_addComplaint)
    fun addComplaint() {
        if (validate()) {
            val complaint = Complaint()
            complaint.uid = mEtAadhaarNumber.text.toString().toLong()
            complaint.name = mEtName.text.toString()
            complaint.address = mEtAddress.text.toString()
            complaint.contactNumber = mEtContanctNumber.text.toString().toLong()
            complaint.dateOfOffence = convertDateIntoMiliseconds(mEtDateOfOffence.text.toString())
            complaint.district = mSelectedDistrict
            complaint.policeStation = mSelectedPoliceStation
            complaint.complaint = mEtComplaintDetail.text.toString()
            complaint.status = Complaint.STATUS_PENDING
            val complaintDAO = ComplaintDAO(mContext!!)
            val complaintNumber = complaintDAO.addComplaint(complaint)
            if (complaintNumber > -1) {
                val builder = AlertDialog.Builder(mContext!!)

                //Setting message manually and performing action on button click
                //Setting message manually and performing action on button click
                builder.setMessage("Your Complaint has been raised. Your complaint number is $complaintNumber")
                    .setCancelable(false)
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        finish()
                    }
                //Creating dialog box
                //Creating dialog box
                val dialog: AlertDialog = builder.create()
                //Setting the title manually
                //Setting the title manually
                dialog.setTitle("Successful !!!")
                dialog.show()
            } else {
                Toast.makeText(mContext, "Failed to Add new complaint", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validate(): Boolean {
        if (mEtAadhaarNumber.text.isNullOrEmpty()) {
            mTilAadhaarNumber.error = "Aadhaar number must not be empty"
            return false
        } else {
            mTilAadhaarNumber.error = null
        }
        if (mEtName.text.isNullOrEmpty()) {
            mTilName.error = "Name must not be empty"
            return false
        } else {
            mTilName.error = null
        }
        if (mEtAddress.text.isNullOrEmpty()) {
            mTilAddress.error = "Address must not be empty"
            return false
        } else {
            mTilAddress.error = null
        }
        if (mEtContanctNumber.text.isNullOrEmpty()) {
            mTilContanctNumber.error = "Contact Number must not be empty"
            return false
        } else {
            mTilContanctNumber.error = null
        }
        if (mEtDateOfOffence.text.isNullOrEmpty()) {
            mTilDateOfOffence.error = "Date of offence must not be empty"
            return false
        } else if (!isValidDate(mEtDateOfOffence.text.toString())) {
            mTilDateOfOffence.error = "Please enter valid date"
            return false
        } else {
            mTilDateOfOffence.error = null
        }
        if (mSelectedDistrict.equals("Select", ignoreCase = true)) {
            mTvDistrictError.visibility = View.VISIBLE
            return false
        } else {
            mTvDistrictError.visibility = View.GONE
        }
        if (mSelectedPoliceStation.equals("Select", ignoreCase = true)) {
            mTvPoliceStationError.visibility = View.VISIBLE
            return false
        } else {
            mTvPoliceStationError.visibility = View.GONE
        }
        if (mEtComplaintDetail.text.isNullOrEmpty()) {
            mTilComplaintDetail.error = "Complaint must not be empty"
            return false
        } else {
            mTilComplaintDetail.error = null
        }

        return true
    }

    @SuppressLint("SimpleDateFormat")
    private fun isValidDate(dateText: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        dateFormat.isLenient = false
        try {
            dateFormat.parse(dateText)
        } catch (e: ParseException) {
            return false
        }

        return true
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertDateIntoMiliseconds(dateText: String): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        dateFormat.isLenient = false
        try {
            val date = dateFormat.parse(dateText)
            return date?.time ?: 0
        } catch (e: ParseException) {

        }

        return 0
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_serarch_complaint, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.action_search_complaint -> {
                val intent = Intent(mContext!!, SearchComplaintActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
