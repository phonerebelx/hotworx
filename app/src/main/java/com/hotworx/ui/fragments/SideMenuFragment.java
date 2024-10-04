package com.hotworx.ui.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.ui.passioactivity.PassioFragment;

import com.hotworx.activities.LoginActivity;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.models.NavigationItem;
import com.hotworx.models.UserData.getUserData;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.room.RoomBuilder;
import com.hotworx.ui.adapters.LeftNavigationBinderAdapter;
import com.hotworx.ui.fragments.ActivityScreenNew.NewActivityScreenFragment;
import com.hotworx.ui.fragments.BusinessCard.BusinessCardFragment;
import com.hotworx.ui.fragments.ComposeFragments.ReferralDetail.ReferralDetailFragment;
import com.hotworx.ui.fragments.GetStarted.GetStartedFragment;
import com.hotworx.ui.fragments.HotsquadList.MyHotsquadListFragment;
import com.hotworx.ui.fragments.Intermittent.IntermittentFastingFragment;
import com.hotworx.ui.fragments.LeaderBoard.LeaderBoardFragment;
import com.hotworx.ui.fragments.Nutritionist.NutritionistFragment;
import com.hotworx.ui.fragments.ProfileAndGoal.GoalFragment;
import com.hotworx.ui.fragments.ProfileAndGoal.ProfileAndGoalFragment;
import com.hotworx.ui.fragments.Rewards.LatestRewardFragment;
import com.hotworx.ui.fragments.Settings.SettingsFragment;
import com.hotworx.ui.fragments.VIDeviceManagement.RegistrationFragment;
import com.hotworx.ui.fragments.VPT.VPTFragment;
import com.hotworx.ui.fragments.VPT.VideoPlayerActivty;
import com.hotworx.ui.views.TitleBar;
import com.passio.modulepassio.ui.activity.PassioUiModuleActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import spencerstudios.com.bungeelib.Bungee;

public class SideMenuFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.left_drawer)
    ListView listView;
    @BindView(R.id.user_image)
    CircleImageView user_image;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_email)
    TextView user_email;
    @BindView(R.id.layout_profile)
    LinearLayout profileLayout;


    int index = 0;

    ArrayList<NavigationItem> drawerList = new ArrayList<>();
    public LeftNavigationBinderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sidemenu, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiCallingForView_Profile();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDrawerList();
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDockActivity.replaceDockableFragment(ProfileAndGoalFragment.newInstance(true));
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @SuppressLint("SetTextI18n")
    private void setValues(getUserData userData) {
        user_email.setText(userData.getData().get(0).getData().getEmail());
        user_name.setText(userData.getData().get(0).getData().getFirst_name() + " " + userData.getData().get(0).getData().getLast_name());
        if (!userData.getData().get(0).getData().getImage_url().isEmpty()) Glide.with(requireContext())
                .load(prefHelper.getImagePath())
                .into(user_image);

    }

