package com.hotworx.ui.fragments;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.text.SpannableString;
import android.text.format.Time;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.activities.BrivoActivity.SitesActivity;
import com.hotworx.activities.DockActivity;
import com.hotworx.activities.MainActivity;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.CustomEvents;
import com.hotworx.helpers.IntermittentHelpers;
import com.hotworx.helpers.InternetHelper;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.interfaces.DialogBoxInterface;
import com.hotworx.interfaces.OnClickItemListener;
import com.hotworx.interfaces.OnClickTypeListener;
import com.hotworx.models.ActiveSessionModel;
import com.hotworx.models.DashboardData.DashboardDataModel;
import com.hotworx.models.DashboardData.NinetyDaysSummary;
import com.hotworx.models.DashboardData.Summary;
import com.hotworx.models.DashboardData.TodaysPendingSession;
import com.hotworx.models.GetRewardRequest;
import com.hotworx.models.GetRewardResponse;
import com.hotworx.models.SessionBookingModel.FinalSessionBookingModel.GetShowSlotDataModelItem;
import com.hotworx.models.UserData.getUserData;
import com.hotworx.requestEntity.CompletedClassesPOJO;
import com.hotworx.requestEntity.IntermittentPlanResponse;
import com.hotworx.requestEntity.SummaryPOJO;
import com.hotworx.requestEntity.ViewSummaryResponse;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.room.RoomBuilder;
import com.hotworx.room.model.SummaryEnt;
import com.hotworx.ui.adapters.ViewPagerAdapter;
import com.hotworx.ui.adapters.abstarct.RecyclerViewListAdapter;
import com.hotworx.ui.dialog.DashboardSession.DashboardSessionDialogFragment;

import com.hotworx.ui.fragments.DashboardFragment.LabelValueFormatter;
import com.hotworx.ui.fragments.DashboardSessionFragments.CompletedSessionFragment;
import com.hotworx.ui.fragments.DashboardSessionFragments.PendingSessionFragment;

import com.hotworx.ui.fragments.Rewards.LevelFiveFragment;
import com.hotworx.ui.fragments.Rewards.LevelSevenFragment;
import com.hotworx.ui.fragments.Rewards.LevelSixFragment;

import com.hotworx.ui.fragments.notifications.NotificationDialogHomeFragment;
import com.hotworx.ui.fragments.notifications.NotificationFragment;
import com.hotworx.ui.views.TitleBar;
import com.hotworx.workManager.MyWorker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.internal.Util;

