package com.hotworx.ui.fragments.LeaderBoard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.hotworx.R;
import com.passio.modulepassio.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.Utils;
import com.hotworx.models.LeaderBoard.Leaderboard;
import com.hotworx.requestEntity.LeaderBoardPOJO;
import com.hotworx.requestEntity.LeaderBoardResponse;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LeaderBoardFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.radio_global)
    RadioButton global;
    @BindView(R.id.radio_local)
    RadioButton local;
    @BindView(R.id.profileImage1)
    ImageView profileImage1;
    @BindView(R.id.profileImage2)
    ImageView profileImage2;
    @BindView(R.id.profileImage3)
    ImageView profileImage3;
    @BindView(R.id.tvName1)
    TextView tvName1;
    @BindView(R.id.tvName2)
    TextView tvName2;
    @BindView(R.id.tvName3)
    TextView tvName3;
     @BindView(R.id.tvCalBurned1)
    TextView tvCalBurned1;
     @BindView(R.id.tvCalBurned2)
    TextView tvCalBurned2;
     @BindView(R.id.tvCalBurned3)
    TextView tvCalBurned3;

//    @BindView(R.id.segmented_leaderboard)
//    SegmentedGroup segmentedGroup;
    public ArrayList<LeaderBoardPOJO> global_list, local_list;
    public Leaderboard top3_global, top3_local;
    public ArrayList<Leaderboard> top3_global_list, top3_local_list;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        unbinder = ButterKnife.bind(this, view);

        global.setChecked(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiCallFor_Global_LeaderBoard();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.leaderboard));
    }

    @OnClick({R.id.radio_global, R.id.radio_local})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_global:
                global.setChecked(true);
                local.setChecked(false);
                global.setBackgroundResource(R.drawable.custom_radio_button_white);
                global.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack));
                local.setBackgroundResource(R.drawable.custom_radio_button);
                local.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

                if (global_list != null && global_list.size() > 0){
                    setTop3_globalUser();
                    getChildFragmentManager().beginTransaction().
                            replace(R.id.leader_list_container, LeaderGlobalFragment.newInstance(global.isChecked())).commitAllowingStateLoss();
                }

                else {
                    Toast.makeText(myDockActivity, "No data found..!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.radio_local:
                local.setChecked(true);
                global.setChecked(false);
                local.setBackgroundResource(R.drawable.custom_radio_button_white);
                local.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack));
                global.setBackgroundResource(R.drawable.custom_radio_button);
                global.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                if (local_list != null && local_list.size() > 0){
                    setTop3_LocalUser();
                    getChildFragmentManager().beginTransaction().
                            replace(R.id.leader_list_container, LeaderGlobalFragment.newInstance(global.isChecked())).commitAllowingStateLoss();
                }
                else {
                    Toast.makeText(myDockActivity, "No data found..!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void setGlobal_list(ArrayList<LeaderBoardPOJO> global_list) {
        top3_global_list = new ArrayList<>();
        for (int i = 0; i < global_list.size(); i++) {
            if (i == 3){
                break;
            }

            top3_global = new Leaderboard(
                    global_list.get(i).getTotalCaloriesBurnt(),
                    global_list.get(i).getImg_url(),
                    global_list.get(i).getReward(),
                    global_list.get(i).getUser_id(),
                    global_list.get(i).getUsername()
            );

            top3_global_list.add(top3_global);
        }

        global_list.remove(0);
        global_list.remove(0);
        global_list.remove(0);
        this.global_list = global_list;

    }

    @SuppressLint("SetTextI18n")
    private void setTop3_globalUser(){
        if (top3_global_list.size() > 0) {
            Glide.with(requireContext())
                    .load(this.top3_global_list.get(0).getImg_url())
                    .into(profileImage2);
            tvName2.setText(" "+top3_global_list.get(0).getUsername());
            tvCalBurned2.setText(" "+top3_global_list.get(0).getTotalCaloriesBurnt());
        }

        if (top3_global_list.size() > 1) {
            Glide.with(requireContext())
                    .load(top3_global_list.get(1).getImg_url())
                    .into(profileImage1);
            tvName1.setText(" "+top3_global_list.get(1).getUsername());
            tvCalBurned1.setText(" "+top3_global_list.get(1).getTotalCaloriesBurnt());
        }

        if (top3_global_list.size() > 2) {
            Glide.with(requireContext())
                    .load(this.top3_global_list.get(2).getImg_url())
                    .into(profileImage3);
            tvCalBurned3.setText(" "+top3_global_list.get(2).getTotalCaloriesBurnt());
            tvName3.setText(" "+top3_global_list.get(2).getUsername());
        }
    }

    public ArrayList<LeaderBoardPOJO> getGlobal_list() {
        return global_list;
    }

    public void setLocal_list(ArrayList<LeaderBoardPOJO> local_list) {
        top3_local_list = new ArrayList<>();
        for (int i = 0; i < local_list.size(); i++) {
            if (i == 3){
                break;
            }

            top3_local = new Leaderboard(
                    local_list.get(i).getTotalCaloriesBurnt(),
                    local_list.get(i).getImg_url(),
                    local_list.get(i).getReward(),
                    local_list.get(i).getUser_id(),
                    local_list.get(i).getUsername()
            );
            top3_local_list.add(top3_local);
        }

        local_list.remove(0);
        local_list.remove(0);
        local_list.remove(0);
        this.local_list = local_list;
    }

    @SuppressLint("SetTextI18n")
    private void setTop3_LocalUser(){
        if (top3_local_list.size() > 0) {
            Glide.with(requireContext())
                    .load(this.top3_local_list.get(0).getImg_url())
                    .into(profileImage2);
            tvName2.setText(" "+top3_local_list.get(0).getUsername());
            tvCalBurned2.setText(" "+top3_local_list.get(0).getTotalCaloriesBurnt());
        }

        if (top3_local_list.size() > 1) {
            Glide.with(requireContext())
                    .load(top3_local_list.get(1).getImg_url())
                    .into(profileImage1);
            tvName1.setText(" "+top3_local_list.get(1).getUsername());
            tvCalBurned1.setText(" "+top3_local_list.get(1).getTotalCaloriesBurnt());
        }

        if (top3_local_list.size() > 2) {
            Glide.with(requireContext())
                    .load(this.top3_local_list.get(2).getImg_url())
                    .into(profileImage3);
            tvCalBurned3.setText(" "+top3_local_list.get(2).getTotalCaloriesBurnt());
            tvName3.setText(" "+top3_local_list.get(2).getUsername());
        }
    }

    public ArrayList<LeaderBoardPOJO> getLocal_list() {
        return local_list;
    }

    private void apiCallFor_Local_LeaderBoard() {
        getServiceHelper().enqueueCall(
                getWebService().getLocalLeaderBoard(
                        ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_LOCAL_LEADERBOARD, true);
    }

    private void apiCallFor_Global_LeaderBoard() {
        getServiceHelper().enqueueCall(
                getWebService().getGlobalLeaderBoard(
                        ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_GLOBAL_LEADERBOARD, true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_LOCAL_LEADERBOARD:
                LeaderBoardResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, LeaderBoardResponse.class);
                if (mContentPojo.getAllData().get(0).getLeaderboard() != null && mContentPojo.getAllData().get(0).getLeaderboard().size() > 0) {
                    local_list = mContentPojo.getAllData().get(0).getLeaderboard();
                    setLocal_list(local_list);


                } else {Utils.customToast(myDockActivity, "No record found...!!!");}
                break;

            case WebServiceConstants.GET_GLOBAL_LEADERBOARD:
                LeaderBoardResponse mPojo = GsonFactory.getConfiguredGson().fromJson(result, LeaderBoardResponse.class);
                if (mPojo.getAllData().get(0).getLeaderboard() != null && mPojo.getAllData().get(0).getLeaderboard().size() > 0) {
                    global_list = mPojo.getAllData().get(0).getLeaderboard();

                    setGlobal_list(global_list);
                    setTop3_globalUser();
                    getChildFragmentManager().beginTransaction().
                            replace(R.id.leader_list_container, LeaderGlobalFragment.newInstance(true)).commitAllowingStateLoss();

                } else {Utils.customToast(myDockActivity, "No record found...!!!");}

                apiCallFor_Local_LeaderBoard();
                break;
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (global_list != null) {
            savedInstanceState.putSerializable("global_list", global_list);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  unbinder.unbind();
    }
}
