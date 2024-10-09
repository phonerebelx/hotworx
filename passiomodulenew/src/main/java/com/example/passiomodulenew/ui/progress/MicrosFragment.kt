package com.example.passiomodulenew.ui.progress

import com.example.passiomodulenew.ui.model.MicroNutrient
import com.example.passiomodulenew.ui.progress.MicroNutrientAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.passiomodulenew.data.SharedPrefUtils
import com.example.passiomodulenew.ui.base.BaseFragment
import com.example.passiomodulenew.ui.util.showDatePickerDialog
import com.passio.passiomodulenew.R
import com.passio.passiomodulenew.databinding.FragmentMicrosBinding
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.Date
import java.util.Locale

class MicrosFragment : BaseFragment<MicrosViewModel>() {

    private var _binding: FragmentMicrosBinding? = null
    private val binding: FragmentMicrosBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMicrosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()

        parentFragment?.arguments?.let {
            if (it.containsKey("currentDate")) {
                val currentDate = it.getLong("currentDate", 0)
                if (currentDate > 0) {
                    viewModel.setCurrentDate(Date(currentDate))
                }
            }
        }


        binding.timeTitle.setOnClickListener {
            showDatePickerDialog(requireContext()) { selectedDate ->
                viewModel.setCurrentDate(selectedDate.toDate())
            }
        }
        binding.moveNext.setOnClickListener {
            viewModel.setNextDay()
        }
        binding.movePrevious.setOnClickListener {
            viewModel.setPreviousDay()
        }
        binding.showInfo.setOnClickListener {
            SharedPrefUtils.put("microsNoteShown", false)
            showMicrosNote()
        }
        showMicrosNote()

    }

    private fun showMicrosNote() {
        if (SharedPrefUtils.get("microsNoteShown", Boolean::class.java)) {
            binding.close.isVisible = false
            binding.microsNote.isVisible = false
            binding.showInfo.isVisible = true
        } else {
            binding.close.isVisible = true
            binding.microsNote.isVisible = true
            binding.showInfo.isVisible = false
        }
        binding.close.setOnClickListener {
            SharedPrefUtils.put("microsNoteShown", true)
            binding.close.isVisible = false
            binding.microsNote.isVisible = false
            binding.showInfo.isVisible = true
        }
    }


    private fun initObserver() {
        viewModel.currentDateEvent.observe(viewLifecycleOwner, ::updateDate)
        viewModel.logsLD.observe(viewLifecycleOwner, ::updateLogs)
        viewModel.showLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loading.isVisible = isLoading
        }
    }


    private fun updateLogs(records: ArrayList<MicroNutrient>) {
        with(binding) {
            if (rvConsumed.adapter is MicroNutrientAdapter) {
                (rvConsumed.adapter as MicroNutrientAdapter).updateData(records)
            } else {
                rvConsumed.adapter = MicroNutrientAdapter(records) {
                    viewModel.setShowMore()
                }
            }

        }
    }

    private fun updateDate(currentDate: Date) {

        val selectedDate = DateTime(currentDate.time)
        val formattedDate = if (selectedDate.toLocalDate() == DateTime.now().toLocalDate()) {
            requireContext().getString(R.string.today)
        } else {
            val dateFormat =
                DateTimeFormat.forPattern("MMMM dd, yyyy").withLocale(Locale.getDefault())
            dateFormat.print(selectedDate)
        }

        binding.timeTitle.text = formattedDate

        viewModel.fetchLogsForCurrentDay()
    }


}