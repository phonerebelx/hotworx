package com.hotworx.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import butterknife.BindView
import com.hotworx.R
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.helpers.ServiceHelper
import com.hotworx.helpers.Utils
import com.hotworx.helpers.UtilsHelpers
import com.hotworx.interfaces.LoadingListener
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.ForgotPassword.ForgotPasswordUserDetail
import com.hotworx.requestEntity.ExtendedBaseModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.retrofit.WebService
import com.hotworx.retrofit.WebServiceFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import spencerstudios.com.bungeelib.Bungee
import java.util.*

class ForgotPasswordActivity : BaseActivity(),LoadingListener {

    private lateinit var etUsername: AppCompatEditText
    private lateinit var etPhoneNumber: AppCompatEditText
    private lateinit var btnSubmit: AppCompatButton
    @BindView(R.id.progressBar)
    private lateinit var progressBar: ProgressBar
    private var isLoading = false
    private lateinit var forgotPasswordUserDetail: ForgotPasswordUserDetail
    protected var webService: WebService? = null
    public var checkForgotPasswordFlow: Boolean = false
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        etUsername = findViewById(R.id.etUsername)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        btnSubmit = findViewById(R.id.btnSubmit)
        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(
                this,
                WebServiceConstants.BASE_URL
            )
        }
        setOnClickListener()
    }


    private fun setOnClickListener() {

        btnSubmit.setOnClickListener {
            val dataVerified = verifyDataFilled()
            if (dataVerified){
                callForgotPasswordApi()
            }
        }

    }


    private fun callForgotPasswordApi(){
        onLoadingStarted()
        webService?.login(
            forgotPasswordUserDetail.email_address,
            forgotPasswordUserDetail.phone_number,
            UtilsHelpers.getDeviceId(this)
        )?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                onLoadingFinished()
                try {
                    if (response.code() == 200 && response.body() != null) {
                        val responseBody = response.body()!!.string()
                        val extBaseModel = GsonFactory.getConfiguredGson().fromJson(responseBody, ExtendedBaseModel::class.java)
                        prefHelper.putLoginToken(extBaseModel.token)
                        try{
                            prefHelper.setUserDataForForgotPassword(forgotPasswordUserDetail)
                            if (extBaseModel.two_factor == "yes"){

                                val intent = Intent(this@ForgotPasswordActivity, OtpActivity::class.java)
                                intent.putExtra(Constants.flag, Constants.ForgotPassword)
                                startActivity(intent)
                                finish()
                            }else{

                                val intent = Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                                intent.putExtra(Constants.flag, Constants.ForgotPassword)
                                startActivity(intent)
                                finish()
                            }
                        }catch (e: Exception){
                            Utils.customToast(this@ForgotPasswordActivity, resources.getString(R.string.error_failure))
                        }
                    } else {
                        val responseBody = response.errorBody()?.string()
                        GsonFactory.getConfiguredGson()?.fromJson(responseBody, ErrorResponseEnt::class.java)?.let { errorResponseEnt ->
                            Utils.customToast(this@ForgotPasswordActivity, errorResponseEnt.error)
                        }
                    }
                } catch (ex: Exception) {

                    Utils.customToast(this@ForgotPasswordActivity, resources.getString(R.string.internal_exception_messsage))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onLoadingFinished()
                t.printStackTrace()
                Log.e("On Failure",ServiceHelper::class.simpleName + " by tag: " + t.toString() )
                Utils.customToast(this@ForgotPasswordActivity, t.toString())
            }
        })
    }

    private fun verifyDataFilled(): Boolean {
        var checkFieldFillOrNot = 0
        when {
            etUsername.text!!.isEmpty() -> {
                etUsername.setError("This field is required");
                checkFieldFillOrNot = 0
            }
            etPhoneNumber.text!!.isEmpty() -> {
                etPhoneNumber.setError("This field is required");
                checkFieldFillOrNot = 0
            }
            else -> {
                forgotPasswordUserDetail = ForgotPasswordUserDetail(
                    etUsername.text.toString(),
                    etPhoneNumber.text.toString(),
                    checkDeviceId(),
                    "phone_number"
                )
                checkFieldFillOrNot = 1
            }
        }
        if (checkFieldFillOrNot == 0) {
            return false
        }
        return true
    }

    private fun checkDeviceId(): String{
        if (prefHelper.deviceId.isEmpty()){
            prefHelper.deviceId = UUID.randomUUID().toString()
            return prefHelper.deviceId
        }
        return prefHelper.deviceId
    }

    override fun onLoadingStarted() {
        isLoading = true
        progressBar.visibility = View.VISIBLE
    }

    override fun onLoadingFinished() {
        isLoading = false
        progressBar.visibility = View.GONE
    }

    override fun onProgressUpdated(percentLoaded: Int) {

    }

    private fun backBtnWork(){
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra(Constants.flag, Constants.ForgotPassword)
            startActivity(intent)
            finish()
            Bungee.split(this)
    }

    override fun onBackPressed() {
        super.onBackPressed() // If not loading, proceed with the default back button action
        if (isLoading) {
            Utils.customToast(this, getString(R.string.message_wait));
        }else{
            backBtnWork()

        }
    }
}