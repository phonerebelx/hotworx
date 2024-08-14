package com.hotworx.ui.fragments.SessionFlow;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.ApplicationManager;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.models.DashboardData.TodaysPendingSession;
import com.hotworx.requestEntity.BaseModel;
import com.hotworx.requestEntity.GetWeightResponse;
import com.hotworx.requestEntity.ViewSummaryResponse;
import com.hotworx.requestEntity.WeightResponse;
import com.hotworx.requestEntity.WorkOutPOJO;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.room.RoomBuilder;
import com.hotworx.room.model.SessionEnt;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.thomashaertel.widget.MultiSpinner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.hotworx.global.Constants.WORKOUT_AFTER_BURN_NAME;

public class SaveCaloriesFragment extends BaseFragment {
    private Unbinder unbinder;

    String currentDate;
    SimpleDateFormat sdf;

    @BindView(R.id.start_date)
    TextView date_start;
    @BindView(R.id.tvTitleDesc)
    TextView tvTitleDesc;
    @BindView(R.id.session_divider)
    TextView session_divider;
    @BindView(R.id.start_text)
    TextView start_text;
    @BindView(R.id.tvOrangeTitle)
    TextView tvOrangeTitle;
    @BindView(R.id.tvBlackTitle)
    TextView tvBlackTitle;
    @BindView(R.id.upload_cam)
    ImageView upload_cam;
    @BindView(R.id.workout_image)
    ImageView workout_image;
    @BindView(R.id.onClickYesMoreWorkOut)
    Button btnYesMoreWorkOut;
    @BindView(R.id.onClickNoMoreWorkOut)
    Button btnNoMoreWorkOut;
    @BindView(R.id.session_row)
    LinearLayout session_row;
    @BindView(R.id.sixty_session_row)
    LinearLayout sixty_session_row;
    @BindView(R.id.spinner_sessions)
    MultiSpinner session_spinner;
    @BindView(R.id.start_cal)
    EditText start_cal;
    @BindView(R.id.weight_et)
    EditText weightEt;
    private NotificationManager notificationmanager;

