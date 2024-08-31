package com.passio.passiomodule.ui.search

import com.passio.passiomodule.ui.search.FoodSearchView
import com.passio.passiomodule.ui.search.FoodSearchViewModel
import com.passio.passiomodule.data.ResultWrapper
import com.passio.passiomodule.databinding.FragmentSearchBinding
import com.passio.passiomodule.ui.base.BaseFragment
import com.passio.passiomodule.ui.util.toast
import ai.passio.passiosdk.passiofood.PassioFoodDataInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible

class FoodSearchFragment : BaseFragment<FoodSearchViewModel>(), FoodSearchView.PassioSearchListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchFoodSearchView.setup(this)

        viewModel.searchResults.observe(viewLifecycleOwner) { searchResult ->
            _binding?.searchFoodSearchView?.updateSearchResult(
                searchResult.query,
                searchResult.results,
                searchResult.suggestions
            )
        }

        sharedViewModel.addIngredientLD.observe(viewLifecycleOwner) { fr ->
            // viewModel.setFoodRecord(fr)
        }
        viewModel.showLoading.observe(viewLifecycleOwner) {
            binding.loading.isVisible = it
        }
        viewModel.logFoodEvent.observe(viewLifecycleOwner, ::foodItemLogged)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun foodItemLogged(resultWrapper: ResultWrapper<Boolean>) {
        when (resultWrapper) {
            is ResultWrapper.Success -> {
                if (resultWrapper.value) {
                    requireContext().toast("Food item(s) logged.")
                    viewModel.navigateToDiary()
                } else {
                    requireContext().toast("Could not log food item(s).")
                }
            }

            is ResultWrapper.Error -> {
                requireContext().toast(resultWrapper.error)
            }
        }
    }

    override fun onQueryChange(query: String) {
        viewModel.fetchSearchResults(query)
    }

    override fun onFoodItemSelected(searchItem: PassioFoodDataInfo) {
        sharedViewModel.passToEdit(searchItem)
        viewModel.navigateToEdit()
    }

    override fun onFoodItemLog(searchItem: PassioFoodDataInfo) {
        viewModel.logFood(searchItem)
    }

    override fun onTextCleared() {

    }

    override fun onViewDismissed() {
        viewModel.requestNavigateBack()
    }

}