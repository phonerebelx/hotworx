package com.hotworx.requestEntity  ;
/*
 * Created by Aamir Saleem on 10/25/2017.
   Email ID: aamirsaleem06@gmail.com
   Skype ID: aamirsaleem07
*/

import java.io.Serializable;

public class GetMonthDataModel implements Serializable {

    private String total_sessions = "";
    private String workout_calorie_burned  = "";
    private String afterburn_calorie_burned = "";
    private String total_calorie_burned = "";
    private String last_weight_reading = "";
    private String last_body_fat_reading = "";

    public String getTotal_sessions() {
        return total_sessions;
    }

    public void setTotal_sessions(String total_sessions) {
        this.total_sessions = total_sessions;
    }

    public String getWorkout_calorie_burned() {
        return workout_calorie_burned;
    }

    public void setWorkout_calorie_burned(String workout_calorie_burned) {
        this.workout_calorie_burned = workout_calorie_burned;
    }

    public String getAfterburn_calorie_burned() {
        return afterburn_calorie_burned;
    }

    public void setAfterburn_calorie_burned(String afterburn_calorie_burned) {
        this.afterburn_calorie_burned = afterburn_calorie_burned;
    }

    public String getTotal_calorie_burned() {
        return total_calorie_burned;
    }

    public void setTotal_calorie_burned(String total_calorie_burned) {
        this.total_calorie_burned = total_calorie_burned;
    }

    public String getLast_weight_reading() {
        return last_weight_reading;
    }

    public void setLast_weight_reading(String last_weight_reading) {
        this.last_weight_reading = last_weight_reading;
    }

    public String getLast_body_fat_reading() {
        return last_body_fat_reading;
    }

    public void setLast_body_fat_reading(String last_body_fat_reading) {
        this.last_body_fat_reading = last_body_fat_reading;
    }
}
