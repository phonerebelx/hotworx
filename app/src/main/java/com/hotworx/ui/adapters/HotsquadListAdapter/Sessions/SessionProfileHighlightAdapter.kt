package com.hotworx.ui.adapters.HotsquadListAdapter.Sessions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.models.HotsquadList.Session.UserActivitiesResponse

class SessionProfileHighlightAdapter(
    private val items: MutableList<UserActivitiesResponse.UserData.Highlight>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SessionProfileHighlightAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: UserActivitiesResponse.UserData.Highlight, position:Int)
    }

    fun updateData(newList: MutableList<UserActivitiesResponse.UserData.Highlight>) {
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
        private val sentTextView: TextView = itemView.findViewById(R.id.tvDate)
        private val moveTextView: TextView = itemView.findViewById(R.id.tvMove)
        private val ExerciseTextView: TextView = itemView.findViewById(R.id.tvExcerciseTime)
        private val listMainView: CardView = itemView.findViewById(R.id.listMainView)

        fun bind(item: UserActivitiesResponse.UserData.Highlight) {
            nameTextView.text = item.title
            sentTextView.text = item.session_date
            moveTextView.text = item.burned_cal
            ExerciseTextView.text = item.exercise_time
            // Display other information as needed, such as sender name, email, etc.

            listMainView.setOnClickListener {
                listener.onItemClick(item, adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sessionprofilehighlight, parent, false)
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
