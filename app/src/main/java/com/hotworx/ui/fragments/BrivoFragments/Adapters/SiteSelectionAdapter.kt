package com.hotworx.ui.adapters.LocationAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.databinding.FragmentDashboardSessionAdapterBinding
import com.hotworx.databinding.FragmentLocationSelectionAdapterBinding
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.BrivoDataModels.BrivoLocation.Site
import com.hotworx.models.BrivoDataModels.Data
//import kotlinx.android.synthetic.main.fragment_location_selection_adapter.view.*

//
class SiteSelectionAdapter(val context: Context, val onItemClickInterface: OnClickItemListener)  : RecyclerView.Adapter<SiteSelectionAdapter.ViewHolder>() {

    lateinit var binding: FragmentLocationSelectionAdapterBinding

    lateinit var locations: ArrayList<Site>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            this.setIsRecyclable(false)

        }


        fun bindItems(item: Site) {
//            binding.ivForwardArrow.visibility = View.VISIBLE
            binding.tvTitle.text = item.siteName
        }
    }
    fun setList(list: ArrayList<Site>) {
        locations = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentLocationSelectionAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
//        tvLocaion = view.findViewById(R.id.tvLocaion)
//        ivForwardArrow = view.findViewById(R.id.ivForwardArrow)
//        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = locations[position]
        holder.bindItems(item)
        binding.tvTitle.setOnClickListener {

        onItemClickInterface.onItemClick(item,"From_Site_Adapter")
        }
    }

    override fun getItemCount(): Int {
        return when {
            ::locations.isInitialized -> locations.size
            else -> 0
        }
    }


}