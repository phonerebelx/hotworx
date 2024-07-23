package com.hotworx.activities.BrivoActivity

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import com.brivo.sdk.BrivoLog
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner
import com.brivo.sdk.BrivoSDK
import com.brivo.sdk.BrivoSDKInitializationException
import com.brivo.sdk.access.BrivoSDKAccess
import com.brivo.sdk.ble.BrivoBLEErrorCodes
import com.brivo.sdk.ble.BrivoBLEErrors
import com.brivo.sdk.enums.AccessPointCommunicationState
import com.brivo.sdk.interfaces.IOnCommunicateWithAccessPointListener
import com.brivo.sdk.localauthentication.BrivoSDKLocalAuthentication
import com.brivo.sdk.model.BrivoConfiguration
import com.brivo.sdk.model.BrivoError
import com.brivo.sdk.model.BrivoResult
import com.brivo.sdk.onair.interfaces.IOnRedeemPassListener
import com.brivo.sdk.onair.interfaces.IOnRetrieveSDKLocallyStoredPassesListener
import com.brivo.sdk.onair.model.BrivoAccessPoint
import com.brivo.sdk.onair.model.BrivoOnairPass
import com.brivo.sdk.onair.model.BrivoOnairPassCredentials
import com.brivo.sdk.onair.model.BrivoSite
import com.brivo.sdk.onair.model.BrivoTokens
import com.brivo.sdk.onair.repository.BrivoSDKOnair
import com.demo.sample.kotlin.BrivoSampleConstants
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.activities.DockActivity
import com.hotworx.activities.MainActivity
import com.hotworx.databinding.ActivitySitesBinding
import com.hotworx.global.Constants
import com.hotworx.global.WebServiceConstants
import com.hotworx.helpers.ServiceHelper
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.ApiListener
import com.hotworx.interfaces.webServiceResponseLisener
import com.hotworx.micsAdapter.SearchSpinnerAdapter
import com.hotworx.models.BrivoDataModels.BrivoCredentialDataModel.BrivoCradentialDataModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.retrofit.WebService
import com.hotworx.retrofit.WebServiceFactory
import com.hotworx.ui.adapters.BrivoAdapter.SitesAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener
import pl.bclogic.pulsator4droid.library.PulsatorLayout
import java.io.IOException
import java.util.Timer
import java.util.TimerTask

class SitesActivity : DockActivity(), ApiListener, webServiceResponseLisener {
    private lateinit var sites: ExpandableListView
    private lateinit var tvMagicDoor: CardView
    private lateinit var etLocation: AppCompatEditText
    private lateinit var ivSettings: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var SearchableSpinner: SearchableSpinner
    lateinit var brivoCradentialDataModel: BrivoCradentialDataModel
    private var loading = false
    private var isFilterOn = false
    private lateinit var onairPasses: LinkedHashMap<String, BrivoOnairPass>
    private var allSitesData: LinkedHashMap<String, BrivoOnairPass> = LinkedHashMap<String, BrivoOnairPass>()
    private lateinit var sitesAdapter: SitesAdapter
    private lateinit var dialog: Dialog
    var SitesValue = ""
    private var timer: Timer? = Timer()
    private val TIMEOUT: Long = 30000
    private var isUnlocking = false
    private var isMagicDoor = false
    private lateinit var brivoAccessPoint: BrivoAccessPoint
    private lateinit var passId: String
    private var cancellationSignal: CancellationSignal? = null

