package com.hotworx.ui.fragments.ActivityScreenNew

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadStateAdapter
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.model.GradientColor
import com.hotworx.PagingSource.ActivityPagingSource
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentNewActivityScreenBinding
import com.hotworx.global.Constants
import com.hotworx.helpers.CustomXAxisRenderer
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.OnClickItemListener
import com.hotworx.interfaces.OnClickPendingModelInterface
import com.hotworx.models.DashboardData.TodaysPendingSession
import com.hotworx.models.NewActivityModels.GraphDataModel
import com.hotworx.models.NewActivityModels.GraphDataModelItem
import com.hotworx.models.NewActivityModels.NinetyDaysActivity
import com.hotworx.models.NewActivityModels.TimelineActivityDataModel
import com.hotworx.models.SessionBookingModel.Location
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.adapters.NewActivityAdapter.ActivityByTimelineAdapter
import com.hotworx.ui.adapters.PagingLoaderAdapter.LoaderAdapter
import com.hotworx.ui.dialog.NewActivityDialog.BarDataShowDialog
import com.hotworx.ui.dialog.SessionDialog.StartSessionDialogFragment
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.pagingAdapter.ActivityPagingAdapter
import com.hotworx.ui.views.TitleBar
import com.hotworx.viewmodel.ActivityViewModel
import com.hotworx.viewmodel.ActivityViewModelFactory
import com.hotworx.viewmodel.ReferralDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class NewActivityScreenFragment : BaseFragment(), OnClickItemListener,
    OnClickPendingModelInterface {

    lateinit var binding: FragmentNewActivityScreenBinding
    lateinit var getGraphData: GraphDataModel

    lateinit var getActivityData: TimelineActivityDataModel
    private lateinit var nintyDaysActivityAdapter: ActivityByTimelineAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private lateinit var activityAdapter: ActivityPagingAdapter
    private val viewModel: ActivityViewModel by activityViewModels {
        ActivityViewModelFactory(getWebService(), myDockActivity, true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNewActivityScreenBinding.inflate(inflater, container, false)
        callApi(Constants.SESSIONTYPEGRAPHCALLING)
        callApi(Constants.ACTIVITYBYTIMELINECALLING)
        activityAdapter = ActivityPagingAdapter(requireContext(), this)
        binding.rvTimeline.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTimeline.adapter = activityAdapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
        setOnClickListener()
        return binding.root
    }


    private fun callApi(type: String) {
        when (type) {

            Constants.SESSIONTYPEGRAPHCALLING -> getServiceHelper().enqueueCallExtended(
                getWebService().getSessionTypeGraph(
                    ApiHeaderSingleton.apiHeader(requireContext())
                ), Constants.SESSIONTYPEGRAPHCALLING, true
            )

            Constants.ACTIVITYBYTIMELINECALLING -> getServiceHelper().enqueueCallExtended(
                getWebService().getActivityByTimeline(
                    ApiHeaderSingleton.apiHeader(requireContext())
                ), Constants.ACTIVITYBYTIMELINECALLING, true
            )
        }
    }


    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)

        if (Constants.SESSIONTYPEGRAPHCALLING == tag) {
            try {
                getGraphData = GsonFactory.getConfiguredGson()
                    .fromJson<GraphDataModel>(
                        liveData.value,
                        GraphDataModel::class.java
                    )
                showBarChart(getGraphData, binding.activityBarchart)

            } catch (e: Exception) {

                Utils.customToast(requireContext(), resources.getString(R.string.error_failure))
            }
        }


        if (Constants.ACTIVITYBYTIMELINECALLING == tag) {
            try {
                getActivityData = GsonFactory.getConfiguredGson()
                    .fromJson<TimelineActivityDataModel>(
                        liveData.value,
                        TimelineActivityDataModel::class.java
                    )


                setTimelineAdapter(ArrayList(getActivityData.ninety_days_activities))
                initSearchView("NintyDaysActivity", getActivityData.ninety_days_activities)
            } catch (e: Exception) {

                Utils.customToast(requireContext(), resources.getString(R.string.error_failure))
            }
        }
    }


    override fun onFailure(message: String, tag: String) {

        when (tag) {
            Constants.BRIVODATACALLING -> {
                myDockActivity.showErrorMessage(message)
                Log.i("xxError", "Error")
            }
        }
    }


    private fun showBarChart(graph: GraphDataModel?, barChart: BarChart) {


        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()


        graph?.forEachIndexed { index, barData ->
            entries.add(BarEntry(index.toFloat(), barData.session_count.toFloat()))
            labels.add(barData.session_type)
        }
        val startColor1 = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        val endColor1 = ContextCompat.getColor(requireContext(), R.color.colorDarkRed)
        val gradientFills: ArrayList<GradientColor> = ArrayList()
        gradientFills.add(GradientColor(startColor1, endColor1))


        val dataSet = BarDataSet(entries, "Bar Data")
        dataSet.gradientColors = gradientFills
        dataSet.setDrawValues(false)


        val barData = BarData(dataSet)
        barData.barWidth = 0.7f

        barChart.xAxis.setLabelCount(labels.size, false)


        barChart.setXAxisRenderer(
            CustomXAxisRenderer(
                barChart.viewPortHandler,
                barChart.xAxis,
                barChart.getTransformer(YAxis.AxisDependency.LEFT)
            )
        )
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.setDrawAxisLine(false)

        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.axisMinimum = 0f
        barChart.axisLeft.isEnabled = true
        barChart.axisRight.setDrawGridLines(false)
        barChart.axisRight.isEnabled = false
        barChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if (value >= 0) {
                    if (value <= labels.size - 1) {
                        return labels[value.toInt()]
                    }
                    return ""
                }
                return ""
            }
        }


        barChart.xAxis.textSize = 7f

        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM


        barChart.data = barData


        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setFitBars(true)
        barChart.setDrawGridBackground(false)
        barChart.invalidate()
        barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e == null) return

                val barEntry = e as BarEntry
                val barIndex = barEntry.x.toInt() // Index of the bar clicked
                val value = barEntry.y // Value associated with the bar clicked


                val sessionType = graph?.getOrNull(barIndex)?.session_type

                val barDataGraphModel = GraphDataModelItem(
                    calories_count = graph?.get(barIndex)?.calories_count.toString(),
                    ratio = graph?.get(barIndex)?.ratio.toString(),
                    session_count = graph?.get(barIndex)?.session_count!!,
                    session_type = graph?.get(barIndex)?.session_type.toString()
                )

                val barGraphDialogFragment = BarDataShowDialog()
                barGraphDialogFragment.barDataGraphModel = barDataGraphModel
                barGraphDialogFragment.show(
                    childFragmentManager,
                    Constants.BarGraphDialogFragment
                )
                // Log or use the retrieved data as needed
            }

            override fun onNothingSelected() {

            }
        })

    }


