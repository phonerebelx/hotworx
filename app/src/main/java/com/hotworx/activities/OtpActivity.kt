
package com.hotworx.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.chaos.view.PinView
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.helpers.ServiceHelper
import com.hotworx.helpers.Utils
import com.hotworx.helpers.UtilsHelpers
import com.hotworx.interfaces.LoadingListener
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.ForgotPassword.ForgotPasswordUserDetail
import com.hotworx.models.UserDataModel
import com.hotworx.models.VerifyOtpPostModel
import com.hotworx.requestEntity.ExtendedBaseModel
import com.hotworx.requestEntity.MainProfileResultModel
import com.hotworx.requestEntity.ViewProfileResponse
import com.hotworx.retrofit.GsonFactory
import com.hotworx.retrofit.WebService
import com.hotworx.retrofit.WebServiceFactory
import com.tapadoo.alerter.Alert

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import spencerstudios.com.bungeelib.Bungee

class OtpActivity : BaseActivity(), LoadingListener {

    var TAG = "OtpActivity"

    lateinit var userInputOtp: String
    protected var serviceHelper: ServiceHelper? = null
    protected var webService: WebService? = null
    private var unbinder: Unbinder? = null
    private var isLoading = false
    private lateinit var getUserData: UserDataModel
    private lateinit var getUserDataForForgotPassword: ForgotPasswordUserDetail
    private lateinit var verifyOtpPostModel: VerifyOtpPostModel
    private lateinit var getDeviceId: String
    private lateinit var btnBack: ImageView
    private lateinit var btnVerify: AppCompatButton
    private lateinit var tvResendCode: AppCompatTextView
    var getForgotPasswordActivity:ForgotPasswordActivity = ForgotPasswordActivity()
    var getLoginActivity:LoginActivity = LoginActivity()


    private lateinit var fpsPinView: PinView

    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        btnBack = findViewById(R.id.btnBack)
        btnVerify = findViewById(R.id.btnVerify)
        tvResendCode = findViewById(R.id.tvResendCode)
        val intent = intent

        when (intent.getStringExtra(Constants.flag).toString()){
            Constants.ForgotPassword -> {
                btnBack.visibility = View.VISIBLE
                getForgotPasswordActivity.checkForgotPasswordFlow = true
                getLoginActivity.checkLoginFlow = false

                getUserDataForForgotPasswordFun()
                verifyOtpPostModel = VerifyOtpPostModel(
                    email_address = getUserDataForForgotPassword.email_address,
                    password = "",
                    phone_number = getUserDataForForgotPassword.phone_number,
                    device_id = getUserDataForForgotPassword.device_id,
                    otp = "",
                    type = getUserDataForForgotPassword.type
                )
            }
            Constants.LoginActivity -> {
                btnBack.visibility = View.GONE
                getForgotPasswordActivity.checkForgotPasswordFlow = false
                getLoginActivity.checkLoginFlow = true

                getUserData()
                getDeviceIdData()
                verifyOtpPostModel = VerifyOtpPostModel(
                    email_address = getUserData.userName,
                    password = getUserData.password,
                    phone_number = "",
                    device_id = getDeviceId,
                    otp = "",
                    type = getUserData.type
                )
            }
        }

