package com.hotworx.ui.adapters.MyReferral

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hotworx.R
import com.hotworx.databinding.FragmentMyReferralAdapterBinding
import com.hotworx.databinding.SingleReferralLocationAdapterBinding
import com.hotworx.models.ComposeModel.MyReferrals.ReferralDetail
import com.hotworx.models.ComposeModel.RefferalDetailModel.Data
import com.hotworx.ui.adapters.ReferralLocation.ReferralLocationAdapter


class MyReferralAdapter(private val context: Context) :
    RecyclerView.Adapter<MyReferralAdapter.ViewHolder>() {


    lateinit var binding: FragmentMyReferralAdapterBinding
    lateinit var getReferralDetail: List<ReferralDetail>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("Range")
        fun bindItems(item: ReferralDetail) {
            setIsRecyclable(false)
            Glide.with(context)
                .load(item.profile_pic_url)
                .into(binding.tvfirstLastName)
                binding.tvLocation.text = item.referral_name
                binding.tvStatus.text = item.member_status
                binding.tvCount.text = (adapterPosition+1).toString()
                if ((adapterPosition+1) > 9){
                    binding.tvFirstCount.visibility = View.GONE
                }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentMyReferralAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }


    override fun getItemCount(): Int {
        return when {
            ::getReferralDetail.isInitialized -> getReferralDetail.size
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getReferralDetail[position]
        holder.bindItems(item)
    }

    fun setList(list: List<ReferralDetail>) {
        getReferralDetail = list
        notifyDataSetChanged()
    }

}