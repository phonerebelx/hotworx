package com.example.passiomodulenew.ui.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.passiomodulenew.ui.model.FoodRecord
import com.example.passiomodulenew.ui.util.StringKT.capitalized
import com.example.passiomodulenew.ui.util.StringKT.singleDecimal
import com.example.passiomodulenew.ui.util.loadFoodImage
import com.passio.passiomodulenew.databinding.FoodLogBodyLayoutBinding
import kotlin.math.roundToInt

class DiaryLogsAdapter(
    private val recordSelected: (foodRecord: FoodRecord) -> Unit
) : RecyclerView.Adapter<DiaryLogsAdapter.LogsViewHolder>() {

    private val records = mutableListOf<FoodRecord>()

    fun updateLogs(records: List<FoodRecord>) {
        this.records.clear()
        this.records.addAll(records)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsViewHolder {
        val binding = FoodLogBodyLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogsViewHolder(binding)
    }

    override fun getItemCount(): Int = records.size

    override fun onBindViewHolder(holder: LogsViewHolder, position: Int) {
        holder.bindTo(records[position], position)
    }

    fun getFoodRecordAt(index: Int) = records[index]

    inner class LogsViewHolder(
        private val binding: FoodLogBodyLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(foodRecord: FoodRecord, position: Int) {
            with(binding) {
                image.loadFoodImage(foodRecord)
                name.text = foodRecord.name.capitalized()
                val cal = foodRecord.nutrientsSelectedSize().calories()?.value?.roundToInt() ?: 0
                calories.text = "$cal cal"

                val quantity = foodRecord.getSelectedQuantity().singleDecimal()
                val selectedUnit = foodRecord.getSelectedUnit()
                val weight = foodRecord.servingWeight().gramsValue()
                servingSize.text = "$quantity $selectedUnit (${weight.roundToInt()}g)"

                root.setOnClickListener {
                    recordSelected.invoke(foodRecord)
                }
            }
        }
    }
}