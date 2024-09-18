package com.hotworx.ui.adapters.HotsquadListAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.hotworx.R
import com.hotworx.helpers.BasePreferenceHelper
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse

class SquadMemberListAdapter(
    val items: MutableList<SquadMemberDetailsResponse.SquadData.Member>,
    private val context: Context,
    private val preferenceHelper: BasePreferenceHelper,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SquadMemberListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: SquadMemberDetailsResponse.SquadData.Member)
    }

    fun updateData(newList: MutableList<SquadMemberDetailsResponse.SquadData.Member>) {
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
        private val iconImageView: AppCompatImageView = itemView.findViewById(R.id.imgIcon)
//        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val phoneTextView: TextView = itemView.findViewById(R.id.tvPhone)
        private val firstNameTextView: TextView = itemView.findViewById(R.id.tvfirstLastName)
        private val cvImageCard: CardView = itemView.findViewById(R.id.cvImageCard)
        private val cardView: CardView = itemView.findViewById(R.id.listMainView)

        fun bind(item: SquadMemberDetailsResponse.SquadData.Member) {
            nameTextView.text = item.name
//            statusTextView.text = item.invite_message
            emailTextView.text = item.email
            phoneTextView.text = item.phone

            if (item.profile_image_url!= null) {
                firstNameTextView.visibility = View.GONE
                cvImageCard.visibility = View.VISIBLE

                Glide.with(context)
                    .load(item.profile_image_url)
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_squadmember, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun getPosition(member: SquadMemberDetailsResponse.SquadData.Member): Int? {
        return items.indexOf(member).takeIf { it >= 0 }
    }

    override fun getItemCount(): Int = items.size
}
