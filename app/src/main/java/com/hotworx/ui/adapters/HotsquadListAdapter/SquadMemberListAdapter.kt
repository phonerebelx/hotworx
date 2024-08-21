package com.hotworx.ui.adapters.HotsquadListAdapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.Hotsquad
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchNotFoundUserAdapter.OnItemClickListener
import com.hotworx.ui.fragments.HotsquadList.HotsquadSearchFragment

class SquadMemberListAdapter(
    private val items: List<SquadMemberDetailsResponse.SquadData.Member>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SquadMemberListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: SquadMemberDetailsResponse.SquadData.Member)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val iconImageView: ImageView = itemView.findViewById(R.id.imgIcon)
        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val phoneTextView: TextView = itemView.findViewById(R.id.tvPhone)
        private val imgcheck: ImageView = itemView.findViewById(R.id.imgCheck)
        private val imgCheckBox: ImageView = itemView.findViewById(R.id.imgCheckBox)
        private val cardView: CardView = itemView.findViewById(R.id.listMainView)

        fun bind(item: SquadMemberDetailsResponse.SquadData.Member) {
            nameTextView.text = item.name
            statusTextView.text = item.invite_message
            emailTextView.text = item.email
            phoneTextView.text = item.phone

            // Use itemView.context to get the context
            Glide.with(itemView.context)
                .load(item.profile_image_url)
                .placeholder(R.drawable.placeholder) // Optional placeholder
                .into(iconImageView)

            // Set the visibility based on the selected state
            imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE

            if(item.has_squad_access){
                imgCheckBox.setOnClickListener {
                    item.selected = !item.selected
                    imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE
                    notifyItemChanged(adapterPosition)
                    listener.onItemClick(item)
                }
            }else{ }
//            cardView.setOnClickListener {
//                listener.onItemClick(item)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_squadmember, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
