package com.hotsquad.hotsquadlist.utils

import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import android.util.Patterns
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.hotsquad.hotsquadlist.koin.InjectUtils
import com.hotsquad.hotsquadlist.storage.AppPreferences
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object Utils {
    /**
     * Email validation
     * @param email to be check
     * @return false if email is not valid
     */
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Check if internet connection available
     * @return true if internet connection available
     */
    fun isOnline(): Boolean {
        val cm =
            InjectUtils.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    /**
     * The method will convert text to text/plain body
     * @param param text to be convert
     * @return Simple text in request body
     */
    fun getSimpleTextBody(param: String): RequestBody {
        return param.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    /**
     * Check if user login
     * @return true if user logged in
     */
    fun isLogin(): Boolean {
        AppPreferences.loginData?.let {
            return true
        } ?: kotlin.run {
            return false
        }
    }

    /**
     * Show Material Date Picker Dialog
     * @param textView Pass the TextView where the selected Date will be show
     * @param validator Pass the Date validation
     * Example DateValidatorPointBackward.now() For Pass Date Selection
     *         DateValidatorPointForward.now() For Future Date Selection
     * @param manager Provide supportFragmentManager
     */
    fun showDatePickerDialog(
        textView: TextView,
        validator: CalendarConstraints.DateValidator,
        manager: FragmentManager
    ) {

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(validator)

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
        datePicker.show(manager, "TAG")

        datePicker.addOnPositiveButtonClickListener {
            textView.text = datePicker.headerText
        }
    }
}