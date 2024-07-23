package com.hotworx.requestEntity;

import java.io.Serializable;

public class NinetyDaysData implements Serializable {
    private DataDetails data;

    public DataDetails getData() {
        return data;
    }

    public void setData(DataDetails data) {
        this.data = data;
    }
}