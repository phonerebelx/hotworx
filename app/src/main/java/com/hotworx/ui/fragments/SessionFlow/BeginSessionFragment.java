package com.hotworx.ui.fragments.SessionFlow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hotworx.R;
import com.passio.modulepassio.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.InternetHelper;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.interfaces.InternetDialogBoxInterface;
import com.hotworx.models.DashboardData.TodaysPendingSession;
import com.hotworx.requestEntity.WorkOutPOJO;
import com.hotworx.requestEntity.WorkOutTypesResp;
import com.hotworx.requestEntity.SessionSummaryDataModel;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.room.RoomBuilder;
import com.hotworx.room.model.WorkOutTypeEnt;
import com.hotworx.ui.adapters.StartSessionAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.HomeFragment;
import com.hotworx.ui.fragments.fitbit.RootActivity;
import com.hotworx.ui.views.TitleBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class BeginSessionFragment extends BaseFragment implements StartSessionAdapter.SessionInterface {
    private Unbinder unbinder;
    private ArrayList<WorkOutPOJO> sessionTypesList = new ArrayList<>();
    private WorkOutPOJO selectedSession;
    private int selectedDuration = -1;
    @BindView(R.id.rvDetail)
    RecyclerView rvDetail;
    @BindView(R.id.tvTotalTime)
    TextView tvTotalTime;
    private StartSessionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_begin_session, container, false);
        unbinder = ButterKnife.bind(this, view);
        rvDetail.setLayoutManager(new LinearLayoutManager(myDockActivity));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiCallForTodayCalories();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.start_session));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();
    }

    @OnClick(R.id.begin_session_button)
    public void onClick() {
        onBeginSession();
    }


    private void apiCallForTodayCalories() {
//        String locale = getContext().getResources().getConfiguration().locale.getCountry();
        getServiceHelper().enqueueCallExtended(getWebService().getTodaysSession(
                ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_TODAYS_SESSION, true);
    }

    @Override
    public void onSuccess(LiveData<String> liveData, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_TODAYS_SESSION:
                Log.d("mContentPojo",liveData.getValue());
                SessionSummaryDataModel mContentPojo = GsonFactory.getConfiguredGson().fromJson(liveData.getValue(), SessionSummaryDataModel.class);
                if (mContentPojo != null && mContentPojo.getData().size() > 0) {
                    updateAdapter(mContentPojo.getData().get(0).getWorkoutData());
                    saveSessionTypesIntoRoom(mContentPojo.getData().get(0));
                } else {
                    Utils.customToast(myDockActivity, "No work-out types found");
                }

                break;
        }
    }

    @Override
    public void onFailure(String message, String tag) {
        switch (tag) {
            case WebServiceConstants.GET_TODAYS_SESSION:
                new AlertDialog.Builder(getContext())
                        .setTitle("Error")
                        .setMessage(message)

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setPositiveButton(android.R.string.yes, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
        }

    }

    private void saveSessionTypesIntoRoom(WorkOutTypesResp workOutTypesResp) {

        Type listType = new TypeToken<List<TodaysPendingSession>>() {
        }.getType();
        String jsonResponse = new Gson().toJson(workOutTypesResp.getWorkoutData(), listType);
        if (RoomBuilder.getHotWorxDatabase(myDockActivity).getWorkoutTypeDao().getWorkoutType() == null) {
            RoomBuilder.getHotWorxDatabase(myDockActivity).getWorkoutTypeDao().insert(new WorkOutTypeEnt(workOutTypesResp.getSixty_min_time(), jsonResponse));
        } else {
            RoomBuilder.getHotWorxDatabase(myDockActivity).getWorkoutTypeDao().update(new WorkOutTypeEnt(workOutTypesResp.getSixty_min_time(), jsonResponse));
        }

        Log.d("saveSessionTypesIntoRoom: ",RoomBuilder.getHotWorxDatabase(myDockActivity).getWorkoutTypeDao().getWorkoutType().getData());
    }

    @Override
    public void ResponseNoInternet(String tag) {
        switch (tag) {
            case WebServiceConstants.GET_TODAYS_SESSION:
                if (RoomBuilder.getHotWorxDatabase(myDockActivity).getWorkoutTypeDao().getWorkoutType() == null)
                    UIHelper.showLongToastInCenter(myDockActivity, myDockActivity.getString(R.string.connection_lost));
                else {
                    Type listType = new TypeToken<List<WorkOutPOJO>>() {
                    }.getType();
                    ArrayList<WorkOutPOJO> workOutPOJOList = new Gson().fromJson(RoomBuilder.getHotWorxDatabase(myDockActivity).getWorkoutTypeDao().getWorkoutType().getData(), listType);
                    updateAdapter(workOutPOJOList);
                }
                break;
        }
    }

    private void updateAdapter(ArrayList<WorkOutPOJO> list) {
        String date = new SimpleDateFormat("dd MMM, yyyy").format(Calendar.getInstance().getTime());

        sessionTypesList = list;
        adapter = new StartSessionAdapter(myDockActivity, sessionTypesList, date, this);
        rvDetail.setAdapter(adapter);
        //ApplicationManager.getInstance(myDockActivity).setListSize(sessionTypesList.size());

    }

    private void onBeginSession() {
        if (this.selectedSession == null) {
            Utils.customToast(myDockActivity, getResources().getString(R.string.selectduration));
            return;
        }
        showDeviceComfirmationDialog();
    }


    private void showDeviceComfirmationDialog() {
        final Dialog dialog = new Dialog(myDockActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_fitness);
        Button tvSmartWatch = (Button) dialog.findViewById(R.id.tvSmartWatch);
        Button tvdonthave = (Button) dialog.findViewById(R.id.tvdonthave);
        Button tvCancel = (Button) dialog.findViewById(R.id.tvCancel);

        tvSmartWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (InternetHelper.CheckInternetConectivity(myDockActivity)) {
                    startSmartWatchSession();
                } else {
                    showNoInternetDialog();
                }
            }
        });

        tvdonthave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startOtherWatchSession();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showNoInternetDialog() {
        UIHelper.showNoInternetDialog(myDockActivity, getString(R.string.network_internet_connection_error), getString(R.string.do_you_want_to_record_calories_manually),getString(R.string.cancel),getString(R.string.continue_), new InternetDialogBoxInterface() {
            @Override
            public void onPositive() {
                startOtherWatchSession();
            }

            @Override
            public void onNegative() {

            }
        });
    }


    private void startSmartWatchSession() {
        Intent intent = new Intent(myDockActivity, RootActivity.class);
        intent.putExtra("activeSession", this.selectedSession);
        BeginSessionFragment.this.startActivityForResult(intent, 1000);
    }

    private void startOtherWatchSession() {
        SaveCaloriesFragment saveCaloriesFragment = new SaveCaloriesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("activeSession", selectedSession);
        saveCaloriesFragment.setArguments(bundle);
        myDockActivity.replaceDockableFragment(saveCaloriesFragment, Constants.SaveCaloriesFragment);
    }

    private void startOtherWatchSessionWithInitialFitbit() {
        SaveCaloriesFragment saveCaloriesFragment = new SaveCaloriesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("activeSession", selectedSession);
        bundle.putSerializable("isFitbitWatchSelected", true);
        saveCaloriesFragment.setArguments(bundle);
        myDockActivity.replaceDockableFragment(saveCaloriesFragment, Constants.SaveCaloriesFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                startOtherWatchSessionWithInitialFitbit();
            }

        }
    }

    @Override
    public void onRadioButton_Click(String duration, String type) {
        selectedDuration = Integer.parseInt(duration);
        tvTotalTime.setText(String.valueOf(60 + selectedDuration).concat(" min"));
    }

    @Override
    public void onRadioButton_Click(int position) {
        this.selectedSession = this.sessionTypesList.get(position);
    }

    @Override
    public void onBackPressed() {
        myDockActivity.emptyBackStack();
        myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);
    }
}
