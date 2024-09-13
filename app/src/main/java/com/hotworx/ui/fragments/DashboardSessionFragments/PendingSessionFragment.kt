package com.hotworx.ui.fragments.DashboardSessionFragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton.apiHeader
import com.hotworx.activities.DockActivity
import com.hotworx.global.Constants
import com.hotworx.global.Constants.WORKOUT_AFTER_BURN_DURATION
import com.hotworx.helpers.InternetHelper
import com.hotworx.helpers.UIHelper
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.InternetDialogBoxInterface
import com.hotworx.interfaces.OnClickPendingModelInterface
import com.hotworx.models.DashboardData.TodaysPendingSession
import com.hotworx.models.DeleteSession.DeleteSessionResponseModel
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.retrofit.GsonFactory
import com.hotworx.room.RoomBuilder
import com.hotworx.room.model.WorkOutTypeEnt
import com.hotworx.ui.adapters.DashboardAdapter.DashboardPendingSessionAdapter
import com.hotworx.ui.dialog.SessionDialog.StartSessionDialogFragment
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.BookSession.LocationSelectionFragment
import com.hotworx.ui.fragments.HomeFragment
import com.hotworx.ui.fragments.SessionFlow.SaveCaloriesFragment
import com.hotworx.ui.fragments.fitbit.RootActivity


