package com.hotworx.ui.dialog.Hotsquad

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
import com.hotworx.interfaces.OnClickSessionPendingModelInterface
import com.hotworx.models.DashboardData.TodaysPendingSession
import com.hotworx.models.HotsquadList.Session.PendingSessionResponse
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class PendingSessionDialogFragment(val onClickSessionPendingInterface: OnClickSessionPendingModelInterface) :
    DialogFragment(), LoadingListener {

    private lateinit var tvTime: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvSessionName: TextView
    private lateinit var tvSauna: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvSessionType: TextView
    private lateinit var tvDateNumber: TextView
    private lateinit var cvSession: CardView
    private lateinit var declineBtn: AppCompatButton
    private lateinit var AcceptBtn: AppCompatButton
    lateinit var todaysPendingSession:  PendingSessionResponse.SquadInvitation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_pending_session_dialog, container, false)
        tvTime = root.findViewById(R.id.tvTimeSlot)
        tvDate = root.findViewById(R.id.tvDateSlot)
        tvSessionName = root.findViewById(R.id.tvSessionName)
        tvSessionType = root.findViewById(R.id.tvSessionType)
        cvSession = root.findViewById(R.id.cvSession)
        AcceptBtn = root.findViewById(R.id.AcceptBtn)
        declineBtn = root.findViewById(R.id.declineBtn)
        tvSauna = root.findViewById(R.id.tvSauna)
        tvLocation = root.findViewById(R.id.tvLocation)

        setDialogData()
        setOnClickListener()
        return root
    }


    private fun setDialogData() {
        tvTime.text = " ".plus(todaysPendingSession.session_info.time_slot)
        tvDate.text = " ".plus(todaysPendingSession.session_info.booking_date)
        tvSessionName.text = " ".plus(todaysPendingSession.squad_event_name)
        tvSessionType.text = " ".plus(todaysPendingSession.session_info.session_type)
        tvSauna.text = " ".plus(todaysPendingSession.session_info.sauna_no)
        tvLocation.text = " ".plus(todaysPendingSession.session_info.location_name)
    }

    private fun setOnClickListener() {
        AcceptBtn.setOnClickListener {
            onClickSessionPendingInterface.onItemClick(todaysPendingSession, "COME_FROM_ACCEPT")
            dialog?.dismiss()
        }

        declineBtn.setOnClickListener {
            onClickSessionPendingInterface.onItemClickDecline(todaysPendingSession, "COME_FROM_DECLINE")
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
}