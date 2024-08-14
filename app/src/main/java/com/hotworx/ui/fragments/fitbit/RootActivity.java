package com.hotworx.ui.fragments.fitbit;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.hotworx.R;
import com.hotworx.activities.BaseActivity;
import com.hotworx.activities.MainActivity;
import com.hotworx.databinding.ActivityRootBinding;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.ApplicationManager;
import com.hotworx.helpers.InternetHelper;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.interfaces.DialogBoxCallBack;
import com.hotworx.interfaces.InternetDialogBoxInterface;
import com.hotworx.interfaces.LoadingListener;
import com.hotworx.models.DashboardData.TodaysPendingSession;
import com.hotworx.requestEntity.WorkOutPOJO;
import com.hotworx.retrofit.WebService;
import com.hotworx.retrofit.WebServiceFactory;
import com.hotworx.room.RoomBuilder;
import com.hotworx.room.model.SessionEnt;
import com.fitbit.api.loaders.ResourceLoaderResult;
import com.fitbit.api.models.DailyActivitySummary;
import com.fitbit.api.models.Summary;
import com.fitbit.api.services.ActivityService;
import com.fitbit.authentication.AuthenticationHandler;
import com.fitbit.authentication.AuthenticationManager;
import com.fitbit.authentication.AuthenticationResult;
import com.fitbit.authentication.Scope;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import static com.hotworx.global.Constants.WORKOUT_AFTER_BURN_NAME;


