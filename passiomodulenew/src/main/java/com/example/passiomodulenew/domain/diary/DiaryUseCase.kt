package com.example.passiomodulenew.domain.diary

import ai.passio.passiosdk.passiofood.data.measurement.Grams
import ai.passio.passiosdk.passiofood.data.measurement.Kilograms
import ai.passio.passiosdk.passiofood.data.measurement.Milligrams
import ai.passio.passiosdk.passiofood.data.measurement.Milliliters
import ai.passio.passiosdk.passiofood.data.measurement.UnitMass

import ai.passio.passiosdk.passiofood.data.model.PassioFoodAmount
import ai.passio.passiosdk.passiofood.data.model.PassioIngredient
import ai.passio.passiosdk.passiofood.data.model.PassioNutrients

import ai.passio.passiosdk.passiofood.data.model.PassioServingSize
import ai.passio.passiosdk.passiofood.data.model.PassioServingUnit
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.passiomodulenew.DietrixBaseURL.RetrofitClient
import com.example.passiomodulenew.Passio.DeleteMealData
import com.example.passiomodulenew.Passio.GetPassioResponse.GetFoodRecord
import com.example.passiomodulenew.Passio.GetPassioResponse.GetFoodRecordItem
import com.example.passiomodulenew.Passio.GetPassioResponse.Ingredient
import com.example.passiomodulenew.Passio.GetPassioResponse.ServingSizeX
import com.example.passiomodulenew.Passio.GetPassioResponse.ServingUnitX
import com.example.passiomodulenew.Passio.ReferenceNutrients
import com.example.passiomodulenew.Passio.WeightX
import com.example.passiomodulenew.data.Repository
import com.example.passiomodulenew.interfaces.DeletePassioDataCallback
import com.example.passiomodulenew.interfaces.PassioDataCallback
import com.example.passiomodulenew.ui.model.FoodRecord
import com.example.passiomodulenew.ui.model.FoodRecordIngredient
import com.example.passiomodulenew.ui.model.MealLabel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object DiaryUseCase {

    private val repository = Repository.getInstance()

    private var callback: PassioDataCallback? = null
    private var callbackdelete: DeletePassioDataCallback? = null
    private var appContext: Context? = null
    private var tokenUser:String = ""
    private lateinit var recordsData: FoodRecord

    fun setPassioDataCallback(callback: PassioDataCallback,tokenUser:String) {
        this.callback = callback
        this.tokenUser = tokenUser

        Log.d("djncjndkjn",tokenUser)
    }

    suspend fun getLogsForDay(day: Date): List<FoodRecord> {

        val formattedDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(day)
        Log.d("PassioFragment", "Formatted date for API: $formattedDate")
        getFoodDetails(formattedDate)
        return repository.getLogsForDay(day)
    }

    fun apiHeader(): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
//        val prefHelper = BasePreferenceHelper(context)
        headerMap.apply {
            put(
                "Authorization",
                "Bearer $tokenUser"
            )
        }
        return headerMap
    }

    suspend fun getFoodDetails(date: String): List<FoodRecord> {
        Log.d("getFoodDetails", "Fetching food details for date: $date")
        return suspendCoroutine { continuation ->
            RetrofitClient.instance.getFoodRecord(apiHeader(), date.toString())
                .enqueue(object : Callback <List<FoodRecord>> {
                    override fun onResponse(
                        call: Call<List<FoodRecord>>,
                        response: Response<List<FoodRecord>>
                    ) {
                        Log.d("APIResponse", "Response Code: ${response.code()}")
                        if (response.isSuccessful) {
                            val foodRecordItems = response.body() ?: emptyList()
                            Log.d("APIResponseBody", "Fetched items count: ${foodRecordItems.size}")
                            Log.d("APIResponseBody", "Fetched items count: ${response.body()}")

                            continuation.resume(foodRecordItems)
                        } else {
                            Log.e("API Error", "Error: ${response.code()} with body: ${response.errorBody()?.string()}")
                            continuation.resume(emptyList())
                        }
                    }

                    override fun onFailure(call: Call<List<FoodRecord>>, t: Throwable) {
                        Log.e("API Failure", "Failure: ${t.message}")
                        continuation.resume(emptyList())
                    }
                })
        }
    }

    suspend fun getLogsForWeek(day: Date): List<FoodRecord> {
        return repository.getLogsForDay(day)
    }

    suspend fun getLogsForMonth(day: Date): List<FoodRecord> {
        return repository.getLogsForMonth(day)
    }

    suspend fun getLogsForLast30Days(): List<FoodRecord> {
        return repository.getLogsForLast30Days()
    }


    suspend fun deleteRecord(foodRecord: FoodRecord): Boolean {
        //Delete api
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)

        deleteFoodDetails(foodRecord.uuid.toString(),formattedDate, foodRecord)
        return repository.deleteFoodRecord(foodRecord.uuid)
    }

    suspend fun deleteFoodDetails(uuid:String,date: String, recordsData: FoodRecord): List<FoodRecord> {
        Log.d("getFoodDetails", "Fetching food details for date: $date")
        this.recordsData = recordsData
        return suspendCoroutine { continuation ->
            RetrofitClient.instance.deletePassioData(apiHeader(), recordsData.uuid.toString(),date.toString())
                .enqueue(object : Callback <List<FoodRecord>> {
                    override fun onResponse(
                        call: Call<List<FoodRecord>>,
                        response: Response<List<FoodRecord>>
                    ) {
                        Log.d("APIResponse", "Response Code: ${response.code()}")
                        if (response.isSuccessful) {
                            Toast.makeText(appContext, response.message(), Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("API Error", "Error: ${response.code()} with body: ${response.errorBody()?.string()}")
                            continuation.resume(emptyList())
                        }
                    }

                    override fun onFailure(call: Call<List<FoodRecord>>, t: Throwable) {
                        Log.e("API Failure", "Failure: ${t.message}")
                        continuation.resume(emptyList())
                    }
                })
        }
    }

    suspend fun fetchAdherence(): List<Long> {
        return repository.fetchAdherence()
    }
}