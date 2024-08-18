package com.hotworx.ui.adapters.HotsquadListAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.FoundUser
import com.hotworx.models.HotsquadList.NotFoundUser

class SearchNotFoundUserAdapter(
    private val items: List<NotFoundUser>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SearchNotFoundUserAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: NotFoundUser)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)
        private val cardView: CardView = itemView.findViewById(R.id.listMainView)
        private val imgcheck: ImageView = itemView.findViewById(R.id.imgCheck)

        fun bind(item: NotFoundUser) {
            titleTextView.text = item.searchBy
            statusTextView.text = item.recordStatus

            // Set the visibility based on the selected state
            imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE

            cardView.setOnClickListener {
                item.selected = !item.selected
                imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE
                listener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notfounduserlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
