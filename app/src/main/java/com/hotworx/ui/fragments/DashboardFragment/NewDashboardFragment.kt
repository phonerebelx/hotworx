package com.hotworx.ui.fragments.DashboardFragment

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.viewpager.widget.ViewPager
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.global.Constants
import com.hotworx.helpers.InternetHelper
import com.hotworx.helpers.Utils
import com.hotworx.models.DashboardData.DashboardDataModel
import com.hotworx.models.DashboardData.NinetyDaysSummary
import com.hotworx.models.DashboardData.Summary
import com.hotworx.models.DashboardData.TodaysPendingSession
import com.hotworx.retrofit.GsonFactory
import com.hotworx.room.RoomBuilder
import com.hotworx.ui.adapters.ViewPagerAdapter
import com.hotworx.ui.dialog.DashboardSession.DashboardSessionDialogFragment
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.DashboardSessionFragments.CompletedSessionFragment
import com.hotworx.ui.fragments.DashboardSessionFragments.PendingSessionFragment
import com.hotworx.ui.views.TitleBar
import me.relex.circleindicator.CircleIndicator


class NewDashboardFragment : BaseFragment() {
    lateinit var circleIndicator3: CircleIndicator
    lateinit var viewPager: ViewPager
    lateinit var getDashboardApiResponse: DashboardDataModel
    lateinit var lineChart: LineChart
    lateinit var ivCircle1: ImageView
    lateinit var ivCircle2: ImageView
    lateinit var ivCircle3: ImageView
    lateinit var ivCircle4: ImageView
    lateinit var ivCircle5: ImageView
    lateinit var ivCircle6: ImageView
    lateinit var ivCircle7: ImageView
    lateinit var pointSessions: ArrayList<ImageView>
    lateinit var tvCalorie: TextView
    lateinit var tvSession: TextView
    lateinit var tvDay: TextView
    lateinit var tvStreak: TextView
    lateinit var tvLevelValue: TextView
    private lateinit var mRequest: OneTimeWorkRequest
    private lateinit var mWorkManager:WorkManager
    private lateinit var data: Data
    private lateinit var constraints: Constraints
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_new_dashboard, container, false)

        circleIndicator3 = root.findViewById<CircleIndicator>(R.id.circleIndicator3)
        viewPager = root.findViewById<ViewPager>(R.id.viewPager)
        lineChart = root.findViewById(R.id.lineChart)
        tvCalorie = root.findViewById(R.id.tvCalorie)
        tvSession = root.findViewById(R.id.tvSession)
        tvDay = root.findViewById(R.id.tvDay)
        tvLevelValue = root.findViewById(R.id.tvLevelValue)
        tvStreak = root.findViewById(R.id.tvStreak)
        ivCircle1 = root.findViewById(R.id.ivCircle1)
        ivCircle2 = root.findViewById(R.id.ivCircle2)
        ivCircle3 = root.findViewById(R.id.ivCircle3)
        ivCircle4 = root.findViewById(R.id.ivCircle4)
        ivCircle5 = root.findViewById(R.id.ivCircle5)
        ivCircle6 = root.findViewById(R.id.ivCircle6)
        ivCircle7 = root.findViewById(R.id.ivCircle7)

        //Save Array View
        pointSessions = arrayListOf()
        pointSessions.add(ivCircle1)
        pointSessions.add(ivCircle2)
        pointSessions.add(ivCircle3)
        pointSessions.add(ivCircle4)
        pointSessions.add(ivCircle5)
        pointSessions.add(ivCircle6)
        pointSessions.add(ivCircle7)
        resumeWorkoutSession()
        callApi(Constants.DASHBOARDCALLING)
        // Create an ArrayList of Entry (data points)
        // Creating data entries

        return root
    }


    private fun callApi(type: String) {
        when (type) {
            Constants.DASHBOARDCALLING -> {
                getServiceHelper().enqueueCallExtended(
                    getWebService().getDashboard(
                        ApiHeaderSingleton.apiHeader(requireContext())
                    ), Constants.DASHBOARDCALLING, true
                )
            }
        }
    }


    private fun resumeWorkoutSession() {
        if (prefHelper.activeSession != null) {
            val sessionDashboardDialogFragment = DashboardSessionDialogFragment()
            sessionDashboardDialogFragment.show(
                childFragmentManager,
                Constants.SessionBookingDialogFragment
            )
            //            WorkoutTimeFragment workoutTimeFragment = new WorkoutTimeFragment();
//            Bundle bundle = new Bundle();
//            bundle.putBoolean("resumed_session", true);
//            workoutTimeFragment.setArguments(bundle);
//            myDockActivity.replaceDockableFragment(workoutTimeFragment, Constants.WorkoutTimeFragment);
        } else {
//            autoSync()
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.DASHBOARDCALLING -> {
                try {
                    getDashboardApiResponse = GsonFactory.getConfiguredGson()?.fromJson(liveData.value, DashboardDataModel::class.java)!!
                    Log.d("getDashboardApiResponse",getDashboardApiResponse.data.toString())
                    setDashboardCardViewData(getDashboardApiResponse.data.summary)
                    setUpViewPager(
                        getDashboardApiResponse.data.todays_pending_sessions,
                        getDashboardApiResponse.data.todays_completed_sessions
                    )
                    setUpGraph(getDashboardApiResponse.data.ninety_days_summary)

                } catch (e: Exception) {
                    Utils.customToast(requireContext(), resources.getString(R.string.error_failure))
                }
            }
        }
    }

    override fun onFailure(message: String, tag: String) {
        when (tag) {
            Constants.DASHBOARDCALLING -> {
                AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage(message) // A null listener allows the button to dismiss the dialog and take no further action.
                    .setPositiveButton(android.R.string.yes, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }
        }
    }

















    ////////////////////////////////Previous home fragment setup/////////////////////////////////

    ////////////////////////////////Previous home fragment setup/////////////////////////////////
    private fun setDashboardCardViewData(getDashboardData: Summary){

        tvCalorie.text = getDashboardData.total_cal_burned.plus(" cal")
        tvCalorie.setTextColor( resources.getColor(R.color.colorWhite))
        tvSession.text = getDashboardData.total_sessions
        tvSession.setTextColor( resources.getColor(R.color.colorWhite))
        tvDay.text = "Day ".plus(getDashboardData.day_for_current_sprint.toString())
        tvDay.setTextColor( resources.getColor(R.color.colorWhite))
        tvStreak.text = getDashboardData.continious_streak.plus(" Day")
        tvStreak.setTextColor( resources.getColor(R.color.colorWhite))
        tvLevelValue.text = "level ".plus(getDashboardData.reward_level)
        tvLevelValue.setTextColor( resources.getColor(R.color.colorWhite))
        if ((getDashboardData.reward_level.toInt()-1)>0) pointSessions[getDashboardData.reward_level.toInt()-1].setImageDrawable(resources.getDrawable(R.drawable.db_card_circle_accent, null))

    }

    private fun setUpGraph(getSummaryDataForGraph: ArrayList<NinetyDaysSummary>) {

        var data = ArrayList<String>()
        val entries = ArrayList<Entry>()
        getSummaryDataForGraph.forEachIndexed { index, entry ->
            data.add(entry.key)
            entries.add(Entry(index.toFloat(),entry.value.toFloat()))
        }
        val labels: Array<String> = data.toTypedArray()


        val dataSet = LineDataSet(entries, null)


        dataSet.color = resources.getColor(R.color.colorAccent)

        dataSet.setDrawValues(false)
        dataSet.lineWidth = 2f
        dataSet.setDrawCircles(true)
        dataSet.circleRadius = 4f // Set circle radius
        dataSet.setCircleColor(resources.getColor(R.color.colorAccent))
        dataSet.setCircleHoleColor(resources.getColor(R.color.colorAccent))

        // Creating LineData from LineDataSet
        val lineData = LineData(dataSet)

        // Setting data to the chart
        lineChart.data = lineData

        // Customizing chart appearance
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.isEnabled = false // Disable description

        // Customizing X-axis
        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = LabelValueFormatter(labels)
        xAxis.textColor = Color.BLACK
        xAxis.setDrawGridLines(false)

        // Customizing Y-axis
        val yAxisLeft: YAxis = lineChart.axisLeft
        yAxisLeft.textColor = Color.BLACK
        yAxisLeft.setDrawGridLines(true)

        val yAxisRight: YAxis = lineChart.axisRight
        yAxisRight.isEnabled = false // Disable right Y-axis

        // Customizing chart legend
        val legend: Legend = lineChart.legend
        legend.textColor = Color.BLACK

        // Refreshes the chart
        lineChart.invalidate()
    }

    private fun setUpViewPager(
        getTodaysPendingSession: ArrayList<TodaysPendingSession>,
        getTodaysCompletedSession: ArrayList<TodaysPendingSession>
    ) {
        if (!isAdded) return

        viewPager.adapter = ViewPagerAdapter(childFragmentManager)
        addTabsMain(viewPager, getTodaysPendingSession, getTodaysCompletedSession)
        circleIndicator3.setViewPager(viewPager)


        viewPager.offscreenPageLimit = 1
    }

    private fun addTabsMain(
        viewPager: ViewPager,
        getTodaysPendingSession: ArrayList<TodaysPendingSession>,
        getTodaysCompletedSession: ArrayList<TodaysPendingSession>
    ) {

//        val adapter = viewPager.adapter as? ViewPagerAdapter ?: ViewPagerAdapter(childFragmentManager)
//
//        val psf = PendingSessionFragment.newInstance(getTodaysPendingSession)
//        //psf.getTodaysPendingSession = getTodaysPendingSession
//        adapter.addFrag(psf, "PSF's")
//
//        val csf = CompletedSessionFragment()
//        csf.getTodaysCompletedSession = getTodaysCompletedSession
//        adapter.addFrag(csf, "CSF's")
//
//        viewPager.adapter = adapter
    }


    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.Dashboard)
    }
}


class LabelValueFormatter(private val labels: Array<String>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val index = value.toInt()
        return if (index >= 0 && index < labels.size) {
            labels[index]
        } else {
            ""
        }
    }
}