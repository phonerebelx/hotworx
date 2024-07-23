package com.hotworx.requestEntity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MonthDataLayer implements Serializable {
    @SerializedName("MonthData")
    private GetMonthDataModel data;

    public GetMonthDataModel getInsideData() {
        return data;
    }

    public void setInsideData(GetMonthDataModel data) {
        this.data = data;
    }
}
