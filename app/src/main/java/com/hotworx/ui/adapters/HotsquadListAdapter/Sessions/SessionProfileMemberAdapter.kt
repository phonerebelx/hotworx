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
import com.hotworx.models.HotsquadList.Session.UserActivitiesResponse
import com.hotworx.ui.adapters.NutritionistChildItemAdapter

class SessionProfileMemberAdapter(
    private val items: MutableList<UserActivitiesResponse.UserData.Activity>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SessionProfileMemberAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: UserActivitiesResponse.UserData.Activity, position:Int)
    }

    fun updateData(newList: MutableList<UserActivitiesResponse.UserData.Activity>) {
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
        private val typeTextView: TextView = itemView.findViewById(R.id.tvType)
        private val dateTextView: TextView = itemView.findViewById(R.id.tvDate)
        private val locationTextView: TextView = itemView.findViewById(R.id.tvLocation)
        private val calTextView: TextView = itemView.findViewById(R.id.tvCal)

        fun bind(item: UserActivitiesResponse.UserData.Activity) {
            typeTextView.text = item.workout_type
            dateTextView.text = item.workout_date
            locationTextView.text = item.location_name
            calTextView.text = item.total_burnt

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sessionactivity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun getPositionById(listId: String): Int {
//        for (i in items.indices) {
//            if (items[i].id == listId) {
//                return i
//            }
//        }
        return -1
    }

    override fun getItemCount(): Int = items.size
}
