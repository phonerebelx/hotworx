package com.hotworx.ui.fragments.Rewards;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

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

public class LevelSevenFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.tvCalBurned)
    TextView tvCalBurned;
    @BindView(R.id.tvNextLevel)
    TextView tvNextLevel;
    @BindView(R.id.tvDaysLeft)
    TextView tvDaysLeft;

    @BindView(R.id.ivCross)
    ImageView ivCross;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_level_seven, container, false);

        unbinder = ButterKnife.bind(this, view);

        apiCallForGetRewards();

        return view;
    }

    private void apiCallForGetRewards() {
        GetRewardRequest getRewardRequest = new GetRewardRequest();
        getRewardRequest.setUserId(prefHelper.getUserId());
        getServiceHelper().enqueueCall(getWebService().getRewards(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_REWARD, true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void ResponseSuccess(String result, String Tag) {
        if (WebServiceConstants.GET_REWARD.equals(Tag)) {
            GetRewardResponse rewardResponse = GsonFactory.getConfiguredGson().fromJson(result, GetRewardResponse.class);

            if (rewardResponse != null) {

                NumberFormat formatter = new DecimalFormat("#,###");
                double myNumber = rewardResponse.getData()[0].getData().getCal_burned();
                String formattedNumber = formatter.format(myNumber);

                tvCalBurned.setText("Congrats on Burning "+ formattedNumber +" Calories in"+ String.valueOf(rewardResponse.getData()[0].getData().getTotal_days() - rewardResponse.getData()[0].getData().getDays_left()) +" Days");
//                tvNextLevel.setText("+ Exclusive Photo with"+rewardResponse.getData().getNext_level() +"Foam Board");
//                tvDaysLeft.setText("You Have"+ rewardResponse.getData().getDays_left() +"days to redeem");
            }
        }
    }

    @OnClick({R.id.ivCross})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCross:
                myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);
                myDockActivity.getSupportFragmentManager().popBackStack(Constants.HomeFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }
}