package com.hotworx.ui.fragments.BrivoFragments

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import com.brivo.sdk.BrivoLog
import com.brivo.sdk.BrivoSDK
import com.brivo.sdk.access.BrivoSDKAccess
import com.brivo.sdk.ble.BrivoBLEErrorCodes
import com.brivo.sdk.enums.AccessPointCommunicationState
import com.brivo.sdk.enums.DoorType
import com.brivo.sdk.interfaces.IOnCommunicateWithAccessPointListener
import com.brivo.sdk.model.AccessPointPath
import com.brivo.sdk.model.BrivoConfiguration
import com.brivo.sdk.model.BrivoError
import com.brivo.sdk.model.BrivoResult
import com.brivo.sdk.onair.model.BrivoOnairPassCredentials
import com.brivo.sdk.onair.model.BrivoSelectedAccessPoint
import com.brivo.sdk.onair.model.BrivoTokens
import com.google.android.material.snackbar.Snackbar
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.databinding.FragmentAccessDoorBinding
import com.hotworx.global.Constants
import com.hotworx.helpers.Utils
import com.hotworx.models.BrivoDataModels.BrivoCredentialDataModel.BrivoCradentialDataModel
import com.hotworx.models.BrivoDataModels.BrivoLocation.Data
import com.hotworx.models.BrivoDataModels.GetAccessPointDataModel
import com.hotworx.models.BrivoRequestModel.BrivoCredentialRequestModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.ui.fragments.BaseFragment
import com.hotworx.ui.fragments.BrivoFragments.Adapters.AccessDoorAdapter
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.IOException

class AccessDoorFragment : BaseFragment() {
    lateinit var binding: FragmentAccessDoorBinding
    lateinit var getAccessPointDataModel: GetAccessPointDataModel
    lateinit var brivoCradentialDataModel: BrivoCradentialDataModel
    lateinit var getSiteDataModel: Data
    lateinit var brivoCredentialRequestModel: BrivoCredentialRequestModel
    private lateinit var accessDoorAdapter: AccessDoorAdapter
    lateinit var getBrivoSelectedAccessPoint: BrivoSelectedAccessPoint
    private lateinit var parts: List<String>
    private var cancellationSignal: CancellationSignal? = null

    //
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAccessDoorBinding.inflate(layoutInflater)
        val args = arguments?.getParcelable<Data>("site_data")

        // hide card view until api dont give data successfully
        binding.cvSiteCard.visibility = View.GONE
        checkForPermissions()
        arePermissionsGranted()
        if (args != null) {
//            callApi(Constants.SITEACCESSPOINTCALLING, args)
            getSiteDataModel = args as Data
            callApi(Constants.GETBRIVOTOKENCALLING)
        }

