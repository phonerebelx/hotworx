package com.hotworx.requestEntity  ;


import java.io.Serializable;

public class FitBitCaloriesResp extends BaseModel implements Serializable{

    private String calories;


    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}