        fpsPinView = findViewById<PinView>(R.id.fps_pinView)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        unbinder = ButterKnife.bind(this)
        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(
                this,
                WebServiceConstants.BASE_URL
            )
        }

        fpsPinView.setAnimationEnable(true)

        setPinViewListener()

        btnBack.setOnClickListener {
            backBtnWork()
        }

        btnVerify.setOnClickListener {
            fpsPinView.text?.let { code ->
                if (code.length == 6) verifyOtp(code = code.toString())
            }
        }

        tvResendCode.setOnClickListener {
            if (getForgotPasswordActivity.checkForgotPasswordFlow) {
                loginWithPhone()
            } else {
                loginWithPassword()
            }
        }
    }

    private fun getUserData() {
        getUserData = prefHelper.getUserDataModel()
    }
    private fun getUserDataForForgotPasswordFun() {
        getUserDataForForgotPassword = prefHelper.getUserDetailForForgotPassword()
    }

    private fun getDeviceIdData() {
        getDeviceId = prefHelper.getDeviceId()
    }
    private fun setPinViewListener() {
        fpsPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun verifyOtp(code: String){
        onLoadingStarted()

        verifyOtpPostModel.otp = code

        webService?.verifyOtp(
            ApiHeaderSingleton.apiHeader(context = this),
            verifyOtpPostModel.email_address,
            verifyOtpPostModel.password,
            verifyOtpPostModel.phone_number,
            UtilsHelpers.getDeviceId(this),
            verifyOtpPostModel.otp,
            verifyOtpPostModel.type
        )?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                onLoadingFinished()
                try {
                    Log.d("getResponseCODE",response.code().toString())
                    if (response.code() == 200 && response.body() != null) {
                        val responseBody = response.body()!!.string()
                        val getResponse = GsonFactory.getConfiguredGson().fromJson(responseBody, MainProfileResultModel::class.java)
//                            saveUser(getResponse)
                        if (getForgotPasswordActivity.checkForgotPasswordFlow == true && getLoginActivity.checkLoginFlow == false){
                            goToResetPassword(getResponse)
                        }else if (getForgotPasswordActivity.checkForgotPasswordFlow == false && getLoginActivity.checkLoginFlow == true){
                            saveUser(getResponse)
                        }

                    } else {
                        val responseBody = response.errorBody()?.string()
                        val getResponse = GsonFactory.getConfiguredGson().fromJson(
                            responseBody,
                            ErrorResponseEnt::class.java
                        )
                        Utils.customToast(this@OtpActivity, getResponse.error)
                    }
                } catch (ex: Exception) {
                    Log.e("Exception OTP", ex.toString())
                    Utils.customToast(
                        this@OtpActivity,
                        resources.getString(R.string.internal_exception_messsage)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                onLoadingFinished()
                t.printStackTrace()
                Log.e(
                    ServiceHelper::class.java.simpleName + " by tag: " + TAG,
                    t.toString()
                )
                Utils.customToast(this@OtpActivity, t.toString())
            }
        })
    }

    private fun loginWithPhone(){
        onLoadingStarted()
        webService?.login(
            verifyOtpPostModel.email_address,
            verifyOtpPostModel.phone_number,
            UtilsHelpers.getDeviceId(this)
        )?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                onLoadingFinished()
                try {
                    if (response.code() == 200 && response.body() != null) {
                        val responseBody = response.body()!!.string()
                        val extBaseModel = GsonFactory.getConfiguredGson().fromJson(responseBody, ExtendedBaseModel::class.java)
                        prefHelper.putLoginToken(extBaseModel.token)
                        Utils.customToast(this@OtpActivity, resources.getString(R.string.otp_message_success))
                    }
                } catch (ex: Exception) {
                    Utils.customToast(this@OtpActivity, resources.getString(R.string.internal_exception_messsage))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onLoadingFinished()
                Utils.customToast(this@OtpActivity, t.toString())
            }
        })
    }

    private fun loginWithPassword(){
        onLoadingStarted()
        webService?.loginwithpassword(
            verifyOtpPostModel.email_address,
            verifyOtpPostModel.password,
            UtilsHelpers.getDeviceId(this)
        )?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                onLoadingFinished()
                try {
                    if (response.code() == 200 && response.body() != null) {
                        val responseBody = response.body()!!.string()
                        val extBaseModel = GsonFactory.getConfiguredGson().fromJson(responseBody, ExtendedBaseModel::class.java)
                        prefHelper.putLoginToken(extBaseModel.token)
                        Utils.customToast(this@OtpActivity, resources.getString(R.string.otp_message_success))
                    }
                } catch (ex: Exception) {
                    Utils.customToast(this@OtpActivity, resources.getString(R.string.internal_exception_messsage))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onLoadingFinished()
                Utils.customToast(this@OtpActivity, t.toString())
            }
        })
    }

    private fun goToResetPassword(result: MainProfileResultModel){
        if (result != null) {
            prefHelper.putLoginToken(result.token)
            val intent = Intent(this, ResetPasswordActivity::class.java)
            intent.putExtra(Constants.flag, Constants.OtpActivity)
            startActivity(intent)
            finish()
            Bungee.split(this)
        } else {
            Utils.customToast(this, getString(R.string.error_failure))
        }
    }
    fun saveUser(result: MainProfileResultModel) {
        // it is saving new token provided by otpVerify Api and userdetail
        if (result != null) {
            Log.e(TAG, "login resultReceived-------- ${result.toString()}")
            prefHelper.setLoginStatus(true)
            prefHelper.putLoginToken(result.token)
//            prefHelper.putLoginData(result.data)
            prefHelper.putUserId(result.data.getUser_id())
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constants.flag, Constants.OtpActivity)
            startActivity(intent)
            finish()
            Bungee.split(this)
        } else {
            Utils.customToast(this, getString(R.string.error_failure))
        }
    }

    private fun backBtnWork(){
        if (getForgotPasswordActivity.checkForgotPasswordFlow == true && getLoginActivity.checkLoginFlow == false){
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            intent.putExtra(Constants.flag, Constants.OtpActivity)
            startActivity(intent)
            finish()
            Bungee.split(this)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed() // If not loading, proceed with the default back button action
        if (isLoading) {
            Utils.customToast(this, getString(R.string.message_wait));
        }else{
            backBtnWork()
            btnBack
        }
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
}