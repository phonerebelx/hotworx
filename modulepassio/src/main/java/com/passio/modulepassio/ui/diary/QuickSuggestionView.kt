package com.passio.modulepassio.ui.diary

import com.passio.modulepassio.databinding.QuickSuggestionViewBinding
import com.passio.modulepassio.ui.model.SuggestedFoods
import com.passio.modulepassio.ui.util.DesignUtils
import com.passio.modulepassio.ui.view.BottomSpaceItemDecoration
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior

class QuickSuggestionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    private var _binding: QuickSuggestionViewBinding? = null
    private val binding: QuickSuggestionViewBinding get() = _binding!!
    private val bottomSheetBehavior: BottomSheetBehavior<View>
    private var quickSuggestionListener: QuickSuggestionListener? = null

    interface QuickSuggestionListener {
        fun onLogFood(suggestedFoods: SuggestedFoods)
        fun onEditFood(suggestedFoods: SuggestedFoods)
    }

    init {
        _binding = QuickSuggestionViewBinding.inflate(LayoutInflater.from(context), this)
        translationZ = DesignUtils.dp2pxFloat(6f)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.peekHeight = DesignUtils.dp2px(98f)

        binding.rvQuickFoods.addItemDecoration(BottomSpaceItemDecoration(DesignUtils.dp2px(130f)))

    }

    private fun logFood(passioFoodDataInfo: SuggestedFoods) {
        quickSuggestionListener?.onLogFood(passioFoodDataInfo)
    }

    private fun editFood(passioFoodDataInfo: SuggestedFoods) {
        quickSuggestionListener?.onEditFood(passioFoodDataInfo)
    }

    fun setup(quickSuggestionListener: QuickSuggestionListener) {
        this.quickSuggestionListener = quickSuggestionListener
    }

    fun updateData(result: List<SuggestedFoods>) {

        isVisible = result.isNotEmpty()

        binding.rvQuickFoods.adapter =
            FoodSuggestionsAdapter(
                result,
                ::logFood,
                ::editFood
            )
    }


    override fun onDetachedFromWindow() {
        _binding = null
        super.onDetachedFromWindow()
    }


}