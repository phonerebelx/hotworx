package com.hotworx.requestEntity  ;

import java.io.Serializable;

public class ninetyDaysSummaryPojo implements Serializable {

    String start_date, end_date;
    double current_calories;

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

    public double getCurrent_calories() {
        return current_calories;
    }

    public void setCurrent_calories(double current_calories) {
        this.current_calories = current_calories;
    }

    public double getMin_value() {
        return min_value;
    }

    public void setMin_value(double min_value) {
        this.min_value = min_value;
    }

    public double getMax_value() {
        return max_value;
    }

    public void setMax_value(double max_value) {
        this.max_value = max_value;
    }

    public int getCurrent_level() {
        return current_level;
    }

    public void setCurrent_level(int current_level) {
        this.current_level = current_level;
    }

    public double getTotal_days() {
        return total_days;
    }

    public void setTotal_days(double total_days) {
        this.total_days = total_days;
    }

    double min_value;
    double max_value;
    int current_level;
    double total_days;

}
