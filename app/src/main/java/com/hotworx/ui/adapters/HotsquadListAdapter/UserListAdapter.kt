package com.hotworx.ui.adapters.HotsquadListAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.models.HotsquadList.UserModel

class UserListAdapter(private val userList: MutableList<UserModel>) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View,private val adapter: UserListAdapter) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvUserName)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.ivDelete)

        fun bind(user: UserModel) {
            nameTextView.text = user.searchedName

            deleteImageView.setOnClickListener {
                adapter.removeAt(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_form, parent, false)
        return ViewHolder(view,this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    fun removeAt(position: Int) {
        if (position >= 0 && position < userList.size) {
            userList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }
}
