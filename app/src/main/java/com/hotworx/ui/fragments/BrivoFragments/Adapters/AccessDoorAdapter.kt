package com.hotworx.ui.fragments.BrivoFragments.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.databinding.AdapterAccessDoorBinding
import com.hotworx.databinding.FragmentShowSlotAdapterBinding
import com.hotworx.interfaces.OnClickStringTypeListener
import com.hotworx.models.BrivoDataModels.AccessPointData
import com.hotworx.models.BrivoDataModels.Data
//import kotlinx.android.synthetic.main.adapter_access_door.view.acbUnlock
//import kotlinx.android.synthetic.main.fragment_location_selection_adapter.view.tvLocaion


class AccessDoorAdapter(val context: Context, val onItemClickInterface: OnClickStringTypeListener) : RecyclerView.Adapter<AccessDoorAdapter.ViewHolder>() {
    lateinit var binding: AdapterAccessDoorBinding
    lateinit var accessPointsArray: ArrayList<AccessPointData>
//    private lateinit var tvAccessPointName: TextView
    private lateinit var acbUnlock: AppCompatButton
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            this.setIsRecyclable(false)
        }


        fun bindItems(item: AccessPointData) {
            binding.tvAccessPointName.text = item.access_point_name

        }
    }

    fun setList(list: ArrayList<AccessPointData>) {
        accessPointsArray = list
        notifyDataSetChanged()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(context)
//        val view =inflater.inflate(R.layout.adapter_access_door, parent, false)
//        tvAccessPointName = view.findViewById(R.id.tvAccessPointName)
//        acbUnlock = view.findViewById(R.id.acbUnlock)
//        return ViewHolder(view)
        binding = AdapterAccessDoorBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = accessPointsArray[position]
        holder.bindItems(item)
        binding.acbUnlock.setOnClickListener {
            onItemClickInterface.onItemClick("${item.lead_id.toString()},${item.access_point_id.toString()}","From_Access_Door_Adapter")
        }
    }


    override fun getItemCount(): Int {
        return when {
            ::accessPointsArray.isInitialized -> accessPointsArray.size
            else -> 0
        }
    }




}