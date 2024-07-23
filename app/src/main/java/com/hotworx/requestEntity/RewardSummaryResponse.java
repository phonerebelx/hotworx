package com.hotworx.requestEntity  ;
import java.io.Serializable;



public class RewardSummaryResponse extends BaseModel<RewardSummaryResponse> implements Serializable {

    private RewardSummaryPojo ninetyDaysSummary;

    public RewardSummaryPojo getNinetyDaysSummary() {
        return ninetyDaysSummary;
    }

    public void setNinetyDaysSummary(RewardSummaryPojo data) {
        this.ninetyDaysSummary = data;
    }
}
