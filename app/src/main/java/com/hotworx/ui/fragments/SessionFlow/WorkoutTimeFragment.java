package com.hotworx.ui.fragments.SessionFlow;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.bikomobile.donutprogress.DonutProgress;
import com.hotworx.R;
import com.hotworx.global.Constants;
import com.hotworx.helpers.ApplicationManager;
import com.hotworx.helpers.InternetHelper;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.interfaces.InternetDialogBoxInterface;
import com.hotworx.models.ActiveSessionModel;
import com.hotworx.models.DashboardData.TodaysPendingSession;
import com.hotworx.requestEntity.WorkOutPOJO;
import com.hotworx.room.RoomBuilder;
import com.hotworx.room.model.SessionEnt;
import com.hotworx.services.NewAlarmReceiver;
import com.hotworx.ui.dialog.DashboardSession.DashboardSessionDialogFragment;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.HomeFragment;
import com.hotworx.ui.fragments.fitbit.RootActivity;
import com.hotworx.ui.views.TitleBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.hotworx.global.Constants.WORKOUT_AFTER_BURN_NAME;
import static com.hotworx.global.Constants.WORKOUT_COMPLETED_NOTIFICATION_ID;

public class WorkoutTimeFragment extends BaseFragment {
    private Unbinder unbinder;

    @BindView(R.id.start_timer_forty)
    AppCompatButton st_forty;
    @BindView(R.id.cancel_timer_forty)
    AppCompatButton ct_forty;
    @BindView(R.id.rec_forty)
    AppCompatButton rec_forty;
    @BindView(R.id.timer_forty_view)
    TextView elapsedFortyTimer;
    @BindView(R.id.workoutTime)
    TextView workoutTime;
    @BindView(R.id.workoutTime1)
    TextView workoutTime1;
    @BindView(R.id.donut_progress)
    DonutProgress donut_progress;

