package com.hotworx.requestEntity  ;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BodyFatGraphOPOJO extends BaseModel<BodyFatGraphDataLayer> implements Serializable {


    @SerializedName("ViewBodyFatGraphData")
    private BodyFatGraphDataLayer data;

    public BodyFatGraphDataLayer getBodyFatDataLayer() {
        return data;
    }

    public void setBodyFatDataLayer(BodyFatGraphDataLayer data) {
        this.data = data;
    }



}


