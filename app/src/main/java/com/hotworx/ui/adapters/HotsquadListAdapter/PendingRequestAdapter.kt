package com.hotworx.ui.adapters.HotsquadListAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.bumptech.glide.Glide
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.FoundUser
import com.hotworx.models.HotsquadList.PendingInvitationResponse.SquadData
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchRegisteredAdapter.OnItemClickListener

class PendingRequestAdapter(
    val items: MutableList<SquadData>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PendingRequestAdapter.ViewHolder>() {

    var id = ""

    interface OnItemClickListener {
        fun onItemClick(item: SquadData,position:Int)
        fun onItemClickDecline(item: SquadData,position:Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val sentTextView: TextView = itemView.findViewById(R.id.tvSentAt)
        private val iconImageView: ImageView = itemView.findViewById(R.id.imgIcon)
        private val declineButton: AppCompatButton = itemView.findViewById(R.id.declineBtn)
        private val acceptButton: AppCompatButton = itemView.findViewById(R.id.AcceptBtn)

        fun bind(item: SquadData , position: Int) {
            nameTextView.text = item.request_from?.name
            emailTextView.text = item.request_from?.email
            sentTextView.text = item.request_from?.sent_at

            // Use itemView.context to get the context
            Glide.with(itemView.context)
                .load(item.request_from?.profile_image_url)
                .placeholder(R.drawable.placeholder) // Optional placeholder
                .into(iconImageView)

            declineButton.setOnClickListener {
                listener.onItemClickDecline(item,position)
            }

            acceptButton.setOnClickListener {
                listener.onItemClick(item, position = position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_squadpendingrequest, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    fun getPositionById(listId: String): Int {
        for (i in items.indices) {
            if (items[i].squad_id == listId) {
                return i
            }
        }
        return -1
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = items.size
}
