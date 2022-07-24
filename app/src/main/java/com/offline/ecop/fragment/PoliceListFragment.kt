package com.offline.ecop.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.offline.ecop.R
import com.offline.ecop.adapter.PoliceListAdapter
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.database.PoliceDAO
import java.lang.Exception


class PoliceListFragment : Fragment() {

    private var mRvPoliceList: RecyclerView? = null

    private var mContext: Context? = null

    private val TAG = "PoliceListFragment"

    private var mPoliceAdapter: PoliceListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        mContext?.registerReceiver(broadcastReceiver, IntentFilter(AppConstant.BroadcastReceiverAction.ACTION_ADD_POLICE))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_police_list, container, false)
        mRvPoliceList = view.findViewById(R.id.recyclerView_policeList)
        val policeDAO = PoliceDAO(mContext!!)
        val data = policeDAO.getPoliceList()
        Log.d(TAG, "Police Size : ${data.size}")
        mPoliceAdapter = PoliceListAdapter(mContext, data)
        mRvPoliceList?.apply {
            layoutManager = LinearLayoutManager(mContext)
            this.adapter = mPoliceAdapter
        }
        return view
    }

    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val policeDAO = PoliceDAO(mContext!!)
            val data = policeDAO.getPoliceList()
            mPoliceAdapter?.setCollection(data)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mContext?.unregisterReceiver(broadcastReceiver)
        } catch (e: Exception) {

        }
    }
}