    private Boolean isAfterBurnWorkoutSession = false;
    private TodaysPendingSession activeSession;
    private int totalTime = 0;
    long startedSessionTimeUnix = 0;
    private Timer timer;
    private Boolean isWatchSelected = false;
    public Boolean sessionPlayButNotStart = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_time, container, false);
        unbinder = ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        extrasWork();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.workout));
    }

    @Override
    public void onResume() {
        super.onResume();
        cancelNotification();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Cancel notification from status bar
        cancelNotification();
    }

    @OnClick({R.id.start_timer_forty, R.id.rec_forty,R.id.cancel_timer_forty})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_timer_forty:
                prefHelper.setSessionStart(true);
                startWorkout();
                startHomeActivity();
                break;

            case R.id.rec_forty:
                if (isWatchSelected) {
                    recordWorkoutCalories();
                } else {
                    saveSession();
                }
                break;

            case R.id.cancel_timer_forty:
                exitDialog();
                break;
        }
    }

    private void extrasWork() {
        //Get active session from intent
        if (getArguments() != null) {
            Boolean isResumeSession = getArguments().getBoolean("resumed_session", false);
            if (isResumeSession) {
                startResumeSession(prefHelper.getActiveSession());
            } else {
                prefHelper.setSessionStart(false);
                startNewSession();
            }
            setData();
            sessionPlayButNotStart = false;
        }
    }

    private void startNewSession() {
        this.activeSession = (TodaysPendingSession) getArguments().getSerializable("activeSession");
        this.isAfterBurnWorkoutSession = getArguments().getBoolean("isAfterBurnWorkoutSession", false);
        this.isWatchSelected = getArguments().getBoolean("isFitbitWatchSelected", false);
        saveActiveSession();
    }

    private void setData() {

        //Get workout total time duration
        int duration;
        if (this.activeSession.getDuration().isEmpty()) {
            this.activeSession.setDuration("0");
            duration = 0;
        } else {
            duration = Integer.parseInt(this.activeSession.getDuration());
        }
        this.totalTime = duration * 60;

        //Update values
        this.workoutTime.setText(this.activeSession.getDuration() + " " + getResources().getString(R.string.minutes));
        this.workoutTime1.setText(this.activeSession.getType());

        //Set workout timer
        this.elapsedFortyTimer.setText(getFormattedTimeString(0));

        elapsedFortyTimer.setContentDescription(elapsedFortyTimer.getText().toString());
    }

    private void startResumeSession(ActiveSessionModel sessionModel) {
        this.activeSession = sessionModel.getWorkout();
        this.isAfterBurnWorkoutSession = sessionModel.getAfterBurnSession();
        this.isWatchSelected = sessionModel.getWatchSelected();

        if (sessionModel.getStartedSessionTime() != 0) {
            // Start workout
            this.startedSessionTimeUnix = sessionModel.getStartedSessionTime();
            Log.d( "startResumeSession: ", String.valueOf(startedSessionTimeUnix));
            //Update UI for session started
            this.st_forty.setClickable(false);
            this.st_forty.setAlpha(0.5f);

            startTimer();
        }
    }

    private void startTimer() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                myDockActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //stopWorkout();
                        elapsedTimer();
                    }
                });
            }
        }, 0, 1000);
    }

    private void elapsedTimer() {
        long elapsedTime = System.currentTimeMillis() - this.startedSessionTimeUnix;
        int seconds = (int) (elapsedTime / 1000L);

        if (seconds >= this.totalTime) {
            stopWorkout();
        } else {
            //Update workout UI
            int progress = (int) (((double) seconds / (double) totalTime) * 100);
            if (donut_progress != null) donut_progress.setProgress(progress);
            if (elapsedFortyTimer != null)
                elapsedFortyTimer.setText(getFormattedTimeString(seconds));
        }
    }

    private void stopWorkout() {
        if (st_forty != null) st_forty.setVisibility(View.GONE);
        if (rec_forty != null) rec_forty.setVisibility(View.VISIBLE);
        if (rec_forty != null) rec_forty.setClickable(true);

        //Update workout timer
        if (elapsedFortyTimer != null) elapsedFortyTimer.setText(getFormattedTimeString(totalTime));
        if (donut_progress != null) donut_progress.setProgress(100);

        stopTimer();
    }

    private String getFormattedTimeString(int value) {
        int hours = value / 3600;
        int minutes = (value % 3600) / 60;
        int seconds = value % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    private void startWorkout() {
        sessionPlayButNotStart = true;
        this.startedSessionTimeUnix = System.currentTimeMillis();

        //Update UI for session started
        this.st_forty.setClickable(false);
        this.st_forty.setAlpha(0.5f);

        //Set alarm service for workout completion
        startAlarmTimerService(this.totalTime);

        startTimer();

        // Update active session details
        saveActiveSession();
    }

    private void saveActiveSession() {
        ActiveSessionModel sessionModel = new ActiveSessionModel();
        sessionModel.setWorkout(this.activeSession);
        sessionModel.setWatchSelected(this.isWatchSelected);
        sessionModel.setStartedSessionTime(this.startedSessionTimeUnix);
        sessionModel.setAfterBurnSession(this.isAfterBurnWorkoutSession);
        sessionModel.setActivityId(ApplicationManager.getInstance(myDockActivity).getActivityId());
        sessionModel.setParentActivityId(ApplicationManager.getInstance(myDockActivity).getParentActivityId());
        prefHelper.setActiveSession(sessionModel);
    }


    private void recordWorkoutCalories() {
        if (InternetHelper.CheckInternetConectivity(myDockActivity)) {
            startFitbitSession();
        } else {
            showNoInternetDialog();
        }
    }

    private void saveSession() {
        if (isAfterBurnWorkoutSession) {
            //Record afterBurn workout calories
            savingWorkOutBurntCalories();
        } else {
            //Record start session calories
            saveEndSession();
        }
    }

    private void saveEndSession() {
        String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US).format(new Date());
        RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().updateEndCalories("0",
                "", currentDate, Constants.Other,
                ApplicationManager.getInstance(myDockActivity).getSessionId());
        completeEndSessionAndGoToBurnOut();
    }

    private void completeEndSessionAndGoToBurnOut() {
        // Clear active session
        prefHelper.removeActiveSession();
        startAnotherSessionConfirmationDialog();

//        if (ApplicationManager.getInstance(myDockActivity).getListSize() > 1) {
//            startAnotherSessionConfirmationDialog();
//        } else {
//            startOneHourAfterburnSession();
//        }
    }

    private void startAnotherSessionConfirmationDialog() {
        UIHelper.showWorkoutConfirmationAlertDialog(getResources().getString(R.string.would_you_like), "Message", myDockActivity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //startBeginSessionActivity();
                startHomeActivity();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                startOneHourAfterburnSession();
            }
        });
    }

    private void startOneHourAfterburnSession() {
        TodaysPendingSession session = new TodaysPendingSession(
                this.activeSession.getApple_watch_type(),
                this.activeSession.getDate(),
                Constants.WORKOUT_AFTER_BURN_DURATION,
                "yes",
                this.activeSession.getLat(),
                this.activeSession.getLead_record_id(),
                this.activeSession.getLocation_name(),
                this.activeSession.getLong(),
                this.activeSession.getSauna(),
                this.activeSession.getSession_name(),
                this.activeSession.getSession_record_id(),
                this.activeSession.getSlot(),
                WORKOUT_AFTER_BURN_NAME,
                this.activeSession.getWeek_day(),
                "",
                "0",
                false,
                "",
                "",
                ""
        );

        this.activeSession = session;
        this.isAfterBurnWorkoutSession = true;
        startWorkoutTimeActivity();
    }

    private void startWorkoutTimeActivity() {
        WorkoutTimeFragment workoutTimeFragment = new WorkoutTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("activeSession", this.activeSession);
        bundle.putBoolean("isAfterBurnWorkoutSession", this.isAfterBurnWorkoutSession);
        bundle.putBoolean("isFitbitWatchSelected", isWatchSelected);
        workoutTimeFragment.setArguments(bundle);
        myDockActivity.replaceDockableFragment(workoutTimeFragment, Constants.WorkoutTimeFragment);
    }

    private void startBeginSessionActivity() {
        myDockActivity.replaceDockableFragment(new BeginSessionFragment(), Constants.BeginSessionFragment);
    }

    private void startFitbitSession() {
        Intent intent = new Intent(myDockActivity, RootActivity.class);
        intent.putExtra("activeSession", this.activeSession);
        intent.putExtra("isSessionCompleted", true);
        intent.putExtra("isAfterBurnWorkoutSession", isAfterBurnWorkoutSession);
        WorkoutTimeFragment.this.startActivityForResult(intent, 1000);
    }

    private void startManualSession() {
        SaveCaloriesFragment saveCaloriesFragment = new SaveCaloriesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("activeSession", this.activeSession);
        bundle.putBoolean("isSessionCompleted", true);
        bundle.putBoolean("isAfterBurnWorkoutSession", isAfterBurnWorkoutSession);
        saveCaloriesFragment.setArguments(bundle);
        myDockActivity.addDockableFragment(saveCaloriesFragment, Constants.SaveCaloriesFragment);
    }

    private void savingWorkOutBurntCalories() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String currentDate = sdf.format(now.getTime());

        Long timestamp = System.currentTimeMillis() / 1000;
        String activity_id = timestamp.toString();
        ApplicationManager.getInstance(myDockActivity).setActivityId(activity_id);

        SessionEnt burntSession = new SessionEnt(
                "0",
                "",
                activeSession.getDate(),
                getCurrentDate(),
                "0",
                "",
                Constants.AFTERBURN,
                "",
                Constants.Other,
                activity_id,
                ApplicationManager.getInstance(myDockActivity).getParentActivityId(),
                true,
                "",
                "no");

        RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().insert(burntSession);
        ApplicationManager.getInstance(myDockActivity).setSessionId(0);
        clearDataAndGoToHomeActivity();
    }

    private void clearDataAndGoToHomeActivity() {
        // Clear parent activity Id
        //  startWorkoutSummaryActivity();
        ApplicationManager.getInstance(myDockActivity).setParentActivityId("0");
        ApplicationManager.getInstance(myDockActivity).setActivityId("0");
        //ApplicationManager.getInstance(myDockActivity).setListSize(0);
        prefHelper.removeActiveSession();
        startHomeActivity();
    }

    private void startHomeActivity() {
        myDockActivity.emptyBackStack();
        myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);
    }

    private void startManualSessionWithInitialFitbit() {
        SaveCaloriesFragment saveCaloriesFragment = new SaveCaloriesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("activeSession", this.activeSession);
        bundle.putBoolean("isSessionCompleted", true);
        bundle.putBoolean("isAfterBurnWorkoutSession", isAfterBurnWorkoutSession);
        bundle.putBoolean("isFitbitWatchSelected", true);
        saveCaloriesFragment.setArguments(bundle);
        myDockActivity.addDockableFragment(saveCaloriesFragment, Constants.SaveCaloriesFragment);
    }

    private void showNoInternetDialog() {
        UIHelper.showNoInternetDialog(myDockActivity, getString(R.string.network_internet_connection_error), getString(R.string.do_you_want_to_record_calories_manually), getString(R.string.cancel), getString(R.string.continue_), new InternetDialogBoxInterface() {
            @Override
            public void onPositive() {
                startManualSession();
            }

            @Override
            public void onNegative() {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                startManualSessionWithInitialFitbit();
            }
        }
    }


    private void clearSession() {
        stopTimer();

        // Reset parent activity id
        //ApplicationManager.getInstance(myDockActivity).setListSize(0);
        ApplicationManager.getInstance(myDockActivity).setActivityId("0");
        ApplicationManager.getInstance(myDockActivity).setParentActivityId("0");
        ApplicationManager.getInstance(myDockActivity).setSessionId(0);

        // Remove active session from shared preferences
        prefHelper.removeActiveSession();

        cancelAlarmNotification();
        myDockActivity.emptyBackStack();
        myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);
    }

    private void startAlarmTimerService(int seconds) {
        AlarmManager alarmManager = (AlarmManager) myDockActivity.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(myDockActivity, NewAlarmReceiver.class);
        PendingIntent broadcast = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            broadcast = PendingIntent.getBroadcast(myDockActivity, 100, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            broadcast = PendingIntent.getBroadcast(myDockActivity, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, seconds);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }

    private void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) myDockActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(WORKOUT_COMPLETED_NOTIFICATION_ID);
    }

    private void cancelAlarmNotification() {
        AlarmManager alarmManager = (AlarmManager) myDockActivity.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(myDockActivity, NewAlarmReceiver.class);
        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(myDockActivity, 100, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(myDockActivity, 100, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

        }
        alarmManager.cancel(pendingIntent);
    }

    private String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault()); // Automatically picks the device's current time zone
        return sdf.format(date);
    }

    private void exitDialog() {
        UIHelper.showAlertDialog(getResources().getString(R.string.are_you_sure_cancel), "", myDockActivity, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isAfterBurnWorkoutSession) {
                    String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).format(new Date());
                    Long timestamp = System.currentTimeMillis() / 1000;
                    String activity_id = timestamp.toString();
                    ApplicationManager.getInstance(myDockActivity).setActivityId(activity_id);

                    SessionEnt burntSession = new SessionEnt(
                            "0",
                            "",
                            activeSession.getDate(),
                            getCurrentDate(),
                            "0",
                            "",
                            Constants.AFTERBURN,
                            "",
                            Constants.Other,
                            activity_id,
                            ApplicationManager.getInstance(myDockActivity).getParentActivityId(),
                            true,
                            Constants.WORKOUT_AFTER_BURN_DURATION,
                            "yes");
                    RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().insert(burntSession);
                    clearSession();
                } else {
                    Toast.makeText(getContext(),"elseeeeeeeeeee"+getCurrentDate(),Toast.LENGTH_LONG).show();
                    RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().updateCancellation("yes", "0", ApplicationManager.getInstance(myDockActivity).getSessionId(), getCurrentDate());
                    clearSession();
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
//        exitDialog();
        myDockActivity.replaceDockableFragment(new HomeFragment());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unbinder.unbind();
    }


}
