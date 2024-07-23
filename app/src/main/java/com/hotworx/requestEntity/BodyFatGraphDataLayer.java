package com.hotworx.requestEntity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BodyFatGraphDataLayer implements Serializable {

    @SerializedName("BodyFatData")
    private BodyFatInsideData data;

    public BodyFatInsideData getInsideBodyFatData() {
        return data;
    }

    public void setInsideBodyFatData(BodyFatInsideData insideData) {
        this.data = insideData;
    }

}

