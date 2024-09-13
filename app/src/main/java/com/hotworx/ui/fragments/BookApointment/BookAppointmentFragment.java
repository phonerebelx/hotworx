package com.hotworx.ui.fragments.BookApointment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.passio.modulepassio.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.Utils;
import com.hotworx.requestEntity.ViewHelpResponse;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookAppointmentFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.webView1)
    WebView webView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_book_appointment,container,false);
       unbinder = ButterKnife.bind(this,view);
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (prefHelper.getLoginData() != null && prefHelper.getLoginData().getAppointmentLink() != null && !prefHelper.getLoginData().getAppointmentLink().isEmpty()) {
            loadUrl(prefHelper.getLoginData().getAppointmentLink());
        } else {
            apiCallBookAppointment();
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.book_appointments));
    }

    private void loadUrl(String content) {
        webView.setWebViewClient(new MyBrowser(myDockActivity, content));
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(content);
    }

    private void apiCallBookAppointment(){
        String locale = getContext().getResources().getConfiguration().locale.getCountry();
        getServiceHelper().enqueueCall(getWebService().getBookingAppointments(ApiHeaderSingleton.apiHeader(requireContext()), locale), WebServiceConstants.Booking_Appointments,true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag){
            case WebServiceConstants.Booking_Appointments:
                ViewHelpResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, ViewHelpResponse.class);
                String content = mContentPojo.getContents();
                if (content != null && content.length() > 1) {
                    loadUrl(content);
//                    webView.setWebViewClient(new MyBrowser(myDockActivity, content));
//                    webView.getSettings().setLoadsImagesAutomatically(true);
//                    webView.getSettings().setJavaScriptEnabled(true);
//                    webView.setWebChromeClient(new WebChromeClient());
//                    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//                    webView.loadUrl(content);
                } else {
                    Utils.customToast(myDockActivity, getString(R.string.error_failure));
                }

                break;
        }
    }


    private class MyBrowser extends WebViewClient {

        MyBrowser(Context context, String url) {
            super();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbinder.unbind();
    }
}
