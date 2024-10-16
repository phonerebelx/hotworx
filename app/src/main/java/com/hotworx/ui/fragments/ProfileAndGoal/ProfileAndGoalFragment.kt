package com.hotworx.ui.fragments.ProfileAndGoal

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.helpers.Utils
import com.hotworx.micsAdapter.SpinnerAdapter
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.UserData.*
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.HomeFragment
import com.hotworx.ui.views.TitleBar
import java.io.File
import java.util.*


class ProfileAndGoalFragment : BaseFragment() {
    private val ARG_FROM_SIDEMENU = "isFromSideMenu"

    // For User Personal Detail
    var gender = arrayListOf<String>("Select Gender", "male", "female")
    private lateinit var getUserDataForUpdate: DataX
    private lateinit var profileImage: ImageView

//    @BindView(R.id.per_spinner_gender)
    private lateinit var per_spinner_gender: Spinner
    private lateinit var backButton: AppCompatButton
    private lateinit var headingTextView: TextView
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var selectDate: TextView
    private lateinit var btnPersonalDetail: AppCompatButton
    private lateinit var selectedGenderValue: String
    private lateinit var calendar: Calendar
    private lateinit var setValueForProfile: SetUserData
    private lateinit var setValueForGoal: SetUserGoalData
    private lateinit var sVPersonalDetail: ScrollView
    private lateinit var clGoalEntry: ConstraintLayout
    private lateinit var pickProfileImage: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var editProfileImage: LinearLayoutCompat
    private lateinit var skipLayout: LinearLayoutCompat
    private lateinit var profileImageForModel: File

    // For User Goals
    private lateinit var etCurrentWeight: EditText
    private lateinit var etTargetWeight: EditText
    private lateinit var etWeeklySession: EditText
    private lateinit var tVTargetWeight: TextView
    private lateinit var btnSaveForm: AppCompatButton
    private lateinit var btnSkip: AppCompatButton

    private var isFromSideMenu: Boolean = false




    var date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val formattedDay = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
        val formattedMonth = if (monthOfYear + 1 < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
        val date = "$year-$formattedMonth-$formattedDay"
        selectDate.setText(date)
    }

    var date2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val formattedDay = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
        val formattedMonth = if (monthOfYear + 1 < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
        val date = "$year-$formattedMonth-$formattedDay"
        tVTargetWeight.setText(date)
    }

