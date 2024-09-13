package com.passio.modulepassio.helpers;

import android.app.Dialog;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.passio.modulepassio.R;
import com.passio.modulepassio.interfaces.ApiListener;
import com.passio.modulepassio.interfaces.webServiceResponseLisener;
import com.passio.modulepassio.models.HotsquadList.Passio.ErrorResponseEnt;
import com.passio.modulepassio.requestEntity.BaseModel;
import com.passio.modulepassio.retrofit.GsonFactory;
import com.passio.modulepassio.retrofit.WebService;
import com.passio.modulepassio.ui.DockActivity;
import com.passio.modulepassio.ui.util.Constant;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                            if (tag.equals(Constant.WebServiceConstants.GET_PASSIO_LIST)){
                                serviceResponseLisener.ResponseSuccess(responseBody, tag);
                            } else {
                                BaseModel baseModel = GsonFactory.getConfiguredGson().fromJson(responseBody, BaseModel.class);
                                if (baseModel.getAllData() != null && !baseModel.getAllData().isEmpty()) {
                                    serviceResponseLisener.ResponseSuccess(responseBody, tag);
                                } else {
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
//                            Utils.customToast(context, context.getString(R.string.error_failure));
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
//                    Utils.customToast(context, t.toString());
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
