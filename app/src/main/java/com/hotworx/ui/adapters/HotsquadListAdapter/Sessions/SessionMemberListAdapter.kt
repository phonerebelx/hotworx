package com.hotworx.ui.adapters.HotsquadListAdapter.Sessions

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.models.HotsquadList.Hotsquad
import com.hotworx.models.HotsquadList.Session.SessionMemberResponse
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchNotFoundUserAdapter.OnItemClickListener
import com.hotworx.ui.fragments.HotsquadList.HotsquadSearchFragment

class SessionMemberListAdapter(
    private val items: List<SessionMemberResponse.SquadData.Member>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SessionMemberListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: SessionMemberResponse.SquadData.Member)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvName)
        private val iconImageView: AppCompatImageView = itemView.findViewById(R.id.imgIcon)
        private val statusTextView: TextView = itemView.findViewById(R.id.tvStatus)
        private val emailTextView: TextView = itemView.findViewById(R.id.tvEmail)
        private val phoneTextView: TextView = itemView.findViewById(R.id.tvPhone)
        private val imgcheck: ImageView = itemView.findViewById(R.id.imgCheck)
        private val imgCheckBox: ImageView = itemView.findViewById(R.id.imgCheckBox)
        private val cardView: CardView = itemView.findViewById(R.id.listMainView)
        private val firstNameTextView: TextView = itemView.findViewById(R.id.tvfirstLastName)
        private val cvImageCard: CardView = itemView.findViewById(R.id.cvImageCard)
        private val detailLayout: LinearLayout = itemView.findViewById(R.id.detailLayoutt)

        fun bind(item: SessionMemberResponse.SquadData.Member) {
            nameTextView.text = item.name
            statusTextView.text = item.invite_message
            emailTextView.text = item.email
            phoneTextView.text = item.phone


            // Set the visibility based on the selected state
            imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE

            Log.d("MemberCheck", "Member ID: ${item.member_id}")

//            val isMemberValid = item.member_id != null && item.has_owner
//
//            if (isMemberValid) {
//                Log.d("MemberCheck", "Handling valid member_id and owner")
//                imgCheckBox.visibility = View.VISIBLE
//                imgCheckBox.setOnClickListener {
//                    item.selected = !item.selected
//                    val color = ContextCompat.getColor(context, R.color.white)
//                    detailLayout.setBackgroundColor(color)
//                    imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE
//                    notifyItemChanged(adapterPosition)
//                    listener.onItemClick(item)
//                }
//            } else {
//                Log.d("MemberCheck", "Handling invalid member_id or owner")
//                imgCheckBox.visibility = View.GONE
//                val color = ContextCompat.getColor(context, R.color.trans_card)
//                detailLayout.setBackgroundColor(color)
//            }

            if (item.member_id == null) {
                Log.d("MemberCheck", "Handling null member_id")
                imgCheckBox.visibility = View.GONE
                val color = ContextCompat.getColor(context, R.color.trans_card)
                detailLayout.setBackgroundColor(color) // Using setBackgroundColor

            } else {
                Log.d("MemberCheckkk", "Handling non-null member_id")
                imgCheckBox.visibility = View.VISIBLE
                imgCheckBox.setOnClickListener {
                    item.selected = !item.selected
                    val color = ContextCompat.getColor(context, R.color.white)
                    detailLayout.setBackgroundColor(color) // Using setBackgroundColor
                    imgcheck.visibility = if (item.selected) View.VISIBLE else View.GONE
                    notifyItemChanged(adapterPosition)
                    listener.onItemClick(item)
                }
            }

            cardView.setOnClickListener {
                listener.onItemClick(item)
            }

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sessionmember, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
