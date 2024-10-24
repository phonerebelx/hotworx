package com.hotworx.ui.passioactivity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.example.passionewsdk.Passio.DeleteMealData
import com.example.passionewsdk.Passio.ErrorResponseEnt
import com.example.passionewsdk.Passio.HotworxUserProfile
import com.example.passionewsdk.Passio.PostPassioResponse
import com.example.passionewsdk.Passio.interfaces.ProfileDataCallback
import com.example.passionewsdk.domain.camera.CameraUseCase
import com.example.passionewsdk.domain.customfood.CustomFoodUseCase
import com.example.passionewsdk.domain.diary.DiaryUseCase
import com.example.passionewsdk.domain.mealplan.MealPlanUseCase
import com.example.passionewsdk.domain.recipe.RecipeUseCase
import com.example.passionewsdk.domain.search.EditFoodUseCase
import com.example.passionewsdk.domain.user.UserProfileUseCase
import com.example.passionewsdk.interfaces.DeletePassioDataCallback
import com.example.passionewsdk.interfaces.NutritionDataCallback
import com.example.passionewsdk.interfaces.PassioDataCallback
import com.example.passionewsdk.interfaces.PostPassioDataCallback
import com.example.passionewsdk.ui.model.FoodRecord
import com.example.passionewsdk.ui.model.UserProfile
import com.example.passionewsdk.uimodule.NutritionUIModule
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentPassioBinding
import com.hotworx.global.Constants
import com.hotworx.models.HotsquadList.Passio.FoodEntry
import com.hotworx.models.HotsquadList.Passio.postPassioRequest
import com.hotworx.models.PassioNutritionGoals.NutritionPercentage
import com.hotworx.models.PassioNutritionGoals.PassioNutritionGoalsRequest
import com.hotworx.models.UserData.ResponseUserProfileModel
import com.hotworx.models.passioprofile.MacroTargets
import com.hotworx.models.passioprofile.MealPlan
import com.hotworx.models.passioprofile.NutritionData
import com.hotworx.models.passioprofile.PassioProfileResponse
import com.hotworx.models.passioprofile.ReminderSettings
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.views.TitleBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PassioFragment : BaseFragment(),
    PassioDataCallback, PostPassioDataCallback,
    ProfileDataCallback, NutritionDataCallback , DeletePassioDataCallback{
    private var _binding: FragmentPassioBinding? = null
    private val binding get() = _binding
    private lateinit var recordList: PostPassioResponse
    private lateinit var deleteList: DeleteMealData
    private lateinit var records: List<FoodRecord>
    private lateinit var userProfile: HotworxUserProfile
    private lateinit var recordsData: FoodRecord
    private var isApiCallInProgress = false // Flag to prevent multiple calls

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("PassioFragmenttttt", "Fragment attached")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPassioBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSDKReady()

        // Set the callback before calling DiaryUseCase
        var tokenUser:String = ""
        tokenUser =  prefHelper.loginToken.toString()
//        passioConnector
        DiaryUseCase.setPassioDataCallback(this,tokenUser)
        UserProfileUseCase.setPassioDataCallback(this,tokenUser)
        UserProfileUseCase.postProfileDataCallback(this)
//        UserProfileUseCase.postNutritionDataCallback(this)
        DiaryUseCase.deletePassioDataCallback(this)
        RecipeUseCase.postPassioDataCallback(this)
        RecipeUseCase.postPassioDataCallback(this)
        CustomFoodUseCase.postPassioDataCallback(this)
        CameraUseCase.postPassioDataCallback(this)
        MealPlanUseCase.postPassioDataCallback(this)
        EditFoodUseCase.postPassioDataCallback(this)
        EditFoodUseCase.postPassioDataCallback(this)
        EditFoodUseCase.deletePassioDataCallback(this)

        CoroutineScope(Dispatchers.IO).launch {
            try {
//                val logsDiary = DiaryUseCase.getLogsForDay(Date())
                val logsDiary = DiaryUseCase.getLogsForLast30Days()
                val deleteDiary = DiaryUseCase.deleteRecord(FoodRecord())
                val logsMeal = MealPlanUseCase.logFoodRecord(FoodRecord())
                // Switch to the main thread for UI updates
                withContext(Dispatchers.Main) {
                    // Check if the fragment is still attached and binding is available before updating UI
                    if (isAdded && binding != null) {
                        binding?.textView?.text = "Logs received: ${logsDiary.size}"
                        Log.e("PassioFragmentTExtData", "Logs received: ${logsDiary.size}")
                        Log.e("PassioLogMeal", "Logs received: ${logsMeal}")
                        Log.e("DeleteeeeLogMeal", "Logs received: ${deleteDiary}")
                    } else {
                        Log.e("PassioFragment", "Fragment is not attached, skipping UI update")
                    }
                }
            } catch (e: Exception) {
                // Handle error on the main thread
                withContext(Dispatchers.Main) {
                    // Ensure the fragment is attached before updating UI on error
                    if (isAdded && binding != null) {
                        binding?.textView?.text = "Error: ${e.message}"
                        Log.e("PassioFragmentTExtData", "Error: ${e.message}")
                    }
                }
            }
        }

    }

    private fun onSDKReady() {
        NutritionUIModule.launch(this.requireContext())
        Log.d("ytytytuy", "jhkjhkhlkhlkllhlk")
//        requireActivity().finish()
    }

    override fun setTitleBar(titleBar: TitleBar) {
        super.setTitleBar(titleBar)

        titleBar.showBackButton()
    }

    override fun onFetchPassioData(day: Date) {
        TODO("Not yet implemented")
    }

    //POST API

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)
        when (tag) {
            Constants.POST_PASSIO_RECORD -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, PostPassioResponse::class.java)!!
                        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                        val currentDate = Date()
                        val formattedDate = dateFormat.format(currentDate)
                        if (response != null) {
//                            onPassioDataSuccess(recordList)
                        } else {
//                            onPassioDataError("Received empty response")
                        }
                    } catch (e: Exception) {
                        dockActivity?.showErrorMessage(e.message.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }

            Constants.DELETE_PASSIO_RECORD -> {
                val responseJson = liveData.value
                Log.d("Response", "LiveData value: $responseJson")

                if (responseJson != null) {
                    try {
                        val response = GsonFactory.getConfiguredGson()?.fromJson(responseJson, DeleteMealData::class.java)!!
                        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                        val currentDate = Date()
                        val formattedDate = dateFormat.format(currentDate)
                        if (response != null) {
                            deleteDataSuccess(deleteList)
                        } else {
                            deleteDataError("Received empty response")
                        }
                    } catch (e: Exception) {
                        dockActivity?.showErrorMessage(e.message.toString())
                        Log.i("Error", e.message.toString())
                    }
                } else {
                    Log.e("Error", "LiveData value is null")
                    dockActivity?.showErrorMessage("No response from server")
                }
            }

            "Set User Api Calling" -> {
                try {
                    val response = GsonFactory.getConfiguredGson()?.fromJson(liveData.value, ResponseUserProfileModel::class.java)!!
                    if (response.msg == "success") {
                        if (response != null) {
                            onProfileDataSuccess(userProfile)
                        } else {
                            onProfileDataError("Received empty response")
                        }
                    }
                } catch (e: Exception) {
                    val genericMsgResponse = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ErrorResponseEnt::class.java)!!
//                    dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                    Log.i("dummy error", e.message.toString())

                }
            }

            "Set Nutrition Api Calling" -> {
                try {
                    val response = GsonFactory.getConfiguredGson()?.fromJson(liveData.value, ResponseUserProfileModel::class.java)!!
                    if (response.msg == "success") {
                        if (response != null) {
                            onProfileDataSuccess(userProfile)
                        } else {
                            onProfileDataError("Received empty response")
                        }
                    }
                } catch (e: Exception) {
                    val genericMsgResponse = GsonFactory.getConfiguredGson()
                        ?.fromJson(liveData.value, ErrorResponseEnt::class.java)!!
//                    dockActivity?.showErrorMessage(genericMsgResponse.error.toString())
                    Log.i("dummy error", e.message.toString())

                }
            }

        }
    }

    override fun onPostPassioData(date: String, url: String, records: List<FoodRecord>) {
        if (!isAdded || context == null) {
            Log.e("PassioFragment", "Fragment is not attached, skipping API call")
            return
        }

        if (records.isNotEmpty()) {
            val firstRecord = records.first()
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val createdAtTime = firstRecord.createdAtTime() ?: Date().time
            val formattedDate = dateFormat.format(Date(createdAtTime))
            Log.d("FormattedDate", formattedDate)

            this.records = records
            val foodEntries = records.map { record ->
                FoodEntry(
                    food_entry_date = formattedDate,  // Use the formatted current date
                    "",           // Replace with actual URL if available
                    food_list = listOf(record)
                )
            }

            // Create the request object
            val request = postPassioRequest(food_entries = foodEntries)

            // Make the API call
            getServiceHelper()?.enqueueCallExtended(
                getWebService()?.postPassioData(
                    ApiHeaderSingleton.apiHeader(requireContext()),
                    request
                ), Constants.POST_PASSIO_RECORD, true
            )
        } else {
            Log.d("FormattedDate", "Records list is empty")
        }
    }

    override fun onPassioDataSuccess(recordList: PostPassioResponse) {
        this.recordList = recordList
        Log.d("recordList", "Record data received: $recordList")
        MealPlanUseCase.onPassioDataPost("","",records)
    }

    override fun Error(error: String) {
        if (::recordList.isInitialized) {
            Log.d("RecordListtt", "Received Passio data from parent: $recordList")
        } else {
            Log.d("RecordListtt", "passioList is not initialized. Error: $error")
        }
        Log.e("RecordListtt", "Server issue: $error")
    }


    //Delete Passio Record
    override fun onDeletePassioData(uuid: String, food_entry_date: String ,recordsData: FoodRecord) {
        if (!isAdded || context == null) {
            Log.e("DeletePassioFragment", "Fragment is not attached, skipping API call")
            return
        }
//        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
//        val currentDate = Date()
//        val formattedDate = dateFormat.format(currentDate)

        if (recordsData.uuid.isNotEmpty()) {
//            val firstRecord = records.first()
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val createdAtTime = recordsData.createdAtTime() ?: Date().time
            val formattedDate = dateFormat.format(Date(createdAtTime))

            this.recordsData = recordsData

            // Make the API call
            getServiceHelper()?.enqueueCallExtended(
                getWebService()?.deletePassioData(
                    ApiHeaderSingleton.apiHeader(requireContext()),
                    recordsData.uuid,
                    formattedDate
                ), Constants.DELETE_PASSIO_RECORD, true
            )
        }
    }

    override fun deleteDataSuccess(deleteList: DeleteMealData) {
        this.deleteList = deleteList
        Log.d("DeleteList", "Record data received: $deleteList")
        DiaryUseCase.onPassioDataDelete("","",deleteList)
    }

    override fun deleteDataError(error: String) {
        if (::deleteList.isInitialized) {
            Log.d("deleteListtttttt", "Received Passio data from parent: $deleteList")
        } else {
            Log.d("deleteListtttttt", "passioList is not initialized. Error: $error")
        }
        Log.e("deleteListtttttt", "Server issue: $error")
    }

    //Update Profile

    override fun onPostProfileData(profile: UserProfile) {
        if (!isAdded || context == null) {
            Log.e("PassioFragment", "Fragment is not attached, skipping API call")
            return
        }

        val reminderSettings = ReminderSettings(
            dinner = true,
            breakfast = true,
            lunch = true
        )

        val macroTargets = MacroTargets(
            fat = profile.fatPer,
            protein = profile.proteinPer,
            carbs = profile.carbsPer
        )

        val mealPlan = MealPlan(
            mealPlanLabel = profile.passioMealPlan!!.mealPlanLabel,
            mealPlanTitle = profile.passioMealPlan!!.mealPlanTitle,
            macroTargets = macroTargets
        )

        val nutritionData = NutritionData(
            proteinPercent =profile.proteinPer,
            goalWater =  profile.waterTarget,
            fatPercent = profile.fatPer,
            waterUnit = "oz",
            weight = profile.weight,
            reminderSettings = reminderSettings,
            caloriesTarget = profile.caloriesTarget,
            height = profile.height,
            age = profile.age,
            goalWeightTimeLine = "",
            mealPlan = mealPlan,
            recommendedCalories = 0,
            units = "",
            gender = profile.gender.toString() ,
            carbsPercent = profile.carbsPer,
            activityLevel = profile.activityLevel.toString(),
            firstName = profile.userName,
            heightUnits =profile.height.toString(),
            goalWeight = profile.targetWeight
        )

        val request = PassioProfileResponse(
            data = nutritionData
        )

        getServiceHelper().enqueueCallExtended(
            webService.update_passio_profile(
                ApiHeaderSingleton.apiHeader(requireContext()),
                request
            ), "Set User Api Calling", true
        )
    }

    override fun onProfileDataSuccess(userProfile: HotworxUserProfile) {
        this.userProfile = userProfile
        Log.d("recordList", "Record data received: $userProfile")
        UserProfileUseCase.onProfileDataPost(userProfile)
    }

    override fun onProfileDataError(error: String) {
        if (::userProfile.isInitialized) {
            Log.d("RecordListtt", "Received Passio data from parent: $userProfile")
        } else {
            Log.d("RecordListtt", "passioList is not initialized. Error: $error")
        }
        Log.e("RecordListtt", "Server issue: $error")
    }

    //Nutrition Goals
    override fun onPostNutritionData(profile: UserProfile) {
        if (!isAdded || context == null) {
            Log.e("PassioFragment", "Fragment is not attached, skipping API call")
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)

        val request = PassioNutritionGoalsRequest(
            0,
            profile.caloriesTarget,
            profile.activityLevel.toString(),
            profile.waterTarget,
            profile.targetWeight,
            formattedDate,
            profile.calorieDeficit.lblImperial,
            profile.passioMealPlan?.mealPlanTitle?.get(0).toString(),
            nutrition_percentage = NutritionPercentage(
                carbs = profile.carbsPer,
                protein =profile.proteinPer,
                fat = profile.fatPer
            )
        )

        getServiceHelper().enqueueCallExtended(
            webService.updatePassioNutritionGoals(
                ApiHeaderSingleton.apiHeader(requireContext()),
                request
            ), "Set Nutrition Api Calling", true
        )
    }

    override fun onNutritionDataSuccess(userProfile: HotworxUserProfile) {
        this.userProfile = userProfile
        Log.d("recordList", "Record data received: $userProfile")
        UserProfileUseCase.onProfileDataPost(userProfile)
    }

    override fun onNutritionDataError(error: String) {
        if (::userProfile.isInitialized) {
            Log.d("RecordListtt", "Received Passio data from parent: $userProfile")
        } else {
            Log.d("RecordListtt", "passioList is not initialized. Error: $error")
        }
        Log.e("RecordListtt", "Server issue: $error")
    }

}