class PendingSessionFragment : BaseFragment(), OnClickPendingModelInterface {
    private lateinit var rvPendingSessions: RecyclerView
    private lateinit var tvNoRecordFound: TextView
    private lateinit var pendingSessionAdapter: DashboardPendingSessionAdapter
    private lateinit var btnBookSession: AppCompatButton
    private lateinit var startSessionDialogFragment: StartSessionDialogFragment
    var set_is_reciprocal_allowed: String = "no"
    //var getTodaysPendingSession: ArrayList<TodaysPendingSession>? = null
    private var sessionTypesList: ArrayList<TodaysPendingSession>? = null
    private var selectedSession: TodaysPendingSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_pending_session, container, false)
        rvPendingSessions = root.findViewById(R.id.rvPendingSessions)
        tvNoRecordFound = root.findViewById(R.id.tvNoRecordFound)
        btnBookSession = root.findViewById(R.id.btnBookSession)
        setPendingSessionAdapter()
        setOnCLickListener()
        return root
    }

    private fun setPendingSessionAdapter() {
        val dataSource = sessionTypesList ?: ArrayList()
        tvNoRecordFound.isVisible = dataSource.isEmpty()
        pendingSessionAdapter = DashboardPendingSessionAdapter(requireContext(), this,activity as? DockActivity)
        pendingSessionAdapter.setList(dataSource)
        rvPendingSessions.adapter = pendingSessionAdapter
        //ApplicationManager.getInstance(myDockActivity).listSize = dataSource.size
    }

    private fun setOnCLickListener() {
        btnBookSession.setOnClickListener {
            dockActivity.replaceDockableFragment(LocationSelectionFragment(set_is_reciprocal_allowed))
        }
    }

    private fun saveSessionTypesIntoRoom(workOutTypesResp: ArrayList<TodaysPendingSession>) {
        val listType = object : TypeToken<List<TodaysPendingSession?>?>() {}.type
        val jsonResponse = Gson().toJson(workOutTypesResp, listType)
        if (RoomBuilder.getHotWorxDatabase(myDockActivity).workoutTypeDao.workoutType == null) {
            RoomBuilder.getHotWorxDatabase(myDockActivity).workoutTypeDao.insert(
                WorkOutTypeEnt(WORKOUT_AFTER_BURN_DURATION, jsonResponse)
            )
        } else {
            RoomBuilder.getHotWorxDatabase(myDockActivity).workoutTypeDao.update(
                WorkOutTypeEnt(WORKOUT_AFTER_BURN_DURATION, jsonResponse)
            )
        }
    }

    private fun onBeginSession() {
        if (selectedSession == null) {
            Utils.customToast(myDockActivity, resources.getString(R.string.selectduration))
            return
        }
        showDeviceComfirmationDialog()
    }

    private fun showDeviceComfirmationDialog() {
        val dialog = Dialog(myDockActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_fitness)
        val tvSmartWatch = dialog.findViewById<View>(R.id.tvSmartWatch) as Button
        val tvdonthave = dialog.findViewById<View>(R.id.tvdonthave) as Button
        val tvCancel = dialog.findViewById<View>(R.id.tvCancel) as Button
        tvSmartWatch.setOnClickListener {
            dialog.dismiss()
            if (InternetHelper.CheckInternetConectivity(myDockActivity)) {
                startSmartWatchSession()
            } else {
                showNoInternetDialog()
            }
        }
        tvdonthave.setOnClickListener {
            dialog.dismiss()
            startOtherWatchSession()
        }
        tvCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun startSmartWatchSession() {
        val intent = Intent(myDockActivity, RootActivity::class.java)
        intent.putExtra("activeSession", selectedSession)
        this@PendingSessionFragment.startActivityForResult(intent, 1000)

        //startOtherWatchSessionWithInitialFitbit()
    }

    private fun startOtherWatchSession() {
        val saveCaloriesFragment = SaveCaloriesFragment()
        val bundle = Bundle()

        bundle.putSerializable("activeSession", selectedSession)
        saveCaloriesFragment.arguments = bundle
        myDockActivity.replaceDockableFragment(saveCaloriesFragment, Constants.SaveCaloriesFragment)
    }

    private fun startOtherWatchSessionWithInitialFitbit() {
        val saveCaloriesFragment = SaveCaloriesFragment()

        val bundle = Bundle()
        bundle.putSerializable("activeSession", selectedSession)
        bundle.putSerializable("isFitbitWatchSelected", true)
        saveCaloriesFragment.arguments = bundle
        myDockActivity.replaceDockableFragment(saveCaloriesFragment, Constants.SaveCaloriesFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                startOtherWatchSessionWithInitialFitbit()
            }
        }
    }

    private fun setSessionDialog(getSelectPendingSession: TodaysPendingSession) {
        val startSessionDialogFragment = StartSessionDialogFragment(this)
        startSessionDialogFragment.todaysPendingSession = getSelectPendingSession
        startSessionDialogFragment.show(
            childFragmentManager, Constants.SessionBookingDialogFragment
        )
    }

    private fun callApi(type: String,value: TodaysPendingSession){
        when (type){
            "COME_FROM_DELETE_IMAGE_VIEW" -> {
                getServiceHelper().enqueueCallExtended(
                    getWebService().deleteSession(
                        apiHeader(requireContext()),
                        value.session_record_id,
                        value.lead_record_id
                    ), "COME_FROM_DELETE_IMAGE_VIEW", true
                )
            }
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            "COME_FROM_DELETE_IMAGE_VIEW" -> {
                try {
                    val deleteSessionResponseModel = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, DeleteSessionResponseModel::class.java)!!
                    myDockActivity.replaceDockableFragment(
                        HomeFragment(),
                        Constants.HomeFragment
                    )
                    Utils.customToast(requireContext(),deleteSessionResponseModel.success)
                } catch (e: Exception) {
                    GsonFactory.getConfiguredGson()?.fromJson(liveData.value, ErrorResponseEnt::class.java)?.let { errorResponseEnt ->
                        dockActivity?.showErrorMessage(errorResponseEnt.error)
                    }
                }
            }

        }
    }

    private fun showNoInternetDialog() {
        UIHelper.showNoInternetDialog(
            myDockActivity,
            getString(R.string.network_internet_connection_error),
            getString(R.string.do_you_want_to_record_calories_manually),
            getString(R.string.cancel),
            getString(R.string.continue_),
            object : InternetDialogBoxInterface {
                override fun onPositive() {
                    startOtherWatchSession()
                }

                override fun onNegative() {}
            })
    }

    override fun onItemClick(value: TodaysPendingSession, type: String) {
        when (type) {
            "COME_FROM_IMAGE_VIEW" -> {
                if (value != null && prefHelper.activeSession == null) {

                    selectedSession = value
                    onBeginSession()
                } else {
                    Utils.customToast(requireContext(), "Session Already Active")
                }
            }
            "COME_FROM_DELETE_IMAGE_VIEW" -> {
                UIHelper.showAlertDialog(
                    resources.getString(R.string.cancel_session), "Confirmation", myDockActivity,
                    { dialogInterface, i ->
                        callApi("COME_FROM_DELETE_IMAGE_VIEW",value)
                    }
                ) { dialogInterface, i ->
                    dialogInterface.cancel()

                }
            }

            "COME_FROM_TAB_VIEW" -> {
                if (value != null && prefHelper.activeSession == null) {
                    setSessionDialog(value)
                } else {
                    Utils.customToast(requireContext(), "Session Already Active")
                }
            }
        }
    }

    fun setData(sessions: ArrayList<TodaysPendingSession>) {
        sessionTypesList = sessions
    }

}