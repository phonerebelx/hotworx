package com.hotworx.ui.adapters.ViewPager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.models.BusinessCard.BusinessCardModel
import com.hotworx.models.BusinessCard.Data

class ViewPagerAdapter(private val items: BusinessCardModel) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items.data?.get(position) ?: Data("","","","","","","", arrayListOf()))
    }

    override fun getItemCount(): Int {
        return items.data?.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)

        fun bind(data: Data) {
//            textView.text = text
        }
    }
}
