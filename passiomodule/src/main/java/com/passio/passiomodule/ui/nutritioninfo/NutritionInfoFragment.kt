package com.passio.passiomodule.ui.nutritioninfo

import com.passio.passiomodule.R
import com.passio.passiomodule.data.SharedPrefUtils
import com.passio.passiomodule.databinding.FragmentNutritionInfoBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.passio.passiomodule.ui.base.BaseFragment
import com.passio.passiomodule.ui.base.BaseToolbar
import com.passio.passiomodule.ui.model.FoodRecord
import com.passio.passiomodule.ui.model.MicroNutrient
import com.passio.passiomodule.ui.util.DesignUtils
import com.passio.passiomodule.ui.util.StringKT.capitalized
import com.passio.passiomodule.ui.util.loadPassioIcon
import com.passio.passiomodule.ui.view.BottomSpaceItemDecoration
import androidx.core.view.isVisible

class NutritionInfoFragment : BaseFragment<NutritionInfoViewModel>() {

    private var _binding: FragmentNutritionInfoBinding? = null
    private val binding: FragmentNutritionInfoBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNutritionInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()

        with(binding) {
            toolbar.setup(getString(R.string.nutrition_information), baseToolbarListener)
            toolbar.hideRightIcon()
            showInfo.setOnClickListener {
                SharedPrefUtils.put("nutritionInfoNoteShown", false)
                showMicrosNote()
            }
            rvConsumed.addItemDecoration(BottomSpaceItemDecoration(DesignUtils.dp2px(20f)))
        }
        showMicrosNote()

    }

    private val baseToolbarListener = object : BaseToolbar.ToolbarListener {
        override fun onBack() {
            viewModel.navigateBack()
        }

        override fun onRightIconClicked() {

        }

    }

    private fun showMicrosNote() {
        if (SharedPrefUtils.get("nutritionInfoNoteShown", Boolean::class.java)) {
            binding.close.isVisible = false
            binding.microsNote.isVisible = false
            binding.showInfo.isVisible = true
        } else {
            binding.close.isVisible = true
            binding.microsNote.isVisible = true
            binding.showInfo.isVisible = false
        }
        binding.close.setOnClickListener {
            SharedPrefUtils.put("nutritionInfoNoteShown", true)
            binding.close.isVisible = false
            binding.microsNote.isVisible = false
            binding.showInfo.isVisible = true
        }
    }

    private fun initObserver() {
        sharedViewModel.nutritionInfoFoodRecordLD.observe(viewLifecycleOwner) { foodRecord ->
            viewModel.setFoodRecord(foodRecord)
        }
        viewModel.logsLD.observe(viewLifecycleOwner, ::updateLogs)
        viewModel.foodRecord.observe(viewLifecycleOwner, ::updateFoodDetails)
    }

    private fun updateFoodDetails(foodRecord: FoodRecord) {
        with(binding)
        {
            image.loadPassioIcon(foodRecord.iconId)
            name.text = foodRecord.name.capitalized()
            upcInfo.text = foodRecord.additionalData
        }
    }

    private fun updateLogs(records: List<MicroNutrient>) {
        with(binding) {
            rvConsumed.adapter = NutritionInfoAdapter(records)
        }
    }

}