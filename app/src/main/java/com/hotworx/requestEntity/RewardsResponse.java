package com.hotworx.requestEntity  ;


import java.io.Serializable;
import java.util.ArrayList;

public class RewardsResponse extends BaseModel implements Serializable {

    private ArrayList<RewardsPOJO> data;

    public ArrayList<RewardsPOJO> getData() {
        return data;
    }

    public void setData(ArrayList<RewardsPOJO> rewardsPOJOs) {
        this.data = data;
    }



}
