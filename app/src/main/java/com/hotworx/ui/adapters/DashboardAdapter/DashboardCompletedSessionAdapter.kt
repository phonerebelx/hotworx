package com.hotworx.ui.adapters.DashboardAdapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.models.DashboardData.TodaysPendingSession

class DashboardCompletedSessionAdapter(val context: Context) : RecyclerView.Adapter<DashboardCompletedSessionAdapter.ViewHolder>() {
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvSessionName: TextView
    private lateinit var tvCalBurned: TextView
//    R.layout.fragment_dashboard_completed_session_adapter,
    private lateinit var getTodaysCompletedSessionArrayList : ArrayList<TodaysPendingSession>
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: TodaysPendingSession) {
            tvDate.text = item.date
            tvSessionName.text = item.type
            tvCalBurned.text = item.cal_burnt + "cal"
        }
    }
    fun setList(list: ArrayList<TodaysPendingSession>) {
        getTodaysCompletedSessionArrayList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_dashboard_completed_session_adapter, parent, false)
        tvDate = view.findViewById(R.id.tvDate)
        tvTime = view.findViewById(R.id.tvTime)
        tvSessionName = view.findViewById(R.id.tvSessionName)
        tvCalBurned = view.findViewById(R.id.tvCalBurned)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getTodaysCompletedSessionArrayList[position]
        holder.bindItems(item)
    }

    override fun getItemCount(): Int {
        return when {
            ::getTodaysCompletedSessionArrayList.isInitialized -> getTodaysCompletedSessionArrayList.size
            else -> 0
        }
    }

}