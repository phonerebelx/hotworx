package com.hotworx.ui.adapters.LocationAdapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hotworx.R
import com.hotworx.databinding.FragmentDashboardSessionAdapterBinding
import com.hotworx.databinding.FragmentLocationSelectionAdapterBinding
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.OnClickStringTypeListener
import com.hotworx.models.SessionBookingModel.Location
//import kotlinx.android.synthetic.main.fragment_location_selection_adapter.view.*

//
class LocationSelectionAdapter(val context: Context, val onItemClickInterface: OnClickStringTypeListener)  : RecyclerView.Adapter<LocationSelectionAdapter.ViewHolder>() {
    lateinit var binding: FragmentLocationSelectionAdapterBinding
//    private lateinit var tvLocaion: TextView
    lateinit var locations: ArrayList<Location>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: Location) {
            this.setIsRecyclable(false)
            binding.tvTitle.text = item.location_name
            binding.tvDesc.text = "this location will charge extra ${item.reciprocal_fees ?: ""}${item.currency_symbol?: ""}"
            if (item.location_tier == "Standard"){
                Glide.with(context)
                    .load(R.drawable.standard)
                    .into(binding.ivTier)
                val params = binding.tvTitle.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = binding.root.context.resources.getDimensionPixelSize(R.dimen._15sdp)
                binding.tvTitle.layoutParams = params
                binding.tvDesc.visibility = View.GONE
            }

            if (item.location_tier == "Premium") {
                Glide.with(context)
                    .load(R.drawable.premium)
                    .into(binding.ivTier)
            }

            if (item.location_tier == "Elite"){
               Glide.with(context)
                    .load(R.drawable.elite)
                    .into(binding.ivTier)
            }

        }
    }
    fun setList(list: ArrayList<Location>) {
        locations = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentLocationSelectionAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = locations[position]
        holder.bindItems(item)
        binding.cvGoTOWorkOut.setOnClickListener {
            if (item.is_allow == "yes") onItemClickInterface.onItemClick(item.location_name,"From_Location_Adapter")
        }
    }

    override fun getItemCount(): Int {
        return when {
            ::locations.isInitialized -> locations.size
            else -> 0
        }
    }


}