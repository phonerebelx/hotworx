package com.hotworx.ui.adapters.HotsquadListAdapter.Sessions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotworx.R

class ParticipantAdapter(
    private val participants: List<SessionSquadEventsResponse.SquadEvent.Participant>
) : RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder>() {

    inner class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.userImage)
        val name: TextView = itemView.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sessionhighlightprofile, parent, false)
        return ParticipantViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val participant = participants[position]
        holder.name.text = participant.name
        // Load image using Glide or any other image loading library
        Glide.with(holder.itemView.context)
            .load(participant.profile_image)
            .into(holder.profileImage)
    }

    override fun getItemCount(): Int = participants.size
}
