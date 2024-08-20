package com.hotworx.ui.adapters.HotsquadListAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.Hotsquad
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.ui.fragments.HotsquadList.HotsquadSearchFragment

class SquadMemberListAdapter(
    private val items: List<SquadMemberDetailsResponse.SquadData.Member>,
    private val listener: OnItemClickListener,
    private val dockActivity: DockActivity? = null
) : RecyclerView.Adapter<SquadMemberListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: SquadMemberDetailsResponse.SquadData.Member)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val iconImageView: ImageView = itemView.findViewById(R.id.imgIcon)

        fun bind(item: SquadMemberDetailsResponse.SquadData.Member) {
            nameTextView.text = item.name

            // Use itemView.context to get the context
            Glide.with(itemView.context)
                .load(item.profile_image_url)
                .placeholder(R.drawable.placeholder) // Optional placeholder
                .into(iconImageView)

//            cardView.setOnClickListener {
//                listener.onItemClick(item)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_squadmember, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}