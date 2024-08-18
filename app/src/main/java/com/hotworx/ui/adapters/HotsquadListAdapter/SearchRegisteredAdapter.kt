package com.hotworx.ui.adapters.HotsquadListAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.FoundUser

class SearchRegisteredAdapter(
    private val items: List<FoundUser>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SearchRegisteredAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: FoundUser)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val phoneTextView: TextView = itemView.findViewById(R.id.tvPhone)
        private val iconImageView: ImageView = itemView.findViewById(R.id.imgIcon)
        private val cardView: CardView = itemView.findViewById(R.id.listMainView)
        private val imgcheck: ImageView = itemView.findViewById(R.id.imgCheck)

        fun bind(item: FoundUser) {
            nameTextView.text = item.name
            emailTextView.text = item.email
            phoneTextView.text = item.phone
//            iconImageView.setImageResource(item.profile_image_url)

            // Set the visibility based on the selected state
            imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE

            cardView.setOnClickListener {
               item.selected = !item.selected
               imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE
               notifyItemChanged(adapterPosition)
               listener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_registereduserlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
