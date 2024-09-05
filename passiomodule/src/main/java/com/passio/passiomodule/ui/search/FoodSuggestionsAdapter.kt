package com.passio.passiomodule.ui.search

import com.passio.passiomodule.databinding.SuggestionsLayoutBinding
import com.passio.passiomodule.ui.util.StringKT.capitalized
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FoodSuggestionsAdapter(
    private val suggestionCallback: (suggestion: String) -> Unit,
) : RecyclerView.Adapter<FoodSuggestionsAdapter.FoodSuggestionViewHolder>() {

    private val suggestions = mutableListOf<String>()

    fun updateSuggestions(suggestions: List<String>) {
        this.suggestions.clear()
        this.suggestions.addAll(suggestions)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodSuggestionViewHolder {
        val binding = SuggestionsLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodSuggestionViewHolder(binding)
    }

    override fun getItemCount(): Int = suggestions.size

    override fun onBindViewHolder(holder: FoodSuggestionViewHolder, position: Int) {
        holder.bind(suggestions[position])
    }

    inner class FoodSuggestionViewHolder(
        private val binding: SuggestionsLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: String) {
            binding.foodSuggestion.text = suggestion.capitalized()
            binding.root.setOnClickListener {
                suggestionCallback(suggestion)
            }
        }
    }
}