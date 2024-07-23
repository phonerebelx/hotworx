package com.hotworx.ui.adapters.DashboardAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hotworx.R
import com.hotworx.databinding.FragmentActivityByTimelineAdapterBinding
import com.hotworx.databinding.FragmentDashboardSessionAdapterBinding
import com.hotworx.interfaces.OnClickPendingModelInterface
import com.hotworx.interfaces.OnClickTypeListener
import com.hotworx.interfaces.OnItemClickInterface
import com.hotworx.models.DashboardData.TodaysPendingSession
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.GetShowSlotDataModelItem
//import kotlinx.android.synthetic.main.fragment_dashboard_session_adapter.view.*

class DashboardPendingSessionAdapter(
    val context: Context,
    val onItemClickInterface: OnClickPendingModelInterface
) : RecyclerView.Adapter<DashboardPendingSessionAdapter.ViewHolder>() {
    lateinit var binding: FragmentDashboardSessionAdapterBinding
    private lateinit var tvDate: TextView
    private lateinit var tvMonth: TextView
    private lateinit var tvSessionName: TextView
    private lateinit var tvSauna: TextView
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private lateinit var ivImg1: ImageView
    private lateinit var ivImg2: ImageView
    private lateinit var cvSession: CardView
    private lateinit var getTodaysPendingSessionArrayList: ArrayList<TodaysPendingSession>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: TodaysPendingSession) {
            binding.tvDate.text = item.date
            binding.tvMonth.text = item.week_day
            binding.tvSessionName.text = item.session_name
            binding.tvSauna.text = item.sauna
            binding.tvStartTime.text = item.slot.split("-").firstOrNull()
            binding.tvEndTime.text = item.slot.split("-").lastOrNull()
        }
    }

    fun setList(list: ArrayList<TodaysPendingSession>) {
        getTodaysPendingSessionArrayList = list
        notifyDataSetChanged()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentDashboardSessionAdapterBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_dashboard_session_adapter, parent, false)
        tvDate = view.findViewById(R.id.tvDate)
        tvMonth = view.findViewById(R.id.tvMonth)
        tvSessionName = view.findViewById(R.id.tvSessionName)
        tvSauna = view.findViewById(R.id.tvSauna)
        tvStartTime = view.findViewById(R.id.tvStartTime)
        tvEndTime = view.findViewById(R.id.tvEndTime)
        ivImg1 = view.findViewById(R.id.ivImg1)
        ivImg2 = view.findViewById(R.id.ivImg2)
        cvSession = view.findViewById(R.id.cvSession)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getTodaysPendingSessionArrayList[position]
        holder.bindItems(item)
        binding.ivImg1.setOnClickListener {
            onItemClickInterface.onItemClick(item, "COME_FROM_IMAGE_VIEW")
        }
        binding.ivImg2.setOnClickListener {
            onItemClickInterface.onItemClick(item, "COME_FROM_DELETE_IMAGE_VIEW")
        }
        binding.cvSession.setOnClickListener {
            onItemClickInterface.onItemClick(item, "COME_FROM_TAB_VIEW")
        }

    }

    override fun getItemCount(): Int {
        return when {
            ::getTodaysPendingSessionArrayList.isInitialized -> getTodaysPendingSessionArrayList.size
            else -> 0
        }
    }
}