package com.example.passiomodulenew.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.passiomodulenew.data.ResultWrapper
import com.example.passiomodulenew.ui.base.BaseFragment
import com.example.passiomodulenew.ui.base.BaseToolbar
import com.example.passiomodulenew.ui.model.UserProfile
import com.example.passiomodulenew.ui.profile.GenericSpinnerAdapter
import com.example.passiomodulenew.ui.profile.LengthUnit
import com.example.passiomodulenew.ui.profile.WeightUnit
import com.example.passiomodulenew.ui.util.ViewEXT.setOnChangeListener
import com.example.passiomodulenew.ui.util.toast
import com.passio.passiomodulenew.R
import com.passio.passiomodulenew.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment<SettingsViewModel>() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()

        with(binding)
        {
            toolbar.setup(getString(R.string.settings), baseToolbarListener)
            toolbar.hideRightIcon()
            lunch.setOnChangeListener { checkbox, isChecked ->
                if (checkbox.isPressed) {
                    viewModel.updateLunchReminder(isChecked)
                }
            }
            dinner.setOnChangeListener { checkbox, isChecked ->
                if (checkbox.isPressed) {
                    viewModel.updateDinnerReminder(isChecked)
                }
            }
            breakfast.setOnChangeListener { checkbox, isChecked ->
                if (checkbox.isPressed) {
                    viewModel.updateBreakfastReminder(isChecked)
                }
            }
        }

    }

    private fun initObserver() {
        viewModel.userProfileEvent.observe(viewLifecycleOwner, ::setSettingInfo)
        viewModel.updateProfileResult.observe(viewLifecycleOwner, ::settingSaved)
    }

    private fun setSettingInfo(userProfile: UserProfile) {
        with(binding) {
            val measurementUnit = userProfile.measurementUnit

            setupLengthView(measurementUnit.lengthUnit)
            setupWeightView(measurementUnit.weightUnit)

            breakfast.isChecked = userProfile.userReminder.isBreakfastOn
            lunch.isChecked = userProfile.userReminder.isLunchOn
            dinner.isChecked = userProfile.userReminder.isDinnerOn
        }
    }

    private fun settingSaved(resultWrapper: ResultWrapper<Boolean>) {
        when (resultWrapper) {
            is ResultWrapper.Success -> {
                if (resultWrapper.value) {
                    requireContext().toast("User settings saved!")
//                    viewModel.navigateBack()
                } else {
                    requireContext().toast("Could not saved settings. Please try again.")
                }
            }

            is ResultWrapper.Error -> {
                requireContext().toast(resultWrapper.error)
            }
        }
    }

    private val baseToolbarListener = object : BaseToolbar.ToolbarListener {
        override fun onBack() {
            viewModel.navigateBack()
        }

        override fun onRightIconClicked() {
        }

    }

    private fun setupLengthView(selectedLengthUnit: LengthUnit) {
        with(binding)
        {
            val items = listOf(LengthUnit.Imperial, LengthUnit.Metric)
            if (length.adapter == null || length.onItemSelectedListener == null) {
                val adapter = GenericSpinnerAdapter(
                    context = requireContext(),
                    items = items
                ) { item ->
                    item.value
                }
                length.adapter = adapter
                length.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        val selectedItem =
                            parent.getItemAtPosition(position) as LengthUnit
                        viewModel.updateLengthUnit(selectedItem)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
            }
            length.setSelection(items.indexOf(selectedLengthUnit))
        }
    }

    private fun setupWeightView(selectedWeightUnit: WeightUnit) {
        with(binding)
        {
            val items = listOf(WeightUnit.Imperial, WeightUnit.Metric)
            if (weight.adapter == null || weight.onItemSelectedListener == null) {
                val adapter = GenericSpinnerAdapter(
                    context = requireContext(),
                    items = items
                ) { item ->
                    item.value
                }
                weight.adapter = adapter
                weight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        val selectedItem =
                            parent.getItemAtPosition(position) as WeightUnit
                        viewModel.updateWeightUnit(selectedItem)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
            }
            weight.setSelection(items.indexOf(selectedWeightUnit))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}