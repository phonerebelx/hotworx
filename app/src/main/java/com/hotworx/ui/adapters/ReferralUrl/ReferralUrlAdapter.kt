package com.hotworx.ui.adapters.ReferralUrl

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.databinding.ItemReferralUrlBinding
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.ComposeModel.RefferalDetailModel.Data

class ReferralUrlAdapter(
    private val context: Context,
    private val clickListener: OnClickItemListener
) : RecyclerView.Adapter<ReferralUrlAdapter.ViewHolder>() {

    private var dataList: ArrayList<Data> = ArrayList()

    interface OnItemClickListener {
        fun onItemClick(data: Data)
    }

    fun setList(data: ArrayList<Data>) {
//        dataList = data
//        Log.d("AdapterBind", "Data list size: ${data.size}")
//        data.forEachIndexed { index, item ->
//            Log.d("AdapterBind", "Item $index - URL: ${item.url}, Type: ${item.type}")
//        }
//        notifyDataSetChanged()

        dataList.clear()  // Clear the old data
        dataList.addAll(data)  // Add the new data
        Log.d("AdapterBind", "Data list size: ${dataList.size}")
        dataList.forEachIndexed { index, item ->
            Log.d("AdapterBind", "Item $index - URL: ${item.url}, Type: ${item.type}")
        }
        notifyDataSetChanged()  // Notify that the data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReferralUrlBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        Log.d("onBindViewHolder", "Binding item at position $position - URL: ${item.url}, Type: ${item.type}")
        holder.bind(item)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: ItemReferralUrlBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Data) {

            binding.tvUrl.text = item.type ?: ""
            this.setIsRecyclable(false)
            binding.root.setOnClickListener {
                Log.d("ViewHolderClick", "Clicked item - URL: ${item.url}, Type: ${item.type}")
                clickListener.onItemClick(item,"Location_URL")
            }
        }
    }
}