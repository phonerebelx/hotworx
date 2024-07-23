package com.hotworx.ui.fragments.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.hotworx.activities.LoginActivity;
import com.hotworx.helpers.UIHelper;
import com.hotworx.room.RoomBuilder;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.PersonalInfo.PersonalInfoFragment;
import com.hotworx.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import spencerstudios.com.bungeelib.Bungee;

public class SettingsFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.to_personal_info)
    TextView to_personal_info;
    @BindView(R.id.to_about_app)
    TextView to_about;
    @BindView(R.id.to_feedback)
    TextView to_feedback;
    @BindView(R.id.to_help)
    TextView to_help;
    @BindView(R.id.to_policy)
    TextView to_privacy;
    @BindView(R.id.tv_log_out)
    TextView tv_log_out;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();
    }

    @OnClick({R.id.to_personal_info,R.id.to_about_app,R.id.to_feedback,R.id.to_help,R.id.to_policy,R.id.tv_log_out})
    public void onClick(View view){
        switch (view.getId()) {

            case R.id.to_personal_info:
                myDockActivity.replaceDockableFragment(new PersonalInfoFragment());
                break;


            case R.id.to_about_app:
                myDockActivity.replaceDockableFragment(new AboutAppFragment());
                break;

            case R.id.to_feedback:
                myDockActivity.replaceDockableFragment(new FeedbackFragment());
                break;

            case R.id.to_help:
                myDockActivity.replaceDockableFragment(new HelpFragment());
                break;

            case R.id.to_policy:
                myDockActivity.replaceDockableFragment(new PrivacyPolicyFragment());
                break;

            case R.id.tv_log_out:
                UIHelper.createQuitDialog(myDockActivity, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefHelper.setLoginStatus(false);
                        prefHelper.putLoginData(null);
                        prefHelper.putLoginToken(null);
                        prefHelper.removeActiveSession();
                        RoomBuilder.getHotWorxDatabase(getContext()).clearAllTables(); //.getSessionTypeDao().deleteAllSessions();
                        getDockActivity().popBackStackTillEntry(0);
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getDockActivity().finishAffinity();
                        Bungee.split(getContext());

                    }
                }, getString(R.string.message_logout)).show();
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.settings));
    }
}
