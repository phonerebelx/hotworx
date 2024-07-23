package com.hotworx.requestEntity;

import com.google.gson.annotations.SerializedName;

public class NotificationResponseEntData {
    @SerializedName("record_id")
    String record_id;
    @SerializedName("notification_type")
    String notification_type;
    @SerializedName("booking_id")
    String booking_id;
    @SerializedName("title")
    String title;
    @SerializedName("body")
    String body;
    @SerializedName("creation_datetime")
    String creation_datetime;

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreation_datetime() {
        return creation_datetime;
    }

    public void setCreation_datetime(String creation_datetime) {
        this.creation_datetime = creation_datetime;
    }
}
