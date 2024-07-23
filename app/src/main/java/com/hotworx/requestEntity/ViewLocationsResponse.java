package com.hotworx.requestEntity  ;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewLocationsResponse extends BaseModel implements Serializable {

    private ArrayList<LocationsPOJO> data;

    public ArrayList<LocationsPOJO> getData() {
        return data;
    }

    public void setData(ArrayList<LocationsPOJO> data) {
        this.data = data;
    }

}
