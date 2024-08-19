package com.hotworx.ui.fragments

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.activities.MainActivity
import com.hotworx.global.WebServiceConstants
import com.hotworx.helpers.ServiceHelper
import com.hotworx.helpers.UIHelper
import com.hotworx.interfaces.ApiListener
import com.hotworx.interfaces.LoadingListener
import com.hotworx.interfaces.webServiceResponseLisener
import com.hotworx.retrofit.WebService
import com.hotworx.retrofit.WebServiceFactory
import com.hotworx.ui.views.TitleBar

abstract class BaseBottomsheetFragment : BottomSheetDialogFragment(), webServiceResponseLisener, ApiListener {
    protected var handler: Handler = Handler()
    protected var webService: WebService? = null
    protected var serviceHelper: ServiceHelper? = null
    protected open var dockActivity: DockActivity? = null
    private var isLoading = false

    override fun onStarted() {
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {
    }

    override fun onFailure(message: String, tag: String) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(
                dockActivity,
                WebServiceConstants.BASE_URL
            )
        }

        if (serviceHelper == null) {
            serviceHelper = ServiceHelper(this, this, dockActivity, webService)
        }

        dockActivity = dockActivity
    }

    open fun getWebServices(): WebService? {
        return WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(
            dockActivity,
            WebServiceConstants.BASE_URL
        )
    }

    open fun getServiceHelpers(): ServiceHelper? {
        return ServiceHelper(this, this, dockActivity, getWebServices())
    }

    override fun onResume() {
        super.onResume()
        if (dockActivity!!.drawerLayout != null) {
            dockActivity!!.releaseDrawer()
        }
    }

    fun fragmentResume() {
        setTitleBar((dockActivity as MainActivity?)!!.titleBar)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
    }

    override fun onPause() {
        super.onPause()
        if (dockActivity!!.window != null) if (dockActivity!!.window.decorView != null) UIHelper.hideSoftKeyboard(
            dockActivity, dockActivity!!
                .getWindow().decorView
        )
    }

    protected fun loadingStarted() {
        if (parentFragment != null) (parentFragment as LoadingListener?)!!.onLoadingStarted()
        else dockActivity!!.onLoadingStarted()

        isLoading = true
    }

    protected fun loadingFinished() {
        if (parentFragment != null) (parentFragment as LoadingListener?)!!.onLoadingFinished()
        else if (dockActivity != null) dockActivity!!.onLoadingFinished()

        isLoading = false
        // else
        // ( (LoadingListener) super.getParentFragment() ).onLoadingFinished();
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dockActivity = context as DockActivity
    }


    override fun ResponseSuccess(result: String, Tag: String) {
    }

    override fun ResponseFailure(message: String, tag: String) {
    }


    override fun ResponseNoInternet(tag: String) {
    }

    /**
     * This is called in the end to modify titlebar. after all changes.
     *
     * @param
     */
    fun setTitleBar(titleBar: TitleBar) {
        titleBar.showTitleBar()
        titleBar.hideSyncBtn()
        titleBar.hideNotificationBtn()
        titleBar.hideBrivoBtn()

        // titleBar.refreshListener();
    }


    /**
     * Trigger when receives broadcasts from device to check wifi connectivity
     * using connectivity manager
     *
     *
     * Usage : registerBroadcastReceiver() on resume of activity to receive
     * notifications where needed and unregisterBroadcastReceiver() when not
     * needed.
     *
     * @return The connectivity of wifi/mobile carrier connectivity.
     */
    protected var mConnectionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var isWifiConnected = false
            var isMobileConnected = false

            val connMgr = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            var networkInfo = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)

            if (networkInfo != null) isWifiConnected = networkInfo.isConnected

            networkInfo = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

            if (networkInfo != null) isMobileConnected = networkInfo.isConnected

            Log.d(
                "NETWORK STATUS", "wifi==" + isWifiConnected + " & mobile=="
                        + isMobileConnected
            )
        }
    }


    protected fun finishLoading() {
        requireActivity().runOnUiThread { loadingFinished() }
    }

    fun checkLoading(): Boolean {
        if (isLoading) {
            UIHelper.showLongToastInCenter(
                activity,
                R.string.message_wait
            )
            return false
        } else {
            return true
        }
    }

    protected val isNotLoading: Boolean
        get() = !isLoading

    fun onBackPressed() {
    }
}
