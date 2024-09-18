package com.hotworx.ui.adapters.HotsquadListAdapter.Sessions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.hotworx.R

class EventHighlightProfileAdapter(
    private val items: MutableList<SessionSquadEventsResponse.SquadEvent.Participant>,
    private val context: Context,
) : RecyclerView.Adapter<EventHighlightProfileAdapter.ViewHolder>() {

    fun updateData(newList: MutableList<SessionSquadEventsResponse.SquadEvent.Participant>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position in items.indices) {  // Safe check for position
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImage1: ImageView = itemView.findViewById(R.id.userImage)
        private val firstNameTextView: TextView = itemView.findViewById(R.id.tvfirstLastName)
        private val cvImageCard: CardView = itemView.findViewById(R.id.cvImageCard)
        private val iconImageView: AppCompatImageView = itemView.findViewById(R.id.userImage)

        fun bind(item: SessionSquadEventsResponse.SquadEvent.Participant) {
            profileImage1?.let {
                Glide.with(context)
                    .load(item.profile_image)
                    .placeholder(R.drawable.placeholder_image)  // Optional placeholder
                    .into(it)

                if (item.profile_image!= null) {
                    firstNameTextView.visibility = View.GONE
                    cvImageCard.visibility = View.VISIBLE

                    Glide.with(context)
                        .load(item.profile_image)
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
                                firstNameTextView.text = getUserInitials(item.name)
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
            } ?: run {
                Log.e("EventHighlightAdapter", "profileImage1 is null")
            }
        }
    }

    // Helper function to get user initials
    private fun getUserInitials(fullName: String?): String {
        val nameParts = fullName?.split(" ") ?: return ""
        val firstNameInitial = nameParts.getOrNull(0)?.firstOrNull()?.toString() ?: ""
        val lastNameInitial = nameParts.getOrNull(1)?.firstOrNull()?.toString() ?: ""
        return firstNameInitial + lastNameInitial
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sessionhighlightprofile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
