package com.hotworx.ui.dialog.SessionBooking

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.global.Constants
import com.hotworx.helpers.UIHelper
import com.hotworx.helpers.Utils
import com.hotworx.interfaces.LoadingListener
import com.hotworx.interfaces.OnClickTypeListener
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.GetShowSlotDataModelItem
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.ShowAppointmentDataModel
import com.hotworx.ui.fragments.HomeFragment
import java.text.SimpleDateFormat
import java.util.*


class SessionViewAppointmentDialogFragment() : DialogFragment(), LoadingListener {

    lateinit var showAppointmentDataModel: ShowAppointmentDataModel
    private lateinit var tvSession: TextView
    private lateinit var tvTimeSlot: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvSkipDialog: TextView
    private lateinit var acbViewAppointment: Button
    private lateinit var onClickTypeListener: OnClickTypeListener
    protected var myDockActivity: DockActivity? = null
    private val PERMISSION_REQUEST_WRITE_CALENDAR = 1001

    constructor(
        onClickTypeListener: OnClickTypeListener
    ) : this() {
        this.onClickTypeListener = onClickTypeListener
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =
            inflater.inflate(R.layout.fragment_session_view_appoinment_dialog, container, false)
        myDockActivity = getDockActivity()
        tvSession = root.findViewById(R.id.tvSession)
        tvTimeSlot = root.findViewById(R.id.tvTimeSlot)
        tvLocation = root.findViewById(R.id.tvLocation)
        tvSkipDialog = root.findViewById(R.id.tvSkipDialog)
        acbViewAppointment = root.findViewById(R.id.acbViewAppointment)
        setData()

        checkCalendarPermissionAndAddEvent()
        setOnClickListener()
        return root
    }


    private fun setData() {
        tvSession.text = showAppointmentDataModel.session
        tvTimeSlot.text = showAppointmentDataModel.time
        tvLocation.text = showAppointmentDataModel.location
    }

    private fun setOnClickListener() {
        acbViewAppointment.setOnClickListener {
//            myDockActivity!!.emptyBackStack()
            myDockActivity!!.replaceDockableFragment(HomeFragment(), Constants.HomeFragment)
        }
        tvSkipDialog.setOnClickListener {
            onClickTypeListener.onItemClick(GetShowSlotDataModelItem(0,"","","","","",""),"FROM_APPOINTMENT_DIALOG")
            dialog!!.dismiss()

        }
    }

    override fun onLoadingStarted() {

    }

    override fun onLoadingFinished() {

    }

    override fun onProgressUpdated(percentLoaded: Int) {
    }

    private fun checkCalendarPermissionAndAddEvent() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestCalendarPermission()
        } else {
            addEventToCalendar()
        }
    }

    private fun requestCalendarPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_CALENDAR),
            PERMISSION_REQUEST_WRITE_CALENDAR
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_WRITE_CALENDAR -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addEventToCalendar()
                } else {
                    Utils.customToast(requireContext(), "Calendar permission denied")
                }
                return
            }
            else -> {
                // Handle other permissions if needed
            }
        }
    }

    private fun addEventToCalendar() {
        val calID: Long = 3

        showAppointmentDataModel.time.split("-").firstOrNull()?.let { startTime ->
            val startDateString = showAppointmentDataModel.date + " " + startTime
            val startDate = UIHelper.getDate(startDateString, "yyyy-MM-dd hh:mma")
            if (startDate != null) {
                val startMillis: Long = startDate.time
                val endMillis: Long = startMillis + showAppointmentDataModel.duration*60*1000
                val values = ContentValues().apply {
                    put(CalendarContract.Events.DTSTART, startMillis)
                    put(CalendarContract.Events.DTEND, endMillis)
                    put(CalendarContract.Events.TITLE, "HOTWORX")
                    put(CalendarContract.Events.DESCRIPTION, showAppointmentDataModel.session)
                    put(CalendarContract.Events.CALENDAR_ID, calID)
                    put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                }

                val uri: Uri? = requireContext().contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
                uri?.lastPathSegment?.let { eventId ->
                    Utils.customToast(requireContext(), "Event added to the calendar")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.setCancelable(false)
        dialog?.window?.setGravity(Gravity.CENTER)
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }


    private fun convertTimeToMillis(timeString: String): Long {
        val sdf = SimpleDateFormat("hh:mma", Locale.getDefault())
        val date = sdf.parse(timeString)

        val calendar = Calendar.getInstance()
        calendar.time = date ?: Date()

        return calendar.timeInMillis
    }

    fun convertStringToDate(inputDate: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(inputDate)

        val calendar = Calendar.getInstance()
        calendar.time = date ?: Date()

        return calendar.timeInMillis
    }

    protected fun getDockActivity(): DockActivity? {
        return myDockActivity
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDockActivity = context as DockActivity
    }
}