//    private fun setTimelineAdapter(timelineAdapterArray: ArrayList<NinetyDaysActivity>) {
//        nintyDaysActivityAdapter = ActivityByTimelineAdapter(requireContext(), this)
//
//        if (timelineAdapterArray.size > 0) {
//            binding.tvDataNotFound.visibility = View.GONE
//
//            nintyDaysActivityAdapter.setList(timelineAdapterArray)
//            binding.rvTimeline.adapter = nintyDaysActivityAdapter
//
//
//        } else {
//            nintyDaysActivityAdapter.setList(ArrayList())
//            binding.rvTimeline.adapter = nintyDaysActivityAdapter
//            binding.tvDataNotFound.visibility = View.VISIBLE
//
//        }
//    }

    private fun setTimelineAdapter(timelineAdapterArray: ArrayList<NinetyDaysActivity>) {
        // Initialize the adapter
        nintyDaysActivityAdapter = ActivityByTimelineAdapter(requireContext(), this)

        // Check if the list is not empty
        if (timelineAdapterArray.isNotEmpty()) {
            // Hide the "Data Not Found" message
            binding.tvDataNotFound.visibility = View.GONE

            // Set the list in the adapter
            nintyDaysActivityAdapter.setList(timelineAdapterArray)
        } else {
            // Show the "Data Not Found" message
            binding.tvDataNotFound.visibility = View.VISIBLE

            // Pass an empty list to the adapter
            nintyDaysActivityAdapter.setList(ArrayList())
        }

        // Attach the adapter to the RecyclerView
        binding.rvTimeline.adapter = nintyDaysActivityAdapter
    }

