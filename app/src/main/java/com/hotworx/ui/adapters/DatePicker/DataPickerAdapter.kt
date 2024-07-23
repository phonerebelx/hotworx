package com.hotworx.ui.adapters.DatePicker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.interfaces.OnItemClickInterface
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DataPickerAdapter(val context: Context, val onItemClickInterface: OnItemClickInterface): RecyclerView.Adapter<DataPickerAdapter.ViewHolder>(){
    lateinit var getDateArray: ArrayList<String>
    lateinit var weekShowTextView: TextView
    lateinit var dayShowTextView: TextView
    var isAdapterLoad: Boolean = false
    private var selectedItemPosition: Int = -1
    var setFirstColor: Boolean = true
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("Range")
        fun bindItems(item: String,position: Int) {
            this.setIsRecyclable(false)

            weekShowTextView.text = getDayOfWeekFromDate(item)
            dayShowTextView.text = item.split("-").lastOrNull()

            if (position == selectedItemPosition) {
                dayShowTextView.setBackgroundResource(R.drawable.rounded_border_assent)
                dayShowTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorWhite))
            } else {
                if (setFirstColor){
                    dayShowTextView.setBackgroundResource(R.drawable.rounded_border_assent)
                    dayShowTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorWhite))
                    onItemClickInterface.onItemClick(item)
                    setFirstColor = false
                }
                else if (!setFirstColor){
                    dayShowTextView.setBackgroundResource(R.drawable.rounded_border)
                    dayShowTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorLightBlack))

                }
            }

            itemView.setOnClickListener {
                selectedItemPosition = adapterPosition
                notifyDataSetChanged()
                onItemClickInterface.onItemClick(item)
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_data_picker_adapter, parent, false)
        weekShowTextView = view.findViewById(R.id.weekShowTextView)
        dayShowTextView = view.findViewById(R.id.dayShowTextView)
        return ViewHolder(view)
    }

    fun setList(list: ArrayList<String>) {
        getDateArray = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getDateArray[position]

        holder.bindItems(item,position)
    }

    override fun getItemCount(): Int {
        return when {
            ::getDateArray.isInitialized -> getDateArray.size
            else -> 0
        }
    }

    fun getDayOfWeekFromDate(inputDate: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date: Date = format.parse(inputDate) ?: Date()

        val calendar = Calendar.getInstance()
        calendar.time = date

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "Sun"
            Calendar.MONDAY -> "Mon"
            Calendar.TUESDAY -> "Tue"
            Calendar.WEDNESDAY -> "Wed"
            Calendar.THURSDAY -> "Thu"
            Calendar.FRIDAY -> "Fri"
            Calendar.SATURDAY -> "Sat"
            else -> ""
        }
    }


}