package com.hotworx.ui.fragments.Rewards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.passio.modulepassio.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.requestEntity.RewardResp;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LatestRewardFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.tvCurrentLevel)
    TextView tvCurrentLevel;
    @BindView(R.id.tvDayLeft)
    TextView tvDayLeft;
    @BindView(R.id.tvMilestone)
    TextView tvMilestone;
    @BindView(R.id.tvEndDate)
    TextView tvEndDate;
    @BindView(R.id.tvStartDate)
    TextView tvStartDate;
    @BindView(R.id.tvCurrentCalories)
    TextView tvCurrentCalories;
    @BindView(R.id.verticalProgressBar)
    ProgressBar pb;
    private ArrayList<String> entries_forBarChart_YearDays;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_reward, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        entries_forBarChart_YearDays = new ArrayList<>();
        entries_forBarChart_YearDays.add("");
        ninetyDaysService();
    }

    private void ninetyDaysService(){
        getServiceHelper().enqueueCall(getWebService().getNinetyDaySummary(
                ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GetNinetyDaySummary,true);
    }

    public void ResponseSuccess(String result, String Tag) {
        switch (Tag){
            case WebServiceConstants.GetNinetyDaySummary:

                RewardResp mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, RewardResp.class);

                if (mContentPojo.getAllData().get(0).getData() != null) {
                    tvCurrentCalories.setText(mContentPojo.getAllData().get(0).getData().getCurrent_calories() + "");
                    tvStartDate.setText(mContentPojo.getAllData().get(0).getData().getStart_date() + "");
                    tvEndDate.setText(mContentPojo.getAllData().get(0).getData().getEnd_date() + "");
                    tvCurrentLevel.setText(mContentPojo.getAllData().get(0).getData().getCurrent_level() + "");
                    tvDayLeft.setText(mContentPojo.getAllData().get(0).getData().getTotal_days() + "");
                    Double maxValue = Double.valueOf(mContentPojo.getAllData().get(0).getData().getMax_value());
                    pb.setMax(maxValue.intValue());
                    Double currentValue = Double.valueOf(mContentPojo.getAllData().get(0).getData().getCurrent_calories());
                    pb.setProgress(currentValue.intValue());
                }
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.rewards));
    }

    @OnClick(R.id.tvMilestone)
    public void onClick(){
        myDockActivity.replaceDockableFragment(new MileStoneFragment());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();
    }

}
