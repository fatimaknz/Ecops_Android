package com.offline.ecop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.offline.ecop.R
import com.offline.ecop.model.Police


class PoliceListAdapter(private val mContext: Context?, private var mPoliceList: List<Police>) :
    RecyclerView.Adapter<PoliceListAdapter.PoliceListAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoliceListAdapterViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.layout_police_list_recycler_item, parent, false)
        return PoliceListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPoliceList.size
    }

    override fun onBindViewHolder(holder: PoliceListAdapterViewHolder, position: Int) {
        val police = mPoliceList[position]
        val firstLetter = police.name?.substring(0, 1)
        val generator = ColorGenerator.MATERIAL
        val randomColor = generator.getColor(police.username)
        val drawable = TextDrawable.builder().buildRound(firstLetter, randomColor)
        holder.mIvPoliceThumbnail?.setImageDrawable(drawable)

        holder.mTvPoliceName?.text = police.name

        holder.mTvPoliceDesignation?.text = police.designation
    }

    fun setCollection(collection: List<Police>) {
        mPoliceList = collection
        notifyDataSetChanged()
    }

    inner class PoliceListAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mIvPoliceThumbnail: AppCompatImageView? = null

        var mTvPoliceName: TextView? = null

        var mTvPoliceDesignation: TextView? = null

        init {
            mIvPoliceThumbnail = itemView.findViewById(R.id.appCompatImageView_police_thumbnail)
            mTvPoliceName = itemView.findViewById(R.id.textView_police_name)
            mTvPoliceDesignation = itemView.findViewById(R.id.textView_police_designation)
        }
    }
}