        setOnClickListener()
        return binding.root
    }

    private fun setData(getSiteName: String) {


    }


    private fun callApi(type: String, data: Any = "", tokenValue: String = "") {
        when (type) {
//            Constants.SITEACCESSPOINTCALLING -> getServiceHelper().enqueueCallExtended(
//                getWebService().getSitesAccessSitesPoints(
//                    ApiHeaderSingleton.apiHeader(requireContext()), data.toString() as String
//                ), Constants.SITEACCESSPOINTCALLING, true
//            )

            Constants.GETBRIVOTOKENCALLING -> getServiceHelper().enqueueCallExtended(
                getWebService().getBrivoToken(
                    ApiHeaderSingleton.apiHeader(requireContext())
                ), Constants.GETBRIVOTOKENCALLING, true
            )

            Constants.UNLOCKBRIVODOORCALLING -> getServiceHelper().enqueueCallExtended(
                getBrivoWebService(parts[1], true).unlock(
                    ApiHeaderSingleton.apiHeaderBrivo(requireContext(), tokenValue),
                    data as BrivoCredentialRequestModel
                ), Constants.UNLOCKBRIVODOORCALLING, true
            )
        }
    }


    override fun onSuccess(liveData: LiveData<String>, tag: String) {
        super.onSuccess(liveData, tag)

        if (Constants.UNLOCKBRIVODOORCALLING == tag) {
            myDockActivity.showSuccessMessage("Door unlocked successfully!")
        }


        if (Constants.GETBRIVOTOKENCALLING == tag) {
            try {
                brivoCradentialDataModel = GsonFactory.getConfiguredGson()
                    .fromJson<BrivoCradentialDataModel>(
                        liveData.value,
                        BrivoCradentialDataModel::class.java
                    )

                setData()

            } catch (e: Exception) {
                Log.d("catchException", e.message.toString())
                Utils.customToast(requireContext(), resources.getString(R.string.error_failure))
            } catch (e: IOException) {
                Log.d("catchException", "IO error: ${e.message}")
                Utils.customToast(requireContext(), "${e.message}")
            } catch (e: IllegalStateException) {
                Log.d("catchException", "State error: ${e.message}")
                Utils.customToast(requireContext(), "${e.message}")
            }

        }
    }


    override fun onFailure(message: String, tag: String) {

        when (tag) {
            Constants.SITEACCESSPOINTCALLING -> {
                myDockActivity.showErrorMessage(message)
                Log.i("xxError", "Error")
            }

            Constants.UNLOCKBRIVODOORCALLING -> {
                myDockActivity.showErrorMessage(message)
                Log.i("xxError", "Error")
            }
        }


    }

    private fun setData() {
        binding.cvSiteCard.visibility = View.VISIBLE
        binding.tvAccessPointName.text = getSiteDataModel.site.siteName

    }


    private fun initBrivoSDK() {
        BrivoSDK.getInstance().init(
            requireContext(), BrivoConfiguration(
                clientId = "15a5bb02-f85b-42d3-96ed-92cb0e64297f",
                clientSecret = "6aceuThRmTElq8Za93b4SWTLlf3pv6gM",
                useSDKStorage = true,
                shouldVerifyDoor = false
            )
        )
    }

    private fun setBrivoSelectedAccessPointModel(): BrivoSelectedAccessPoint {

        try {
            getBrivoSelectedAccessPoint = BrivoSelectedAccessPoint(
                AccessPointPath(
                    accessPointId = getSiteDataModel.site.accessPoints[0].id.toString(),
                    passId = getSiteDataModel.pass,
                    siteId = getSiteDataModel.site.id.toString()
                ),

                DoorType.INTERNET,
                BrivoOnairPassCredentials(
                    getSiteDataModel.accountId.toString(),
                    BrivoTokens(
                        brivoCradentialDataModel.data.access_token,
                        brivoCradentialDataModel.data.refresh_token
                    )
                ),

                getSiteDataModel.site.accessPoints[0].twoFactorEnabled,
                getSiteDataModel.bleAuthTimeFrame,
                getSiteDataModel.site.accessPoints[0].bluetoothReader.readerUid,
                getSiteDataModel.bleCredential,
                "",
                getSiteDataModel.site.hasTrustedNetwork,
            )
        } catch (e: Exception) {
            throw IllegalStateException("Failed to set BrivoSelectedAccessPoint", e)
        }
        return getBrivoSelectedAccessPoint
    }


    private fun calUnlockAccessPoint(getBrivoSelectedAccessPoint: BrivoSelectedAccessPoint) {
        cancellationSignal = CancellationSignal()
        BrivoSDKAccess.getInstance().unlockAccessPoint(
            getBrivoSelectedAccessPoint,
            cancellationSignal,
            object : IOnCommunicateWithAccessPointListener {
                override fun onResult(result: BrivoResult) {
                    onUnlockAccessPointResult(result)
                }
            }

        )

    }


    private fun onUnlockAccessPointResult(result: BrivoResult) {
        when (result.communicationState) {
            AccessPointCommunicationState.SUCCESS -> {
                activity?.runOnUiThread {
                    showUnlockResultSnackbar(true)
                    prepareUIForResult(cancellationSignal)
                }
            }

            AccessPointCommunicationState.FAILED -> {
                result.error?.let {
                    onUnlockFailed(it, cancellationSignal)
                }
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


    private fun showUnlockResultSnackbar(isUnlockSuccessful: Boolean) {
        myDockActivity.onLoadingFinished()
        val getResponse =
            getString(if (isUnlockSuccessful) R.string.brivo_sample_door_unlocked else R.string.brivo_sample_failed_to_unlock_door)
        if (isUnlockSuccessful) myDockActivity.showSuccessMessage(getResponse)
        else myDockActivity.showErrorMessage(getResponse)
    }

    private fun prepareUIForResult(cancellationSignal: CancellationSignal?) {
        cancellationSignal?.cancel()
    }

    private fun onUnlockFailed(error: BrivoError, cancellationSignal: CancellationSignal?) {
        activity?.runOnUiThread {
            myDockActivity.onLoadingFinished()
            when (error.code) {
                BrivoBLEErrorCodes.BLE_DISABLED_ON_DEVICE -> myDockActivity.showErrorMessage(
                    getString(R.string.brivo_sample_please_enable_ble_to_continue)
                )

                BrivoBLEErrorCodes.BLE_FAILED_TRANSMISSION,
                BrivoBLEErrorCodes.BLE_ACCESS_DENIED,
                BrivoBLEErrorCodes.BLE_CONNECTION_MANAGER_FAILED_TO_INITIALIZE,
                BrivoBLEErrorCodes.BLE_UNKNOWN_ERROR -> {
                    myDockActivity.showErrorMessage(getString(R.string.brivo_sample_failed_to_unlock_door))
                    showUnlockResultSnackbar(false)
                }

                BrivoBLEErrorCodes.BLE_AUTHENTICATION_TIMED_OUT -> {
                    myDockActivity.showErrorMessage(getString(R.string.brivo_sample_failed_to_unlock_door_timeout))
                    showUnlockResultSnackbar(false)
                }

                else -> {

                    Utils.customToast(
                        requireContext(),
                        "Error: " + error.code + " " + error.message
                    )
                }
            }
            prepareUIForResult(cancellationSignal)
        }
    }

    private fun checkForPermissions() {
        Dexter.withContext(requireContext())
            .withPermission(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Manifest.permission.ACCESS_BACKGROUND_LOCATION else Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Log.d( "onPermissionsChecked: ",response.requestedPermission.toString())
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {}

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.permission_dialog_title))
                        .setMessage(getString(R.string.permission_dialog_message))
                        .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                            navigateToPermissionSettings()
                        }
                        .setNegativeButton(
                            android.R.string.cancel
                        ) { _: DialogInterface?, _: Int ->
                            Snackbar.make(
                                binding.root,
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

    //    android.permission.BLUETOOTH_SCAN
//    android.permission.BLUETOOTH_CONNECT
    @RequiresApi(Build.VERSION_CODES.S)
    fun arePermissionsGranted() {
        Dexter.withActivity(requireActivity()) // Assuming you're inside an Activity context
            .withPermissions(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    report.deniedPermissionResponses.forEach {
                        Log.d( "onPermissionsChecked: ",it.permissionName)
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.permission_dialog_title_bluetooth))
                        .setMessage(getString(R.string.permission_dialog_message_bluetooth))
                        .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                            navigateToPermissionSettings()
                        }
                        .setNegativeButton(
                            android.R.string.cancel
                        ) { _: DialogInterface?, _: Int ->
                            Snackbar.make(
                                binding.root,
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


            })
            .check()

    }

    private fun setOnClickListener() {
        binding.let {
            it.acbUnlock.setOnClickListener {
                myDockActivity.onLoadingStarted()
                initBrivoSDK()
                calUnlockAccessPoint(setBrivoSelectedAccessPointModel())
            }
        }
    }

    private fun navigateToPermissionSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}