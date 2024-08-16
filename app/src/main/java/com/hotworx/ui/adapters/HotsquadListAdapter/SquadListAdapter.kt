package com.hotworx.ui.adapters.HotsquadListAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.Hotsquad
import com.hotworx.ui.fragments.HotsquadList.HotsquadSearchFragment

class SquadListAdapter(
    private val items: List<Hotsquad>,
    private val listener: OnItemClickListener,
    private val dockActivity: DockActivity? = null
) : RecyclerView.Adapter<SquadListAdapter.ViewHolder>() {

    var id = ""

    interface OnItemClickListener {
        fun onItemClick(item: Hotsquad)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val countTextView: TextView = itemView.findViewById(R.id.counter)
        private val iconImageView: ImageView = itemView.findViewById(R.id.imgIcon)
        private val addButton: ImageView = itemView.findViewById(R.id.addButton)
        private val cardView: CardView = itemView.findViewById(R.id.listMainView)

        fun bind(item: Hotsquad) {
            titleTextView.text = item.name
            countTextView.text = item.total_members.toString()
//            iconImageView.setImageResource(item.iconResId)

            cardView.setOnClickListener {
                listener.onItemClick(item)
            }

            addButton.setOnClickListener {
                val hotsquadSearchFragment = HotsquadSearchFragment().apply {
                    arguments = Bundle().apply {
                        putString("squad_id", item.squad_id)
                    }
                }
                dockActivity?.replaceDockableFragment(hotsquadSearchFragment)
//                val hotsquadSearchFragment = HotsquadSearchFragment()
//                dockActivity?.replaceDockableFragment(hotsquadSearchFragment)
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
