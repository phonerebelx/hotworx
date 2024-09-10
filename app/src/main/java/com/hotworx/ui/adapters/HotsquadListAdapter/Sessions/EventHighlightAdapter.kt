package com.hotworx.ui.adapters.HotsquadListAdapter.Sessions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R

class EventHighlightAdapter(
    private val items: MutableList<SessionSquadEventsResponse.SquadEvent>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<EventHighlightAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: SessionSquadEventsResponse.SquadEvent, position: Int)
    }

    fun updateData(newList: MutableList<SessionSquadEventsResponse.SquadEvent>) {
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
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val sentTextView: TextView = itemView.findViewById(R.id.tvDate)
        private val moveTextView: TextView = itemView.findViewById(R.id.tvMove)
        private val exerciseTextView: TextView = itemView.findViewById(R.id.tvExcerciseTime)
        private val listMainView: CardView = itemView.findViewById(R.id.listMainView)
        private val recyclerViewHighlightProfile: RecyclerView = itemView.findViewById(R.id.recyclerViewHighlightProfile)

        fun bind(item: SessionSquadEventsResponse.SquadEvent) {
            // Binding data to the TextViews
            nameTextView.text = item.highlights.title
            sentTextView.text = item.highlights.sessionDate
            moveTextView.text = item.highlights.totalBurnedCal
            exerciseTextView.text = item.highlights.avgExerciseTime

            // Set up the RecyclerView for participants
            recyclerViewHighlightProfile.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val participantAdapter = EventHighlightProfileAdapter(item.participants, itemView.context)
            recyclerViewHighlightProfile.adapter = participantAdapter

            
            // Click listener for the main view
            listMainView.setOnClickListener {
                listener.onItemClick(item, bindingAdapterPosition)  // Use bindingAdapterPosition
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sessionhighlight, parent, false)
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
