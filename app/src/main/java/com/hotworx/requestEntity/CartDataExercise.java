package com.hotworx.requestEntity;

public class CartDataExercise {

    String session_name;
    float calorie_burn;
    int time_in_min;

    public CartDataExercise(String mSessionName, float mCalBurn, int mTimeInMin) {
        this.session_name = mSessionName;
        this.calorie_burn = mCalBurn;
        this.time_in_min= mTimeInMin;

    }
    public String getSession_name() {
        return this.session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }


    public float getCalorie_burn() {
        return this.calorie_burn;
    }

    public void setCalorie_burn(float calorie_burn) {
        this.calorie_burn = calorie_burn;
    }


    public int getTime_in_min() {
        return this.time_in_min;
    }

    public void setTime_in_min(int time_in_min) {
        this.time_in_min = time_in_min;
    }

}
