package com.hotworx.ui.adapters.HotsquadListAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.HotsquadItem
import com.hotworx.ui.fragments.HotsquadList.HotsquadSearchFragment

class SquadListAdapter(
    private val items: List<HotsquadItem>,
    private val listener: OnItemClickListener,
    private val dockActivity: DockActivity? = null
) : RecyclerView.Adapter<SquadListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: HotsquadItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val countTextView: TextView = itemView.findViewById(R.id.counter)
        private val iconImageView: ImageView = itemView.findViewById(R.id.imgIcon)
        private val addButton: ImageView = itemView.findViewById(R.id.addButton)
        private val cardView: CardView = itemView.findViewById(R.id.listMainView)

        fun bind(item: HotsquadItem) {
            titleTextView.text = item.title
            countTextView.text = item.count.toString()
            iconImageView.setImageResource(item.iconResId)

            cardView.setOnClickListener {
                Log.d("SquadListAdapter", "Card clicked: ${item.title}")
                val hotsquadSearchFragment = HotsquadSearchFragment()
                dockActivity?.replaceDockableFragment(hotsquadSearchFragment)
            }

            addButton.setOnClickListener {
                Log.d("SquadListAdapter", "Add button clicked: ${item.title}")
                listener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_myhotsquadlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