    //Constants
    private Calendar now;
    private TodaysPendingSession activeSession;
    private Boolean isSessionCompleted = false;
    private Boolean isAfterBurnWorkoutSession = false;
    private Boolean isWatchSelected = false;
    private File file = null;
    private Long timestamp;
    HashMap<String, String> apiHeader = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_calories, container, false);
        apiHeader =  ApiHeaderSingleton.apiHeader(requireContext());

        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        extrasWork();

        callApiForGetWeight();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();

        titleBar.setSubHeading(getString(R.string.record_cal));
    }

    private void init() {
        notificationmanager = (NotificationManager) myDockActivity.getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(2);
        notificationmanager.cancel(3);
        now = Calendar.getInstance();
        upload_cam.setImageResource(R.drawable.camera);
        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String formattedDate = df.format(now.getTime());
        currentDate = sdf.format(now.getTime());
        Log.i("xxDate", currentDate);
        date_start.setText(formattedDate);
    }

    private void extrasWork() {
        // Get active session from intent
        if (getArguments() != null) {
            this.activeSession = (TodaysPendingSession) getArguments().getSerializable("activeSession");
            this.isSessionCompleted = getArguments().getBoolean("isSessionCompleted", false);
            this.isAfterBurnWorkoutSession = getArguments().getBoolean("isAfterBurnWorkoutSession", false);
            this.isWatchSelected = getArguments().getBoolean("isFitbitWatchSelected", false);
        }
        setData();
    }

    private void setData() {
        tvOrangeTitle.setText(activeSession.getDuration() + " Minutes");
        tvBlackTitle.setText(activeSession.getType());

        if (isSessionCompleted) {
            tvTitleDesc.setTextSize(17);
            tvTitleDesc.setText(getString(R.string.completed));
        }
    }

    @OnClick({R.id.start_date, R.id.upload_cam, R.id.save_start_read_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_date:
                break;

            case R.id.upload_cam:
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON)
                        .setAllowFlipping(false)
                        .setAllowRotation(false)
                        .setCropMenuCropButtonIcon(R.drawable.crop_icon)
                        .setMinCropWindowSize(700, 700)
                        .start(myDockActivity, SaveCaloriesFragment.this);
                break;

            case R.id.save_start_read_button:
                onSaveCalories();
                break;
        }
    }

    private void onSaveCalories() {

//        if (start_cal.getText().toString().trim().equals("")) {
//            Utils.customToast(myDockActivity, getResources().getString(R.string.enter_calories));
//            return;
//        }
        callApiForSetWeight(weightEt.getText().toString());

        if (isAfterBurnWorkoutSession) {
            //Record afterBurn workout calories
            savingWorkOutBurntCalories();
        } else {
            if (isSessionCompleted) {
                //Record after session calories
                saveEndSession();
            } else {
                //Record start session calories
                saveStartSession();
            }
        }
    }

    private void callApiForSetWeight(String weight) {
        getServiceHelper().enqueueCall(getWebService().setWeight(apiHeader, weight), WebServiceConstants.SET_WEIGHT, false);
    }

    private void saveStartSession() {
        SessionEnt sessionEnt = new SessionEnt(getStartCalories(), "",getCurrentDate(),"", activeSession.getType(), Constants.Other, false, activeSession.getDuration(), "no", activeSession.getSession_record_id());
        long session_id = RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().insert(sessionEnt);
        ApplicationManager.getInstance(myDockActivity).setSessionId((int) session_id);
        saveStartSessionIntoRoom();
    }

    private void saveStartSessionIntoRoom() {
        timestamp = System.currentTimeMillis() / 1000;
        String activity_id = timestamp.toString();
        ApplicationManager.getInstance(myDockActivity).setActivityId(activity_id);
        if (ApplicationManager.getInstance(myDockActivity).getParentActivityId().equals("0")) {
            ApplicationManager.getInstance(myDockActivity).setParentActivityId(activity_id);
            RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().updateActivitiesId(activity_id, activity_id, ApplicationManager.getInstance(myDockActivity).getSessionId());
        } else {
            RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().updateActivitiesId(activity_id, ApplicationManager.getInstance(myDockActivity).getParentActivityId(), ApplicationManager.getInstance(myDockActivity).getSessionId());
        }
        startWorkoutTimeActivity();
    }

    private void saveEndSession() {
        if (RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().getSession(ApplicationManager.getInstance(myDockActivity).getSessionId()) != null) {
            String start_calories = RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().getSession(ApplicationManager.getInstance(myDockActivity).getSessionId()).getStart_calories();
            if (start_calories != null && !start_calories.equals("")) {
                if (Integer.parseInt(start_calories) > Integer.parseInt(getStartCalories())) {
                    Utils.customToast(myDockActivity, "Please Enter calories greater than start calories.");
                    return;
                }
            } else {
                return;
            }
        } else {
            return;
        }

        String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US).format(new Date());
        RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().updateEndCalories(getStartCalories(),
                "", currentDate, Constants.Other,
                ApplicationManager.getInstance(myDockActivity).getSessionId());
        completeEndSessionAndGoToBurnOut();
    }

    private void completeEndSessionAndGoToBurnOut() {
        startAnotherSessionConfirmationDialog();
//        if (ApplicationManager.getInstance(myDockActivity).getListSize() > 1) {
//            startAnotherSessionConfirmationDialog();
//        } else {
//            startOneHourAfterburnSession();
//        }
    }

    private void callApiForGetWeight() {
        getServiceHelper().enqueueCallExtended(getWebService().getWeight(apiHeader), WebServiceConstants.GET_WEIGHT, false);

    }

    @Override
    public void onSuccess(LiveData<String> liveData, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_WEIGHT:
                WeightResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(liveData.getValue(), WeightResponse.class);
                if (!Objects.equals(mContentPojo.getData().get(0).getWeight_in_pound(), "")) {
                    Log.d("onSuccess: WeightResponse", mContentPojo.getData().get(0).getWeight_in_pound());
                    weightEt.setText(mContentPojo.getData().get(0).getWeight_in_pound());
                }
            case WebServiceConstants.SET_WEIGHT:
                BaseModel mContentResponse = GsonFactory.getConfiguredGson().fromJson(liveData.getValue(), BaseModel.class);

                if (mContentResponse.getMessage().equals("success")) {
//                    Log.d("mContentResponse",mContentResponse.getMessage().toString());
                }
                break;
        }
    }

    private String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault()); // Automatically picks the device's current time zone
        return sdf.format(date);
    }

    private void savingWorkOutBurntCalories() {
        if (RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().getSession(ApplicationManager.getInstance(myDockActivity).getSessionId()) != null) {
            String end_calroies = RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().getSession(ApplicationManager.getInstance(myDockActivity).getSessionId()).getEnd_calories();
            if (end_calroies != null && !end_calroies.equals("")) {
                if (Integer.parseInt(end_calroies) > Integer.parseInt(getStartCalories())) {
                    Utils.customToast(myDockActivity, "Please Enter calories greater than previous session.");
                    return;
                }
            } else {
                return;
            }
        } else {
            return;
        }

        timestamp = System.currentTimeMillis() / 1000;
        String activity_id = timestamp.toString();
        ApplicationManager.getInstance(myDockActivity).setActivityId(activity_id);

        SessionEnt burntSession = new SessionEnt(
                "0",
                "",
                getCurrentDate(),
                "",
                "0",
                "",
                Constants.AFTERBURN,
                "",
                Constants.Other,
                activity_id,
                ApplicationManager.getInstance(myDockActivity).getParentActivityId(),
                true,
                Constants.WORKOUT_AFTER_BURN_DURATION,
                "no");

        RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().insert(burntSession);
        ApplicationManager.getInstance(myDockActivity).setSessionId(0);
        clearDataAndGoToSummaryScreen();
    }

    private void clearDataAndGoToSummaryScreen() {
        // Clear parent activity Id
        startWorkoutSummaryActivity();
        ApplicationManager.getInstance(myDockActivity).setParentActivityId("0");
        ApplicationManager.getInstance(myDockActivity).setActivityId("0");
        //ApplicationManager.getInstance(myDockActivity).setListSize(0);
        prefHelper.removeActiveSession();
    }


    private String getStartCalories() {
        //   String start_calories = start_cal.getText().toString();

//        if (start_calories.contains("Cal"))
//            start_calories = start_calories.replace("Cal", "");
//
//        start_calories = start_calories.replaceAll(" ", "%20");
//
//        if (start_cal.getText().toString().length() == 0) {
//            start_calories = "0";
//        }

        return "0";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbinder.unbind();
    }


    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                workout_image.setImageURI(imageUri);
                this.file = new File(imageUri.getPath());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getContext(), "Please Try Again.", Toast.LENGTH_LONG).show();
            }
        }
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

    private void startAnotherSessionConfirmationDialog() {
        UIHelper.showWorkoutConfirmationAlertDialog(getResources().getString(R.string.would_you_like), "Message", myDockActivity,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startBeginSessionActivity();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                startOneHourAfterburnSession();
            }
        });
    }

    private void startBeginSessionActivity() {
        myDockActivity.replaceDockableFragment(new BeginSessionFragment(), Constants.BeginSessionFragment);
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
//        session.setType(WORKOUT_AFTER_BURN_NAME);
//        session.setDuration(RoomBuilder.getHotWorxDatabase(myDockActivity).getWorkoutTypeDao().getWorkoutType().getSixty_min_time());
//        session.setApple_watch_type(activeSession.getApple_watch_type());

        this.activeSession = session;
        this.isAfterBurnWorkoutSession = true;
        //Start one hour after burn session
        startWorkoutTimeActivity();
    }

    private void startWorkoutSummaryActivity() {
        WorkoutSummaryFragment workoutSummaryFragment = new WorkoutSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("parentActivityId", ApplicationManager.getInstance(myDockActivity).getParentActivityId());
        workoutSummaryFragment.setArguments(bundle);
        myDockActivity.replaceDockableFragment(workoutSummaryFragment, Constants.WorkoutSummaryFragment);
    }

}