//    private fun setOnClickListener() {
//        binding.btnNinty.setOnClickListener {
//
//            binding.etWorkOut.setText("")
//            binding.btnNinty.isChecked = true
//            binding.btnLifetime.isChecked = false
//            binding.btnNinty.setBackgroundResource(R.drawable.multicolor_background)
//            binding.btnNinty.setTextColor(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.colorWhite
//                )
//            )
//            binding.btnLifetime.setBackgroundResource(R.color.colorWhite)
//            binding.btnLifetime.setTextColor(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.colorBlack
//                )
//            )
//            if (::getActivityData.isInitialized) {
//                setTimelineAdapter(ArrayList(getActivityData.ninety_days_activities))
//                initSearchView("NintyDaysActivity", getActivityData.ninety_days_activities)
//
//            } else {
//                setTimelineAdapter(ArrayList())
//            }
//        }
//
//        binding.btnLifetime.setOnClickListener {
//
//            binding.etWorkOut.setText("")
//
//            binding.btnNinty.isChecked = false
//            binding.btnLifetime.isChecked = true
//
//
//            binding.btnLifetime.setBackgroundResource(R.drawable.multicolor_background)
//            binding.btnLifetime.setTextColor(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.colorWhite
//                )
//            )
//            binding.btnNinty.setBackgroundResource(R.color.colorWhite)
//            binding.btnNinty.setTextColor(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.colorBlack
//                )
//            )
//
//            if (binding.tvDataNotFound.isVisible) {
//                binding.tvDataNotFound.visibility = View.GONE
//            }
//
//            binding.rvTimeline.layoutManager = LinearLayoutManager(requireContext())
//            binding.rvTimeline.adapter = activityAdapter.withLoadStateHeaderAndFooter(
//                header = LoaderAdapter(),
//                footer = LoaderAdapter()
//            )
//
//            lifecycleScope.launch {
//                viewModel.list.collectLatest {
//
//                    activityAdapter.submitData(it)
//                }
//            }
//            initSearchView("AllTimeActivity", ArrayList())
//
//        }
//
//
//    }

    private fun setOnClickListener() {
        binding.btnNinty.setOnClickListener {
            Log.d("DataCheckNinty", "Ninety Days Activities: ${getActivityData.ninety_days_activities}")

            binding.etWorkOut.setText("")
            binding.btnNinty.isChecked = true
            binding.btnLifetime.isChecked = false
            binding.btnNinty.setBackgroundResource(R.drawable.multicolor_background)
            binding.btnNinty.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.colorWhite)
            )
            binding.btnLifetime.setBackgroundResource(R.color.colorWhite)
            binding.btnLifetime.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.colorBlack)
            )

            if (::getActivityData.isInitialized) {
                // Pass the data to setTimelineAdapter to update the adapter
                setTimelineAdapter(ArrayList(getActivityData.ninety_days_activities))
                initSearchView("NintyDaysActivity", getActivityData.ninety_days_activities)
            } else {
                setTimelineAdapter(ArrayList())
            }
        }

        binding.btnLifetime.setOnClickListener {

            Log.d("DataCheckLifTime", "Ninety Days Activities: ${getActivityData.ninety_days_activities}")

            binding.etWorkOut.setText("")

            binding.btnNinty.isChecked = false
            binding.btnLifetime.isChecked = true
            binding.btnLifetime.setBackgroundResource(R.drawable.multicolor_background)
            binding.btnLifetime.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.colorWhite)
            )
            binding.btnNinty.setBackgroundResource(R.color.colorWhite)
            binding.btnNinty.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.colorBlack)
            )

            if (binding.tvDataNotFound.isVisible) {
                binding.tvDataNotFound.visibility = View.GONE
            }

            // Set the paging adapter
            binding.rvTimeline.layoutManager = LinearLayoutManager(requireContext())
            binding.rvTimeline.adapter = activityAdapter.withLoadStateHeaderAndFooter(
                header = LoaderAdapter(),
                footer = LoaderAdapter()
            )

            // Reload the data for the lifetime activities
            lifecycleScope.launch {
                viewModel.list.collectLatest {
                    activityAdapter.submitData(it)
                }
            }
            initSearchView("AllTimeActivity", ArrayList())
        }
    }


    private fun initSearchView(checkActivity: String, list: List<NinetyDaysActivity>) {

        binding.etWorkOut.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {

                if (checkActivity == "AllTimeActivity") {
                    activityAdapter =
                        ActivityPagingAdapter(requireContext(), this@NewActivityScreenFragment)
                    binding.rvTimeline.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvTimeline.adapter = activityAdapter.withLoadStateHeaderAndFooter(
                        header = LoaderAdapter(),
                        footer = LoaderAdapter()
                    )
                    runnable?.let { handler.removeCallbacks(it) }

                    runnable = Runnable {
                        p0?.let {
                            val query = it.toString()
                            // Use query to set the session type in ViewModel
                            viewModel.setSessionType(query)
                        }
                    }
                    handler.postDelayed(runnable!!, 1000)
                }

                if (checkActivity == "NintyDaysActivity") {
                    if (binding.etWorkOut.text!!.isNotEmpty()) {
                        val filteredList = arrayListOf<NinetyDaysActivity>()
                        list.forEach {
                            if (it.workout_type.lowercase()
                                    .startsWith(binding.etWorkOut.text.toString().lowercase())
                            ) {
                                filteredList.add(it)
                            }
                        }

                        if (filteredList.isNotEmpty()) {
                            binding.rvTimeline.isGone = false
                            setTimelineAdapter(filteredList)

                        } else {
                            binding.rvTimeline.isGone = true
                        }
                    } else {
                        binding.rvTimeline.isGone = false
                        setTimelineAdapter(ArrayList(list))

                    }
                }


            }
        }
        )
    }

    override fun <T> onItemClick(data: T, type: String) {
        val getNinetyDaysActivity = data as NinetyDaysActivity

        val setTodaysPendingSession = TodaysPendingSession(
            session_name = getNinetyDaysActivity.workout_type,
            comeFromActivity = true,
            display_date = getNinetyDaysActivity.display_date,
            start_date_time = getNinetyDaysActivity.start_date_time,
            end_date_time = getNinetyDaysActivity.end_date_time,
            sauna = getNinetyDaysActivity.sauna_no,
            location_name = getNinetyDaysActivity.location_name
        )

        val startSessionDialogFragment = StartSessionDialogFragment(this)
        startSessionDialogFragment.comeFromTimelineFragment = true
        startSessionDialogFragment.todaysPendingSession = setTodaysPendingSession
        startSessionDialogFragment.show(
            childFragmentManager, Constants.SessionBookingDialogFragment
        )
    }


    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()

        titleBar.subHeading = resources.getString(R.string.activity)
        titleBar.contentDescription = getString(R.string.activity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        runnable?.let { handler.removeCallbacks(it) }
    }

    override fun onItemClick(value: TodaysPendingSession, type: String) {

    }
}


