package com.hotworx.ui.adapters.HotsquadListAdapter.Sessions

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotworx.R

class EventHighlightProfileAdapter(
    private val items: MutableList<SessionSquadEventsResponse.SquadEvent.Participant>,
    private val context: Context,
) : RecyclerView.Adapter<EventHighlightProfileAdapter.ViewHolder>() {

    fun updateData(newList: MutableList<SessionSquadEventsResponse.SquadEvent.Participant>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position in items.indices) {  // Safe check for position
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImage1: ImageView = itemView.findViewById(R.id.userImage)

        fun bind(item: SessionSquadEventsResponse.SquadEvent.Participant) {
            profileImage1?.let {
                Glide.with(context)
                    .load(item.profile_image)
                    .placeholder(R.drawable.placeholder_image)  // Optional placeholder
                    .into(it)
            } ?: run {
                Log.e("EventHighlightAdapter", "profileImage1 is null")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sessionhighlightprofile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
