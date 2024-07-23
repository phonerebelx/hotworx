package com.hotworx.requestEntity;


import java.util.ArrayList;
import java.util.List;

public class WeightResponse {
    private ArrayList<GetWeightResponse> data;
    private String msg;

    public ArrayList<GetWeightResponse> getData() {
        return data;
    }

    public void setData(ArrayList<GetWeightResponse> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}