package com.passio.modulepassio.domain.diary

import android.util.Log
import com.passio.modulepassio.RetrofitClient.RetrofitClient
import com.passio.modulepassio.data.Repository
import com.passio.modulepassio.interfaces.DeletePassioDataCallback
import com.passio.modulepassio.interfaces.PassioDataCallback
import com.passio.modulepassio.models.HotsquadList.Passio.DeleteMealData
import com.passio.modulepassio.models.HotsquadList.Passio.GetPassioResponse
import com.passio.modulepassio.ui.model.FoodRecord
import com.passio.modulepassio.ui.util.getBefore30Days
import kotlinx.coroutines.delay
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object DiaryUseCase{

    private var callback: PassioDataCallback? = null
    private var callbackdelete: DeletePassioDataCallback? = null
    private val repository = Repository.getInstance()
    private var tokenUser :String = ""

    fun setPassioDataCallback(callback: PassioDataCallback,tokenUser:String) {
        this.callback = callback
        this.tokenUser = tokenUser
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

        Log.d("ncklnklbcb", tokenUser)
        return headerMap
    }

    fun deletePassioDataCallback(callbackdelete: DeletePassioDataCallback) {
        this.callbackdelete = callbackdelete
    }

    suspend fun getLogsForDay(day: Date): List<FoodRecord> {
        Log.d("DiaryUseCase", "Callback to fetch passio data for day: $day")

//        callback?.onFetchPassioData(day)
        val formattedDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(day)
        Log.d("PassioFragment", "Formatted date for API: $formattedDate")
        getFoodDetails(formattedDate)
        return repository.getLogsForDay(day)
    }


    suspend fun getFoodDetails(date: String): List<FoodRecord> {
        Log.d("getFoodDetails", "Fetching food details for date: $date")
        return suspendCoroutine { continuation ->
            RetrofitClient.instance.getFoodRecord(apiHeader(), date.toString())
                .enqueue(object : Callback<List<FoodRecord>> {
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
        return repository.getLogsForWeek(day)
    }

    suspend fun getLogsForMonth(day: Date): List<FoodRecord> {
        return repository.getLogsForMonth(day)
    }
    suspend fun getLogsForLast30Days(): List<FoodRecord> {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)
//        callback?.onFetchPassioData(currentDate)
        return repository.getLogsForLast30Days()
//        return repository.getLogsForDay(currentDate)
    }

    suspend fun deleteRecord(foodRecord: FoodRecord): Boolean {
        //Delete api
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = dateFormat.format(currentDate)

        callbackdelete?.onDeletePassioData(foodRecord.uuid.toString(),formattedDate,foodRecord)

        return repository.deleteFoodRecord(record = foodRecord)
    }

    // This method will be called from the parent once the API data is available
    fun onPassioDataDelete(uuid:String,food_entry_date:String,deleteList: DeleteMealData) {
        if (deleteList.data.isNotEmpty()) {
            Log.d("DiaryDeletee", "Passio data received delete: $deleteList")
            // Process the data
        } else {
            Log.d("DiaryDeleteeElse", "Received empty Passio data")
        }
    }

    suspend fun fetchAdherence(): List<Long> {
        return repository.fetchAdherence()
    }
}