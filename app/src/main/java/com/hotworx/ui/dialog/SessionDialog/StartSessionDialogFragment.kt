package com.hotworx.ui.dialog.SessionDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.hotworx.R
import com.hotworx.interfaces.LoadingListener
import com.hotworx.interfaces.OnClickPendingModelInterface
import com.hotworx.models.DashboardData.TodaysPendingSession
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

//inflater.inflate(R.layout.fragment_start_session_dialog, container, false)
class StartSessionDialogFragment(val onClickPendingModelInterface: OnClickPendingModelInterface) :
    DialogFragment(), LoadingListener {

    private lateinit var tvDate: TextView
    private lateinit var tvSessionName: TextView
    private lateinit var tvSauna: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvUpperDate: TextView
    private lateinit var tvDateNumber: TextView
    private lateinit var tvMonthDay: TextView
    private lateinit var tvStartEndTime: TextView
    private lateinit var cvTimeDateDialog: CardView
    private lateinit var cvTimeDateDialog2: CardView
    private lateinit var acbStartSession: AppCompatButton
    lateinit var todaysPendingSession: TodaysPendingSession
    var comeFromTimelineFragment: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_start_session_dialog, container, false)
        tvDate = root.findViewById(R.id.tvDate)
        tvSessionName = root.findViewById(R.id.tvSessionName)
        cvTimeDateDialog = root.findViewById(R.id.cvTimeDateDialog)
        cvTimeDateDialog2 = root.findViewById(R.id.cvTimeDateDialog2)

        tvUpperDate = root.findViewById(R.id.tvUpperDate)
        tvDateNumber = root.findViewById(R.id.tvDateNumber)
        tvMonthDay = root.findViewById(R.id.tvMonthDay)
        tvStartEndTime = root.findViewById(R.id.tvStartEndTime)


        tvSauna = root.findViewById(R.id.tvSauna)
        tvLocation = root.findViewById(R.id.tvLocation)
        acbStartSession = root.findViewById(R.id.acbStartSession)
        if (comeFromTimelineFragment) {
            acbStartSession.visibility = View.GONE
        }
        setDialogData()
        setOnClickListener()
        return root
    }


    private fun setDialogData() {
        if (todaysPendingSession.comeFromActivity == false) {
            cvTimeDateDialog.visibility = View.VISIBLE
            cvTimeDateDialog2.visibility = View.GONE

            tvDate.text = " ".plus(todaysPendingSession.slot)
            tvSessionName.text = " ".plus(todaysPendingSession.session_name)
            tvSauna.text = " ".plus(todaysPendingSession.sauna)
            tvLocation.text = " ".plus(todaysPendingSession.location_name)
        } else {
            cvTimeDateDialog.visibility = View.GONE
            cvTimeDateDialog2.visibility = View.VISIBLE
            acbStartSession.visibility = View.GONE

            if (todaysPendingSession.display_date.isNotEmpty()) {

                val components =
                    getSeperateDate(todaysPendingSession.display_date, "formattedDate").split("\\s".toRegex())

                tvUpperDate.text = components[1]
                tvDateNumber.text = components[0]
                tvMonthDay.text =  getSeperateDate(todaysPendingSession.display_date, "day")
                val startTime = getTimeZone(todaysPendingSession.start_date_time)
                val endTime = getTimeZone(todaysPendingSession.end_date_time)

                if (startTime != null && endTime != null) {
                    tvStartEndTime.text = "$startTime - $endTime"
                } else {
                    tvStartEndTime.text = ""
                }
            }

        }

    }

    private fun setOnClickListener() {
        acbStartSession.setOnClickListener {
            onClickPendingModelInterface.onItemClick(todaysPendingSession, "COME_FROM_IMAGE_VIEW")
            dialog?.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
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

    private fun getTimeZone(date: String): String? {
        if (date == "0000-00-00 00:00:00") {
            return null
        }

        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val dateTime = LocalDateTime.parse(date, dateFormat)
        val timeFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        val formattedTime = dateTime.format(timeFormat)
        return formattedTime.toString()
    }

    private fun getSeperateDate(date: String, returnObject: String): String {


        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH)
        val date = dateFormat.parse(date)

        // Format the parsed date to extract day, date, and time
        val dayFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
        val dateFormat2 = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)

        val day = dayFormat.format(date)
        val formattedDate = dateFormat2.format(date)
        val time = timeFormat.format(date)

        when (returnObject) {
            "day" -> {
                return day
            }

            "formattedDate" -> {
                return formattedDate
            }

            "time" -> {
                return time
            }
        }
        return day
    }




}