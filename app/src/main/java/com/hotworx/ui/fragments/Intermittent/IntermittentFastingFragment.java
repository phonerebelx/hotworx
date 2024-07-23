package com.hotworx.ui.fragments.Intermittent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.google.android.gms.common.api.Api;
import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.helpers.UIHelper;
import com.hotworx.interfaces.OnIntermittentFastingItemClick;
import com.hotworx.interfaces.OnItemClickInterface;
import com.hotworx.requestEntity.GenericMsgResponse;
import com.hotworx.requestEntity.IntermittentPlanResponse.Setting_data;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.requestEntity.IntermittentPlanResponse;
import com.hotworx.requestEntity.NutritionCaloriesResponse;
import com.hotworx.requestEntity.ParentNutritionistItem;
import com.hotworx.requestEntity.SetIntermittentPlan;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.IntermittentFastingAdapter;
import com.hotworx.ui.adapters.NutritionistParentItemAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.HomeFragment;
import com.hotworx.ui.views.TitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IntermittentFastingFragment extends BaseFragment implements OnIntermittentFastingItemClick {

    Unbinder unbinder;

    @BindView(R.id.intermittent_rv)
    RecyclerView recyclerView;
    @BindView(R.id.switchAB)
    SwitchCompat switchAB;
    @BindView(R.id.send_data)
    Button send_data_btn;

    IntermittentFastingAdapter adapter;
    private List<Setting_data.Plan_data> setData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_intermittent_fasating, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiCallForIntermittentPlan();
        switchAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSwitch();
            }
        });
        send_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallForSetIntermittentPlan();
            }
        });
    }

    private void checkSwitch() {
        if (!switchAB.isChecked()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.intermittent_fasting));
    }

    @Override
    public void onBackPressed() {
        myDockActivity.emptyBackStack();
        myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);
    }

    private void apiCallForIntermittentPlan() {
        getServiceHelper().enqueueCall(getWebService().getIntermittentPlan(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_INTERMITTENT_PLAN, true);
    }

    private void apiCallForSetIntermittentPlan() {
        SetIntermittentPlan data = new SetIntermittentPlan();
        data.setIntermittent_status(switchAB.isChecked());
        data.setPlan_data(setData);
        getServiceHelper().enqueueCall(getWebService().setIntermittentPlan(ApiHeaderSingleton.apiHeader(requireContext()),data), WebServiceConstants.SET_INTERMITTENT_PLAN, true);
        prefHelper.putIntermittentData(null);
        prefHelper.putIntermittentStatus(false);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_INTERMITTENT_PLAN:

                IntermittentPlanResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, IntermittentPlanResponse.class);
                switchAB.setChecked(mContentPojo.getAllData().get(0).getSetting_data().getIntermittent_status());
                checkSwitch();
                if (mContentPojo.getAllData().get(0).getSetting_data().getPlan_data() != null) {
                    this.setData = mContentPojo.getAllData().get(0).getSetting_data().getPlan_data();
                    initRecyclerview(mContentPojo.getAllData().get(0).getSetting_data().getPlan_data());
                }
                break;

            case WebServiceConstants.SET_INTERMITTENT_PLAN:
                GenericMsgResponse mContent = GsonFactory.getConfiguredGson().fromJson(result, GenericMsgResponse.class);
                UIHelper.showLongToastInCenter(requireContext(), R.string.message_success);
                Log.i(WebServiceConstants.SET_INTERMITTENT_PLAN,mContent.getMessage());

                break;
        }
    }

    private void initRecyclerview(List<Setting_data.Plan_data> list) {
        if (list.size() != 0) {
            adapter = new IntermittentFastingAdapter(myDockActivity, list,getContext(), this);
            adapter.addAll(list);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(int position, Boolean get_active, int hours, String startTime, String endTime) {
        Log.i("TEST1",get_active+" "+hours+" "+startTime+" "+endTime+" ");
        setData.get(position).setActive(get_active);
        setData.get(position).setIntermittent_hrs(hours);
        setData.get(position).setStart_time(startTime);
        setData.get(position).setEnd_time(endTime);

    }
}