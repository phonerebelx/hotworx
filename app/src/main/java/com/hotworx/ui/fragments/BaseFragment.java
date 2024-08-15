package com.hotworx.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.activities.LoginActivity;
import com.hotworx.activities.MainActivity;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.BasePreferenceHelper;
import com.hotworx.helpers.ServiceHelper;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.interfaces.ApiListener;
import com.hotworx.interfaces.LoadingListener;
import com.hotworx.interfaces.OnBackPressed;
import com.hotworx.interfaces.webServiceResponseLisener;
import com.hotworx.retrofit.WebService;
import com.hotworx.retrofit.WebServiceFactory;
import com.hotworx.room.RoomBuilder;
import com.hotworx.ui.views.TitleBar;

import spencerstudios.com.bungeelib.Bungee;

public abstract class BaseFragment extends Fragment implements webServiceResponseLisener, OnBackPressed,ApiListener {
    protected Handler handler = new Handler();
    protected BasePreferenceHelper prefHelper;
    protected WebService webService;
    protected ServiceHelper serviceHelper;
    protected DockActivity myDockActivity;
    private boolean isLoading;

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess( LiveData<String> liveData, @NonNull String tag) {

    }

    @Override
    public void onFailure(@NonNull String message, @NonNull String tag) {

    }

    @Override
    public void onFailureWithResponseCode(int code, @NonNull String message, @NonNull String tag) {
        if (code == 551) {
            Dialog alertDialog = UIHelper.createAlertDialog(getDockActivity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    prefHelper.setLoginStatus(false);
                    prefHelper.putLoginData(null);
                    prefHelper.putLoginToken(null);
                    prefHelper.removeActiveSession();
                    RoomBuilder.getHotWorxDatabase(getContext()).clearAllTables(); //.getSessionTypeDao().deleteAllSessions();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }, message, "Ok");
            alertDialog.show();
        } else if (code == 552) {
            Dialog alertDialog = UIHelper.createAlertDialog(getDockActivity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                            ("market://details?id=com.hotworx")));
                }
            }, message, "Update");
            alertDialog.show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefHelper = new BasePreferenceHelper(getContext());
        if (getDockActivity().getDrawerLayout() != null) {
            getDockActivity().lockDrawer();
        }

        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(getDockActivity(), WebServiceConstants.BASE_URL);
        }

        if (serviceHelper == null) {
            serviceHelper = new ServiceHelper(this,this, getDockActivity(), webService);
        }

        myDockActivity = getDockActivity();
    }

    public WebService getWebService() {
        return WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(getDockActivity(), WebServiceConstants.BASE_URL);
    }

    public WebService getBrivoWebService(String get_access_point_id,Boolean isBrivoApi) {
        String BASE_URL = "https://api.brivo.com/v1/api/access-points/" + get_access_point_id + "/";
        return WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(getDockActivity(), BASE_URL,isBrivoApi);
    }



    public WebService getCustomWebService() {
        return WebServiceFactory.getUnSafeOkHttp(getDockActivity(), WebServiceConstants.CUSTOM_BASE_URL);
    }

    public ServiceHelper getServiceHelper() {
        return new ServiceHelper(this,this, getDockActivity(), getWebService());
    }

    public ServiceHelper getCustomServiceHelper() {
        return new ServiceHelper(this,this, getDockActivity(), getCustomWebService());
    }

    public Fragment getBaseTargetFragment() {
        return getTargetFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getDockActivity().getDrawerLayout() != null) {
            getDockActivity().releaseDrawer();
        }
    }

    public void fragmentResume() {
        setTitleBar(((MainActivity) getDockActivity()).titleBar);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getDockActivity().getWindow() != null)
            if (getDockActivity().getWindow().getDecorView() != null)
                UIHelper.hideSoftKeyboard(getDockActivity(), getDockActivity()
                        .getWindow().getDecorView());

    }

    protected void loadingStarted() {

        if (getParentFragment() != null)
            ((LoadingListener) getParentFragment()).onLoadingStarted();
        else
            getDockActivity().onLoadingStarted();

        isLoading = true;
    }

    protected void checkIsLoadingStart(){
        isLoading = true;
    }
    protected void checkIsLoadingEnd(){
        isLoading = false;
    }

    protected void loadingFinished() {

        if (getParentFragment() != null)
            ((LoadingListener) getParentFragment()).onLoadingFinished();
        else if (getDockActivity() != null)
            getDockActivity().onLoadingFinished();

        isLoading = false;
        // else
        // ( (LoadingListener) super.getParentFragment() ).onLoadingFinished();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myDockActivity = (DockActivity) context;
    }


    @Override
    public void ResponseSuccess(String result, String Tag) {

    }

    @Override
    public void ResponseFailure(String message, String tag) {

    }


    @Override
    public void ResponseNoInternet(String tag) {

    }

    protected DockActivity getDockActivity() {
        return myDockActivity;
    }

    /**
     * This is called in the end to modify titlebar. after all changes.
     *
     * @param
     */
    public void setTitleBar(TitleBar titleBar) {
        titleBar.showTitleBar();
        titleBar.hideSyncBtn();
        titleBar.hideNotificationBtn();
        titleBar.hideBrivoBtn();

        // titleBar.refreshListener();
    }

    /**
     * Gets the preferred height for each item in the ListView, in pixels, after
     * accounting for screen density. ImageLoader uses this value to resize
     * thumbnail images to match the ListView item height.
     *
     * @return The preferred height in pixels, based on the current theme.
     */
    protected int getListPreferredItemHeight() {
        final TypedValue typedValue = new TypedValue();

        // Resolve list item preferred height theme attribute into typedValue
        getActivity().getTheme().resolveAttribute(
                android.R.attr.listPreferredItemHeight, typedValue, true);

        // Create a new DisplayMetrics object
        final DisplayMetrics metrics = new android.util.DisplayMetrics();

        // Populate the DisplayMetrics
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);

        // Return theme value based on DisplayMetrics
        return (int) typedValue.getDimension(metrics);
    }


    protected void notImplemented() {
        UIHelper.showLongToastInCenter(getActivity(), "Coming Soon");
    }

    protected void serverNotFound() {
        UIHelper.showLongToastInCenter(getActivity(),
                "Unable to connect to the server, "
                        + "are you connected to the internet?");
    }


    /**
     * Trigger when receives broadcasts from device to check wifi connectivity
     * using connectivity manager
     * <p>
     * Usage : registerBroadcastReceiver() on resume of activity to receive
     * notifications where needed and unregisterBroadcastReceiver() when not
     * needed.
     *
     * @return The connectivity of wifi/mobile carrier connectivity.
     */

    protected BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            boolean isWifiConnected = false;
            boolean isMobileConnected = false;

            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (networkInfo != null)
                isWifiConnected = networkInfo.isConnected();

            networkInfo = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (networkInfo != null)
                isMobileConnected = networkInfo.isConnected();

            Log.d("NETWORK STATUS", "wifi==" + isWifiConnected + " & mobile=="
                    + isMobileConnected);
        }
    };


    protected void finishLoading() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                loadingFinished();
            }
        });
    }

    public boolean checkLoading() {
        if (isLoading) {
            UIHelper.showLongToastInCenter(getActivity(),
                    R.string.message_wait);
            return false;
        } else {
            return true;
        }
    }

    protected boolean isNotLoading() {
        return !isLoading;
    }

    public void onBackPressed() {

    }


}
