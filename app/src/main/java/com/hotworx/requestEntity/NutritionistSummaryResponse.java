package com.hotworx.requestEntity;

import java.io.Serializable;
import java.util.List;

public class NutritionistSummaryResponse  {
    private String msg;
    private List<NutritionistData> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NutritionistData> getData() {
        return data;
    }

    public void setData(List<NutritionistData> data) {
        this.data = data;
    }
}


