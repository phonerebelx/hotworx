package com.passio.modulepassio.ui.water

import com.passio.modulepassio.databinding.ItemWeightRecordBinding
import com.passio.modulepassio.ui.activity.UserCache
import com.passio.modulepassio.ui.model.WaterRecord
import com.passio.modulepassio.ui.util.StringKT.singleDecimal
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class WaterAdapter(
    private val weightRecords: ArrayList<WaterRecord>,
    private val onTapped: (weightRecord: WaterRecord) -> Unit,
) :
    RecyclerView.Adapter<WaterAdapter.WaterViewHolder>() {

    inner class WaterViewHolder(val binding: ItemWeightRecordBinding) :
        ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(weightRecord: WaterRecord) {
            with(binding) {
                weight.text = weightRecord.getWaterInCurrentUnit().singleDecimal()
                weightUnit.text = UserCache.getProfile().measurementUnit.waterUnit.value
                dateTime.text = "${weightRecord.getDisplayDay()}\n${weightRecord.getDisplayTime()}"
                root.setOnClickListener {
                    onTapped.invoke(weightRecord)
                }
            }
        }
    }

    fun getItem(position: Int): WaterRecord
    {
        return weightRecords[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterViewHolder {
        return WaterViewHolder(
            ItemWeightRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return weightRecords.size
    }

    override fun onBindViewHolder(holder: WaterViewHolder, position: Int) {
        holder.bind(weightRecord = weightRecords[position])
    }
}