import static com.hotworx.global.Constants.CALENDER_TITLE;
import static com.hotworx.global.Constants.SessionHistoryFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnClickItemListener {

    private Unbinder unbinder;
    private final String TAG = HomeFragment.class.getSimpleName();
    //  new dashboard data
    CircleIndicator circleIndicator3;
    ViewPager viewPager;
    DashboardDataModel getDashboardApiResponse;
    LineChart lineChart;
    ImageView ivCircle1;
    ImageView ivCircle2;
    ImageView ivCircle3;
    ImageView ivCircle4;
    ImageView ivCircle5;
    ImageView ivCircle6;
    ImageView ivCircle7;
    ArrayList<ImageView> pointSessions;
    TextView tvCalorie;
    TextView tvSession;
    TextView tvDay;
    TextView tvStreak;
    TextView tvlevel;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    private String daybefore;
    private String today;
    private String yesterday;
    private RecyclerViewListAdapter<CompletedClassesPOJO> adapter;
    private WorkManager mWorkManager;
    private OneTimeWorkRequest mRequest;
    private Data data;
    private Constraints constraints;
    private Animation popIn;
    private final MutableLiveData<String> unreadNotifications = new MutableLiveData<>();

    public LiveData<String> getUnreadNotifications() {
        return unreadNotifications;
    }


    private final MutableLiveData<String> is_new_reciprocal = new MutableLiveData<>();

    public LiveData<String> get_is_new_reciprocal() {
        return is_new_reciprocal;
    }


    DashboardSessionDialogFragment sessionDashboardDialogFragment;


    public static HomeFragment newInstance(String navigateTo, String hashId,String image,String body,String title, String notification_type, String custom_message, String booking_date, String objid, String calender_title, int duration) {
        Bundle bundle = new Bundle();
        bundle.putString("navigateTo", navigateTo);
        bundle.putString("hashId", hashId);
        bundle.putString("image", image);
        bundle.putString("body", body);
        bundle.putString(Constants.NOTIFICATION_TYPE, notification_type);
        bundle.putString(Constants.CUSTOM_MESSAGE, custom_message);
        bundle.putString(Constants.BOOKING_DATE, booking_date);
        bundle.putString(Constants.CUSTOM_TITLE, title);
        bundle.putString(Constants.OBJ_ID, objid);
        bundle.putString(Constants.CALENDER_TITLE, calender_title);
        bundle.putInt(Constants.DURATION, duration);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(requireContext(), POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(myDockActivity, new String[]{POST_NOTIFICATIONS}, 1);
        }
        apiCallGetRewards();
    }


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        prefHelper.setNotificationString("");
        circleIndicator3 = view.findViewById(R.id.circleIndicator3);
        viewPager = view.findViewById(R.id.viewPager);
        lineChart = view.findViewById(R.id.lineChart);
        tvCalorie = view.findViewById(R.id.tvCalorie);
        tvSession = view.findViewById(R.id.tvSession);
        tvDay = view.findViewById(R.id.tvDay);
        tvlevel = view.findViewById(R.id.tvlevel);
        tvStreak = view.findViewById(R.id.tvStreak);
        ivCircle1 = view.findViewById(R.id.ivCircle1);
        ivCircle2 = view.findViewById(R.id.ivCircle2);
        ivCircle3 = view.findViewById(R.id.ivCircle3);
        ivCircle4 = view.findViewById(R.id.ivCircle4);
        ivCircle5 = view.findViewById(R.id.ivCircle5);
        ivCircle6 = view.findViewById(R.id.ivCircle6);
        ivCircle7 = view.findViewById(R.id.ivCircle7);
        pointSessions = new ArrayList<>();
        pointSessions.add(ivCircle1);
        pointSessions.add(ivCircle2);
        pointSessions.add(ivCircle3);
        pointSessions.add(ivCircle4);
        pointSessions.add(ivCircle5);
        pointSessions.add(ivCircle6);
        pointSessions.add(ivCircle7);

        //initAnimations();
        swipeRefreshLayout.setOnRefreshListener(this);
        mWorkManager = WorkManager.getInstance();


        Log.d("currentDateeee",getCurrentDate());

        data = new Data.Builder()
                .putString(Constants.USER_ID, prefHelper.getUserId())
                .build();

        constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        getFirebaseToken();
        apiCallForIntermittentPlan();
        callApi(Constants.PROFILE_API_CALLING);
        callApi(Constants.DASHBOARDCALLING);

        //addCustomEventToCalendar();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null &&
                getArguments().getString(Constants.NOTIFICATION_TYPE) != null
                && getArguments().getString(Constants.NOTIFICATION_TYPE).equals("custom")
                && getArguments().getString(Constants.CUSTOM_MESSAGE) != null
                && getArguments().getString(Constants.BOOKING_DATE) != null
                && getArguments().getString(Constants.CUSTOM_TITLE) != null
                && getArguments().getString(Constants.OBJ_ID) != null
                && getArguments().getString(Constants.CALENDER_TITLE) != null) {
            myDockActivity.showCancelDialog(getArguments().getString(CALENDER_TITLE), getArguments().getString(Constants.CUSTOM_MESSAGE), getArguments().getString(Constants.BOOKING_DATE), new DialogBoxInterface() {
                @Override
                public void onPositive(String message) {
                    addEventToCalender(getArguments().getString(Constants.BOOKING_DATE));
                    getServiceHelper().enqueueCall(getWebService().updateUserAction(ApiHeaderSingleton.apiHeader(requireContext()), getArguments().getString(Constants.OBJ_ID), Constants.ACTION_ADD_TO_CALENDER), WebServiceConstants.ADD_USER_NOTIFICATION_ACTION, true);
                    getArguments().putString(Constants.OBJ_ID, null);
                }

                @Override
                public void onNegative() {
                    getServiceHelper().enqueueCall(getWebService().updateUserAction(ApiHeaderSingleton.apiHeader(requireContext()), getArguments().getString(Constants.OBJ_ID), Constants.ACTION_REJECTED), WebServiceConstants.ADD_USER_NOTIFICATION_ACTION, true);
                    getArguments().putString(Constants.OBJ_ID, null);
                }
            });
        }

        if (getArguments()!= null && getArguments().getString("navigateTo")!=null && getArguments().getString("hashId") != null) {
            String navigateTo = getArguments().getString("navigateTo");
            String hashId = getArguments().getString("hashId");
            String image = getArguments().getString("image");
            String body = getArguments().getString("body");
            String notification_type = getArguments().getString("notification_type");
            String custom_message = getArguments().getString("custom_message");
            String booking_date = getArguments().getString("booking_date");
            String title = getArguments().getString(Constants.CUSTOM_TITLE);
            String objid = getArguments().getString("objid");
            String calendar_title = getArguments().getString("calendar_title");
            int duration = getArguments().getInt("duration",0);

            NotificationDialogHomeFragment notificationDialogHomeFragment = new NotificationDialogHomeFragment(this);
            notificationDialogHomeFragment.setNotificationModel(hashId,title,body,image);
            notificationDialogHomeFragment.show(
                    getParentFragmentManager(), Constants.notificationDialogHomeFragment
            );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentResume();
        createWorkoutSessionDialog();
        autoSync();
    }

    @Override
    public void onPause() {
        super.onPause();
        removedWorkoutSessionDialog();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showMenuButton();
        titleBar.showSyncBtn();

        getUnreadNotifications().observe(getViewLifecycleOwner(), new Observer<String>() {
            public void onChanged(String unreadNotifications) {
                titleBar.showNotificationBtn(unreadNotifications);

                if (unreadNotifications.equals("0")) {
                    titleBar.hideNotificationText();
                }

            }
        });
        titleBar.showBrivoBtn();
        titleBar.setSubHeading(getResources().getString(R.string.welcome));
        titleBar.setContentDescription(getString(R.string.welcome_to_the_dashboard));
    }

    private void initAnimations() {
        popIn = AnimationUtils.loadAnimation(myDockActivity, R.anim.pop_in);
    }

    public void createWorkoutSessionDialog() {
        if (prefHelper.getActiveSession() != null && !checkWorkoutSessionDialogShowing()) {
            DashboardSessionDialogFragment sessionDashboardDialogFragment = new DashboardSessionDialogFragment();
            sessionDashboardDialogFragment.show(getChildFragmentManager(), Constants.SessionBookingDialogFragment);
        }
    }

    private Boolean checkWorkoutSessionDialogShowing() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Fragment dialogFragment = getChildFragmentManager().findFragmentByTag(Constants.SessionBookingDialogFragment);
        if (dialogFragment instanceof DialogFragment && ((DialogFragment) dialogFragment).getDialog() != null && ((DialogFragment) dialogFragment).getDialog().isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    public void removedWorkoutSessionDialog() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Fragment dialogFragment = getChildFragmentManager().findFragmentByTag(Constants.SessionBookingDialogFragment);
        if (dialogFragment instanceof DialogFragment && ((DialogFragment) dialogFragment).getDialog() != null && ((DialogFragment) dialogFragment).getDialog().isShowing()) {
            ((DialogFragment) dialogFragment).dismiss();
            transaction.remove(dialogFragment).commit();
        }
    }

    ////////////////////////////closed due to new dashboard //////////////////////////////////////////////
    private String getCurrentDate(int type) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        DateFormat dateFormat2 = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());

        Date date = new Date();
        dateFormat.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//        today_textview.setText(today);
