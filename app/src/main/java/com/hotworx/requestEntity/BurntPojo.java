package com.hotworx.requestEntity;

/**
 * Created by Addi.
 */
public class BurntPojo {

    String activity_name;
    String calorie_burnt;
    String activity_time;


    public BurntPojo() {

    }

    public BurntPojo(String activity_name, String calorie_burnt, String activity_time) {
        this.activity_name = activity_name;
        this.calorie_burnt = calorie_burnt;
        this.activity_time = activity_time;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getCalorie_burnt() {
        return calorie_burnt;
    }

    public void setCalorie_burnt(String calorie_burnt) {
        this.calorie_burnt = calorie_burnt;
    }

    public String getActivity_time() {
        return activity_time;
    }

    public void setActivity_time(String activity_time) {
        this.activity_time = activity_time;
    }
}