public class RootActivity extends BaseActivity implements AuthenticationHandler, LoaderManager.LoaderCallbacks<ResourceLoaderResult<DailyActivitySummary>>, LoadingListener, View.OnClickListener {
    private Boolean isCompletedWorkout = false;
    private Boolean isAfterBurnWorkoutSession = false;
    private ActivityRootBinding binding;
    private TodaysPendingSession activeSession;
    protected WebService webService;
    Long timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_root);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_root);
        if (binding.btnSubmit != null) binding.btnSubmit.setOnClickListener(this);

        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(this, WebServiceConstants.BASE_URL);
        }

        this.isCompletedWorkout = getIntent().getBooleanExtra("isSessionCompleted", false);
        this.isAfterBurnWorkoutSession = getIntent().getBooleanExtra("isAfterBurnWorkoutSession", false);

        if (getIntent().hasExtra("activeSession")) {
            this.activeSession = (TodaysPendingSession) getIntent().getSerializableExtra("activeSession");
        } else {
            Utils.customToast(this, "Failed to get active session");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.setLoading(false);

        /**
         *  (Look in FitbitAuthApplication for Step 1)
         */


        /**
         *  2. If we are logged in, go to next activity
         *      Otherwise, display the login screen
         */

        if (AuthenticationManager.isLoggedIn()) {
            onLoggedIn();

        }

    }

    private void showNoInternetDialog() {
        UIHelper.showNoInternetDialog(this, getString(R.string.network_internet_connection_error), getString(R.string.do_you_want_to_record_calories_manually), getString(R.string.cancel), getString(R.string.continue_), new InternetDialogBoxInterface() {
            @Override
            public void onPositive() {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onNegative() {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         *  4. When the Login UI finishes, it will invoke the `onActivityResult` of this activity.
         *  We call `AuthenticationManager.onActivityResult` and set ourselves as a login listener
         *  (via AuthenticationHandler) to check to see if this result was a login result. If the
         *  result code matches login, the AuthenticationManager will process the login request,
         *  and invoke our `onAuthFinished` method.
         *
         *  If the result code was not a login result code, then `onActivityResult` will return
         *  false, and we can handle other onActivityResult result codes.
         *
         */

        if (!AuthenticationManager.onActivityResult(requestCode, resultCode, data, this)) {
            // Handle other activity results, if needed
        }

    }

    public void onLoggedIn() {
        binding.setLoading(false);
        getLoaderManager().initLoader(101, null, this).forceLoad();
    }

    public void onLoginClick(View view) {
        if (InternetHelper.CheckInternetConectivity(this)) {
            login();
        } else {
            showNoInternetDialog();
        }
    }

    private void login() {
        binding.setLoading(true);
        /**
         *  3. Call login to show the login UI
         */
        AuthenticationManager.login(this);
    }

    public void onAuthFinished(AuthenticationResult authenticationResult) {
        binding.setLoading(false);

        /**
         * 5. Now we can parse the auth response! If the auth was successful, we can continue onto
         *      the next activity. Otherwise, we display a generic error message here
         */
        if (authenticationResult.isSuccessful()) {
            onLoggedIn();
        } else {
            displayAuthError(authenticationResult);
        }
    }

    private void displayAuthError(AuthenticationResult authenticationResult) {
        String message = "";

        switch (authenticationResult.getStatus()) {
            case dismissed:
                message = getString(R.string.login_dismissed);
                break;
            case error:
                message = authenticationResult.getErrorMessage();
                break;
            case missing_required_scopes:
                Set<Scope> missingScopes = authenticationResult.getMissingScopes();
                String missingScopesText = TextUtils.join(", ", missingScopes);
                message = getString(R.string.missing_scopes_error) + missingScopesText;
                break;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.login_title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();
    }

    private void startAnotherSessionConfirmationDialog(String caloriesOut) {
        UIHelper.showWorkoutConfirmationAlertDialog(getResources().getString(R.string.would_you_like), "Message", this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //startBeginSessionActivity();
                startHomeActivity();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                startOneHourAfterburnSession(caloriesOut);
            }
        });
    }

    private void startBeginSessionActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.flag, Constants.BeginSessionFragment);
        startActivity(intent);
        finishAffinity();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.flag, Constants.HomeFragment);
        startActivity(intent);
        finishAffinity();
    }

    private void startWorkoutTimeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.flag, Constants.WorkoutTimeFragment);
        intent.putExtra("activeSession", this.activeSession);
        intent.putExtra("isAfterBurnWorkoutSession", this.isAfterBurnWorkoutSession);
        intent.putExtra("isFitbitWatchSelected", true);
        startActivity(intent);
        finishAffinity();
    }

    private void startWorkoutSummaryActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.flag, Constants.WorkoutSummaryFragment);
        intent.putExtra("parentActivityId", ApplicationManager.getInstance(this).getParentActivityId());
        startActivity(intent);
        finishAffinity();
    }

    private void startOneHourAfterburnSession(String caloriesOut) {
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
                caloriesOut,
                false,
                "",
                "",
                ""
        );
        this.activeSession = session;
        this.isAfterBurnWorkoutSession = true;
        //Start one hour after burn session
        startWorkoutTimeActivity();
    }


    @Override
    public Loader<ResourceLoaderResult<DailyActivitySummary>> onCreateLoader(int id, Bundle args) {
        return ActivityService.getDailyActivitySummaryLoader(this, new Date());
    }

    @Override
    public void onLoadFinished(Loader<ResourceLoaderResult<DailyActivitySummary>> loader, ResourceLoaderResult<DailyActivitySummary> resourceLoaderResult) {
        if (resourceLoaderResult != null && resourceLoaderResult.isSuccessful()) {
            bindActivityData(resourceLoaderResult.getResult());
        } else {
            Utils.customToast(this,"Failed to get calories");
        }
    }

    @Override
    public void onLoaderReset(Loader<ResourceLoaderResult<DailyActivitySummary>> loader) {
        //no-op
    }

    public void bindActivityData(DailyActivitySummary dailyActivitySummary) {
        final Summary summary = dailyActivitySummary.getSummary();
        if (summary.getCaloriesOut() != null){
            String calories = "Fitbit Calorie: " + summary.getCaloriesOut();
            UIHelper.showCustomDialog(this, calories, "Would you like to refresh this calorie reading with what's displayed on your Fitbit?", new DialogBoxCallBack() {

                @Override
                public void onPositive() {
                    callAction(getStartCalories(summary.getCaloriesOut()));
                }

                @Override
                public void onRedirect() {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://hotworx.net/app-help/"));
                    startActivity(i);
                }

                @Override
                public void onRefresh() {
                    login();
                }

                @Override
                public void onManual() {
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }else{
            Utils.customToast(this,"Calories not fount");
        }
    }

    private void callAction(String calories) {
        if (isAfterBurnWorkoutSession) {
            //Record after burn workout calories
            saveOneHourCalories(calories);
        } else {
            if (isCompletedWorkout) {
                //Record after session calories
                saveEndSession(calories);
            } else {
                //Record start session calories
                saveStartSession(calories);
            }
        }
    }

    private String getStartCalories(int caloriesOut) {
        String start_calories = caloriesOut + "";
        if (start_calories.contains("Cal"))
            start_calories = start_calories.replace("Cal", "");
        start_calories = start_calories.replaceAll(" ", "%20");
        return start_calories;
    }

    private void saveStartSession(String caloriesOut) {
        String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US).format(new Date());
        SessionEnt sessionEnt = new SessionEnt(caloriesOut, "", currentDate,"", activeSession.getType(), Constants.FITBIT, false, activeSession.getDuration(), "no", activeSession.getSession_record_id());
        long session_id = RoomBuilder.getHotWorxDatabase(this).getSessionTypeDao().insert(sessionEnt);
        ApplicationManager.getInstance(this).setSessionId((int) session_id);
        saveStartSessionIntoRoom();
    }

    private void saveStartSessionIntoRoom() {
        timestamp = System.currentTimeMillis() / 1000;
        String activity_id = timestamp.toString();

        ApplicationManager.getInstance(this).setActivityId(activity_id);
        if (ApplicationManager.getInstance(this).getParentActivityId().equals("0")) {
            ApplicationManager.getInstance(this).setParentActivityId(activity_id);
            RoomBuilder.getHotWorxDatabase(this).getSessionTypeDao().updateActivitiesId(activity_id, activity_id, ApplicationManager.getInstance(this).getSessionId());
        } else {
            RoomBuilder.getHotWorxDatabase(this).getSessionTypeDao().updateActivitiesId(activity_id, ApplicationManager.getInstance(this).getParentActivityId(), ApplicationManager.getInstance(this).getSessionId());
        }
        startWorkoutTimeActivity();
    }

    private void saveEndSession(String caloriesOut) {
        String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US).format(new Date());
        RoomBuilder.getHotWorxDatabase(this).getSessionTypeDao().updateEndCalories(caloriesOut,
                "", currentDate, Constants.FITBIT,
                ApplicationManager.getInstance(this).getSessionId());
        completeEndSessionAndGoToBurnOut(caloriesOut);

    }

    private void completeEndSessionAndGoToBurnOut(String caloriesOut) {
        startAnotherSessionConfirmationDialog(caloriesOut);
//        if (ApplicationManager.getInstance(this).getListSize() > 1) {
//        } else {
//            startOneHourAfterburnSession();
//        }
    }

    private String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault()); // Automatically picks the device's current time zone
        return sdf.format(date);
    }

    private void saveOneHourCalories(String caloriesOut) {
        String currentDate = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US).format(new Date());
        timestamp = System.currentTimeMillis() / 1000;
        String activity_id = timestamp.toString();
        ApplicationManager.getInstance(this).setActivityId(activity_id);

        SessionEnt burntSession = new SessionEnt(
                activeSession.getStartCalories(),
                "",
                activeSession.getDate(),
                getCurrentDate(),
                caloriesOut,
                "",
                Constants.AFTERBURN,
                "",
                Constants.FITBIT,
                activity_id,
                ApplicationManager.getInstance(this).getParentActivityId(),
                true,
                Constants.WORKOUT_AFTER_BURN_DURATION,
                "no");

        RoomBuilder.getHotWorxDatabase(this).getSessionTypeDao().insert(burntSession);
        ApplicationManager.getInstance(this).setSessionId(0);
        clearDataAndGoToSummaryScreen();
    }

    private void clearDataAndGoToSummaryScreen() {
        // Clear parent activity Id
        startWorkoutSummaryActivity();
        ApplicationManager.getInstance(this).setParentActivityId("0");
        ApplicationManager.getInstance(this).setActivityId("0");
        //ApplicationManager.getInstance(this).setListSize(0);
        prefHelper.removeActiveSession();
    }

    @Override
    public void onLoadingStarted() {
//        isLoading = true;
//        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFinished() {
//        isLoading = false;
//        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onProgressUpdated(int percentLoaded) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (isLoading) Utils.customToast(this, getString(R.string.message_wait));
    }

    @Override
    public void onClick(View v) {
        if (binding.evManualCalories.getText().toString().trim().equals("")) {
            Utils.customToast(this, getResources().getString(R.string.enter_calories));
            return;
        }
        callAction(binding.evManualCalories.getText().toString());

    }
}