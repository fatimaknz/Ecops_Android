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
import butterknife.BindView
import butterknife.ButterKnife

import com.offline.ecop.R
import com.offline.ecop.adapter.CriminalListAdapter
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.database.CriminalDAO
import java.lang.Exception


class CriminalListFragment : Fragment() {

    @BindView(R.id.recyclerView_criminalList)
    lateinit var mRvCriminalList: RecyclerView

    private var mContext: Context? = null
    private var mCriminalListAdapter: CriminalListAdapter? = null

    private val TAG = "CriminalListFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
        mContext?.registerReceiver(
            broadcastReceiver,
            IntentFilter(AppConstant.BroadcastReceiverAction.ACTION_ADD_CRIMINAL)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_criminal_list, container, false)
        ButterKnife.bind(this, view)

        val criminalDAO = CriminalDAO(mContext!!)

        val data = criminalDAO.getCriminalList()

        Log.d(TAG, "Criminal List Size : ${data.size}")

        mRvCriminalList.apply {
            layoutManager = LinearLayoutManager(mContext)
            mCriminalListAdapter = CriminalListAdapter(mContext!!, data)
            adapter = mCriminalListAdapter
        }

        return view
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val criminalDAO = CriminalDAO(mContext!!)
            val data = criminalDAO.getCriminalList()
            mCriminalListAdapter?.setCollection(data)
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
