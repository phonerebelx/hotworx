package com.hotworx.models;


import com.hotworx.models.DashboardData.TodaysPendingSession;
import com.hotworx.requestEntity.WorkOutPOJO;

import java.io.Serializable;

public class ActiveSessionModel implements Serializable {
    private TodaysPendingSession workout;
    private String activityId;
    private String parentActivityId;
    private Boolean isAfterBurnSession;
    private Boolean isWatchSelected;
    private long startedSessionTime = 0;

    public TodaysPendingSession getWorkout() {
        return workout;
    }

    public void setWorkout(TodaysPendingSession workout) {
        this.workout = workout;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getParentActivityId() {
        return parentActivityId;
    }

    public void setParentActivityId(String parentActivityId) {
        this.parentActivityId = parentActivityId;
    }

    public Boolean getAfterBurnSession() {
        return isAfterBurnSession;
    }

    public void setAfterBurnSession(Boolean afterBurnSession) {
        isAfterBurnSession = afterBurnSession;
    }

    public Boolean getWatchSelected() {
        return isWatchSelected;
    }

    public void setWatchSelected(Boolean watchSelected) {
        isWatchSelected = watchSelected;
    }

    public long getStartedSessionTime() {
        return startedSessionTime;
    }

    public void setStartedSessionTime(long startedSessionTime) {
        this.startedSessionTime = startedSessionTime;
    }
}
