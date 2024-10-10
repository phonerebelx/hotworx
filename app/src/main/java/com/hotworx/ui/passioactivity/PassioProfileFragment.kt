package com.hotworx.ui.passioactivity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.passiomodulenew.NutritionUIModule
import com.example.passiomodulenew.NutritionUIModule.connector
import com.example.passiomodulenew.data.PassioConnector
import com.example.passiomodulenew.data.PassioConnectorImpl
import com.example.passiomodulenew.ui.model.UserProfile
//import com.example.passiomodulenew.ui.base.BaseFragment
import com.example.passiomodulenew.ui.profile.DailyNutritionTargetDialog
import com.example.passiomodulenew.ui.profile.MyProfileViewModel
import com.example.passiomodulenew.ui.settings.HeightPickerDialog
import com.example.passiomodulenew.ui.util.RoundedSlicesPieChartRenderer
import com.example.passiomodulenew.ui.util.StringKT.singleDecimal
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.hotworx.ui.fragments.BaseFragment
import com.passio.passiomodulenew.R

import com.passio.passiomodulenew.databinding.FragmentMyProfileBinding
import kotlinx.coroutines.launch

class PassioProfileFragment : BaseFragment(){

    private var _binding: FragmentMyProfileBinding? = null
    private val binding: FragmentMyProfileBinding get() = _binding!!
    private var baseFragment:com.example.passiomodulenew.ui.base.BaseFragment<MyProfileViewModel>? = null

//    private var connector: PassioConnector? = null
    fun launch(context: Context, connector: PassioConnector? = null) {
        NutritionUIModule.connector = connector ?: PassioConnectorImpl()
    }

    internal fun getConnector(): PassioConnector? = NutritionUIModule.connector

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch(requireContext(), connector)

        val connector = getConnector()
        connector?.let {
            // Fetch user profile data asynchronously
            lifecycleScope.launch {
                val userProfile = it.fetchUserProfile()
                renderChart(userProfile)
            }
        }

        with(binding)
        {
            height.setOnClickListener {
                showHeightPicker()
            }

            setupChart()
        }
    }

    private val heightPickerListener = object : HeightPickerDialog.HeightPickerListener {
        override fun onHeightPicked(height: Double) {
            baseFragment?.viewModel?.updateHeight(height)
        }

    }

    private val dailyNutritionTargetCustomizeListener =
        object : DailyNutritionTargetDialog.DailyNutritionTargetCustomizeListener {
            override fun onCustomized(dailyNutritionTarget: DailyNutritionTargetDialog.DailyNutritionTarget) {
                baseFragment?.viewModel?.changeDailyNutritionTarget(dailyNutritionTarget)
            }
        }

    private fun showDailyNutritionTargetPicker(dailyNutritionTarget: DailyNutritionTargetDialog.DailyNutritionTarget) {
        DailyNutritionTargetDialog(
            dailyNutritionTarget,
            dailyNutritionTargetCustomizeListener
        ).show(
            childFragmentManager,
            "MyProfileFragment"
        )
    }

    private fun showHeightPicker() {
        HeightPickerDialog(heightPickerListener).show(childFragmentManager, "MyProfileFragment")
    }

    private fun setupChart() {
        with(binding.macrosChart) {
            renderer = RoundedSlicesPieChartRenderer(this, animator, viewPortHandler)
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            holeRadius = 85f
            transparentCircleRadius = 85f
            description.isEnabled = false
            legend.isEnabled = false
            setDrawEntryLabels(false)
//            setDrawSliceText(false)
            setDrawMarkers(false)
            setTouchEnabled(false)
        }
    }

    private fun renderChart(userProfile: UserProfile) {

        binding.height.text = userProfile.getDisplayHeight()


        binding.bmiChart.setCurrentBMI(userProfile.calculateBMI())

        val carbColor = ContextCompat.getColor(requireContext(), R.color.passio_carbs)
        val proteinColor = ContextCompat.getColor(requireContext(), R.color.passio_protein)
        val fatColor = ContextCompat.getColor(requireContext(), R.color.passio_fat)

        val carbPercent = userProfile.carbsPer
        val proteinPercent = userProfile.proteinPer
        val fatPercent = userProfile.fatPer
        val calories = userProfile.caloriesTarget

        with(binding) {

            val carbGrams = "${userProfile.getCarbsGrams().singleDecimal()} g"
            val proteinGrams = "${userProfile.getProteinGrams().singleDecimal()} g"
            val fatGrams = "${userProfile.getFatGrams().singleDecimal()} g"

            val carbString = SpannableString(" $carbGrams ($carbPercent%)")
            carbString.setSpan(
                ForegroundColorSpan(carbColor),
                0,
                carbGrams.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            carbsValue.text = carbString

            val proteinString = SpannableString(" $proteinGrams ($proteinPercent%)")
            proteinString.setSpan(
                ForegroundColorSpan(proteinColor),
                0,
                proteinGrams.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            proteinValue.text = proteinString

            val fatString = SpannableString(" $fatGrams ($fatPercent%)")
            fatString.setSpan(
                ForegroundColorSpan(fatColor),
                0,
                fatGrams.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            fatValue.text = fatString
            caloriesValue.text = calories.toString()

            val entries = mutableListOf<PieEntry>()
            entries.add(PieEntry(carbPercent.toFloat(), "carbs"))
            entries.add(PieEntry(proteinPercent.toFloat(), "protein"))
            entries.add(PieEntry(fatPercent.toFloat(), "fat"))

            val dataSet = PieDataSet(entries, "Macros")
            val colors = listOf(carbColor, proteinColor, fatColor)
            dataSet.colors = colors
            dataSet.valueTextSize = 0f
            dataSet.sliceSpace = 8f

            val data = PieData(dataSet)
            macrosChart.data = data
            macrosChart.invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}