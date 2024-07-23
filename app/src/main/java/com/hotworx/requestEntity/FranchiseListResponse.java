package com.hotworx.requestEntity;

import java.util.ArrayList;

public class FranchiseListResponse {
    private ArrayList<FranchiseListResponseData> data;

    public ArrayList<FranchiseListResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<FranchiseListResponseData> data) {
        this.data = data;
    }
}
