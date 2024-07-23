package com.hotworx.models;

import java.util.List;

public class AddNutritionistRequestBody {
//    public String getUser_id() {
//        return this.user_id;
//    }
//
//    public void setUser_id(String user_id) {
//        this.user_id = user_id;
//    }
//
//    String user_id;

    public List<CartData> getCart_data() {
        return this.cart_data;
    }

    public void setCart_data(List<CartData> cart_data) {
        this.cart_data = cart_data;
    }

    List<CartData> cart_data;

    public String getRecording_date() {
        return this.recording_date;
    }

    public void setRecording_date(String recording_date) {
        this.recording_date = recording_date;
    }

    String recording_date;
    String intermittent_hr;

    public String getLocal_date() {
        return this.local_date;
    }

    public void setLocal_date(String local_date) {
        this.local_date = local_date;
    }

    public String getIntermittent_hr() {
        return this.intermittent_hr;
    }

    public void setIntermittent_hr(String intermittent_hr) {
        this.intermittent_hr = intermittent_hr;
    }

    String local_date;
}
