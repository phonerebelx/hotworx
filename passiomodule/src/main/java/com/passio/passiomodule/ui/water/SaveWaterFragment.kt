package com.passio.passiomodule.ui.water

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.passio.passiomodule.ui.base.BaseToolbar
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.passio.passiomodule.R
import com.passio.passiomodule.data.ResultWrapper
import com.passio.passiomodule.databinding.FragmentSaveWeightBinding
import com.passio.passiomodule.ui.activity.UserCache
import com.passio.passiomodule.ui.base.BaseFragment
import com.passio.passiomodule.ui.model.WaterRecord
import com.passio.passiomodule.ui.util.StringKT.singleDecimal
import com.passio.passiomodule.ui.util.showDatePickerDialog
import com.passio.passiomodule.ui.util.showTimePickerDialog
import com.passio.passiomodule.ui.util.toast

class SaveWaterFragment : BaseFragment<WaterTrackingViewModel>() {

    private var _binding: FragmentSaveWeightBinding? = null
    private val binding: FragmentSaveWeightBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveWeightBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding)
        {
            toolbar.setup(getString(R.string.water_tracking), baseToolbarListener)
            toolbar.hideRightIcon()
            lblWeight.text = getString(R.string.water_consumed)
            weight.hint = getString(R.string.water_consumed)
            weight.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    viewModel.updateWeight(p0.toString())
                }

            })

            dayValue.setOnClickListener {
                showDatePickerDialog(requireContext()) { selectedDate ->
                    viewModel.setDay(selectedDate)
                }
            }

            timeValue.setOnClickListener {
                showTimePickerDialog(requireContext()) { selectedTime ->
                    viewModel.setTime(selectedTime)
                }
            }

            save.setOnClickListener {
                viewModel.updateWeightRecord()
            }
            cancel.setOnClickListener {
                viewModel.navigateBack()
            }
        }

        initObserver()
        viewModel.initRecord(null)

    }

    private fun initObserver() {
        viewModel.weightRecordCurrentEvent.observe(viewLifecycleOwner, ::updateRecord)
        viewModel.saveRecord.observe(viewLifecycleOwner, ::recordSaved)
        sharedViewModel.addWaterLD.observe(viewLifecycleOwner) {
            viewModel.initRecord(it)
        }
    }

    private fun recordSaved(resultWrapper: ResultWrapper<Boolean>) {
        when (resultWrapper) {
            is ResultWrapper.Success -> {
                if (resultWrapper.value) {
                    requireContext().toast("Water record saved!")
                    viewModel.navigateBack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Could not record water. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            is ResultWrapper.Error -> {
                Toast.makeText(
                    requireContext(),
                    resultWrapper.error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun updateRecord(weightRecord: WaterRecord) {
        with(binding)
        {
            val currentValue = weightRecord.getWaterInCurrentUnit()
            if (currentValue <= 0.0) {
                weight.text?.clear()
            } else {
                weight.setText(currentValue.singleDecimal())
            }
            weightUnit.text = UserCache.getProfile().measurementUnit.waterUnit.value
            dayValue.text = weightRecord.getDisplayDay()
            timeValue.text = weightRecord.getDisplayTime()
        }
    }

    private val baseToolbarListener = object : BaseToolbar.ToolbarListener {
        override fun onBack() {
            viewModel.navigateBack()
        }

        override fun onRightIconClicked() {

        }

    }

}