    private lateinit var btnUnlock: Button
    private lateinit var ivLock: ImageView
    private lateinit var tvMessage: TextView
    private lateinit var pulsator: PulsatorLayout
    private lateinit var requestMultiplePermissions: ActivityResultLauncher<Array<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBrivoSDK()
        setupBinding()
        setupDialog()
        initializeUI()
        requestPermissions()
        callApi(Constants.GETBRIVOTOKENCALLING)
    }


    fun getWebService(): WebService? {
        return WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(
            this@SitesActivity,
            WebServiceConstants.BASE_URL
        )
    }

    fun getServiceHelper(): ServiceHelper? {
        return ServiceHelper(this, this, this, getWebService())
    }

    private fun initBrivoSDK() {
        BrivoSDK.getInstance().init(
            this@SitesActivity, BrivoConfiguration(
                clientId = "15a5bb02-f85b-42d3-96ed-92cb0e64297f",
                clientSecret = "6aceuThRmTElq8Za93b4SWTLlf3pv6gM",
                useSDKStorage = true,
                shouldVerifyDoor = false
            )
        )
    }


    private fun callApi(type: String, data: Any = "", tokenValue: String = "") {
        onLoadingStarted()
        when (type) {

            Constants.GETBRIVOTOKENCALLING ->

                getServiceHelper()?.enqueueCallExtended(
                    getWebService()?.getBrivoToken(
                        ApiHeaderSingleton.apiHeader(this@SitesActivity)
                    ), Constants.GETBRIVOTOKENCALLING, true
                )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBinding() {
        val binding = DataBindingUtil.setContentView<ActivitySitesBinding>(
            this, R.layout.activity_sites
        )
        SearchableSpinner = binding.SearchableSpinner
        progressBar = binding.progressBar
        binding.executePendingBindings()
        binding.lifecycleOwner = this
        sites = binding.sites
//        tvMagicDoor = binding.toolbar.findViewById(R.id.cvNearby)
        ivSettings = binding.ivSettings
        toolbar = binding.toolbar

        btnUnlock = binding.btnUnlock
        ivLock = binding.ivLock
        tvMessage = binding.tvMessage
        pulsator = binding.pulsator


        findViewById<View>(android.R.id.content).setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val location = IntArray(2)
                SearchableSpinner.getLocationOnScreen(location)
                val spinnerX = location[0]
                val spinnerY = location[1]
                val spinnerWidth = SearchableSpinner.width
                val spinnerHeight = SearchableSpinner.height

                // Check if the touch is outside the bounds of the spinner
                if (event.rawX < spinnerX || event.rawX > spinnerX + spinnerWidth ||
                    event.rawY < spinnerY || event.rawY > spinnerY + spinnerHeight
                ) {
                    // Collapse or close the spinner
                    SearchableSpinner.hideEdit()
                }
            }
            // Return false to allow other touch events to be handled
            false
        }
    }




    private fun setupDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.progress)
        dialog = builder.create()
    }


    private fun initializeUI() {
        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }



//        sites.setOnChildClickListener { _, view, groupPosition, childPosition, _ ->
//            startAccessPointsActivity(view.context, groupPosition, childPosition)
//            false
//        }
        btnUnlock.setOnClickListener { unlockDoor() }
        ivLock.setOnClickListener { unlockDoor() }



        val intent = intent
        isMagicDoor = intent.getBooleanExtra(BrivoSampleConstants.IS_MAGIC_DOOR, true)

        try {
            BrivoSDKLocalAuthentication.getInstance().init(
                applicationContext,
                getString(R.string.two_factor_dialog_title),
                getString(R.string.two_factor_dialog_message),
                getString(R.string.two_factor_dialog_cancel),
                ""
            )
        } catch (e: BrivoSDKInitializationException) {
            e.printStackTrace()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (cancellationSignal?.isCanceled == false) {
                    cancellationSignal?.cancel()
                }
                finish()
            }
        })

