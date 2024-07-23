package com.hotworx.ui.adapters.FrequentlyBooked

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.OnItemClickInterface
import com.hotworx.models.SessionBookingModel.Location

class FrequentBookingAdapter(val context: Context,val onItemClickInterface: OnItemClickInterface): RecyclerView.Adapter<FrequentBookingAdapter.ViewHolder>() {

    lateinit var countryBookingName: ArrayList<Location>
    private lateinit var tvCountryBooking: TextView
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("Range")
        fun bindItems(item: Location) {
            this.setIsRecyclable(false)
            tvCountryBooking.text = item.location_name
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_frequent_booking_adapter, parent, false)
        tvCountryBooking = view.findViewById(R.id.tvCountryBooking)
        return ViewHolder(view)
    }

    fun setList(list: ArrayList<Location>) {
        countryBookingName = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = countryBookingName[position]
        holder.bindItems(item)
        holder.itemView.findViewById<ConstraintLayout>(R.id.clFrequentAdapter).setOnClickListener{
            if (item.is_allow == "yes") {
                onItemClickInterface.onItemClick(item.location_name)
            }
        }
    }

    override fun getItemCount(): Int {
        return when {
            ::countryBookingName.isInitialized -> countryBookingName.size
            else -> 0
        }
    }

}