package com.hotworx.requestEntity;

import java.util.List;

public class ExerciseData {

    public String getRecord_id() {
        return this.record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    String record_id;

    public String getSession_name() {
        return this.session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    String session_name = "";

    public String getCalorie_burn() {
        return this.calorie_burn;
    }

    public void setCalorie_burn(String calorie_burn) {
        this.calorie_burn = calorie_burn;
    }

    String calorie_burn = "";

    public String getTime_in_min() {
        return this.time_in_min;
    }

    public void setTime_in_min(String time_in_min) {
        this.time_in_min = time_in_min;
    }

    String time_in_min;

    public String getImg_url() {
        return this.img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    String img_url;
}

//public class Root {
//    @JsonProperty("status")
//    public boolean getStatus() {
//        return this.status;
//    }
//
//    public void setStatus(boolean status) {
//        this.status = status;
//    }
//
//    boolean status;
//
//    @JsonProperty("message")
//    public String getMessage() {
//        return this.message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    String message;
//
//    public List<DayData> getDay_data() {
//        return this.day_data;
//    }
//
//    public void setDay_data(List<DayData> day_data) {
//        this.day_data = day_data;
//    }
//
//    List<DayData> day_data;
//
//    public UserData getUser_data() {
//        return this.user_data;
//    }
//
//    public void setUser_data(UserData user_data) {
//        this.user_data = user_data;
//    }
//
//    UserData user_data;
//
//    public List<ExerciseData> getExercise_data() {
//        return this.exercise_data;
//    }
//
//    public void setExercise_data(List<ExerciseData> exercise_data) {
//        this.exercise_data = exercise_data;
//    }
//
//    List<ExerciseData> exercise_data;
//}


