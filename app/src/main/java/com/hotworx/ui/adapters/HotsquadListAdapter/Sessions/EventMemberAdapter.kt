package com.hotworx.ui.adapters.HotsquadListAdapter.Sessions

import SessionSquadEventsResponse
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
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
import com.hotworx.ui.adapters.NutritionistChildItemAdapter

class EventMemberAdapter(
    private val items: MutableList<SessionSquadEventsResponse.Member>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<EventMemberAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: SessionSquadEventsResponse.Member, position:Int)
    }

    fun updateData(newList: MutableList<SessionSquadEventsResponse.Member>) {
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
        private val emailsentTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val calTextView: TextView = itemView.findViewById(R.id.tvCal)
        private val profileImage: AppCompatImageView = itemView.findViewById(R.id.imgIcon)
        private val listMainView: CardView = itemView.findViewById(R.id.listMainView)
        private val firstNameTextView: TextView = itemView.findViewById(R.id.tvfirstLastName)
        private val cvImageCard: CardView = itemView.findViewById(R.id.cvImageCard)

        fun bind(item: SessionSquadEventsResponse.Member) {
            nameTextView.text = item.name
            emailsentTextView.text = item.email
            calTextView.text = item.burnedCal

            // Display other information as needed, such as sender name, email, etc.
            listMainView.setOnClickListener{
                listener.onItemClick(item,position)
            }

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
                    .into(profileImage)
            } else {
                cvImageCard.visibility = View.GONE
                firstNameTextView.visibility = View.VISIBLE
                firstNameTextView.text = getUserInitials(item.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activitymember, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
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
            if (items[i].id == listId) {
                return i
            }
        }
        return -1
    }

    override fun getItemCount(): Int = items.size
}
