package com.hotworx.ui.adapters.HotsquadListAdapter.Sessions

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.hotworx.models.HotsquadList.PendingInvitationResponse.SquadData
import com.hotworx.models.HotsquadList.Session.PendingSessionResponse
import com.hotworx.models.HotsquadList.Session.SessionMemberResponse
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchNotFoundUserAdapter.OnItemClickListener
import com.hotworx.ui.fragments.HotsquadList.HotsquadSearchFragment

class SessionPendingListAdapter(
    private val items: MutableList<PendingSessionResponse.SquadInvitation>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SessionPendingListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: PendingSessionResponse.SquadInvitation, position:Int)
    }

    fun updateData(newList: MutableList<PendingSessionResponse.SquadInvitation>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val sentTextView: TextView = itemView.findViewById(R.id.tvSentAt)
        private val listMainView: CardView = itemView.findViewById(R.id.listMainView)
        private val eventNameTextView: TextView = itemView.findViewById(R.id.tvSquadEventName)
        private val image: ImageView = itemView.findViewById(R.id.imgIcon)

        fun bind(item: PendingSessionResponse.SquadInvitation) {
            nameTextView.text = item.sender_info.name
            emailTextView.text = item.sender_info.email
            sentTextView.text = item.sender_info.sent_at
            eventNameTextView.text = item.squad_event_name
            // Display other information as needed, such as sender name, email, etc.

            listMainView.setOnClickListener{
                listener.onItemClick(item,position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sessionpendingrequest, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun getPositionById(listId: String): Int {
        for (i in items.indices) {
            if (items[i].squad_id == listId) {
                return i
            }
        }
        return -1
    }

    override fun getItemCount(): Int = items.size
}
