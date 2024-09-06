package com.hotworx.ui.adapters.HotsquadListAdapter.Sessions

import SessionSquadEventsResponse
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
import com.hotworx.ui.adapters.NutritionistChildItemAdapter

class EventMemberAdapter(
    private val items: MutableList<SessionSquadEventsResponse.Member>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<EventMemberAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: SessionSquadEventsResponse.Member, position:Int)
    }

    fun updateData(newList: MutableList<SessionSquadEventsResponse.Member>) {
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
        private val emailsentTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val calTextView: TextView = itemView.findViewById(R.id.tvCal)
        private val profileImage: ImageView = itemView.findViewById(R.id.imgIcon)
        private val listMainView: CardView = itemView.findViewById(R.id.listMainView)

        fun bind(item: SessionSquadEventsResponse.Member) {
            nameTextView.text = item.name
            emailsentTextView.text = item.email
            calTextView.text = item.burnedCal

            Glide.with(context)
                .load(item.profileImage)
                .into(profileImage)

            // Display other information as needed, such as sender name, email, etc.
            listMainView.setOnClickListener{
                listener.onItemClick(item,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activitymember, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun getPositionById(listId: String): Int {
        for (i in items.indices) {
            if (items[i].id == listId) {
                return i
            }
        }
        return -1
    }

    override fun getItemCount(): Int = items.size
}
