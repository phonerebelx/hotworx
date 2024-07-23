package com.hotworx.requestEntity  ;


import java.io.Serializable;

public class RewardsPOJO implements Serializable {

    TypeRewardResponse type;

    public TypeRewardResponse getType() {
        return type;
    }

    public void setType(TypeRewardResponse type) {
        this.type = type;
    }
}
