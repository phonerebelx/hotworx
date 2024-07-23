package com.hotworx.requestEntity  ;


import java.io.Serializable;
import java.util.ArrayList;

public class ViewAllSessionsPOJO extends BaseModel implements Serializable {

    public ArrayList<ViewAllSessionResponse> getData() {
        return data;
    }

    public void setData(ArrayList<ViewAllSessionResponse> data) {
        this.data = data;
    }

    private ArrayList<ViewAllSessionResponse> data;

}
