package com.hotworx.ui.adapters.HotsquadListAdapter.Sessions

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.Hotsquad
import com.hotworx.models.HotsquadList.Session.SessionMemberResponse
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchNotFoundUserAdapter.OnItemClickListener
import com.hotworx.ui.fragments.HotsquadList.HotsquadSearchFragment

class SessionMemberListAdapter(
    private val items: List<SessionMemberResponse.SquadData.Member>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SessionMemberListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: SessionMemberResponse.SquadData.Member)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val iconImageView: ImageView = itemView.findViewById(R.id.imgIcon)
        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val phoneTextView: TextView = itemView.findViewById(R.id.tvPhone)
        private val imgcheck: ImageView = itemView.findViewById(R.id.imgCheck)
        private val imgCheckBox: ImageView = itemView.findViewById(R.id.imgCheckBox)
        private val cardView: CardView = itemView.findViewById(R.id.listMainView)

        fun bind(item: SessionMemberResponse.SquadData.Member) {
            nameTextView.text = item.name
            statusTextView.text = item.invite_message
            emailTextView.text = item.email
            phoneTextView.text = item.phone

            // Use itemView.context to get the context
            Glide.with(itemView.context)
                .load(item.profile_image_url)
                .placeholder(R.drawable.placeholder) // Optional placeholder
                .into(iconImageView)

            // Set the visibility based on the selected state
            imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE

            Log.d("MemberCheck", "Member ID: ${item.member_invite_id}")
            if (item.member_invite_id == null) {
                Log.d("MemberCheck", "Handling null member_id")
                imgCheckBox.visibility = View.GONE
                val color = ContextCompat.getColor(context, R.color.colorLine)
                cardView.backgroundTintList = ColorStateList.valueOf(color)
            } else {
                Log.d("MemberCheckkk", "Handling non-null member_id")
                imgCheckBox.visibility = View.VISIBLE
                imgCheckBox.setOnClickListener {
                    item.selected = !item.selected
                    val color = ContextCompat.getColor(context, R.color.white)
                    cardView.backgroundTintList = ColorStateList.valueOf(color)
                    imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE
                    notifyItemChanged(adapterPosition)
                    listener.onItemClick(item)
                }
            }
//            cardView.setOnClickListener {
//                listener.onItemClick(item)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sessionmember, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
