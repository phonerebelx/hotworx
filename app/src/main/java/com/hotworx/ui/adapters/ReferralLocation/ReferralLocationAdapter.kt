package com.hotworx.ui.adapters.ReferralLocation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.databinding.SingleReferralLocationAdapterBinding
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.ComposeModel.RefferalDetailModel.Data

class ReferralLocationAdapter(
    private val context: Context,
    private val clickListener: OnClickItemListener
) : RecyclerView.Adapter<ReferralLocationAdapter.ViewHolder>() {

    lateinit var binding: SingleReferralLocationAdapterBinding
    lateinit var referralData: List<Data>
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("Range")
        fun bindItems(item: Data) {
            this.setIsRecyclable(false)
            binding.tvLocation.text = item.location_name
            binding.tvCount.text = (adapterPosition+1).toString()

            binding.cvProperty.setOnClickListener {
                clickListener.onItemClick(item, "LOCATION_DETAIL")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = SingleReferralLocationAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return when {
            ::referralData.isInitialized -> referralData.size
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = referralData[position]
        holder.bindItems(item)


    }

    fun setList(list: List<Data>) {
        referralData = list
        notifyDataSetChanged()
    }

}