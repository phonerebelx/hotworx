package com.hotworx.ui.fragments.Dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.Utils;
import com.hotworx.requestEntity.FrachiseSummaryEnt;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.LeaderBoard.LeaderBoardFragment;
import com.hotworx.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FranchiseSummaryFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.txtIsoCalories)
    TextView txtIsoCalories;
    @BindView(R.id.txtHitCalories)
    TextView txtHitCalories;
    @BindView(R.id.txtAfterBurn)
    TextView txtAfterBurn;
    @BindView(R.id.txtytd)
    TextView txtYtd;
    @BindView(R.id.txtmtd)
    TextView txtMtd;
    @BindView(R.id.txtToday)
    TextView txtToday;
    private String location_id;

    public static FranchiseSummaryFragment newInstance(String location_id) {
        FranchiseSummaryFragment franchiseSummaryFragment = new FranchiseSummaryFragment();
        Bundle args = new Bundle();
        args.putString("location_id", location_id);
        franchiseSummaryFragment.setArguments(args);
        return franchiseSummaryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_franchise_summary, container, false);
        unbinder = ButterKnife.bind(this, view);
        location_id = getArguments().getString("location_id");
        callApiForSummary(Constants.TODAY);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.dashboard));
    }

    @OnClick({R.id.txtytd, R.id.txtmtd, R.id.txtToday, R.id.btnFranchiseLeaderBoard, R.id.btnMemberLeaderBoard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtytd:
                setTextColor(0);
                callApiForSummary(Constants.YTD);
                break;

            case R.id.txtmtd:
                setTextColor(1);
                callApiForSummary(Constants.MTD);
                break;

            case R.id.txtToday:
                setTextColor(2);
                callApiForSummary(Constants.TODAY);
                break;

            case R.id.btnFranchiseLeaderBoard:
            case R.id.btnMemberLeaderBoard:
                myDockActivity.replaceDockableFragment(new LeaderBoardFragment());
                break;
        }
    }

    private void setTextColor(int position) {
        switch (position) {
            case 0:
                txtYtd.setTextColor(myDockActivity.getResources().getColor(R.color.colorRed));
                txtMtd.setTextColor(myDockActivity.getResources().getColor(R.color.colorWhite));
                txtToday.setTextColor(myDockActivity.getResources().getColor(R.color.colorWhite));
                break;

            case 1:
                txtYtd.setTextColor(myDockActivity.getResources().getColor(R.color.colorWhite));
                txtMtd.setTextColor(myDockActivity.getResources().getColor(R.color.colorRed));
                txtToday.setTextColor(myDockActivity.getResources().getColor(R.color.colorWhite));
                break;

            case 2:
                txtYtd.setTextColor(myDockActivity.getResources().getColor(R.color.colorWhite));
                txtMtd.setTextColor(myDockActivity.getResources().getColor(R.color.colorWhite));
                txtToday.setTextColor(myDockActivity.getResources().getColor(R.color.colorRed));
                break;
        }
    }

    private void callApiForSummary(String interval) {
        getServiceHelper().enqueueCall(getWebService().getFranchiseSummary(ApiHeaderSingleton.apiHeader(requireContext()),
                location_id, interval), WebServiceConstants.FRANCHISE_SUMMARY, true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.OVERALL_SUMMARY:
                FrachiseSummaryEnt mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, FrachiseSummaryEnt.class);
                if (mContentPojo != null && mContentPojo.getSummaryPOJO() != null) {
                    txtIsoCalories.setText(mContentPojo.getSummaryPOJO().getIsometric_calories());
                    txtHitCalories.setText(mContentPojo.getSummaryPOJO().getHiit_calories());
                    txtAfterBurn.setText(mContentPojo.getSummaryPOJO().getAfter_burn());
                } else
                    Utils.customToast(myDockActivity, getString(R.string.error_failure));
                break;
        }
    }

}
