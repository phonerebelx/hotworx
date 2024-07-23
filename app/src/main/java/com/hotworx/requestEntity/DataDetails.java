package com.hotworx.requestEntity;
import java.io.Serializable;

public class DataDetails implements Serializable {
    private String start_date;
    private String end_date;
    private String total_days;
    private String current_calories;
    private String min_value;
    private String max_value;
    private String current_level;


    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getTotal_days() {
        return total_days;
    }

    public void setTotal_days(String total_days) {
        this.total_days = total_days;
    }

    public String getCurrent_calories() {
        return current_calories;
    }

    public void setCurrent_calories(String current_calories) {
        this.current_calories = current_calories;
    }

    public String getMin_value() {
        return min_value;
    }

    public void setMin_value(String min_value) {
        this.min_value = min_value;
    }

    public String getMax_value() {
        return max_value;
    }

    public void setMax_value(String max_value) {
        this.max_value = max_value;
    }

    public String getCurrent_level() {
        return current_level;
    }

    public void setCurrent_level(String current_level) {
        this.current_level = current_level;
    }




    // Generate getters and setters for these fields
    // ...
}
