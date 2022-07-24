package com.offline.ecop.activity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.offline.ecop.R
import com.offline.ecop.database.PoliceDAO
import com.offline.ecop.model.Police
import com.offline.ecop.util.DistrictUtil


class AddPoliceActivity : AppCompatActivity() {

    private var mToolbar: Toolbar? = null
    private var mTilUID: TextInputLayout? = null
    private var mTilName: TextInputLayout? = null
    private var mTilDesignation: TextInputLayout? = null
    private var mTilAge: TextInputLayout? = null
    private var mTilUsername: TextInputLayout? = null
    private var mTilPassword: TextInputLayout? = null
    private var mEtUID: TextInputEditText? = null
    private var mEtName: TextInputEditText? = null
    private var mEtDesignation: TextInputEditText? = null
    private var mEtAge: TextInputEditText? = null
    private var mEtUsername: TextInputEditText? = null
    private var mEtPassword: TextInputEditText? = null
    private var mSpinnerDistrict: Spinner? = null
    private var mSpinnerPoliceStation: Spinner? = null
    private var mTvDistrictError: TextView? = null
    private var mTvPoliceStationError: TextView? = null


    private var mContext: Context? = null
    private var mSelectedDistrict: String = "Select"
    private var mSelectedPoliceStation: String? = "Select"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_police)

        mContext = this

        mToolbar = findViewById(R.id.addPolice_toolbar)
        mTilUID = findViewById(R.id.textInputLayout_addPolice_aadhaarNumber)
        mTilName = findViewById(R.id.textInputLayout_addPolice_name)
        mTilDesignation = findViewById(R.id.textInputLayout_addPolice_designation)
        mTilAge = findViewById(R.id.textInputLayout_addPolice_age)
        mTilUsername = findViewById(R.id.textInputLayout_addPolice_userName)
        mTilPassword = findViewById(R.id.textInputLayout_addPolice_password)
        mEtUID = findViewById(R.id.textInputEditText_addPolice_aadhaarNumber)
        mEtName = findViewById(R.id.textInputEditText_addPolice_name)
        mEtDesignation = findViewById(R.id.textInputEditText_addPolice_designation)
        mEtAge = findViewById(R.id.textInputEditText_addPolice_age)
        mEtUsername = findViewById(R.id.textInputEditText_addPolice_userName)
        mEtPassword = findViewById(R.id.textInputEditText_addPolice_password)
        mSpinnerDistrict = findViewById(R.id.spinner_addPolice_district)
        mSpinnerPoliceStation = findViewById(R.id.spinner_addPolice_policeStation)
        mTvDistrictError = findViewById(R.id.textView_addPolice_districtError)
        mTvPoliceStationError = findViewById(R.id.textView_addPolice_policeStationError)

        setupToolbar()
        setupSpinners()
    }

    private fun setupToolbar() {
        mToolbar?.title = "Add Police"
        setSupportActionBar(mToolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupSpinners() {
        val districtUtil = DistrictUtil()
        val districtList = districtUtil.getDistrictList()
        val districtAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, districtList)
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        //Setting the ArrayAdapter data on the Spinner
        mSpinnerDistrict?.adapter = districtAdapter

        mSpinnerDistrict?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        val policeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, policeStationList)
        policeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        //Setting the ArrayAdapter data on the Spinner
        mSpinnerPoliceStation?.adapter = policeAdapter

        mSpinnerPoliceStation?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                mSelectedPoliceStation = policeStationList[position]
            }

        }
    }

    fun addPolice(view: View) {
        if (validate()) {
            val policeDAO = PoliceDAO(mContext!!)
            val police = Police()
            police.UID = mEtUID?.text.toString().toLong()
            police.name = mEtName?.text.toString()
            police.age = mEtAge?.text.toString().toInt()
            police.designation = mEtDesignation?.text.toString()
            police.username = mEtUsername?.text.toString()
            police.password = mEtPassword?.text.toString()
            police.district = mSelectedDistrict
            police.policeStation = mSelectedPoliceStation

            if (policeDAO.addPolice(police)) {
                Toast.makeText(mContext, "Police Record added successfully", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(mContext, "Failed to add Police Record", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validate(): Boolean {
        if (mEtUID?.text?.isEmpty() == true) {
            mTilUID?.error = "Aadhaar Number should not be empty"
            return false
        } else {
            mTilUID?.error = null
        }
        if (mEtName?.text?.isEmpty() == true) {
            mTilName?.error = "Name should not be empty"
            return false
        } else {
            mTilName?.error = null
        }
        if (mEtDesignation?.text?.isEmpty() == true) {
            mTilDesignation?.error = "Designation should not be empty"
            return false
        } else {
            mTilDesignation?.error = null
        }
        if (mEtAge?.text?.isEmpty() == true) {
            mTilAge?.error = "Age should not be empty"
            return false
        } else {
            mTilAge?.error = null
        }
        if (mEtUsername?.text?.isEmpty() == true) {
            mTilUsername?.error = "Username should not be empty"
            return false
        } else {
            mTilUsername?.error = null
        }
        if (mEtPassword?.text?.isEmpty() == true) {
            mTilPassword?.error = "Password should not be empty"
            return false
        } else {
            mTilPassword?.error = null
        }
        if (mSelectedDistrict.equals("Select", ignoreCase = true)) {
            mTvDistrictError?.visibility = View.VISIBLE
            return false
        } else {
            mTvDistrictError?.visibility = View.GONE
        }
        if (mSelectedPoliceStation .equals("Select", ignoreCase = true)) {
            mTvPoliceStationError?.visibility = View.VISIBLE
            return false
        } else {
            mTvPoliceStationError?.visibility = View.GONE
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
