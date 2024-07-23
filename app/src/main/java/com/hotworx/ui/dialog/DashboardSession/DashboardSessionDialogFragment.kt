package com.hotworx.ui.dialog.DashboardSession

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.bikomobile.donutprogress.DonutProgress
import com.hotworx.R
import com.hotworx.activities.DockActivity
import com.hotworx.global.Constants
import com.hotworx.helpers.BasePreferenceHelper
import com.hotworx.models.ActiveSessionModel
import com.hotworx.models.DashboardData.TodaysPendingSession
import com.hotworx.requestEntity.WorkOutPOJO
import com.hotworx.ui.fragments.SessionFlow.WorkoutTimeFragment
import java.util.*

class DashboardSessionDialogFragment : DialogFragment() {
    private lateinit var prefHelper: BasePreferenceHelper
    private var timer: Timer? = null
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var activeSession: TodaysPendingSession
    private var totalTime = 0
    private var startedSessionTimeUnix: Long = 0
    private val isWatchSelected = false
    private lateinit var donut_progress: DonutProgress
    private lateinit var elapsedFortyTimer: TextView
    private lateinit var rlTimer: RelativeLayout
    private lateinit var acbCompletedTimer: AppCompatButton
    private lateinit var acbStartTimer: AppCompatButton
    private lateinit var cvGoTOWorkOut: CardView
    private lateinit var tvTotalMinute: AppCompatTextView
    private lateinit var tvSessionName: AppCompatTextView
    protected var myDockActivity: DockActivity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME,0)
        myDockActivity = getDockActivity()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myDockActivity = context as DockActivity
    }


    protected fun getDockActivity(): DockActivity? {
        return myDockActivity
    }
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_dashboard_session_dialog, container, false)
        root.setOnClickListener {

                val sessionModel = prefHelper.activeSession
                if (sessionModel != null) {

                    val workoutTimeFragment = WorkoutTimeFragment()
                    val bundle = Bundle()
                    bundle.putBoolean("resumed_session", true)
                    workoutTimeFragment.arguments = bundle
                    myDockActivity?.replaceDockableFragment(
                        workoutTimeFragment,
                        Constants.WorkoutTimeFragment
                    )
                }
        }
        donut_progress = root.findViewById(R.id.donut_progress)
        elapsedFortyTimer = root.findViewById(R.id.timer_forty_view)
        rlTimer = root.findViewById(R.id.rlTimer)
        acbCompletedTimer = root.findViewById(R.id.acbCompletedTimer)
        acbStartTimer = root.findViewById(R.id.acbStartTimer)
        cvGoTOWorkOut = root.findViewById(R.id.cvGoTOWorkOut)
        tvTotalMinute = root.findViewById(R.id.tvTotalMinute)
        tvSessionName = root.findViewById(R.id.tvSessionName)

        prefHelper = BasePreferenceHelper(requireContext())

        if (prefHelper.sessionStart == false){
            rlTimer.visibility = View.GONE
            acbStartTimer.visibility = View.VISIBLE
        }else{
            acbStartTimer.visibility = View.GONE
            rlTimer.visibility = View.VISIBLE
        }

        getSessionModel(prefHelper.activeSession)
        setDialogData()
        root.post {
            val dialogWindow = dialog!!.window


            dialogWindow!!.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            )
            dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            root.invalidate()
        }

        return root
    }


    private fun getSessionModel(sessionModel: ActiveSessionModel) {
        if (sessionModel != null){
            activeSession = sessionModel.workout
            var duration = 0
            if (activeSession.duration == ""){
                activeSession.duration = "0"
                 duration = activeSession.duration.toInt()
            }else{
                duration = activeSession.duration.toInt()
            }

            totalTime = duration * 60

            if (sessionModel.startedSessionTime != null && sessionModel.startedSessionTime != 0L) {
                // Start workout
                startedSessionTimeUnix = sessionModel.startedSessionTime
                startTimer()
            }
        }

    }

    private fun setDialogData(){
        tvTotalMinute.text = activeSession.duration.plus(" MINUTE")
        tvSessionName.text = activeSession.type
    }

    private fun startTimer() {
        this.timer = Timer()
        this.timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    elapsedTimer()
                }
            }
        }, 0, 1000)
    }

    private fun elapsedTimer() {
        val elapsedTime: Long = System.currentTimeMillis() - this.startedSessionTimeUnix
        val seconds = (elapsedTime / 1000L).toInt()
        if (seconds >= this.totalTime) {
            stopWorkout()
        } else {
            //Update workout UI
            val progress: Int = (seconds.toDouble() / totalTime.toDouble() * 100).toInt()
            if (donut_progress != null) donut_progress.setProgress(progress)
            if (elapsedFortyTimer != null) elapsedFortyTimer.setText(getFormattedTimeString(seconds))
        }
    }

    private fun getFormattedTimeString(value: Int): String? {
        val minutes = value % 3600 / 60
        val seconds = value % 60
        return String.format("%02d:%02d",  minutes, seconds)
    }


    private fun stopWorkout() {
        rlTimer.visibility = View.GONE
        acbCompletedTimer.visibility = View.VISIBLE
        acbCompletedTimer.isClickable = false
        //Update workout timer
        if (elapsedFortyTimer != null) elapsedFortyTimer.text = getFormattedTimeString(totalTime)
        if (donut_progress != null) donut_progress.progress = 100
        stopTimer()
    }

    private fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    private fun resumeWorkoutSession() {

    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


}