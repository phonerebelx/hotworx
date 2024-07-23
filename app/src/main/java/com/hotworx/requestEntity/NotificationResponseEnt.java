package com.hotworx.requestEntity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationResponseEnt extends BaseModel<NotificationResponseEnt> {
    @SerializedName("notificationList")
    ArrayList<NotificationResponseEntData> notificationsData;

    public ArrayList<NotificationResponseEntData> getNotificationsData() {
        return notificationsData;
    }

    public void setNotificationsData(ArrayList<NotificationResponseEntData> notificationsData) {
        this.notificationsData = notificationsData;
    }
}
