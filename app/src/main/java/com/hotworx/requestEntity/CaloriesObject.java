package com.hotworx.requestEntity  ;

import java.io.Serializable;


public class CaloriesObject implements Serializable {

    private String activity_id;
    private String date;
    private String calories_forty_session_burned;
    private String calories_sixty_session_burned_hour;

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCalories_forty_session_burned() {
        return calories_forty_session_burned;
    }

    public void setCalories_forty_session_burned(String calories_forty_session_burned) {
        this.calories_forty_session_burned = calories_forty_session_burned;
    }

    public String getCalories_sixty_session_burned_hour() {
        return calories_sixty_session_burned_hour;
    }

    public void setCalories_sixty_session_burned_hour(String calories_sixty_session_burned_hour) {
        this.calories_sixty_session_burned_hour = calories_sixty_session_burned_hour;
    }
}
