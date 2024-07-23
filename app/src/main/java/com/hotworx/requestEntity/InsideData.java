package com.hotworx.requestEntity;

import java.io.Serializable;
import java.util.ArrayList;

public class InsideData implements Serializable {

    private String total_calories_burned;
    private String total_session;
    private ArrayList<YearObject> total_calories_burned_yearly_graph;
    private ArrayList<CaloriesObject> monthly_calories_session;
    private ArrayList<CaloriesObject> ninty_days_calories_session;

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

    public ArrayList<YearObject> getTotal_calories_burned_yearly_graph() {
        return total_calories_burned_yearly_graph;
    }

    public void setTotal_calories_burned_yearly_graph(ArrayList<YearObject> total_calories_burned_yearly_graph) {
        this.total_calories_burned_yearly_graph = total_calories_burned_yearly_graph;
    }

    public ArrayList<CaloriesObject> getMonthly_calories_session() {
        return monthly_calories_session;
    }

    public void setMonthly_calories_session(ArrayList<CaloriesObject> monthly_calories_session) {
        this.monthly_calories_session = monthly_calories_session;
    }

    public ArrayList<CaloriesObject> getNinty_days_calories_session() {
        return ninty_days_calories_session;
    }

    public void setNinty_days_calories_session(ArrayList<CaloriesObject> ninty_days_calories_session) {
        this.ninty_days_calories_session = ninty_days_calories_session;
    }
//    }

}
