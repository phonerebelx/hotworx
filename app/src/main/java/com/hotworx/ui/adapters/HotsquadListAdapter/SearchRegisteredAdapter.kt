package com.hotworx.ui.adapters.HotsquadListAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.hotworx.R
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
        private val imgCheck: ImageView = itemView.findViewById(R.id.imgCheck)
        private val imgCheckBox: ImageView = itemView.findViewById(R.id.imgCheckBox)
        private val firstNameTextView: TextView = itemView.findViewById(R.id.tvfirstLastName)
        private val cvImageCard: CardView = itemView.findViewById(R.id.cvImageCard)

        fun bind(item: FoundUser) {
            nameTextView.text = item.name
            statusTextView.text = item.squad_invite_status
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

            // Make the checkbox visible by default
            imgCheckBox.visibility = View.VISIBLE

            // Set the visibility based on the selected state
            imgCheck.visibility = if (item.selected) View.VISIBLE else View.GONE

            // Determine card color based on invite status
            if (item.squad_invite_status == null) {
                cardView.setOnClickListener {
                    item.selected = !item.selected
                    imgCheck.visibility = if (item.selected) View.VISIBLE else View.GONE
                    notifyItemChanged(adapterPosition)
                    listener.onItemClick(item)
                }
                cardView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            } else {
                imgCheckBox.visibility = View.GONE
                cardView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorLine))
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_registereduserlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
