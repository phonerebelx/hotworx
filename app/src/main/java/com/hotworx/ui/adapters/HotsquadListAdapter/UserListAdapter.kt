package com.hotworx.ui.adapters.HotsquadListAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.models.HotsquadList.UserModel

class UserListAdapter(
    private val userList: MutableList<UserModel>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvUserName)
        private val deleteImageView: ImageView = itemView.findViewById(R.id.ivDelete) // Ensure this ID matches your XML

        fun bind(user: UserModel, position: Int) {
            nameTextView.text = user.searchedName
            deleteImageView.setOnClickListener {
                onDeleteClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_form, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position], position)
    }

    override fun getItemCount(): Int = userList.size
}
