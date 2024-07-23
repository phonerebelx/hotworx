package com.hotworx.workManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.activities.MainActivity;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.UIHelper;
import com.hotworx.requestEntity.BaseModel;
import com.hotworx.requestEntity.UpdateSessionEnt;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.retrofit.WebService;
import com.hotworx.retrofit.WebServiceFactory;
import com.hotworx.room.RoomBuilder;
import com.hotworx.room.model.SessionEnt;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on : Jan 29, 2020
 * Author     : Usman Siddiqui
 */
public class MyWorker extends Worker {
    private static final String WORK_RESULT = "work_result";
    private WebService webService;
    private String user_id;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(getApplicationContext(), WebServiceConstants.BASE_URL);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data taskData = getInputData();
        user_id = taskData.getString(Constants.USER_ID);
        uploadSessionToServer();
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Sessions Uploaded Successfully").build();
        return Result.success(outputData);
    }

    private void uploadSessionToServer() {
        List<SessionEnt> sessionEntList = RoomBuilder.getHotWorxDatabase(getApplicationContext()).getSessionTypeDao().getAllSession();
        for (SessionEnt sessionEnt : sessionEntList) {
            updateSession(sessionEnt);
        }
    }

    private void updateSession(SessionEnt sessionEnt) {
        try {
            UpdateSessionEnt updateSessionEnt = webService.updateSessions(ApiHeaderSingleton.apiHeader(getApplicationContext()),
                    "HOTWORX",
                    sessionEnt.getActivity_id(),
                    sessionEnt.getParent_id(),
                    sessionEnt.getSession_record_id(),
                    sessionEnt.getWorkout_type(),
                    sessionEnt.getStart_date(),
                    sessionEnt.getEnd_date(),
                    sessionEnt.getStart_calories(),
                    sessionEnt.getEnd_calories(),
                    String.valueOf(Integer.parseInt(sessionEnt.getEnd_calories()) - Integer.parseInt(sessionEnt.getStart_calories())),
                    sessionEnt.getIs_cancelled(), sessionEnt.getSession_type_start(),
                    sessionEnt.getSession_type_end(), user_id).execute().body();

            if (updateSessionEnt != null && updateSessionEnt.getAllData().size() > 0) {
                RoomBuilder.getHotWorxDatabase(getApplicationContext()).getSessionTypeDao().deleteSessionByActivityId(sessionEnt.getActivity_id());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}