package com.hotworx.ui.fragments.Settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AboutAppFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.about_app_tv)
    TextView about_app;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_app,container,false);
        unbinder = ButterKnife.bind(this,view);
        PackageManager manager = myDockActivity.getPackageManager();

        try {
            PackageInfo info = manager.getPackageInfo(myDockActivity.getPackageName(), 0);
            String version = info.versionName;
            int versionCode = info.versionCode;
            about_app.setText("HotWorx\n" + "Build Number: " + versionCode + "\n" + "Version: " + version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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
        titleBar.setSubHeading(getString(R.string.about_app));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  unbinder.unbind();
    }
}
