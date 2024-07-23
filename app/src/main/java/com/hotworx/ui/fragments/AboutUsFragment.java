package com.hotworx.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.hotworx.R;
import com.hotworx.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AboutUsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        unbinder = ButterKnife.bind(this, view);

        String about = getResources().getString(R.string.about_hotworx);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, about.toString(), "text/html", "UTF-8", null);
        return view;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.about_diettrax));
    }
}