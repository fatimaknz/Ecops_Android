package com.offline.ecop.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.offline.ecop.R
import com.offline.ecop.activity.ComplaintDetailActivity
import com.offline.ecop.constant.AppConstant
import com.offline.ecop.model.Complaint

class ComplaintListAdapter(val mContext: Context, var mComplaintList: MutableList<Complaint>) :
    RecyclerView.Adapter<ComplaintListAdapter.ComplaintListAdapterViewHolder>() {

    private val TAG = "ComplaintListAdapter"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ComplaintListAdapterViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.layout_complaint_list_recycler_item, parent, false)
        return ComplaintListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mComplaintList.size
    }

    override fun onBindViewHolder(holder: ComplaintListAdapterViewHolder, position: Int) {
        val complaint = mComplaintList[position]
        holder.mTvComplaintDetail.text = complaint.complaint
        holder.mTvComplainantName.text = complaint.name
        holder.mTvComplainantAddress.text = complaint.address
        holder.mTvStatus.text = complaint.status

        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, ComplaintDetailActivity::class.java)
            intent.putExtra(AppConstant.BundleKey.COMPLAINT_ID, complaint.id)
            mContext.startActivity(intent)
        }

    }

    fun setCollection(collection: MutableList<Complaint>) {
        mComplaintList = collection
        notifyDataSetChanged()
    }

    inner class ComplaintListAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.textView_complaintList_complaintDetail)
        lateinit var mTvComplaintDetail: TextView

        @BindView(R.id.textView_complaintList_complainantName)
        lateinit var mTvComplainantName: TextView

        @BindView(R.id.textView_complaintList_address)
        lateinit var mTvComplainantAddress: TextView

        @BindView(R.id.textView_complaintList_status)
        lateinit var mTvStatus: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}