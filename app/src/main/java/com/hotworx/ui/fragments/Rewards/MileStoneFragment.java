package com.hotworx.ui.fragments.Rewards;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.passio.modulepassio.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.requestEntity.RewardSummaryPojo;
import com.hotworx.requestEntity.RewardSummaryResponse;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MileStoneFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.tvCurrentCalories)
    TextView tv_calories;
    @BindView(R.id.tvStartDate)
    TextView tv_start_date;
    @BindView(R.id.tvEndDate)
    TextView tv_end_date;
    @BindView(R.id.tv_expiry_date_id)
    TextView tv_expires;
    @BindView(R.id.img_lock_id)
    ImageView img_lock;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_milestone, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRedemptionStatus();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.levels_mile));
    }

    @OnClick(R.id.img_lock_id)
    public void onClick(){
        redeemReward();
    }

    private void redeemReward(){
        getServiceHelper().enqueueCall(getWebService().markRedemption(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.MARK_REDEMPTION,true);
    }

    private void getRedemptionStatus(){
         getServiceHelper().enqueueCall(getWebService().getRedemptionStatus(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_REDEMPTION_STATUS,true);
    }

    public void ResponseSuccess(String result, String Tag) {
        switch (Tag){
            case WebServiceConstants.GET_REDEMPTION_STATUS:
                RewardSummaryResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, RewardSummaryResponse.class);
                Log.d("RewardSummaryResponse",mContentPojo.getAllData().get(0).getNinetyDaysSummary().getAllow_redeem().toString());
                if (mContentPojo.getAllData().get(0).getNinetyDaysSummary() != null) {
                    RewardSummaryPojo summaryPojo = mContentPojo.getAllData().get(0).getNinetyDaysSummary();

                    tv_calories.setText(summaryPojo.getCalories());
                    tv_start_date.setText(summaryPojo.getStart_date());
                    tv_end_date.setText(summaryPojo.getEnd_date());

                    if (summaryPojo.getLock_status().equalsIgnoreCase("open")) {
                        tv_expires.setText("Redeemed on " + summaryPojo.getExpiry_date());
                        img_lock.setImageResource(R.drawable.icon_unlock);
                    } else  {
                        tv_expires.setText("Expires on " + summaryPojo.getExpiry_date());
                        img_lock.setImageResource(R.drawable.icon_lock);
                    }

                    if (summaryPojo.getAllow_redeem().equalsIgnoreCase("no")) {
                        img_lock.setClickable(false);
                    } else {
                        img_lock.setClickable(true);
                    }
                }
                break;

            case WebServiceConstants.MARK_REDEMPTION:
                myDockActivity.finishAffinity();
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
   //     unbinder.unbind();
    }
}
