package com.hotworx.requestEntity  ;


import java.io.Serializable;

public class tempModel extends BaseModel implements Serializable {

    ViewProfileResponse data;

    public ViewProfileResponse getData() {
        return data;
    }

    public void setData(ViewProfileResponse data) {
        this.data = data;
    }
}
