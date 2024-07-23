package com.hotworx.requestEntity;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewActivityResponse extends BaseModel<DataLayer> implements Serializable {
    @SerializedName("ViewActivityData")
    private DataLayer data;

    public DataLayer getActivityData() {
        return data;
    }

    public void setActivityData(DataLayer data) {
        this.data = data;
    }
}
