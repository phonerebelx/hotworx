package com.hotworx.requestEntity;

import java.io.Serializable;

public class DataLayer implements Serializable {
    private InsideData data;

    public InsideData getInsideData() {
        return data;
    }

    public void setInsideData(InsideData insideData) {
        this.data = insideData;
    }


}
