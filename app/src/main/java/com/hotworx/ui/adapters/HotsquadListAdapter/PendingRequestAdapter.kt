package com.hotworx.ui.adapters.HotsquadListAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.FoundUser
import com.hotworx.models.HotsquadList.PendingInvitationResponse.SquadData
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchRegisteredAdapter.OnItemClickListener

class PendingRequestAdapter(
    var items: MutableList<SquadData>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PendingRequestAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: SquadData,position:Int)
        fun onItemClickDecline(item: SquadData,position:Int)
    }

    fun updateData(newList: MutableList<SquadData>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val sentTextView: TextView = itemView.findViewById(R.id.tvSentAt)
        private val squadTextView: TextView = itemView.findViewById(R.id.tvSquad)
        private val iconImageView: ImageView = itemView.findViewById(R.id.imgIcon)
        private val declineButton: AppCompatButton = itemView.findViewById(R.id.declineBtn)
        private val acceptButton: AppCompatButton = itemView.findViewById(R.id.AcceptBtn)
        private val firstNameTextView: TextView = itemView.findViewById(R.id.tvfirstLastName)
        private val cvImageCard: CardView = itemView.findViewById(R.id.cvImageCard)
        private val cardView: CardView = itemView.findViewById(R.id.listMainView)

        fun bind(item: SquadData , position: Int) {
            nameTextView.text = item.request_from?.name
            emailTextView.text = item.request_from?.email
            sentTextView.text = item.request_from?.sent_at
            squadTextView.text = item.name

            // Use itemView.context to get the context
//            Glide.with(itemView.context)
//                .load(item.request_from?.profile_image_url)
//                .placeholder(R.drawable.placeholder) // Optional placeholder
//                .into(iconImageView)

            declineButton.setOnClickListener {
                listener.onItemClickDecline(item,position)
            }

            acceptButton.setOnClickListener {
                listener.onItemClick(item, position = position)
            }

            if (item.request_from?.profile_image_url!= null) {
                firstNameTextView.visibility = View.GONE
                cvImageCard.visibility = View.VISIBLE

                Glide.with(context)
                    .load(item.request_from?.profile_image_url)
                    .listener(object : RequestListener<Drawable> {
                        @SuppressLint("SetTextI18n")
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {

                            cvImageCard.visibility = View.GONE
                            firstNameTextView.visibility = View.VISIBLE
                            firstNameTextView.text = getUserInitials(item.request_from?.name)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: com.bumptech.glide.load.DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {

                            return false
                        }
                    })
                    .into(iconImageView)
            } else {
                cvImageCard.visibility = View.GONE
                firstNameTextView.visibility = View.VISIBLE
                firstNameTextView.text = getUserInitials(item.name)
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

    // Helper function to get user initials
    private fun getUserInitials(fullName: String?): String {
        val nameParts = fullName?.split(" ") ?: return ""
        val firstNameInitial = nameParts.getOrNull(0)?.firstOrNull()?.toString() ?: ""
        val lastNameInitial = nameParts.getOrNull(1)?.firstOrNull()?.toString() ?: ""
        return firstNameInitial + lastNameInitial
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
