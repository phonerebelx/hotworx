package com.hotworx.ui.adapters.HotsquadListAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.models.HotsquadList.RegisteredMemberItem

class SearchRegisteredAdapter(
    private val items: List<RegisteredMemberItem>,
) : RecyclerView.Adapter<SearchRegisteredAdapter.ViewHolder>() {

//    interface OnItemClickListener {
//        fun onItemClick(item: RegisteredMemberItem)
//    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val phoneTextView: TextView = itemView.findViewById(R.id.tvPhone)
        private val iconImageView: ImageView = itemView.findViewById(R.id.imgIcon)

        fun bind(item: RegisteredMemberItem) {
            nameTextView.text = item.name
            emailTextView.text = item.email
            phoneTextView.text = item.phone
            iconImageView.setImageResource(item.iconResId)
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
