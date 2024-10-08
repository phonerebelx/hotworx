package com.example.passiomodulenew.ui.diary

import ai.passio.nutrition.uimodule.ui.model.SuggestedFoods
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.passiomodulenew.ui.util.StringKT.capitalized
import com.example.passiomodulenew.ui.util.loadPassioIcon
import com.passio.passiomodulenew.databinding.ItemFoodSuggestionBinding

class FoodSuggestionsAdapter(
    private val suggestedFoods: List<SuggestedFoods>,
    private val onFoodLog: (suggestedFood: SuggestedFoods) -> Unit,
    private val onFoodEdit: (suggestedFood: SuggestedFoods) -> Unit
) :
    RecyclerView.Adapter<FoodSuggestionsAdapter.FoodSuggestionViewHolder>() {

    inner class FoodSuggestionViewHolder(val binding: ItemFoodSuggestionBinding) :
        ViewHolder(binding.root) {
        fun bind(suggestedFood: SuggestedFoods) {
            binding.foodName.text = suggestedFood.name.capitalized()
            binding.foodImage.loadPassioIcon(suggestedFood.iconId)
            binding.addFood.setOnClickListener {
                onFoodLog.invoke(suggestedFood)
            }
            binding.viewFoodAlternative.setOnClickListener {
                onFoodEdit.invoke(suggestedFood)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodSuggestionViewHolder {
        return FoodSuggestionViewHolder(
            ItemFoodSuggestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return suggestedFoods.size
    }

    override fun onBindViewHolder(holder: FoodSuggestionViewHolder, position: Int) {
        holder.bind(suggestedFoods[position])
    }
}