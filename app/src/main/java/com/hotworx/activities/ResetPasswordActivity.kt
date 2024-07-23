package com.hotworx.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import butterknife.BindView
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.helpers.HashUtils
import com.hotworx.helpers.ServiceHelper
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.LoadingListener
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.ResetPassword.ResetPasswordResponse
import com.hotworx.requestEntity.ExtendedBaseModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.retrofit.WebService
import com.hotworx.retrofit.WebServiceFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : BaseActivity(), LoadingListener {

    private lateinit var etPassword: AppCompatEditText
    private lateinit var etConformPassword: AppCompatEditText
    private lateinit var btnSubmit: AppCompatButton
    protected var webService: WebService? = null

    @BindView(R.id.progressBar)
    private lateinit var progressBar: ProgressBar
    private var isLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        etPassword = findViewById(R.id.etPassword)
        etConformPassword = findViewById(R.id.etConformPassword)
        btnSubmit = findViewById(R.id.btnSubmit)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
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
            val checkDataVerify = verifyPasswordMatch()
            if (checkDataVerify) {
                callResetPasswordApi()
            }
        }
    }

    private fun callResetPasswordApi() {
        onLoadingStarted()
        val encodedPassword = HashUtils.sha256(etPassword.text.toString())
        webService?.resetpassword(
            ApiHeaderSingleton.apiHeader(this@ResetPasswordActivity), encodedPassword
        )?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                onLoadingFinished()
                try {
                    if (response.code() == 200 && response.body() != null) {
                        val responseBody = response.body()!!.string()
                        val resetPasswordResponse = GsonFactory.getConfiguredGson()
                            .fromJson(responseBody, ResetPasswordResponse::class.java)
                        try {
                            if (resetPasswordResponse.data == "password updated") {
                                Utils.customToast(
                                    this@ResetPasswordActivity,
                                    resetPasswordResponse.data
                                )
                                val intent =
                                    Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                                intent.putExtra(Constants.flag, Constants.ForgotPassword)
                                startActivity(intent)
                                finish()
                            }
                        } catch (e: Exception) {
                            Utils.customToast(
                                this@ResetPasswordActivity,
                                resources.getString(R.string.error_failure)
                            )
                        }
                    } else {
                        val responseBody = response.errorBody()?.string()
                        GsonFactory.getConfiguredGson()?.fromJson(responseBody, ErrorResponseEnt::class.java)?.let { errorResponseEnt ->
                            Utils.customToast(this@ResetPasswordActivity, errorResponseEnt.error)
                        }
                    }
                } catch (ex: Exception) {
                    Utils.customToast(
                        this@ResetPasswordActivity,
                        resources.getString(R.string.internal_exception_messsage)
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onLoadingFinished()
                t.printStackTrace()
                Log.e("On Failure", ServiceHelper::class.simpleName + " by tag: " + t.toString())
                Utils.customToast(this@ResetPasswordActivity, t.toString())
            }
        })
    }

    private fun verifyPasswordMatch(): Boolean {
        var checkFieldFillOrNot = 0
        when {
            etPassword.text.toString() == etConformPassword.text.toString() -> {
                checkFieldFillOrNot = 1
            }
            else -> {
                Utils.customToast(this@ResetPasswordActivity, "Password Not Matched")
                checkFieldFillOrNot = 0
            }
        }
        if (checkFieldFillOrNot == 0) {
            return false
        }
        return true
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

    override fun onBackPressed() {
        super.onBackPressed() // If not loading, proceed with the default back button action
        if (isLoading) Utils.customToast(this, getString(R.string.message_wait));
    }
}