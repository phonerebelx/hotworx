package com.hotworx.ui.fragments.ProfileAndGoal

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.LiveData
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.helpers.Utils
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.UserData.GetUserGoalData
import com.hotworx.models.UserData.ResponseUserGoalModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.HomeFragment
import com.hotworx.ui.views.TitleBar
import java.util.Calendar

class GoalFragment : BaseFragment() {
    private lateinit var etCurrentWeight: EditText
    private lateinit var etTargetWeight: EditText
    private lateinit var etWeeklySession: EditText
    private lateinit var tVTargetWeight: TextView
    private lateinit var btnSaveForm: AppCompatButton
    private lateinit var btnSkip: AppCompatButton

    private lateinit var calendar: Calendar

    var date2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val formattedDay = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
        val formattedMonth = if (monthOfYear + 1 < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
        val date = "$year-$formattedMonth-$formattedDay"
        tVTargetWeight.setText(date)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_goal, container, false)
        etCurrentWeight = root.findViewById(R.id.etCurrentWeight)
        etTargetWeight = root.findViewById(R.id.etTargetWeight)
        etWeeklySession = root.findViewById(R.id.etWeeklySession)
        tVTargetWeight = root.findViewById(R.id.tVTargetWeight)
        btnSaveForm = root.findViewById(R.id.btnSaveForm)
        btnSkip = root.findViewById(R.id.btnSkip)

        initView()
        setOnClickListener()
        callGoalDetailsApi()

        return root
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)
        titleBar.showBackButton()
        titleBar.subHeading = getString(R.string.fitness_goal)
    }

    private fun initView() {
        calendar = Calendar.getInstance()
    }

    private fun setOnClickListener() {
        tVTargetWeight.setOnClickListener {
            DatePickerDialog(
                myDockActivity, date2,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
            if (date2.toString().isNotEmpty()) {
                tVTargetWeight.error = null
            }
        }

        btnSaveForm.setOnClickListener {
            callUpdateGoalApi()
        }

        btnSkip.setOnClickListener{
            myDockActivity?.emptyBackStack()
            myDockActivity?.replaceDockableFragment(HomeFragment())
        }
    }

    private fun setGoalData(result: GetUserGoalData) {

        val allNull = with(result.data) {
            current_weight == null &&
                    target_weight == null &&
                    target_weight_goal_date == null &&
                    weekly_session_goal == null
        }
        if (allNull) {
            etCurrentWeight.setText(result.data.current_weight?.toString())
            etTargetWeight.setText(result.data.target_weight?.toString())
            etWeeklySession.setText(result.data.weekly_session_goal?.toString())
            tVTargetWeight.text = result.data.target_weight_goal_date?.toString()
        } else {
            etCurrentWeight.setText(result.data.current_weight.toString())
            etTargetWeight.setText(result.data.target_weight.toString())
            etWeeklySession.setText(result.data.weekly_session_goal.toString())
            tVTargetWeight.text = result.data.target_weight_goal_date.toString()
        }


    }
    private fun callGoalDetailsApi() {
        getServiceHelper().enqueueCallExtended(
            getWebService().viewGoals(
                ApiHeaderSingleton.apiHeader(requireContext())
            ), "Goal Entry Api Calling", true
        )
    }

    private fun callUpdateGoalApi() {
        getServiceHelper().enqueueCallExtended(
            webService.updateGoals(
                ApiHeaderSingleton.apiHeader(requireContext()),
                etCurrentWeight.text.toString(),
                etTargetWeight.text.toString(),
                tVTargetWeight.text.toString(),
                etWeeklySession.text.toString()

            ), "Set Goal Api Calling", true
        )
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            "Goal Entry Api Calling" -> {
                try {
                    val response = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, GetUserGoalData::class.java)!!
                    setGoalData(response)
                } catch (e: Exception) {
                    GsonFactory.getConfiguredGson()?.fromJson(liveData.value, ErrorResponseEnt::class.java)?.let { errorResponseEnt ->
                        dockActivity?.showErrorMessage(errorResponseEnt.error)
                    }
                }
            }

            "Set Goal Api Calling" -> {
                try {
                    val response = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ResponseUserGoalModel::class.java)!!
                    if (response.msg == "success") {
                        Utils.customToast(context, getString(R.string.fitness_goal_message))
                    }
                } catch (e: Exception) {
                    GsonFactory.getConfiguredGson()?.fromJson(liveData.value, ErrorResponseEnt::class.java)?.let { errorResponseEnt ->
                        dockActivity?.showErrorMessage(errorResponseEnt.error)
                    }
                }
            }
        }
    }

    override fun onFailure(message: String, tag: String) {
        when (tag) {
            "Goal Entry Api Calling" -> {
                myDockActivity?.showErrorMessage(message)
                Log.i("xxError", "Error")
            }

            "Set Goal Api Calling" -> {
                myDockActivity?.showErrorMessage(message)
                Log.i("xxError", "Error")
            }
        }
    }
}