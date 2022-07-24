package com.offline.ecop.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife

import com.offline.ecop.R
import com.offline.ecop.adapter.ComplaintListAdapter
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.constant.SharedPreferenceConstant
import com.offline.ecop.database.ComplaintDAO
import com.offline.ecop.model.Complaint
import com.offline.ecop.util.SharedPreferenceUtil


class ComplaintFragment : Fragment() {

    @BindView(R.id.recyclerView_complaintList)
    lateinit var mRvComplaintList: RecyclerView

    private var mContext: Context? = null
    private var mAdapter: ComplaintListAdapter? = null

    private val TAG = "ComplaintFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        mContext?.registerReceiver(
            broadcastReceiver,
            IntentFilter(AppConstant.BroadcastReceiverAction.ACTION_UPDATE_COMPLAINT)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_complaint, container, false)
        ButterKnife.bind(this, view)

        val preferenceUtil = SharedPreferenceUtil(mContext!!)
        val role =
            preferenceUtil.getString(SharedPreferenceConstant.ROLE, AppConstant.UserRole.ROLE_ADMIN)
        val data = getData(role)

        Log.d(TAG, "Data Size : ${data.size}")
        mRvComplaintList.apply {
            layoutManager = LinearLayoutManager(mContext)
            mAdapter = ComplaintListAdapter(mContext!!, data)
            adapter = mAdapter
        }

        return view
    }

    private fun getData(role: String?): MutableList<Complaint> {
        val complaintDAO = ComplaintDAO(mContext!!)
        return if (AppConstant.UserRole.ROLE_ADMIN == role) {
            complaintDAO.getComplaintList()
        } else {
            val preferenceUtil = SharedPreferenceUtil(mContext!!)
            val uid = preferenceUtil.getLong(SharedPreferenceConstant.LOGGED_IN_USER_UID, 0)
            complaintDAO.getComplaintListByUID(uid)
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {
            val preferenceUtil = SharedPreferenceUtil(mContext!!)
            val role =
                preferenceUtil.getString(
                    SharedPreferenceConstant.ROLE,
                    AppConstant.UserRole.ROLE_ADMIN
                )
            val data = getData(role)
            mAdapter?.setCollection(data)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mContext?.unregisterReceiver(broadcastReceiver)
    }
}
