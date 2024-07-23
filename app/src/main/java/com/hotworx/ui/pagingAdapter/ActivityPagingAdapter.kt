package com.hotworx.ui.pagingAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.databinding.FragmentActivityByTimelineAdapterBinding
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.models.NewActivityModels.NinetyDaysActivity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class ActivityPagingAdapter(val context: Context, val onItemClickInterface: OnClickItemListener): PagingDataAdapter<NinetyDaysActivity,ActivityPagingAdapter.ViewHolder>(COMPARATOR) {
    lateinit var binding: FragmentActivityByTimelineAdapterBinding


    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<NinetyDaysActivity>() {
            override fun areItemsTheSame(oldItem: NinetyDaysActivity, newItem: NinetyDaysActivity): Boolean {
                // Check if the items have the same unique identifier.
                return oldItem.activity_id == newItem.activity_id
            }

            override fun areContentsTheSame(oldItem: NinetyDaysActivity, newItem: NinetyDaysActivity): Boolean {
                // Check if the contents of the items are the same.
                return oldItem == newItem
            }
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("Range")
        fun bindItems(item: NinetyDaysActivity) {
            this.setIsRecyclable(false)

            binding.tvStartTime.text =  getSeperateDate(item.display_date,"formattedDate")
            binding.tvEndTime.text =  getTimeZone(item.display_date)
            binding.tvCal.text = item.total_burnt
            binding.tvSessionName.text = item.workout_type

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        Log.d("onBindViewHolder: ",item.toString())
        if (item != null) {
            holder.bindItems(item)
        }
        holder.itemView.findViewById<CardView>(R.id.linearLayoutCompat14).setOnClickListener{
//            if (item.is_allow == "yes") {
            onItemClickInterface.onItemClick(item,"")
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentActivityByTimelineAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }



    private fun getTimeZone(date: String): String{
        try {
            val dateFormat = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH)
            val dateTime = LocalDateTime.parse(date, dateFormat)
            val timeFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
            val formattedTime = dateTime.format(timeFormat)
            return formattedTime.toString()
        } catch (e: Exception){
            Log.d("getTimeZone: ",e.message.toString())
            Utils.customToast(context,e.message)
        }
        return ""
    }

    private fun getSeperateDate(date: String,returnObject: String): String{

        // Parse the string using SimpleDateFormat
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH)
        val date = dateFormat.parse(date)

        // Format the parsed date to extract day, date, and time
        val dayFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
        val dateFormat2 = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)

        val day = dayFormat.format(date)
        val formattedDate = dateFormat2.format(date)
        val time = timeFormat.format(date)

        when (returnObject) {
            "day" -> {
                return day
            }
            "formattedDate" -> {
                return formattedDate
            }
            "time" -> {
                return time
            }
        }
        return day
    }
}