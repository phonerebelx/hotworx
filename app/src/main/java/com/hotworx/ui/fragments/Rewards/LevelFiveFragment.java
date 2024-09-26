package com.hotworx.ui.fragments.Rewards;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.models.GetRewardRequest;
import com.hotworx.models.GetRewardResponse;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.HomeFragment;
import com.hotworx.ui.views.TitleBar;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LevelFiveFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.tvCalBurned)
    TextView tvCalBurned;
    @BindView(R.id.tvCalBurnedLevel)
    TextView tvCalBurnedLevel;
    @BindView(R.id.tvDaysLeft)
    TextView tvDaysLeft;

    @BindView(R.id.ivCross)
    ImageView ivCross;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_level_five, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiCallForGetRewards();
    }

    private void apiCallForGetRewards() {
        GetRewardRequest getRewardRequest = new GetRewardRequest();
        getRewardRequest.setUserId(prefHelper.getUserId());
        getServiceHelper().enqueueCall(getWebService().getRewards(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_REWARD, true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        if (WebServiceConstants.GET_REWARD.equals(Tag)) {
            GetRewardResponse rewardResponse = GsonFactory.getConfiguredGson().fromJson(result, GetRewardResponse.class);

            if (rewardResponse != null) {

                NumberFormat formatter = new DecimalFormat("#,###");
                double myNumber = rewardResponse.getData()[0].getData().getCal_burned();
                String formattedNumber = formatter.format(myNumber);

                tvCalBurned.setText("Congrats on Burning "+ formattedNumber +" Calories");
                tvCalBurnedLevel.setText("Burn " +rewardResponse.getData()[0].getData().getCal_req_for_next_level() +" More Calories to Reach Level "+ rewardResponse.getData()[0].getData().getNext_level());
                tvDaysLeft.setText("You Have "+ rewardResponse.getData()[0].getData().getDays_left() +" days left of your" +rewardResponse.getData()[0].getData().getTotal_days()+" -Day challenge to reach level 6!");
            }
        }
    }

    @OnClick({R.id.ivCross})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCross:
                myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);

        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }
}