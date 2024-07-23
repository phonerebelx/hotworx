package com.hotworx.requestEntity  ;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RewardResp extends BaseModel<NinetyDaysData> implements Serializable {
    @SerializedName("NinetyDaysData")
    private NinetyDaysData data;

    public NinetyDaysData getData() {
        return data;
    }

    public void setData(NinetyDaysData data) {
        this.data = data;
    }
}