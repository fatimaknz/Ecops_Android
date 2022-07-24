package com.offline.ecop.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mikhaellopez.circularimageview.CircularImageView
import com.offline.ecop.R
import com.offline.ecop.database.CriminalDAO
import com.offline.ecop.model.Criminal
import com.offline.ecop.util.DistrictUtil
import com.offline.ecop.util.Util
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AddCriminalActivity : AppCompatActivity() {

    @BindView(R.id.addCriminal_toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.imageView_addCriminal_profilePhoto)
    lateinit var mIvProfilePhoto: CircularImageView

    @BindView(R.id.textInputLayout_addCriminal_aadhaarNumber)
    lateinit var mTilAadhaarNumber: TextInputLayout

    @BindView(R.id.textInputEditText_addCriminal_aadhaarNumber)
    lateinit var mEtAadhaarNumber: TextInputEditText

    @BindView(R.id.textInputEditText_addCriminal_fir)
    lateinit var mEtFIRNumber: TextInputEditText

    @BindView(R.id.textInputLayout_addCriminal_name)
    lateinit var mTilName: TextInputLayout

    @BindView(R.id.textInputEditText_addCriminal_name)
    lateinit var mEtName: TextInputEditText

    @BindView(R.id.textInputLayout_addCriminal_address)
    lateinit var mTilAddress: TextInputLayout

    @BindView(R.id.textInputEditText_addCriminal_address)
    lateinit var mEtAddress: TextInputEditText

    @BindView(R.id.radioGroup_addCriminal_gender)
    lateinit var mRadioGroupGender: RadioGroup

    @BindView(R.id.textInputLayout_addCriminal_dateOfBirth)
    lateinit var mTilDateOfBirth: TextInputLayout

    @BindView(R.id.textInputEditText_addCriminal_dateOfBirth)
    lateinit var mEtDateOfBirth: TextInputEditText

    @BindView(R.id.textInputLayout_addCriminal_weight)
    lateinit var mTilWeight: TextInputLayout

    @BindView(R.id.textInputEditText_addCriminal_weight)
    lateinit var mEtWeight: TextInputEditText

    @BindView(R.id.textInputLayout_addCriminal_height)
    lateinit var mTilHeight: TextInputLayout

    @BindView(R.id.textInputEditText_addCriminal_height)
    lateinit var mEtHeight: TextInputEditText

    @BindView(R.id.textView_addCriminal_districtError)
    lateinit var mTvDistrictError: TextView

    @BindView(R.id.spinner_addCriminal_district)
    lateinit var mSpinnerDistrict: Spinner

    @BindView(R.id.textView_addCriminal_policeStationError)
    lateinit var mTvPoliceStationError: TextView

    @BindView(R.id.spinner_addCriminal_policeStation)
    lateinit var mSpinnerPoliceStation: Spinner

    @BindView(R.id.textInputLayout_addCriminal_crimeDetail)
    lateinit var mTilCrimeDetail: TextInputLayout

    @BindView(R.id.textInputEditText_addCriminal_crimeDetail)
    lateinit var mEtCrimeDetail: TextInputEditText

    @BindView(R.id.textInputLayout_addCriminal_dateOfCrime)
    lateinit var mTilDateOfCrime: TextInputLayout

    @BindView(R.id.textInputEditText_addCriminal_dateOfCrime)
    lateinit var mEtDateOfCrime: TextInputEditText

    private var mContext: Context? = null
    private var mSelectedGender: String = "male"
    private var mProfileImageData: String? = null
    private var mSelectedDistrict: String = "Select"
    private var mSelectedPoliceStation: String? = "Select"

    private val TAG = "AddCriminalActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_criminal)
        ButterKnife.bind(this)
        mContext = this

        setupToolbar()
        val criminalDAO = CriminalDAO(mContext!!)
        val firNO = (criminalDAO.getCriminalCount() + 1).toString()
        mEtFIRNumber.setText(firNO)
        mEtFIRNumber.isFocusable = false
        mEtFIRNumber.isClickable = false

        setupSpinners()

        mRadioGroupGender.setOnCheckedChangeListener { _, id ->
            mSelectedGender = if (id == R.id.radioButton_addCriminal_male) "male" else "female"
        }
    }

    private fun setupToolbar() {
        mToolbar.title = "Add Criminal"
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

    fun addCriminal(view: View) {
        if (validate()) {
            val criminal = Criminal()
            criminal.UID = mEtAadhaarNumber.text.toString().toLong()
            criminal.FIRNumber = mEtFIRNumber.text.toString().toInt()
            criminal.imageData = mProfileImageData
            criminal.name = mEtName.text.toString()
            criminal.address = mEtAddress.text.toString()
            criminal.gender = mSelectedGender
            criminal.dateOfBirth = convertDateIntoMiliseconds(mEtDateOfBirth.text.toString())
            criminal.weight = mEtWeight.text.toString()
            criminal.height = mEtHeight.text.toString()
            criminal.district = mSelectedDistrict
            criminal.policeStation = mSelectedPoliceStation
            criminal.crimeDetail = mEtCrimeDetail.text.toString()
            criminal.dateOfCrime = convertDateIntoMiliseconds(mEtDateOfCrime.text.toString())
            val criminalDAO = CriminalDAO(mContext!!)
            if (criminalDAO.addCriminal(criminal)) {
                Toast.makeText(mContext, "Criminal added successfully", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(mContext, "Failed to add Criminal", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validate(): Boolean {
        if (mEtAadhaarNumber.text.isNullOrEmpty()) {
            mTilAadhaarNumber.error = "Aadhaar must not be empty"
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
        if (mEtDateOfBirth.text.isNullOrEmpty()) {
            mTilDateOfBirth.error = "Date of birth must not be empty"
            return false
        } else if (!isValidDate(mEtDateOfBirth.text.toString())) {
            mTilDateOfBirth.error = "Please enter valid date"
            return false
        } else {
            mTilDateOfBirth.error = null
        }
        if (mEtWeight.text.isNullOrEmpty()) {
            mTilWeight.error = "Weight must not be empty"
            return false
        } else {
            mTilWeight.error = null
        }
        if (mEtHeight.text.isNullOrEmpty()) {
            mTilHeight.error = "Height must not be empty"
            return false
        } else {
            mTilHeight.error = null
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
        if (mEtCrimeDetail.text.isNullOrEmpty()) {
            mTilCrimeDetail.error = "Crime description must not be empty"
            return false
        } else {
            mTilCrimeDetail.error = null
        }
        if (mEtDateOfCrime.text.isNullOrEmpty()) {
            mTilDateOfCrime.error = "Crime date must not be empty"
            return false
        } else if (!isValidDate(mEtDateOfCrime.text.toString())) {
            mTilDateOfCrime.error = "Please enter valid date"
            return false
        } else {
            mTilDateOfCrime.error = null
        }
        if (mProfileImageData == null) {
            Toast.makeText(mContext, "Please choose Profile Photo", Toast.LENGTH_LONG).show()
        }

        return true
    }

    @OnClick(R.id.imageView_addCriminal_editPhoto)
    fun openCamera() {
        if (Util.checkCameraPermission(mContext as Activity)) {
            captureImage()
        } else {
            Util.requestCameraPermission(mContext as Activity)
        }
    }

    private fun captureImage() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, Util.CAMERA_REQUEST_CODE)
    }

    private val mTextWatcher = object : TextWatcher {
        private var current = ""
        private val ddmmyyyy = "DDMMYYYY"
        private val cal: Calendar = Calendar.getInstance()

        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!s.toString().equals(current, ignoreCase = true)) {
                var clean: String = s.toString().replace("[^\\d.]|\\.", "")
                val cleanC = current.replace("[^\\d.]|\\.".toRegex(), "")
                val cl = clean.length
                var sel = cl
                var i = 2
                while (i <= cl && i < 6) {
                    sel++
                    i += 2
                }
                //Fix for pressing delete next to a forward slash
                if (clean == cleanC) sel--
                if (clean.length < 8) {
                    clean += ddmmyyyy.substring(clean.length)
                } else {
                    //This part makes sure that when we finish entering numbers
                    // the date is correct, fixing it otherwise
                    var day = clean.substring(0, 2).toInt()
                    var mon = clean.substring(2, 4).toInt()
                    var year = clean.substring(4, 8).toInt()
                    mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                    cal[Calendar.MONTH] = mon - 1
                    year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                    cal[Calendar.YEAR] = year
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012
                    day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(
                        Calendar.DATE
                    ) else day
                    clean = String.format("%02d%02d%02d", day, mon, year)
                }
                clean = String.format(
                    "%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8)
                )
                sel = if (sel < 0) 0 else sel
                current = clean
                mEtDateOfBirth.setText(current)
                mEtDateOfBirth.setSelection(if (sel < current.length) sel else current.length)
            }
        }

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
            Log.d(TAG, "Exception in parsing DATE ==> $dateText : Error : ${e.message}")
        }

        return 0
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Util.CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage()
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Util.CAMERA_REQUEST_CODE) {
            val photo = data?.extras?.get("data") as? Bitmap
            if (photo != null) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                val byteArray = byteArrayOutputStream.toByteArray()

                mProfileImageData =
                    android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
                Glide.with(mContext!!)
                    .asBitmap()
                    .load(byteArray)
                    .into(mIvProfilePhoto)

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
