package com.hotworx.ui.adapters.HotsquadListAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.FoundUser
import com.hotworx.models.HotsquadList.NotFoundUser

class SearchNotFoundUserAdapter(
    private val items: List<NotFoundUser>,
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<SearchNotFoundUserAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: FoundUser)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val phoneTextView: TextView = itemView.findViewById(R.id.tvPhone)
        private val iconImageView: ImageView = itemView.findViewById(R.id.imgIcon)

        fun bind(item: NotFoundUser) {
            nameTextView.text = item.name
            emailTextView.text = item.email
            phoneTextView.text = item.phone
//            iconImageView.setImageResource(item.profileImageUrl)
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
