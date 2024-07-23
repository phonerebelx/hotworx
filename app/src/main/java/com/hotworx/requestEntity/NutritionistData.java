package com.hotworx.requestEntity;

import java.util.List;



public class NutritionistData {
    private String total_days_tracked;
    private String total_day_streak;
    private String avcerage_bmr_deficit;
    private double ninety_days_in;
    private double ninety_days_out;
    private List<BmrDeficit> bmr_deficit;
    private List<CalBurn> cal_burn;
    private List<NintyDaysCaloriesSession> ninty_days_calories_session;
    private boolean status;
    private String message;

    public String getTotal_days_tracked() {
        return total_days_tracked;
    }

    public void setTotal_days_tracked(String total_days_tracked) {
        this.total_days_tracked = total_days_tracked;
    }

    public String getTotal_day_streak() {
        return total_day_streak;
    }

    public void setTotal_day_streak(String total_day_streak) {
        this.total_day_streak = total_day_streak;
    }

    public String getAvcerage_bmr_deficit() {
        return avcerage_bmr_deficit;
    }

    public void setAvcerage_bmr_deficit(String avcerage_bmr_deficit) {
        this.avcerage_bmr_deficit = avcerage_bmr_deficit;
    }

    public double getNinety_days_in() {
        return ninety_days_in;
    }

    public void setNinety_days_in(double ninety_days_in) {
        this.ninety_days_in = ninety_days_in;
    }

    public double getNinety_days_out() {
        return ninety_days_out;
    }

    public void setNinety_days_out(double ninety_days_out) {
        this.ninety_days_out = ninety_days_out;
    }

    public List<BmrDeficit> getBmr_deficit() {
        return bmr_deficit;
    }

    public void setBmr_deficit(List<BmrDeficit> bmr_deficit) {
        this.bmr_deficit = bmr_deficit;
    }

    public List<CalBurn> getCal_burn() {
        return cal_burn;
    }

    public void setCal_burn(List<CalBurn> cal_burn) {
        this.cal_burn = cal_burn;
    }

    public List<NintyDaysCaloriesSession> getNinty_days_calories_session() {
        return ninty_days_calories_session;
    }

    public void setNinty_days_calories_session(List<NintyDaysCaloriesSession> ninty_days_calories_session) {
        this.ninty_days_calories_session = ninty_days_calories_session;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}