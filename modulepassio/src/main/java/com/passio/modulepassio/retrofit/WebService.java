package com.passio.modulepassio.retrofit;

import com.hotworx.models.HotsquadList.Passio.getPassioRequest;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface WebService {

    //Passio
    @GET("https://sailposapi.uhfdemo.com/api/v1/activities/GetPassioData")
    Call<ResponseBody> getPassioData(
            @HeaderMap Map<String, String> headers,
            @Query("date") String date
    );
}
