package com.hotworx.ui

import android.R
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.hotworx.activities.DockActivity
import com.hotworx.activities.LoginActivity
import com.hotworx.activities.MainActivity
import com.hotworx.global.WebServiceConstants
import com.hotworx.helpers.BasePreferenceHelper
import com.hotworx.helpers.ServiceHelper
import com.hotworx.helpers.UIHelper
import com.hotworx.interfaces.ApiListener
import com.hotworx.interfaces.LoadingListener
import com.hotworx.interfaces.OnBackPressed
import com.hotworx.interfaces.webServiceResponseLisener
import com.hotworx.retrofit.WebService
import com.hotworx.retrofit.WebServiceFactory
import com.hotworx.room.RoomBuilder
import com.hotworx.ui.views.TitleBar

abstract class BaseActivity : AppCompatActivity(), webServiceResponseLisener, OnBackPressed, ApiListener {
    protected var handler: Handler = Handler()
    protected var prefHelper: BasePreferenceHelper? = null
    protected var webService: WebService? = null
    protected var serviceHelperr: ServiceHelper? = null
    protected var dockActivityy: DockActivity? = null
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefHelper = BasePreferenceHelper(applicationContext)

        // Ensure DockActivity is properly initialized
        dockActivityy?.let { dock ->
            dock.getDrawerLayout()?.let {
                dock.lockDrawer()
            }
        }

        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(
                this,
                WebServiceConstants.BASE_URL
            )
        }

        if (serviceHelperr == null) {
            serviceHelperr = ServiceHelper(this, this, dockActivityy, webService)
        }
    }

    fun setDockActivity(dockActivity: DockActivity) {
        this.dockActivityy = dockActivity
    }

    protected fun getDockActivity(): DockActivity? {
        return dockActivityy
    }

    override fun onStarted() {}

    override fun onSuccess(liveData: LiveData<String>, tag: String) {}

    override fun onFailure(message: String, tag: String) {}

    override fun onFailureWithResponseCode(code: Int, message: String, tag: String) {
        if (code == 551) {
            val alertDialog = UIHelper.createAlertDialog(dockActivityy, { dialogInterface, i ->
                prefHelper!!.setLoginStatus(false)
                prefHelper!!.putLoginData(null)
                prefHelper!!.putLoginToken(null)
                prefHelper!!.removeActiveSession()
                RoomBuilder.getHotWorxDatabase(applicationContext)
                    .clearAllTables() //.getSessionTypeDao().deleteAllSessions();
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }, message, "Ok")
            alertDialog.show()
        } else if (code == 552) {
            val alertDialog = UIHelper.createAlertDialog(dockActivityy, { dialogInterface, i ->
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.hotworx"))
                )
            }, message, "Update")
            alertDialog.show()
        }
    }

    // Renamed function to avoid conflict
    fun createWebService(): WebService {
        return WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(
            dockActivityy,
            WebServiceConstants.BASE_URL
        )
    }

    fun getBrivoWebService(get_access_point_id: String, isBrivoApi: Boolean?): WebService {
        val baseUrl = "https://api.brivo.com/v1/api/access-points/$get_access_point_id/"
        return WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(
            dockActivityy,
            baseUrl,
            isBrivoApi
        )
    }

    val customWebService: WebService
        get() = WebServiceFactory.getUnSafeOkHttp(dockActivityy, WebServiceConstants.CUSTOM_BASE_URL)

    fun getServiceHelper(): ServiceHelper {
        return ServiceHelper(this, this, dockActivityy, createWebService())
    }

    val customServiceHelper: ServiceHelper
        get() = ServiceHelper(this, this, dockActivityy, customWebService)

    val baseTargetActivity: Activity?
        get() = dockActivityy

    override fun onResume() {
        super.onResume()
        dockActivityy?.drawerLayout?.let {
            dockActivityy?.releaseDrawer()
        }
    }

    fun activityResume() {
        setTitleBar((dockActivityy as? MainActivity)?.titleBar ?: return)
    }

    override fun onPause() {
        super.onPause()
        dockActivityy?.window?.decorView?.let {
            UIHelper.hideSoftKeyboard(dockActivityy, it)
        }
    }

    protected fun loadingStarted() {
        if (applicationContext is LoadingListener) {
            (applicationContext as LoadingListener).onLoadingStarted()
        } else {
            dockActivityy?.onLoadingStarted()
        }
        isLoading = true
    }

    protected fun checkIsLoadingStart() {
        isLoading = true
    }

    protected fun checkIsLoadingEnd() {
        isLoading = false
    }

    protected fun loadingFinished() {
        if (applicationContext is LoadingListener) {
            (applicationContext as LoadingListener).onLoadingFinished()
        } else {
            dockActivityy?.onLoadingFinished()
        }
        isLoading = false
    }

    override fun ResponseSuccess(result: String, tag: String) {}

    override fun ResponseFailure(message: String, tag: String) {}

    override fun ResponseNoInternet(tag: String) {}

    open fun setTitleBar(titleBar: TitleBar) {
        titleBar.showTitleBar()
        titleBar.hideSyncBtn()
        titleBar.hideNotificationBtn()
        titleBar.hideBrivoBtn()
    }

    protected val listPreferredItemHeight: Int
        get() {
            val typedValue = TypedValue()
            applicationContext.theme.resolveAttribute(R.attr.listPreferredItemHeight, typedValue, true)
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            return typedValue.getDimension(metrics).toInt()
        }

    protected fun notImplemented() {
        UIHelper.showLongToastInCenter(applicationContext, "Coming Soon")
    }

    protected fun serverNotFound() {
        UIHelper.showLongToastInCenter(applicationContext, "Unable to connect to the server, are you connected to the internet?")
    }

    protected var mConnectionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifiConnected = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.isConnected == true
            val mobileConnected = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)?.isConnected == true
            Log.d("NETWORK STATUS", "wifi== $wifiConnected & mobile== $mobileConnected")
        }
    }

    protected fun finishLoading() {
        runOnUiThread { loadingFinished() }
    }

    fun checkLoading(): Boolean {
        return if (isLoading) {
            UIHelper.showLongToastInCenter(applicationContext, com.hotworx.R.string.message_wait)
            false
        } else {
            true
        }
    }

    protected val isNotLoading: Boolean
        get() = !isLoading

//    override fun onBackPressed() {
//        super.onBackPressed()
//        // Optional custom behavior
//    }
}
