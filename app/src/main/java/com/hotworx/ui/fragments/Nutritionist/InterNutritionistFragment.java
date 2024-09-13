package com.hotworx.ui.fragments.Nutritionist;

import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bikomobile.donutprogress.DonutProgress;
import com.hotworx.R;
import com.passio.modulepassio.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.UIHelper;
import com.hotworx.interfaces.OnItemClick;
import com.hotworx.interfaces.OnNutritionistItemClick;
import com.hotworx.requestEntity.GetIntermittentFood;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.InterNutritionistAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.HomeFragment;
import com.hotworx.ui.views.TitleBar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InterNutritionistFragment extends BaseFragment implements OnNutritionistItemClick, OnItemClick {
    private Unbinder unbinder;

    @BindView(R.id.internutrition_timer_forty_view)
    TextView elapsedFortyTimer;
    @BindView(R.id.tv_internutrition_start_time)
    TextView tv_internutrition_start_time;
    @BindView(R.id.tv_internutrition_end_time)
    TextView tv_internutrition_end_time;
    @BindView(R.id.donut_progress_internutrition)
    DonutProgress donut_progress;
    @BindView(R.id.rv)
    RecyclerView recyclerView;

    private int totalTime = 0;
    private Timer timer;
    private String startTime = null, endTime = null;
    private InterNutritionistAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inter_nutritionist, container, false);
        unbinder = ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (prefHelper.getIntermittentData().getStart_time()!= null && prefHelper.getIntermittentData().getEnd_time() != null) {
            setData(UIHelper.convert24HourTo12Hour(prefHelper.getIntermittentData().getStart_time()),
                    UIHelper.convert24HourTo12Hour(prefHelper.getIntermittentData().getEnd_time()));
        }
        getIntermittentFood();
        //extrasWork();
    }

    @Override
    public void onBackPressed() {
        myDockActivity.emptyBackStack();
        myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireView().setFocusable(true);
        requireView().setFocusableInTouchMode(true);
        requireView().requestFocus();
        requireView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    myDockActivity.emptyBackStack();
                    myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.workout));
    }

    private void getTimeForTimer(String sTime, String eTime){
        if (sTime != null && eTime != null) {
            try {

                //adding seconds to time
                SimpleDateFormat spf = new SimpleDateFormat("hh:mm a");
                Date newDate = spf.parse(sTime);
                spf = new SimpleDateFormat("hh:mm:ss a");
                startTime = spf.format(newDate);

                this.totalTime = prefHelper.getIntermittentData().getIntermittent_hrs_in_Secs();

                startTimer();

            } catch (Exception e) {
                Log.e("InterNutritionistFragment", e.getMessage());
            }
        }
    }


    private int convert12HourTimeToSeconds(String time) {
        int totalSeconds = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
            Date myDate = sdf.parse(time);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            int hourToSeconds = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60;
            int minutesToSeconds = calendar.get(Calendar.MINUTE) * 60;
            int seconds = calendar.get(Calendar.SECOND);

            totalSeconds = hourToSeconds + minutesToSeconds + seconds;

        } catch (Exception e) {  }
        return totalSeconds;
    }

    private void setData(String startTime, String endTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        String dayToday = sdf.format(new Date());

        if (Objects.equals(prefHelper.getIntermittentData().getPlan_day(), dayToday)) {
            tv_internutrition_start_time.setText(startTime);
            tv_internutrition_end_time.setText(endTime);
            getTimeForTimer(startTime, endTime);
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
        int shouldTime = totalTime - (convert12HourTimeToSeconds(getCurrentTime()) - convert12HourTimeToSeconds(startTime));
        int shouldTime2 = convert12HourTimeToSeconds(getCurrentTime()) - convert12HourTimeToSeconds(startTime);
        if (shouldTime <= this.totalTime && shouldTime >= 0) {
            //Update workout UI
            int progress = (int) (((double) shouldTime2 / (double) totalTime) * 100);
            if (donut_progress != null) donut_progress.setProgress(progress);
            if (elapsedFortyTimer != null) elapsedFortyTimer.setText(getFormattedTimeString(shouldTime));
        } else {
            stopFasting();
        }
    }

    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("hh:mm:ss a");
        return date.format(currentLocalTime);
    }

    private void stopFasting() {
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

    private void getIntermittentFood() {
        getServiceHelper().enqueueCall(getWebService().getIntermittentFood(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_INTERMITTENT_FOOD, true);
    }


    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_INTERMITTENT_FOOD:
                GetIntermittentFood mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, GetIntermittentFood.class);
                if (mContentPojo != null && mContentPojo.getAllData().size() > 0) {
                    if (mContentPojo.getAllData().get(0).getFood_list().size() > 0) {
                        initRecyclerView(mContentPojo.getAllData().get(0).getFood_list());
                    }
                }
                break;
        }
    }

    private void initRecyclerView(List<GetIntermittentFood.Food_list> dayData) {
        if (dayData.size() != 0) {
            adapter = new InterNutritionistAdapter(myDockActivity, getContext(), this, "", this);
            adapter.addAll(dayData);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
//                startManualSessionWithInitialFitbit();
            }
        }
    }


    @Override
    public void onItemClick(int position, String tag) {
//        BasketFragment basketFragment = new BasketFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.isFasting, "yes");
//        bundle.putString(Constants.FastingItem, tag);
//        basketFragment.setArguments(bundle);
//        myDockActivity.replaceDockableFragment(basketFragment);
    }


    @Override
    public void onClick(@NonNull String productName, @NonNull String productId) {
        BasketFragment basketFragment = new BasketFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.isFasting, "yes");
        bundle.putString(Constants.FastingItem, productName);
        bundle.putString(Constants.FastingItemId, productId);
        basketFragment.setArguments(bundle);
        myDockActivity.replaceDockableFragment(basketFragment);
    }
}