//        yesterday_textview.setText(yesterday);
//        dayBefore_textview.setText(daybefore);
//
//        SpannableString content;
//        today_textview.setPaintFlags(today_textview.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
//        yesterday_textview.setPaintFlags(0);
//        dayBefore_textview.setPaintFlags(View.INVISIBLE);
//        if (type == 0) {
//            calendar.add(Calendar.DATE, 0);
//            content = new SpannableString(dateFormat2.format(calendar.getTime()));
//            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//            today_textview.setText(content);
//
//        } else if (type == -1) {
//            calendar.add(Calendar.DATE, -1);
//            content = new SpannableString(dateFormat2.format(calendar.getTime()));
//            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//            yesterday_textview.setText(content);
//        } else if (type == -2) {
//            calendar.add(Calendar.DATE, -2);
//            content = new SpannableString(dateFormat2.format(calendar.getTime()));
//            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//            dayBefore_textview.setText(content);
//        }
        return dateFormat.format(calendar.getTime());
    }

    private void apiCallForOverAllSummary(String date) {
        getServiceHelper().enqueueCall(getWebService().getSummaryByDate(ApiHeaderSingleton.apiHeader(requireContext()), date), WebServiceConstants.OVERALL_SUMMARY, false);

    }

    private void getFirebaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        try {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();

                            getServiceHelper().enqueueCall(getWebService().updateToken(ApiHeaderSingleton.apiHeader(requireContext()), Constants.device_type, token), WebServiceConstants.UPDATE_FIREBASE_TOKEN, false);

                        } catch (Exception ex) {

                        }
                    }
                });

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        try {
//                            String token = task.getResult().getToken();
//                            getServiceHelper().enqueueCall(getWebService().updateToken(prefHelper.getUserId(), Constants.device_type, token), WebServiceConstants.UPDATE_FIREBASE_TOKEN, false);
//                        } catch (Exception ex) {
//                        }
//
//                    }
//                });

    }

    private void callApi(String type) {


        switch (type) {
            case Constants.DASHBOARDCALLING:
                getServiceHelper().enqueueCallExtended(
                        getWebService().getDashboard(
                                ApiHeaderSingleton.apiHeader(requireContext()),
                                getCurrentDate()
                        ), Constants.DASHBOARDCALLING, true
                );

                break;

            case Constants.PROFILE_API_CALLING:
                getServiceHelper().enqueueCallExtended(
                        getWebService().viewProfile(
                                ApiHeaderSingleton.apiHeader(requireContext())
                        ), Constants.PROFILE_API_CALLING, true
                );
                break;
        }
    }

    /////////////////////////// new extended webservice /////////////////////////////
    @Override
    public void onSuccess(LiveData<String> liveData, String tag) {
        super.onSuccess(liveData, tag);
        if (!isAdded()) { return; }
        if (Constants.DASHBOARDCALLING.equals(tag)) {

            try {
                getDashboardApiResponse = GsonFactory.getConfiguredGson()
                        .fromJson(liveData.getValue(), DashboardDataModel.class);
                setDashboardCardViewData(getDashboardApiResponse.getData().getSummary());
                setUpViewPager(
                        getDashboardApiResponse.getData().getTodays_pending_sessions(),
                        getDashboardApiResponse.getData().getTodays_completed_sessions()
                );
                setUpGraph(getDashboardApiResponse.getData().getNinety_days_summary());
            } catch (Exception e) {
                Log.d("Exception: ",e.getMessage().toString());
                Utils.customToast(requireContext(), getResources().getString(R.string.error_failure));
            }
        }
        if (Constants.PROFILE_API_CALLING.equals(tag)){

            try {
                getUserData userData = GsonFactory.getConfiguredGson()
                        .fromJson(liveData.getValue(), getUserData.class);
                if (userData.getData() != null && !userData.getData().isEmpty() &&
                        userData.getData().get(0).getData() != null &&
                        userData.getData().get(0).getData().getUnread_notifications() != null) {
                    is_new_reciprocal.setValue( userData.getData().get(0).getData().getNew_reciprocal_enabled());
                    unreadNotifications.setValue( userData.getData().get(0).getData().getUnread_notifications());
                } else {
                    unreadNotifications.setValue("0");
                }
                if (!userData.getData().get(0).getData().is_brivo_allowed().equals("yes")) {
                    EventBus.getDefault().post(new CustomEvents.checkBrivoAllowed());
                }

            } catch (Exception e) {
                Utils.customToast(requireContext(), getResources().getString(R.string.error_failure));
            }
        }
    }

    private String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault()); // Automatically picks the device's current time zone
        return sdf.format(date);
    }

    @Override
    public void onFailure(String message, String tag) {
        if (!isAdded()) { return; }
        if (Constants.DASHBOARDCALLING.equals(tag)) {
            myDockActivity.showErrorMessage(message);
        }
        if (Constants.PROFILE_API_CALLING.equals(tag)) {
            myDockActivity.showErrorMessage(message);
        }
    }

    /////////////////////////// new extended webservice /////////////////////////////


    /////////////////////////// adjust view pager and graph /////////////////////////////
    private void setDashboardCardViewData(Summary getDashboardData) {
        tvCalorie.setText(getDashboardData.getTotal_cal_burned());
        tvCalorie.setTextColor(getResources().getColor(R.color.colorWhite));
        tvSession.setText(getDashboardData.getTotal_sessions());
        tvSession.setTextColor(getResources().getColor(R.color.colorWhite));
        tvDay.setText(getDashboardData.getDay_for_current_sprint());
        tvDay.setTextColor(getResources().getColor(R.color.colorWhite));
        tvStreak.setText(getDashboardData.getContinious_streak());
        tvStreak.setTextColor(getResources().getColor(R.color.colorWhite));
        tvlevel.setText(getDashboardData.getReward_level());
        tvlevel.setTextColor(getResources().getColor(R.color.colorWhite));

        if ((Integer.parseInt(getDashboardData.getReward_level()) - 1) > 0) {
            pointSessions.get(Integer.parseInt(getDashboardData.getReward_level()) - 1)
                    .setImageDrawable(getResources().getDrawable(R.drawable.db_card_circle_accent_yellow, null));
        }
    }


    private void setUpViewPager(ArrayList<TodaysPendingSession> getTodaysPendingSession, ArrayList<TodaysPendingSession> getTodaysCompletedSession) {
        if (!isAdded()) return;

        //viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        addTabsMain(viewPager, getTodaysPendingSession, getTodaysCompletedSession);
        circleIndicator3.setViewPager(viewPager);

        viewPager.setOffscreenPageLimit(1);
    }

    private void addTabsMain(ViewPager viewPager, ArrayList<TodaysPendingSession> getTodaysPendingSession, ArrayList<TodaysPendingSession> getTodaysCompletedSession) {
        if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() == 2) {
            ((PendingSessionFragment) viewPager.getAdapter().instantiateItem(viewPager, 0)).setData(getTodaysPendingSession);
            ((CompletedSessionFragment) viewPager.getAdapter().instantiateItem(viewPager, 1)).setData(getTodaysCompletedSession);
            viewPager.getAdapter().notifyDataSetChanged();
        } else {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager()); //viewPager.getAdapter() instanceof ViewPagerAdapter ? (ViewPagerAdapter) viewPager.getAdapter() : new ViewPagerAdapter(getChildFragmentManager());
            PendingSessionFragment psf = new PendingSessionFragment();
            String reciprocalValue = get_is_new_reciprocal().getValue();
            if (reciprocalValue != null) {
                psf.setSet_is_reciprocal_allowed(reciprocalValue);
            } else {
                psf.setSet_is_reciprocal_allowed("no");
            }
            psf.setData(getTodaysPendingSession);
            adapter.addFrag(psf, "PSF's");

            CompletedSessionFragment csf = new CompletedSessionFragment();
            csf.setData(getTodaysCompletedSession);
            adapter.addFrag(csf, "CSF's");

            adapter.notifyDataSetChanged();
            viewPager.setAdapter(adapter);
        }
    }

    private void setUpGraph(ArrayList<NinetyDaysSummary> getSummaryDataForGraph) {
        ArrayList<String> data = new ArrayList<>();
        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < getSummaryDataForGraph.size(); index++) {
            NinetyDaysSummary entry = getSummaryDataForGraph.get(index);
            data.add(entry.getKey());
            entries.add(new Entry(index, Float.parseFloat(String.valueOf(entry.getValue()))));
        }
        String[] labels = data.toArray(new String[0]);

        LineDataSet dataSet = new LineDataSet(entries, null);
        dataSet.setColor(getResources().getColor(R.color.colorAccent));
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(true);
        dataSet.setCircleRadius(4f);
        dataSet.setCircleColor(getResources().getColor(R.color.colorAccent));
        dataSet.setCircleHoleColor(getResources().getColor(R.color.colorAccent));

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getDescription().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new LabelValueFormatter(labels));
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setTextColor(Color.BLACK);
        yAxisLeft.setDrawGridLines(true);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.BLACK);

        lineChart.invalidate();
    }


    /////////////////////////// adjust view pager and graph /////////////////////////////


    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.OVERALL_SUMMARY:
                ViewSummaryResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, ViewSummaryResponse.class);
                if (mContentPojo != null) {
//                    updatetTimeLineAdapterAndData(mContentPojo.getAllData().get(0));
                    saveSummaryIntoRoom(mContentPojo.getAllData().get(0));
                } else
                    Utils.customToast(myDockActivity, getString(R.string.error_failure));


                break;


            case WebServiceConstants.GET_INTERMITTENT_PLAN:

                IntermittentPlanResponse mContentPojo2 = GsonFactory.getConfiguredGson().fromJson(result, IntermittentPlanResponse.class);
                if (mContentPojo2.getAllData().get(0).getSetting_data().getPlan_data() != null) {
                    IntermittentHelpers.INSTANCE.setSettingData(mContentPojo2.getAllData().get(0).getSetting_data());
                    setIntermittentData(mContentPojo2.getAllData().get(0).getSetting_data().getPlan_data(), mContentPojo2);
                }
                break;

            case WebServiceConstants.GET_REWARD:


                GetRewardResponse response = GsonFactory.getConfiguredGson().fromJson(result, GetRewardResponse.class);

                if (response.getData().length > 0 && response.getData()[0].getData() != null) {
                    if (response.getData()[0].getData().getLevel() == 5) {
                        myDockActivity.replaceDockableFragment(new LevelFiveFragment(), Constants.LevelFive);
                    }
                    if (response.getData()[0].getData().getLevel() == 6) {
                        myDockActivity.replaceDockableFragment(new LevelSixFragment(), Constants.LevelSix);
                    }
                    if (response.getData()[0].getData().getLevel() == 7) {
                        myDockActivity.replaceDockableFragment(new LevelSevenFragment(), Constants.LevelSeven);
                    }
                }
                break;
        }
    }

    private void apiCallForIntermittentPlan() {
        getServiceHelper().enqueueCall(getWebService().getIntermittentPlan(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_INTERMITTENT_PLAN, true);
    }

    private void setIntermittentData(List<IntermittentPlanResponse.Setting_data.Plan_data> list, IntermittentPlanResponse entireResponse) {

        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
//                IntermittentPlanResponse.Setting_data.Plan_data item = list.get(i);
                IntermittentPlanResponse.Setting_data.Plan_data item = list.get(i);

                if (Objects.equals(item.getPlan_day(), getDayToday())) {

                    prefHelper.putIntermittentData(null);
                    prefHelper.putIntermittentStatus(false);

                    prefHelper.putIntermittentData(list.get(i));
                    prefHelper.putIntermittentStatus(entireResponse.getAllData().get(0).getSetting_data().getIntermittent_status());
//                    Log.d("setIntermittentData: ", prefHelper.getIntermittentData().getIntermittent_hrs().toString());
                    break;
                } else {
                    prefHelper.putIntermittentData(null);
                    prefHelper.putIntermittentStatus(false);
                }
            }
        }
    }

    private String getDayToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        String dayToday = sdf.format(new Date());
        return dayToday;
    }

    @Override
    public void ResponseNoInternet(String tag) {
        switch (tag) {
            case WebServiceConstants.OVERALL_SUMMARY:
                if (RoomBuilder.getHotWorxDatabase(myDockActivity).getSummaryDao().getOfflineSummary() == null)
                    Utils.customToast(myDockActivity, myDockActivity.getString(R.string.connection_lost));
                else {
                    ViewSummaryResponse viewSummaryResponse = new ViewSummaryResponse();
                    Type listTypeSummary = new TypeToken<List<SummaryPOJO>>() {
                    }.getType();
                    ArrayList<SummaryPOJO> listSummary = new Gson().fromJson(RoomBuilder.getHotWorxDatabase(myDockActivity).getSummaryDao().getOfflineSummary().getSummary(), listTypeSummary);
                    Type listTypeClasses = new TypeToken<List<CompletedClassesPOJO>>() {
                    }.getType();
                    ArrayList<CompletedClassesPOJO> listClasses = new Gson().fromJson(RoomBuilder.getHotWorxDatabase(myDockActivity).getSummaryDao().getOfflineSummary().getClasses(), listTypeClasses);
                    viewSummaryResponse.setClasses_completed(listClasses);
                    viewSummaryResponse.setSummary(listSummary);
//                    updatetTimeLineAdapterAndData(viewSummaryResponse);
                }
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }


    ////////////////////////////closed due to new dashboard //////////////////////////////////////////////
//    private void updatetTimeLineAdapterAndData(ViewSummaryResponse mContentPojo) {
//        Log.e(TAG, "ViewCaloriesResponse dataReceived-------- " + mContentPojo.toString());
//        if (mContentPojo.getSummary() != null && mContentPojo.getSummary().size() > 0) {
//            tv_session.setText(mContentPojo.getSummary().get(0).getIsometric_calories());
//            tv_workout.setText(mContentPojo.getSummary().get(0).getHiit_calories());
//            tv_one_hour.setText(mContentPojo.getSummary().get(0).getAfter_burn());
//        } else {
//            tv_session.setText("0");
//            tv_workout.setText("0");
//            tv_one_hour.setText("0");
//        }
//
//        if (mContentPojo.getClasses_completed().size() > 0) {
//            adapter.clear();
//            adapter.addAll(mContentPojo.getClasses_completed());
//        } else rvDetail.setVisibility(View.GONE);
//
//    }
//
//    private void setNoOfSession() {
//        txtNoOfSessions.setText(String.valueOf(RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().getAllSession().size()));
//        txtNoOfSessions.setVisibility(View.VISIBLE);
//        txtNoOfSessions.setAnimation(popIn);
//
//    }

    private void apiCallGetRewards() {
        GetRewardRequest getRewardRequest = new GetRewardRequest();
        getRewardRequest.setUserId(prefHelper.getUserId());
        getServiceHelper().enqueueCall(getWebService().getRewards(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_REWARD, true);
    }

//    @OnClick({R.id.dayBefore_textview, R.id.yesterday_textview, R.id.today_textview, R.id.start_session_container,R.id.newDashboard, R.id.btnSync, R.id.txtNoOfSessions})
//    public void onClick(View view) {
//        switch (view.getId()) {
////////////////////////////////////////////////////////////////////////////////////////////////////////////
//            this scenario is closed due to new dashboard data will give you
//            case R.id.dayBefore_textview:
//                apiCallForOverAllSummary(getCurrentDate(-2));
//                break;
//            case R.id.yesterday_textview:
//                apiCallForOverAllSummary(getCurrentDate(-1));
//                break;
//            case R.id.today_textview:
//                apiCallForOverAllSummary(getCurrentDate(0));
//                break;
//            case R.id.start_session_container:
//                myDockActivity.replaceDockableFragment(new NewDashboardFragment(), Constants.BookingSessionFragment);
////                myDockActivity.replaceDockableFragment(new InterNutritionistFragment(), Constants.BeginSessionFragment);
//                break;
//                case R.id.newDashboard:
//                myDockActivity.replaceDockableFragment(new BeginSessionFragment(), Constants.BeginSessionFragment);
////                myDockActivity.replaceDockableFragment(new InterNutritionistFragment(), Constants.BeginSessionFragment);
//                break;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////  sync will be continue not closed ///////////////////////////
//            case R.id.btnSync:
//                if (InternetHelper.CheckInternetConectivity(myDockActivity)) {
//                    if (RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().getAllSession().size() > 0) {
//                        enqueueRequest();
//                    } else {
//                        Utils.customToast(myDockActivity, "All sessions are already synced.");
////                        apiCallGetRewards();
//                    }
//                } else {
//                    Utils.customToast(myDockActivity, "There is no internet connection.");
//                }
//                break;
    ///////////////////////////  sync will be continue not closed ///////////////////////////


//            ///////////////////////////    currently closed scenarios     ///////////////////////////
//            case R.id.txtNoOfSessions:
//                if (RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().getAllSession().size() > 0) {
//                    myDockActivity.replaceDockableFragment(new SessionHistoryFragment(), SessionHistoryFragment);
//                } else {
//                    Utils.customToast(myDockActivity, "There are no sessions to show!");
//                }
//                break;
//        }
//    }

    private void enqueueRequest() {
        mRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data)
                .setConstraints(constraints).build();

//        mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(this, new Observer<WorkInfo>() {
        mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    if (state.isFinished()) {
                        if (state.toString().toLowerCase().contains("succeeded") || state.toString().toLowerCase().equals("succeeded")) {

//                            setNoOfSession();
//                            apiCallGetRewards();
                            Utils.customToast(myDockActivity, "Sessions Synced Successfully");
                        }
                    }

                }
            }
        });

        mWorkManager.enqueue(mRequest);
    }


    private void saveSummaryIntoRoom(ViewSummaryResponse viewSummaryResponse) {
        Type listTypeSummary = new TypeToken<List<SummaryPOJO>>() {
        }.getType();
        String jsonResponseSummary = new Gson().toJson(viewSummaryResponse.getSummary(), listTypeSummary);

        Type listTypeClasses = new TypeToken<List<CompletedClassesPOJO>>() {
        }.getType();
        String jsonResponseClasses = new Gson().toJson(viewSummaryResponse.getClasses_completed(), listTypeClasses);

        SummaryEnt summaryEnt = new SummaryEnt(jsonResponseSummary, jsonResponseClasses);

        if (RoomBuilder.getHotWorxDatabase(myDockActivity).getSummaryDao().getOfflineSummary() == null) {
            RoomBuilder.getHotWorxDatabase(myDockActivity).getSummaryDao().insert(summaryEnt);
        } else {
            RoomBuilder.getHotWorxDatabase(myDockActivity).getSummaryDao().update(summaryEnt);
        }
    }

    private void autoSync() {
        if (InternetHelper.CheckInternetConectivity(myDockActivity)) {
            if (RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().getAllSession().size() > 0) {
                enqueueRequest();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbinder.unbind();

        if (sessionDashboardDialogFragment != null && sessionDashboardDialogFragment.isVisible()) {
            sessionDashboardDialogFragment.dismiss();
        }
    }

    @Override
    public void ResponseFailure(String message, String tag) {
        Utils.customToast(requireContext(),message);
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        callApi(Constants.DASHBOARDCALLING);
        callApi(Constants.PROFILE_API_CALLING);
    }

    private void addEventToCalender(String date) {
        String dateTxt = UIHelper.convertTimeIntoUtc(date);
        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        beginTime.set(UIHelper.getYear(dateTxt), UIHelper.getMonth(dateTxt), UIHelper.getDay(dateTxt), UIHelper.getHours(dateTxt), UIHelper.getMinutes(dateTxt));
        endTime.set(UIHelper.getYear(dateTxt), UIHelper.getMonth(dateTxt), UIHelper.getDay(dateTxt), UIHelper.getHours(dateTxt), UIHelper.getMinutes(dateTxt));
        endTime.add(Calendar.MINUTE, getArguments().getInt(Constants.DURATION));
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, getArguments().getString(Constants.CALENDER_TITLE));
        startActivity(intent);
    }

    private void addCustomEventToCalendar() {
        Calendar cal = Calendar.getInstance();
        Uri EVENTS_URI = CalendarContract.Events.CONTENT_URI;
        ContentResolver cr1 = this.getActivity().getContentResolver();

        // event insert
        ContentValues values = new ContentValues();
        values.put("calendar_id", 3);
        values.put("title", "HOTWORX2");
        values.put("dtstart", cal.getTimeInMillis()); // event starts at 1hours from now
        values.put("dtend", cal.getTimeInMillis() + 3*60*60*1000); // ends 3 hours from now
        values.put("description", "Hotworx test description");
        values.put("eventTimezone", Time.getCurrentTimezone());
        Uri event = cr1.insert(EVENTS_URI, values);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDrawerOpenEvent(CustomEvents.openDrawer event) {
        removedWorkoutSessionDialog();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDrawerCloseEvent(CustomEvents.clossDrawer event) {
        if (prefHelper.getActiveSession() != null) {
            createWorkoutSessionDialog();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void syncSessionEvent(CustomEvents.syncSession event) {
        autoSync();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void brivoSessionEvent(CustomEvents.brivoSession event) {

        startActivity(new Intent(getDockActivity(), SitesActivity.class));
        myDockActivity.finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notificationSessionEvent(CustomEvents.notificationSession event) {
        myDockActivity.replaceDockableFragment(new NotificationFragment());

    }

    @Override
    public <T> void onItemClick(T data, @NonNull String type) {
        if (type.equals("COME_FROM_CLOSE")){
            setArguments(null);
        }else{
            if (getArguments()!= null && getArguments().getString("navigateTo")!=null) {
                String navigateTo = getArguments().getString("navigateTo");
                String hashId = getArguments().getString("hashId");
                String image = getArguments().getString("image");
                String body = getArguments().getString("body");
                String notification_type = getArguments().getString("notification_type");
                String custom_message = getArguments().getString("custom_message");
                String booking_date = getArguments().getString("booking_date");
                String title = getArguments().getString(Constants.CUSTOM_TITLE);
                String objid = getArguments().getString("objid");
                String calendar_title = getArguments().getString("calendar_title");
                int duration = getArguments().getInt("duration", 0);


                myDockActivity.replaceDockableFragment(NotificationFragment.newInstance(
                        navigateTo,
                        hashId,
                        notification_type,
                        custom_message,
                        booking_date,
                        title,
                        objid,
                        calendar_title,
                        duration
                ));
            }
        setArguments(null);
        }
    }



}