    companion object {
        @JvmStatic
        fun newInstance(isFromSideMenu: Boolean) = ProfileAndGoalFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_FROM_SIDEMENU, isFromSideMenu)
            }
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            isFromSideMenu = it.getBoolean(ARG_FROM_SIDEMENU, false)
        }

        pickProfileImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val setProfileImage = myDockActivity?.pickPhotoOnly(
                    requireContext(),
                    uri
                )
                Glide.with(requireContext()).load(setProfileImage).into(profileImage)
                if (setProfileImage != null) {
                    profileImageForModel = setProfileImage
                }
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_profile_and_goal, container, false)
        headingTextView = root.findViewById(R.id.tv_heading)
        backButton = root.findViewById(R.id.btn_back)
        profileImage = root.findViewById(R.id.profileImage)
        editProfileImage = root.findViewById(R.id.editProfileImage)
        clGoalEntry = root.findViewById(R.id.clGoalEntry)
        sVPersonalDetail = root.findViewById(R.id.sVPersonalDetail)
        etFirstName = root.findViewById(R.id.etFirstName)
        etLastName = root.findViewById(R.id.etLastName)
        etEmail = root.findViewById(R.id.etEmail)
        etHeight = root.findViewById(R.id.etHeight)
        etWeight = root.findViewById(R.id.etWeight)
        selectDate = root.findViewById(R.id.tvDob)
        btnPersonalDetail = root.findViewById(R.id.btnPersonalDetail)
        per_spinner_gender = root.findViewById(R.id.per_spinner_gender)
        skipLayout = root.findViewById(R.id.linearLayoutCompat2)

        //For User Goals
        etCurrentWeight = root.findViewById(R.id.etCurrentWeight)
        etTargetWeight = root.findViewById(R.id.etTargetWeight)
        etWeeklySession = root.findViewById(R.id.etWeeklySession)
        tVTargetWeight = root.findViewById(R.id.tVTargetWeight)
        btnSaveForm = root.findViewById(R.id.btnSaveForm)
        btnSkip = root.findViewById(R.id.btnSkip)

        setupViews()
        initView()
        setOnClickListener()
        callApi("Profile Api Calling", "")

        return root

    }

    private fun setupViews() {
        if (isFromSideMenu) {
            backButton.isVisible = true
            skipLayout.isVisible = false
            headingTextView.text = "Personal Details"
            btnPersonalDetail.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.save_btn))
        }
    }

    private fun initView() {
        calendar = Calendar.getInstance()
        selectDate.setOnFocusChangeListener { v, hasFocus ->
            var hasElementFocus = hasFocus
            if (!hasElementFocus && TextUtils.isEmpty(selectDate.text)) {
                selectDate.translationY = 0f
            } else {
                selectDate.translationY = (-selectDate.height).toFloat()
            }
        }
    }

    private fun callApi(type: String, data: String) {
        when (type) {
            "Profile Api Calling" -> {
                getServiceHelper().enqueueCallExtended(
                    getWebService().viewProfile(
                        ApiHeaderSingleton.apiHeader(requireContext())
                    ), "Profile Api Calling", true
                )
            }

            "Goal Entry Api Calling" -> {
                getServiceHelper().enqueueCallExtended(
                    getWebService().viewGoals(
                        ApiHeaderSingleton.apiHeader(requireContext())
                    ), "Goal Entry Api Calling", true
                )
            }

            "Set User Api Calling" -> {
                getServiceHelper().enqueueCallExtended(
                    webService.update_profile(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        setValueForProfile.first_name,
                        setValueForProfile.last_name,
                        setValueForProfile.image_url,
                        setValueForProfile.dob,
                        setValueForProfile.gender,
                        "",
                        setValueForProfile.height,
                        setValueForProfile.weight.toDouble(),
                        setValueForProfile.address,
                    ), "Set User Api Calling", true
                )
            }

            "Set Goal Api Calling" -> {
                getServiceHelper().enqueueCallExtended(
                    webService.updateGoals(
                        ApiHeaderSingleton.apiHeader(requireContext()),
                        setValueForGoal.current_weight,
                        setValueForGoal.target_weight,
                        setValueForGoal.target_weight_goal_date,
                        setValueForGoal.weekly_session_goal

                    ), "Set Goal Api Calling", true
                )
            }
        }
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            "Profile Api Calling" -> {
                try {
                    val response = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, getUserData::class.java)!!
//                    Log.d("On My Success", response.data)
                    if (response.msg == "success"){
                        setProfileData(response)
                    }else{
                        dockActivity?.showErrorMessage("Something Went Wrong")
                    }

                } catch (e: Exception) {
                    val genericMsgResponse = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ErrorResponseEnt::class.java)!!
                    dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                    Log.i("dummy error", e.message.toString())

                }
            }

            "Goal Entry Api Calling" -> {
                try {
                    val response = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, GetUserGoalData::class.java)!!
                    setGoalData(response)
                } catch (e: Exception) {
                    val genericMsgResponse = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ErrorResponseEnt::class.java)!!
                    dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                    Log.i("dummy error", e.message.toString())

                }
            }

            "Set User Api Calling" -> {
                try {
                    val response = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ResponseUserProfileModel::class.java)!!
                    if (response.msg == "success") {
                        if (isFromSideMenu) {
                            Utils.customToast(context, "Profile updated successfully")
                            return
                        }
                        sVPersonalDetail.visibility = View.GONE
                        clGoalEntry.visibility = View.VISIBLE
                        callApi("Goal Entry Api Calling", "")
                    }
                } catch (e: Exception) {
                    val genericMsgResponse = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ErrorResponseEnt::class.java)!!
                    dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                    Log.i("dummy error", e.message.toString())

                }
            }
            "Set Goal Api Calling" -> {
                try {
                    val response = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ResponseUserGoalModel::class.java)!!
                    if (response.msg == "success") {
                        Log.d("Request Successfull", "Request Successfull")

                    }
                } catch (e: Exception) {
                    val genericMsgResponse = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ErrorResponseEnt::class.java)!!
                    dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                    Log.i("dummy error", e.message.toString())

                }
            }
        }
    }

    override fun onFailure(message: String, tag: String) {

        when (tag) {
            "Profile Api Calling" -> {
                myDockActivity?.showErrorMessage(message)
                Log.i("xxError", "Error")
            }

            "Goal Entry Api Calling" -> {
                myDockActivity?.showErrorMessage(message)
                Log.i("xxError", "Error")
            }

            "Set User Api Calling" -> {
                myDockActivity?.showErrorMessage(message)
                Log.i("xxError", "Error")
            }

            "Set Goal Api Calling" -> {
                myDockActivity?.showErrorMessage(message)
                Log.i("xxError", "Error")
            }

        }
    }

    private fun setProfileData(result: getUserData) {
        result.data.forEach {
            getUserDataForUpdate = it.data
        }
        prefHelper.putLoginData(getUserDataForUpdate)
        setSpinners(gender)
        etFirstName.setText(getUserDataForUpdate.first_name)
        etLastName.setText(getUserDataForUpdate.last_name)
        etEmail.setText(getUserDataForUpdate.email)
        etHeight.setText(getUserDataForUpdate.height)
        etWeight.setText(getUserDataForUpdate.weight)
        selectDate.setText(getUserDataForUpdate.dob)

        if (getUserDataForUpdate.image_url.isNotEmpty()){
            prefHelper.imagePath = getUserDataForUpdate.image_url
            Glide.with(requireContext())
                .load(getUserDataForUpdate.image_url)
                .into(profileImage)
        }
        profileImageForModel = File(getUserDataForUpdate.image_url)

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

    private fun setSpinners(genderList: ArrayList<String>) {
        val genderAdapter = SpinnerAdapter(requireContext(), genderList)
        per_spinner_gender.adapter = genderAdapter
        per_spinner_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                selectedGenderValue = gender[position]
            }
        }
        if (getUserDataForUpdate.gender != "" && (getUserDataForUpdate.gender == "Male" || getUserDataForUpdate.gender == "male" || getUserDataForUpdate.gender == "MALE")) {
            per_spinner_gender.setSelection(1)
        } else if (getUserDataForUpdate.gender != "") {
            per_spinner_gender.setSelection(2)
        }
    }

    private fun setOnClickListener() {
        editProfileImage.setOnClickListener {
            pickProfileImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            //showAttachmentDialog()
        }
        selectDate.setOnClickListener {
            DatePickerDialog(
                myDockActivity, date,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
            if (date.toString().length > 0) {
                selectDate.setError(null)
            }
        }
        btnPersonalDetail.setOnClickListener {
            val checkVerifyData: Boolean = verifyProfileData()
            if (checkVerifyData) {
                callApi("Set User Api Calling", "")
            }
        }
        tVTargetWeight.setOnClickListener {
            DatePickerDialog(
                myDockActivity, date2,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
            if (date2.toString().length > 0) {
                tVTargetWeight.setError(null)
            }
        }
        btnSaveForm.setOnClickListener {
            val checkVerifyData: Boolean = verifyGoalData()
            if (checkVerifyData) {
                callApi("Set Goal Api Calling", "")
                myDockActivity?.emptyBackStack()
                myDockActivity?.replaceDockableFragment(HomeFragment())

            }

        }
        btnSkip.setOnClickListener {
            myDockActivity?.emptyBackStack()
            myDockActivity?.replaceDockableFragment(HomeFragment())

        }
        backButton.setOnClickListener {
            dockActivity.onBackPressed()
        }
    }

    private fun verifyGoalData(): Boolean {
        var checkFieldFillOrNot = 0
        when {
            etCurrentWeight.text.isEmpty() -> {
                etCurrentWeight.setError("This field is required");
                checkFieldFillOrNot = 0
            }

            etTargetWeight.text.isEmpty() -> {
                etTargetWeight.setError("This field is required");
                checkFieldFillOrNot = 0
            }

            etWeeklySession.text.isEmpty() -> {
                etWeeklySession.setError("This field is required");
                checkFieldFillOrNot = 0
            }

            tVTargetWeight.text.isEmpty() -> {
                tVTargetWeight.setError("This field is required");
                checkFieldFillOrNot = 0
            }

            else -> {
                setValueForGoal = SetUserGoalData(
                    current_weight = etCurrentWeight.text.toString(),
                    target_weight = etTargetWeight.text.toString(),
                    weekly_session_goal = etWeeklySession.text.toString(),
                    target_weight_goal_date = tVTargetWeight.text.toString()
                )
                checkFieldFillOrNot = 1
            }
        }
        if (checkFieldFillOrNot == 0) {
            return false
        }
        return true
    }

    private fun verifyProfileData(): Boolean {
        var checkFieldFillOrNot = 0
        when {
            etFirstName.text.isEmpty() -> {
                etFirstName.setError("This field is required");
                checkFieldFillOrNot = 0
            }

            etLastName.text.isEmpty() -> {
                etLastName.setError("This field is required");
                checkFieldFillOrNot = 0
            }

            etEmail.text.isEmpty() -> {
                etEmail.setError("This field is required");
                checkFieldFillOrNot = 0
            }

            etHeight.text.isEmpty() -> {
                etHeight.setError("This field is required");
                checkFieldFillOrNot = 0
            }

            etWeight.text.isEmpty() -> {
                etWeight.setError("This field is required");
                checkFieldFillOrNot = 0
            }

            selectDate.text.isEmpty() -> {

                selectDate.setError("This field is required")
                checkFieldFillOrNot = 0
            }

            selectedGenderValue == gender[0] -> {
                myDockActivity?.showErrorMessage("Select Gendor")
            }

            else -> {

                setValueForProfile = SetUserData(
                    first_name = etFirstName.text.toString(),
                    last_name = etLastName.text.toString(),
                    image_url = profileImageForModel.toString(),
                    dob = selectDate.text.toString(),
                    "",
                    gender = selectedGenderValue,
                    height = etHeight.text.toString().toDouble(),
                    weight = etWeight.text.toString().toDouble()

                )


                checkFieldFillOrNot = 1
            }
        }
        if (checkFieldFillOrNot == 0) {
            return false
        }
        return true
    }

    override fun setTitleBar(titleBar: TitleBar) {
        titleBar.hideTitleBar()
        titleBar.hideNotificationBtn()
        titleBar.hideNotificationText()
        titleBar.hideBrivoBtn()
    }
}