//        apiCallForCaloriesStats();


    @Override
    public void onPause() {
        super.onPause();
    }

    public static SideMenuFragment newInstance() {
        return new SideMenuFragment();
    }

    public void setDrawerList() {
        drawerList.clear();

        if (prefHelper.getLoginData() != null && (prefHelper.getLoginData().getIsAmbassadorAllowed() != null && String.valueOf(prefHelper.getLoginData().getIsAmbassadorAllowed()).equals("yes"))){
            drawerList.add(new NavigationItem(R.string.invite, R.drawable.invite_friend,  new ReferralDetailFragment(), null,null, Constants.INVITE_FRIEND));
        }
        if (prefHelper.getLoginData() != null && (prefHelper.getLoginData().getIsEmployeeAllowed() != null && String.valueOf(prefHelper.getLoginData().getIsEmployeeAllowed()).equals("yes"))){
            drawerList.add(new NavigationItem(R.string.business,0,  new BusinessCardFragment(), null,null, Constants.BUSINESS_CARD));
          }
        drawerList.add(new NavigationItem(R.string.home, R.drawable.icon_menu_home, null, null,null, Constants.ACTION_HOME));
        drawerList.add(new NavigationItem(R.string.vi_management, R.drawable.hotworx_icons, new RegistrationFragment(), null,null, null));

//        drawerList.add(new NavigationItem(R.string.diettrax, R.drawable.icon_menu_diettrax, new PassioFragment(),null, null, null));
        
        if (prefHelper.getLoginData() != null && prefHelper.getLoginData().getIs_passio_enabled() != null && prefHelper.getLoginData().getIs_passio_enabled().equalsIgnoreCase("yes")) {
            drawerList.add(new NavigationItem(R.string.diettrax, R.drawable.icon_menu_diettrax, new PassioFragment(),null, null, null));
        }
        drawerList.add(new NavigationItem(R.string.getting_started, R.drawable.icon_menu_getting_started, GetStartedFragment.Companion.newInstance(false), null,null, Constants.ACTION_GETTING_STARTED));
        drawerList.add(new NavigationItem(R.string.activity, R.drawable.icon_menu_activity, new NewActivityScreenFragment(), null,null, null));
        drawerList.add(new NavigationItem(R.string.leaderboard, R.drawable.icon_menu_leaderboard, new LeaderBoardFragment(), null,null, null));

        if (prefHelper.getLoginData() != null && prefHelper.getLoginData().getIs_hotsquad_enabled() != null && prefHelper.getLoginData().getIs_hotsquad_enabled().equalsIgnoreCase("yes")) {
            drawerList.add(new NavigationItem(R.string.hotsquadlist, R.drawable.icon_menu_hotsquad,new MyHotsquadListFragment(), null,null, null));
        }else {}
//        drawerList.add(new NavigationItem(R.string.pending_invite, R.drawable.icon_menu_hotsquad,new RecieverPendingRequestFragment(), null,null, null));

        drawerList.add(new NavigationItem(R.string.rewards, R.drawable.icon_menu_rewards, new LatestRewardFragment(), null,null, null));

        if (prefHelper.getLoginData() != null && prefHelper.getLoginData().getDietTrax() != null && prefHelper.getLoginData().getDietTrax().equalsIgnoreCase("yes")) {
            drawerList.add(new NavigationItem(R.string.diettrax, R.drawable.icon_menu_diettrax, new NutritionistFragment(), null,null, null));
            drawerList.add(new NavigationItem(R.string.intermittent_fasting, R.drawable.icon_menu_intermitent, new IntermittentFastingFragment(), null,null, null));
        }

        if (prefHelper.getLoginData() != null && prefHelper.getLoginData().getShowpvt() != null && (prefHelper.getLoginData().getShowpvt().toLowerCase().equals("yes") || prefHelper.getLoginData().getShowpvt().toLowerCase().contains("yes"))) {
            drawerList.add(new NavigationItem(R.string.vpt, R.drawable.icon_menu_trainer, VPTFragment.newInstance(false), null,null, null));
            drawerList.add(new NavigationItem(R.string.hotworx_at_home, R.drawable.icon_menu_hotworx, GetStartedFragment.Companion.newInstance(true), null,null, null));
        }

        drawerList.add(new NavigationItem(R.string.fitness_goal, R.drawable.icon_menu_goals, new GoalFragment(), null,null, null));
        drawerList.add(new NavigationItem(R.string.frincies_info, R.drawable.icon_menu_franchise, null, null,"https://hotworx.net/franchising/", null));
        drawerList.add(new NavigationItem(R.string.blog, R.drawable.mdi_blog_outline, null, null,"https://hotworx.net/blog/", null));
        drawerList.add(new NavigationItem(R.string.settings, R.drawable.icon_menu_settings, new SettingsFragment(), null,null, null));
//        drawerList.add(new NavigationItem(R.string.notifications, R.drawable.menu_profile, new NotificationFragment(), null, null));
        drawerList.add(new NavigationItem(R.string.log_out, R.drawable.material_symbols_logout, null, null,null, Constants.ACTION_LOGOUT));

//        drawerList.add(new NavigationItem(R.string.compose, R.drawable.hotworx_icon, new ComposeFragment(), null, Constants.COMPOSE));

        for (int i = 0; i < drawerList.size(); i++) {
            NavigationItem item = drawerList.get(i);
            if (item.getText() == R.string.diettrax) {
                index = i;
            }
        }
        adapter = new LeftNavigationBinderAdapter(myDockActivity, drawerList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (prefHelper.getLoginData() != null && prefHelper.getLoginData().getDietTrax() != null && prefHelper.getLoginData().getDietTrax().equalsIgnoreCase("yes")) {
                    if (position == index) {
                        //drawerList.set(5, new NavigationItem(R.string.diettrax, R.drawable.activity_icon, new InterNutritionistFragment(), null, null));
                        drawerList.set(index, new NavigationItem(R.string.diettrax, R.drawable.icon_menu_diettrax, new NutritionistFragment(), null,null, null));

                        adapter.notifyDataSetChanged();
                    }
                }

                myDockActivity.closeDrawer();
                NavigationItem navigationItem = drawerList.get(position);

                if (navigationItem.getFragment() != null) {
                    myDockActivity.replaceDockableFragment(navigationItem.getFragment());
                }else if (navigationItem.getActivity() != null) {
                  var intent = new Intent(getActivity(), PassioUiModuleActivity.class);
                  startActivity(intent);
                }
                else if (navigationItem.getUrl() != null) {
                    openUrl(navigationItem.getUrl());
                } else if (navigationItem.getAction() != null) {
                    if (navigationItem.getAction().equals(Constants.ACTION_LOGOUT)) {
                        UIHelper.createQuitDialog(myDockActivity, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                prefHelper.setLoginStatus(false);
                                prefHelper.putLoginData(null);
                                prefHelper.putLoginToken(null);
                                prefHelper.removeActiveSession();
                                RoomBuilder.getHotWorxDatabase(getContext()).clearAllTables(); //.getSessionTypeDao().deleteAllSessions();
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                getDockActivity().finishAffinity();
                                Bungee.split(myDockActivity);
                            }
                        }, getString(R.string.message_logout)).show();
                    } else if (navigationItem.getAction().equals(Constants.ACTION_GETTING_STARTED)) {
                        String videoUrl = "https://fc32287beb49638acebf-db234c26d1b6940ea2271e1bbb478d1d.ssl.cf1.rackcdn.com/New%20Member%20video_compressed.mp4";
                        showVideoPlayer(videoUrl);
                    }
                }
            }
        });
    }

    private void apiCallForCaloriesStats() {
        getServiceHelper().enqueueCall(getWebService().getCaloriesStat(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.CALORIES_STAT, false);
    }

    private void apiCallingForView_Profile() {
        getServiceHelper().enqueueCall(
                getWebService().viewProfile(
                        ApiHeaderSingleton.apiHeader(requireContext())
                ), "Profile Api Calling", true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.CALORIES_STAT:
//                final ViewCaloriesResponse mContentPojo;
//                mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, ViewCaloriesResponse.class);
//                if (mContentPojo != null) {
////                    tv_total_calories.setText(mContentPojo.getTotal_calories_burned());
////                    tv_total_sessions.setText(mContentPojo.getTotal_session());
//                } else Utils.customToast(myDockActivity, getString(R.string.error_failure));
                break;
            case "Profile Api Calling":
                getUserData response = GsonFactory.getConfiguredGson().fromJson(result, getUserData.class);
                setValues(response);
                response.getData();
                if (!response.getData().isEmpty()) {
                    prefHelper.putLoginData(response.getData().get(0).getData());
                    setDrawerList();
                }

                break;
        }

    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void showVideoPlayer(String videoUrl) {
        Intent intent = new Intent(myDockActivity, VideoPlayerActivty.class);
        if (videoUrl != null && !videoUrl.equals("")) {
            intent.putExtra("url", videoUrl) ;
            startActivity(intent);
        } else {
            Utils.customToast(myDockActivity, "No Video Available");
        }
    }

}
