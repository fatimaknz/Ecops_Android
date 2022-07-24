package com.offline.ecop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.mikhaellopez.circularimageview.CircularImageView
import com.offline.ecop.R
import com.offline.ecop.model.Criminal

class CriminalListAdapter(val mContext: Context, var mCriminalList: List<Criminal>) :
    RecyclerView.Adapter<CriminalListAdapter.CriminalListAdapterViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CriminalListAdapterViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.layout_criminal_list_recycler_item, parent, false)
        return CriminalListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mCriminalList.size
    }

    override fun onBindViewHolder(holder: CriminalListAdapterViewHolder, position: Int) {
        val criminal = mCriminalList[position]

        val decodedImage =
            android.util.Base64.decode(criminal.imageData, android.util.Base64.DEFAULT)
        Glide.with(mContext)
            .asBitmap()
            .load(decodedImage)
            .into(holder.mCriminalThumnail)

        holder.mTvCriminalName.text = criminal.name
    }

    fun setCollection(collection: List<Criminal>) {
        mCriminalList = collection
        notifyDataSetChanged()
    }

    inner class CriminalListAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.appCompatImageView_criminal_thumbnail)
        lateinit var mCriminalThumnail: CircularImageView

        @BindView(R.id.textView_criminal_name)
        lateinit var mTvCriminalName: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}