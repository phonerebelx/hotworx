package com.hotworx.ui.adapters.HotsquadListAdapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.FoundUser

class SearchRegisteredAdapter(
    private val items: MutableList<FoundUser>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SearchRegisteredAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: FoundUser)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val phoneTextView: TextView = itemView.findViewById(R.id.tvPhone)
        private val iconImageView: ImageView = itemView.findViewById(R.id.imgIcon)
        private val cardView: CardView = itemView.findViewById(R.id.listMainView)
        private val imgcheck: ImageView = itemView.findViewById(R.id.imgCheck)
        private val imgCheckBox: ImageView = itemView.findViewById(R.id.imgCheckBox)

        fun bind(item: FoundUser) {
            nameTextView.text = item.name
            statusTextView.text = item.squad_invite_status
            emailTextView.text = item.email
            phoneTextView.text = item.phone

            // Use itemView.context to get the context
            Glide.with(itemView.context)
                .load(item.profile_image_url)
                .placeholder(R.drawable.placeholder) // Optional placeholder
                .into(iconImageView)

            // Set the visibility based on the selected state
            imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE

            if(item.squad_invite_status == null){
                cardView.setOnClickListener {
                    item.selected = !item.selected
                    imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE
                    notifyItemChanged(adapterPosition)
                    listener.onItemClick(item)
                }
            }else{
                imgCheckBox.visibility = View.GONE
                val color = ContextCompat.getColor(context, R.color.colorLine)
                cardView.backgroundTintList = ColorStateList.valueOf(color)
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
