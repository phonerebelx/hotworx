package com.hotworx.requestEntity;

import java.io.Serializable;
import java.util.List;

public class NutritionCaloriesResponse extends BaseModel<NutritionCaloriesResponse> implements Serializable {

//    private String message;
//    private boolean status;
    List<DayData> day_data;
    UserData user_data;
    List<ExerciseData> exercise_data;

//    public boolean isStatus() {
//        return status;
//    }
//
//    public void setStatus(boolean status) {
//        this.status = status;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public List<DayData> getDay_data() {
        return this.day_data;
    }

    public void setDay_data(List<DayData> day_data) {
        this.day_data = day_data;
    }


    public UserData getUser_data() {
        return this.user_data;
    }

    public void setUser_data(UserData user_data) {
        this.user_data = user_data;
    }


    public List<ExerciseData> getExercise_data() {
        return this.exercise_data;
    }

    public void setExercise_data(List<ExerciseData> exercise_data) {
        this.exercise_data = exercise_data;
    }

}
