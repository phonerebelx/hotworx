package com.example.passiomodulenew.ui.camera


import com.example.passiomodulenew.ui.util.loadPassioIcon
import com.example.passiomodulenew.ui.util.StringKT.capitalized
import ai.passio.passiosdk.passiofood.DetectedCandidate
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.passio.passiomodulenew.databinding.ItemFoodAlternativeBinding

class FoodAlternativeAdapter(
    private val alternatives: List<DetectedCandidate>,
    private val onAlternativeLogged: (detectedCandidate: DetectedCandidate) -> Unit,
    private val onAlternativeEdit: (detectedCandidate: DetectedCandidate) -> Unit
) :
    RecyclerView.Adapter<FoodAlternativeAdapter.FoodAlternativeViewHolder>() {

    inner class FoodAlternativeViewHolder(val binding: ItemFoodAlternativeBinding) :
        ViewHolder(binding.root) {
        fun bind(detectedCandidate: DetectedCandidate) {
            binding.foodName.text = detectedCandidate.foodName.capitalized()
            binding.foodImage.loadPassioIcon(detectedCandidate.passioID)
            binding.addFood.setOnClickListener {
                onAlternativeLogged.invoke(detectedCandidate)
            }
            binding.viewFoodAlternative.setOnClickListener {
                onAlternativeEdit.invoke(detectedCandidate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAlternativeViewHolder {
        return FoodAlternativeViewHolder(
            ItemFoodAlternativeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return alternatives.size
    }

    override fun onBindViewHolder(holder: FoodAlternativeViewHolder, position: Int) {
        holder.bind(alternatives[position])
    }
}