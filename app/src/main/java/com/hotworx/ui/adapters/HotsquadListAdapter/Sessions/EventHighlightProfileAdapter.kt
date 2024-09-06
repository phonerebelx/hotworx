package com.hotworx.ui.adapters.HotsquadListAdapter.Sessions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotworx.R
import com.hotworx.models.HotsquadList.Session.PendingSessionResponse

class EventHighlightProfileAdapter(
    private val items: MutableList<SessionSquadEventsResponse.SquadEvent.Participant>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<EventHighlightProfileAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: SessionSquadEventsResponse.SquadEvent.Participant, position:Int)
    }

    fun updateData(newList: MutableList<SessionSquadEventsResponse.SquadEvent.Participant>) {
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
        private val profileImage1: ImageView = itemView.findViewById(R.id.user_image1)

        fun bind(item: SessionSquadEventsResponse.SquadEvent.Participant) {

            Glide.with(context)
                .load(item.profile_image)
                .into(profileImage1)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sessionhighlight, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

//    fun getPositionById(listId: String): Int {
////        for (i in items.indices) {
////            if (items[i]. == listId) {
////                return i
////            }
////        }
////        return -1
//    }

    override fun getItemCount(): Int = items.size
}
