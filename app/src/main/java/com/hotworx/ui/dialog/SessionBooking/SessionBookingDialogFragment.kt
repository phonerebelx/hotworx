package com.hotworx.ui.dialog.SessionBooking

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.hotworx.R
import com.hotworx.Singletons.ApiHeaderSingleton
import com.hotworx.activities.LoginActivity
import com.hotworx.activities.MainActivity
import com.hotworx.global.WebServiceConstants
import com.hotworx.helpers.ServiceHelper
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.BookingConfirmationDialogClickListener
import com.hotworx.interfaces.GetStringOnClickListener
import com.hotworx.interfaces.LoadingListener
import com.hotworx.models.ErrorResponseEnt
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.GetBookSessionDataModel
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.PostBookSessionDataModel
import com.hotworx.retrofit.GsonFactory
import com.hotworx.retrofit.WebService
import com.hotworx.retrofit.WebServiceFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionBookingDialogFragment(val clickListener: BookingConfirmationDialogClickListener): DialogFragment(), LoadingListener {
    lateinit var btnCancel: AppCompatButton
    lateinit var btnOk: AppCompatButton
    lateinit var postBookSessionDataModel: PostBookSessionDataModel
    var isApiSuccess: Boolean = false
    private var mActivity: Activity? = null
    protected var webService: WebService? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_session_booking_dialog, container, false)
        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(
                requireContext(),
                WebServiceConstants.BASE_URL
            )
        }


        btnCancel = root.findViewById(R.id.btnCancel)
        btnOk = root.findViewById(R.id.btnOk)
        setOnClickListener()
        return root
    }


    private fun setOnClickListener() {
        btnCancel.setOnClickListener {
            dismiss() // Dismiss the dialog when btnCancel is clicked
        }
        btnOk.setOnClickListener {
            clickListener.onConfirmBooking()
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.setCancelable(false)
        dialog?.window?.setGravity(Gravity.CENTER)
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onLoadingStarted() {

    }

    override fun onLoadingFinished() {

    }

    override fun onProgressUpdated(percentLoaded: Int) {

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Save the activity context when fragment attaches
        mActivity = requireActivity()
    }

    override fun onDetach() {
        super.onDetach()
        // Release the stored activity context when fragment detaches
        mActivity = null
    }
}
