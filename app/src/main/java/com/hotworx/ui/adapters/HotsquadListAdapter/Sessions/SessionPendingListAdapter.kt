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
import android.widget.BaseAdapter
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
import com.hotworx.helpers.BasePreferenceHelper
import com.hotworx.models.HotsquadList.Hotsquad
import com.hotworx.models.HotsquadList.PendingInvitationResponse.SquadData
import com.hotworx.models.HotsquadList.Session.PendingSessionResponse
import com.hotworx.models.HotsquadList.Session.SessionMemberResponse
import com.hotworx.models.HotsquadList.SquadMemberDetailsResponse
import com.hotworx.ui.adapters.HotsquadListAdapter.SearchNotFoundUserAdapter.OnItemClickListener
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.HotsquadList.HotsquadSearchFragment

class SessionPendingListAdapter(
    private val items: MutableList<PendingSessionResponse.SquadInvitation>,
    private val context: Context,
    private val preferenceHelper: BasePreferenceHelper,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SessionPendingListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: PendingSessionResponse.SquadInvitation, position:Int)
    }

    fun updateData(newList: MutableList<PendingSessionResponse.SquadInvitation>) {
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
        private val listMainView: CardView = itemView.findViewById(R.id.listMainView)
        private val eventNameTextView: TextView = itemView.findViewById(R.id.tvSquadEventName)
        private val firstNameTextView: TextView = itemView.findViewById(R.id.tvfirstLastName)
        private val cvImageCard: CardView = itemView.findViewById(R.id.cvImageCard)
        private val image: AppCompatImageView = itemView.findViewById(R.id.imgIcon)

        fun bind(item: PendingSessionResponse.SquadInvitation) {
            nameTextView.text = item.sender_info.name
            emailTextView.text = item.sender_info.email
            sentTextView.text = item.sender_info.sent_at
            eventNameTextView.text = item.squad_event_name
            // Display other information as needed, such as sender name, email, etc.

            listMainView.setOnClickListener{
                listener.onItemClick(item,position)
            }


            if (preferenceHelper.imagePath != null) {
                firstNameTextView.visibility = View.GONE
                cvImageCard.visibility = View.VISIBLE

                Glide.with(context)
                    .load(preferenceHelper.imagePath)
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
                            firstNameTextView.text = "${getUserDetail()[1]}${getUserDetail()[2]}"
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
                    .into(image)
            } else {
                cvImageCard.visibility = View.GONE
                firstNameTextView.visibility = View.VISIBLE
                firstNameTextView.text = "${getUserDetail()[1]}${getUserDetail()[2]}"
            }
        }
    }

    private fun getUserDetail(): ArrayList<String> {
        val userName = preferenceHelper.loginData.full_name.split(" ")

        val arrayString = ArrayList<String>()
        var firstName = userName[0]
        var lastName = if (userName.size > 1) {
            if (userName.size > 2 && userName[1].isEmpty()) {
                userName[2]
            } else {
                userName[1]
            }
        } else {
            ""
        }

        val firstFullName = firstName
        val lastFullName = lastName
        if (firstName.isNotEmpty()) firstName = firstName[0].toString()
        if (lastName.isNotEmpty()) lastName = lastName[0].toString()

        val fullName = firstName + lastName

        arrayString.add(fullName)
        arrayString.add(firstName)
        arrayString.add(lastName)
        arrayString.add(firstFullName)
        arrayString.add(lastFullName)

        return arrayString
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sessionpendingrequest, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
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
