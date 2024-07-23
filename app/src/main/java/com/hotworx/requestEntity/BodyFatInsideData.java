package com.hotworx.requestEntity;

import java.io.Serializable;
import java.util.ArrayList;

public class BodyFatInsideData implements Serializable {

    private ArrayList<BodyFatGraphModel> daily_bodyfat;

    public ArrayList<BodyFatGraphModel> getDaily_bodyfat() {
        return daily_bodyfat;
    }

    public void setDaily_bodyfat(ArrayList<BodyFatGraphModel> daily_bodyfat) {
        this.daily_bodyfat = daily_bodyfat;
    }
}
