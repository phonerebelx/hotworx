package com.hotworx.workManager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.passio.modulepassio.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.requestEntity.UpdateSessionEnt;
import com.hotworx.retrofit.WebService;
import com.hotworx.retrofit.WebServiceFactory;
import com.hotworx.room.RoomBuilder;
import com.hotworx.room.model.SessionEnt;

import java.util.List;

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
                    sessionEnt.getStart_date(),
                    sessionEnt.getEnd_date(),
                    sessionEnt.getSession_record_id(),
                    sessionEnt.getWorkout_type(),
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