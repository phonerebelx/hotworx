package com.hotworx.requestEntity  ;



import java.io.Serializable;

public class ViewCaloriesResponse extends BaseModel implements Serializable {

    private String workout_calories_burned;
    private String total_one_hour_burned;
    private String total_calories_burned;
    private String total_session;
    private String avg_calorie_burned;

    public String getWorkout_calories_burned() {
        return workout_calories_burned;
    }

    public void setWorkout_calories_burned(String workout_calories_burned) {
        this.workout_calories_burned = workout_calories_burned;
    }

    public String getTotal_one_hour_burned() {
        return total_one_hour_burned;
    }

    public void setTotal_one_hour_burned(String total_one_hour_burned) {
        this.total_one_hour_burned = total_one_hour_burned;
    }

    public String getTotal_calories_burned() {
        return total_calories_burned;
    }

    public void setTotal_calories_burned(String total_calories_burned) {
        this.total_calories_burned = total_calories_burned;
    }

    public String getTotal_session() {
        return total_session;
    }

    public void setTotal_session(String total_session) {
        this.total_session = total_session;
    }

    public String getAvg_calorie_burned() {
        return avg_calorie_burned;
    }

    public void setAvg_calorie_burned(String avg_calorie_burned) {
        this.avg_calorie_burned = avg_calorie_burned;
    }

    public String getLast_workout_date() {
        return last_workout_date;
    }

    public void setLast_workout_date(String last_workout_date) {
        this.last_workout_date = last_workout_date;
    }

    private String last_workout_date;

}
