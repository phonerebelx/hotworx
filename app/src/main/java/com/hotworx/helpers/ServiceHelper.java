package com.hotworx.helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;

import com.hotworx.PagingSource.ActivityPagingSource;
import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.activities.LoginActivity;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.interfaces.ApiListener;
import com.hotworx.models.BrivoRequestModel.BrivoErrorResponse;
import com.hotworx.models.ErrorResponseEnt;
import com.hotworx.models.NewActivityModels.NinetyDaysActivity;
import com.hotworx.requestEntity.BaseModel;
import com.hotworx.interfaces.webServiceResponseLisener;
import com.hotworx.requestEntity.ExtendedBaseModel;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.retrofit.WebService;
import com.hotworx.room.RoomBuilder;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.bungeelib.Bungee;

/**
 * Created on 7/17/2017.
 */

public class ServiceHelper {
    private webServiceResponseLisener serviceResponseLisener;
    private ApiListener apiListener;
    private DockActivity context;
    private WebService webService;
    private BasePreferenceHelper prefHelper;
    private Dialog alertDialog;
    private MutableLiveData<String> apiResponse = new MutableLiveData<>();

    public ServiceHelper(ApiListener apiListener,webServiceResponseLisener serviceResponseLisener, DockActivity context, WebService webService) {
        this.serviceResponseLisener = serviceResponseLisener;
        this.apiListener = apiListener;
        this.context = context;
        this.webService = webService;
        this.prefHelper = new BasePreferenceHelper(context);
    }



    public void enqueueCall(Call<ResponseBody> call, final String tag, final boolean showLoader) {
        if (InternetHelper.CheckInternetConectivity(context)) {
            if (showLoader)
                context.onLoadingStarted();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (showLoader)
                        context.onLoadingFinished();
                    try {
                        if (response.code() == 200 && response.body() != null) {
                            String responseBody = response.body().string();
                            if (tag.equals(WebServiceConstants.GET_FOOD)
                                    || tag.equals(WebServiceConstants.GET_BARCODE_FOOD)
                                    || tag.equals(WebServiceConstants.GET_EXERCISE_DATA)
                                    || tag.equals(WebServiceConstants.MARK_NOTIFICATION_READ)
                                    || tag.equals(WebServiceConstants.GET_SQUAD_LIST)
                                    || tag.equals(WebServiceConstants.GET_NOTIFICATION_HISTORY)){
                                serviceResponseLisener.ResponseSuccess(responseBody, tag);
                            } else {
                                BaseModel baseModel = GsonFactory.getConfiguredGson().fromJson(responseBody, BaseModel.class);
                                if (baseModel.getAllData() != null && !baseModel.getAllData().isEmpty()) {
                                    serviceResponseLisener.ResponseSuccess(responseBody, tag);
                                } else {
                                    Utils.customToast(context, context.getString(R.string.error_failure));
                                    serviceResponseLisener.ResponseFailure(context.getString(R.string.error_failure), tag);
                                }
                            }
                        }
                        else if (response.code() == 551) {
                            apiListener.onFailureWithResponseCode(response.code(), "You've been forced logout", tag);
                        }
                        else if (response.code() == 552) {
                            apiListener.onFailureWithResponseCode(response.code(), "New version of app is available, please update to continue", tag);
                        } else {
                            Utils.customToast(context, context.getString(R.string.error_failure));
                            serviceResponseLisener.ResponseFailure(context.getString(R.string.error_failure), tag);
                        }
                    } catch (Exception ex) {
                        Log.d( "onResponse: ",ex.toString());
                        //Utils.customToast(context, context.getResources().getString(R.string.internal_exception_messsage));
                        serviceResponseLisener.ResponseFailure(context.getResources().getString(R.string.internal_exception_messsage), tag);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (showLoader)
                        context.onLoadingFinished();
                    t.printStackTrace();
                    Log.e(ServiceHelper.class.getSimpleName() + " by tag: " + tag, t.toString());
                    Utils.customToast(context, t.toString());
                    serviceResponseLisener.ResponseFailure(t.toString(), tag);
                }
            });
        } else {
            serviceResponseLisener.ResponseNoInternet(tag);
        }
    }


    public void enqueueCallExtended(Call<ResponseBody> call, final String tag, final boolean showLoader) {
        apiResponse = new MutableLiveData<String>();
        if (InternetHelper.CheckInternetConectivity(context)) {
            if (showLoader)
                context.onLoadingStarted();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (showLoader)
                        context.onLoadingFinished();

                    if (response.code() == 200 && response.isSuccessful()) {
                        try {
                            apiResponse.setValue(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        apiListener.onSuccess(apiResponse, tag);
                    }
                    else if (response.code() == 204){
                        apiListener.onSuccess(apiResponse, tag);
                    }
                    else if (response.code() == 551) {
                        apiListener.onFailureWithResponseCode(response.code(), "You've been forced logout", tag);
                    }

                    else if (response.code() == 552) {
                        apiListener.onFailureWithResponseCode(response.code(), "New version of app is available, please update to continue", tag);
                    }
                    else if (response.code() == 403) {

                        try {
                            String errorBody = response.errorBody().string();
                            apiResponse.setValue(errorBody);
                            BrivoErrorResponse errorResponseEnt = GsonFactory.getConfiguredGson().fromJson(apiResponse.getValue(), BrivoErrorResponse.class);
                            if (errorResponseEnt != null && errorResponseEnt.getMessage() != null) {
                                apiListener.onFailure(errorResponseEnt.getMessage(), tag);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }



                    }
                    else {
                        try {
                            String errorBody = response.errorBody().string();
                            apiResponse.setValue(errorBody);

                            ErrorResponseEnt errorResponseEnt = GsonFactory.getConfiguredGson().fromJson(apiResponse.getValue(), ErrorResponseEnt.class);
                            if (errorResponseEnt != null && errorResponseEnt.getError() != null) {
                                apiListener.onFailure(errorResponseEnt.getError(), tag);
                            } else {
                                apiListener.onFailure("Something went wrong", tag);
                            }
                        } catch (Exception e) {
                            apiListener.onFailure("Something went wrong", tag);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    apiResponse.setValue(t.getMessage());
                    apiListener.onFailure(t.getMessage(), tag);
                }
            }); // This bracket was missing in your code
        } else {
            serviceResponseLisener.ResponseNoInternet(tag);
        }
    }


}
