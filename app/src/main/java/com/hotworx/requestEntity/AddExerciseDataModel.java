package com.hotworx.requestEntity;

import java.util.ArrayList;

public class AddExerciseDataModel {
    public String getRecording_date() {
        return this.recording_date;
    }

    public void setRecording_date(String recording_date) {
        this.recording_date = recording_date;
    }

    String recording_date;

    public String getLocal_date() {
        return this.local_date;
    }

    public void setLocal_date(String local_date) {
        this.local_date = local_date;
    }

    String local_date;

//    public String getUser_id() {
//        return this.user_id;
//    }
//
//    public void setUser_id(String user_id) {
//        this.user_id = user_id;
//    }
//
//    String user_id;

    public ArrayList<CartDataExercise> getCart_data() {
        return this.cart_data;
    }

    public void setCart_data(ArrayList<CartDataExercise> cart_data) {
        this.cart_data = cart_data;
    }

    ArrayList<CartDataExercise> cart_data;

}