//        tvMagicDoor.setOnClickListener {
//            val intent = Intent(this@SitesActivity, UnlockAccessPointActivity::class.java)
//            intent.putExtra(BrivoSampleConstants.IS_MAGIC_DOOR, true)
//            startActivity(intent)
//        }

    }




    private fun startAccessPointsActivity(
        context: Context, groupPosition: Int, siteItem: BrivoSite
    ) {
        val passId = java.util.ArrayList(onairPasses.keys)[groupPosition]
        var item = siteItem

        onairPasses[passId]?.sites!!.forEach {}
        val selectedSite = Gson().toJson(item)

        val intent = Intent(context, AccessPointsActivity::class.java)
        intent.putExtra(BrivoSampleConstants.SELECTED_SITE, selectedSite)
        intent.putExtra(BrivoSampleConstants.PASS_ID, passId)

        context.startActivity(intent)
    }


    private fun refreshPasses(isLoaderRunSuccessfully: Boolean = false) {

        for (pass in onairPasses.values) {
            try {
                if (!isLoaderRunSuccessfully) {
                    onLoadingStarted()
                }

                BrivoSDKOnair.instance?.refreshPass(pass.brivoOnairPassCredentials.tokens,
                    object : IOnRedeemPassListener {
                        override fun onSuccess(pass: BrivoOnairPass?) {
                            refreshPassesListUI()
                        }

                        override fun onFailed(error: BrivoError) {
                            onLoadingFinished()
                            Toast.makeText(
                                this@SitesActivity,
                                "Error " + error.message + " " + error.code,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            } catch (e: BrivoSDKInitializationException) {
                e.printStackTrace()
            }
        }
    }




    private fun refreshPassesListUI() {
        runOnUiThread {
            try {

                BrivoSDKOnair.instance?.retrieveSDKLocallyStoredPasses(object :
                    IOnRetrieveSDKLocallyStoredPassesListener {
                    override fun onSuccess(passes: LinkedHashMap<String, BrivoOnairPass>?) {
                        onLoadingFinished()
                        onairPasses = passes ?: LinkedHashMap<String, BrivoOnairPass>()
//                        tvMagicDoor.visibility = if (passes?.isEmpty() == true) View.GONE else View.VISIBLE
                        if (::sitesAdapter.isInitialized.not()) {
                            onairPasses.keys.forEach { key ->
                                val sites = onairPasses[key]?.sites ?: ArrayList()
                                setSpinner(sites)
                            }

                        } else {
                            sitesAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailed(error: BrivoError) {
                        onLoadingFinished()
                        Log.d( "onLoadingFinished(): ", "I called")
                        Toast.makeText(this@SitesActivity, error.message, Toast.LENGTH_LONG).show()
                    }
                })
            } catch (e: BrivoSDKInitializationException) {
                onLoadingFinished()
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requestPermissions()
        if (timer == null) {
            timer = Timer()
            triggerRefreshPasses()
        }
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                triggerRefreshPasses()
            }

        }, 0, 15000)
    }


    private fun requestPermissions() {
        // Initialize requestMultiplePermissions if it's not initialized
        if (!::requestMultiplePermissions.isInitialized) {
            requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    Log.i("permission", "${it.key} = ${it.value}")
                }
            }
        }

        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        requestMultiplePermissions.launch(permissions)
    }

    private fun setSpinner(brivoSiteArray: ArrayList<BrivoSite>){

        val brivoSiteList = brivoSiteArray.map { it.siteName } as ArrayList<String>


        val adapter =   SearchSpinnerAdapter(this, brivoSiteList)
        SearchSpinnerAdapter(this, brivoSiteList)
        SearchableSpinner.setAdapter(adapter)

        // Assuming `SearchableSpinner` is an instance of your custom spinner
        SearchableSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(view: View?, position: Int, id: Long) {
                val selectedItemName = brivoSiteArray.find { it.siteName == SearchableSpinner.selectedItem }
                selectedItemName?.let {
                    startAccessPointsActivity(this@SitesActivity, 0, it)
                }
            }

            override fun onNothingSelected() {

            }
        })



    }

    private fun triggerRefreshPasses() {
        try {

            BrivoSDKOnair.instance?.retrieveSDKLocallyStoredPasses(object :
                IOnRetrieveSDKLocallyStoredPassesListener {
                override fun onSuccess(passes: LinkedHashMap<String, BrivoOnairPass>?) {

                    runOnUiThread {
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }

                        onairPasses = passes ?: LinkedHashMap<String, BrivoOnairPass>()
                        refreshPasses(true)
                    }
                }

                override fun onFailed(error: BrivoError) {
                    Log.d("triggerRefreshPasses: ", error.message.toString())
                    runOnUiThread {
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                        Toast.makeText(this@SitesActivity, error.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
        } catch (e: BrivoSDKInitializationException) {

            e.printStackTrace()
        }
    }


    override fun onPause() {
        super.onPause()
        timer?.cancel()
        timer = null
    }

    override fun onLoadingStarted() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE)
        }
        loading = true
    }

    override fun onLoadingFinished() {
        if (progressBar != null) {
            progressBar.visibility = View.INVISIBLE
        }

        loading = false
    }

    override fun onProgressUpdated(percentLoaded: Int) {
        TODO("Not yet implemented")
    }

    override fun getDockFrameLayoutId(): Int {
        TODO("Not yet implemented")
    }

    override fun onStarted() {
        Log.d("onStarted", "onStarted: ")
    }

    override fun onSuccess(liveData: LiveData<String>, tag: String) {


        if (Constants.GETBRIVOTOKENCALLING == tag) {
            try {
                onairPasses = LinkedHashMap<String, BrivoOnairPass>()
                brivoCradentialDataModel = GsonFactory.getConfiguredGson()
                    .fromJson<BrivoCradentialDataModel>(
                        liveData.value,
                        BrivoCradentialDataModel::class.java
                    )

                val brivoTokens = BrivoTokens(
                    brivoCradentialDataModel.data.access_token,
                    brivoCradentialDataModel.data.refresh_token
                )
                val brivoOnairPassCredentials = BrivoOnairPassCredentials("userId", brivoTokens)

                val brivoOnairPass = BrivoOnairPass()
                brivoOnairPass.brivoOnairPassCredentials = brivoOnairPassCredentials

                onairPasses[""] = brivoOnairPass

                refreshPasses(false)

            } catch (e: Exception) {
                Log.d("catchException", e.message.toString())
                Utils.customToast(this, resources.getString(R.string.error_failure))
            } catch (e: IOException) {
                Log.d("catchException", "IO error: ${e.message}")
                Utils.customToast(this, "${e.message}")
            } catch (e: IllegalStateException) {
                Log.d("catchException", "State error: ${e.message}")
                Utils.customToast(this, "${e.message}")
            }

        }
    }


    override fun onFailure(message: String, tag: String) {
        Log.d("onFailure", "onFailure: ")
    }

    override fun onFailureWithResponseCode(code: Int, message: String, tag: String) {
        Log.d("onFailureWithResponseCode", "onFailureWithResponseCode: ")
    }

    override fun ResponseSuccess(result: String?, Tag: String?) {
        Log.d("ResponseSuccess", "ResponseSuccess: ")
    }

    override fun ResponseFailure(message: String?, tag: String?) {
        Log.d("ResponseFailure", "ResponseFailure: ")
    }

    override fun ResponseNoInternet(tag: String?) {

    }


    override fun onBackPressed() {
//        super.onBackPressed()
        if (loading) Utils.customToast(
            this,
            getString(R.string.message_wait)
        ) else {
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()

        }
    }




    private fun unlockDoor() {
        if (isUnlocking) {
            return
        }
        isUnlocking = true
        ivLock.setImageResource(R.drawable.hotworx_icons)
        togglePulseAnimation(true)
        cancellationSignal = CancellationSignal()
        waitForSlowConnection(cancellationSignal)
        tvMessage.text = ""

        try {
            if (isMagicDoor) {
                BrivoSDKAccess.getInstance().unlockNearestBLEAccessPoint(
                    cancellationSignal,
                    object : IOnCommunicateWithAccessPointListener {
                        override fun onResult(result: BrivoResult) {
                            onUnlockAccessPointResult(result)
                        }
                    })

            } else {
                brivoAccessPoint.id?.let { accessPointId ->
                    BrivoSDKAccess.getInstance().unlockAccessPoint(passId,
                        accessPointId,
                        cancellationSignal,
                        object : IOnCommunicateWithAccessPointListener {
                            override fun onResult(result: BrivoResult) {
                                onUnlockAccessPointResult(result)
                            }
                        })
                } ?: run {
                    Toast.makeText(
                        this@SitesActivity,
                        "Error: missing accessPointId",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: BrivoSDKInitializationException) {
            e.printStackTrace()
        }
    }

    private fun onUnlockAccessPointResult(result: BrivoResult) {
        when (result.communicationState) {
            AccessPointCommunicationState.SUCCESS -> {
                runOnUiThread {
                    showUnlockResultSnackbar(true)
                    prepareUIForResult(cancellationSignal)
                }
            }

            AccessPointCommunicationState.FAILED -> {
                result.error?.let { onUnlockFailed(it, cancellationSignal) }
            }

            AccessPointCommunicationState.SHOULD_CONTINUE -> {
                result.shouldContinueListener?.onShouldContinue(true)
            }
            AccessPointCommunicationState.SCANNING -> BrivoLog.i("unlockAccessPoint -> scanning")
            AccessPointCommunicationState.AUTHENTICATE -> BrivoLog.i("unlockAccessPoint -> authenticate")
            AccessPointCommunicationState.CONNECTING -> BrivoLog.i("unlockAccessPoint -> connecting")
            AccessPointCommunicationState.COMMUNICATING -> BrivoLog.i("unlockAccessPoint -> communicating")
        }
    }

    private fun prepareUIForResult(cancellationSignal: CancellationSignal?) {
        isUnlocking = false
        cancellationSignal?.cancel()
        togglePulseAnimation(false)
    }



    private fun checkForPermissions() {
        Dexter.withContext(this)
            .withPermission(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Manifest.permission.ACCESS_BACKGROUND_LOCATION else Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {}

                override fun onPermissionDenied(response: PermissionDeniedResponse) {}

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    AlertDialog.Builder(this@SitesActivity)
                        .setTitle(getString(R.string.permission_dialog_title))
                        .setMessage(getString(R.string.permission_dialog_message))
                        .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                            navigateToPermissionSettings()
                        }
                        .setNegativeButton(
                            android.R.string.cancel
                        ) { _: DialogInterface?, _: Int ->
                            Snackbar.make(
                                findViewById(R.id.SiteActivity),
                                getString(R.string.permission_dialog_reason),
                                Snackbar.LENGTH_LONG
                            )
                                .setAction(android.R.string.ok) {
                                    navigateToPermissionSettings()
                                }
                                .show()
                        }
                        .show()
                }
            }).check()
    }

    private fun showUnlockResultSnackbar(isUnlockSuccessful: Boolean) {
        val snackBar: Snackbar = Snackbar.make(
            window.decorView,
            getString(if (isUnlockSuccessful) R.string.brivo_sample_door_unlocked else R.string.brivo_sample_door_unlocked_failed),
            Snackbar.LENGTH_SHORT
        )
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            if (isUnlockSuccessful) ContextCompat.getColor(
                this,
                android.R.color.holo_green_dark
            ) else ContextCompat.getColor(
                this, android.R.color.holo_red_dark
            )
        )
        snackBar.show()
//        ivLock.setImageResource(if (isUnlockSuccessful) R.drawable.lock_open else R.drawable.lock)
    }

    private fun waitForSlowConnection(cancellationSignal: CancellationSignal?) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            onUnlockFailed(
                BrivoBLEErrors.getBleAuthenticationTimedOut(),
                cancellationSignal
            )
        }, TIMEOUT)
    }

    private fun togglePulseAnimation(started: Boolean) {
        pulsator.start()
        pulsator.animate()
            ?.alpha(if (started) 1.0f else 0.0f)
            ?.setDuration(300)
            ?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    pulsator.visibility = if (started) View.VISIBLE else View.GONE
                }
            })
    }

    private fun onUnlockFailed(error: BrivoError, cancellationSignal: CancellationSignal?) {
        runOnUiThread {
            when (error.code) {
                BrivoBLEErrorCodes.BLE_DISABLED_ON_DEVICE -> tvMessage.text =
                    getString(R.string.brivo_sample_please_enable_ble_to_continue)
                BrivoBLEErrorCodes.BLE_FAILED_TRANSMISSION,
                BrivoBLEErrorCodes.BLE_ACCESS_DENIED,
                BrivoBLEErrorCodes.BLE_CONNECTION_MANAGER_FAILED_TO_INITIALIZE,
                BrivoBLEErrorCodes.BLE_UNKNOWN_ERROR -> {
                    tvMessage.text = getString(R.string.brivo_sample_failed_to_unlock_door)
                    showUnlockResultSnackbar(false)
                }
                BrivoBLEErrorCodes.BLE_AUTHENTICATION_TIMED_OUT -> {
                    tvMessage.text = getString(R.string.brivo_sample_failed_to_unlock_door_timeout)
                    showUnlockResultSnackbar(false)
                }
                else -> {
                    Toast.makeText(
                        this@SitesActivity,
                        "Error: " + error.code + " " + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            prepareUIForResult(cancellationSignal)
        }
    }

    private fun navigateToPermissionSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}