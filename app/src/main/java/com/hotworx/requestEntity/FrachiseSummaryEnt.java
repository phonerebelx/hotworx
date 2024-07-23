package com.hotworx.requestEntity;

import com.google.gson.annotations.SerializedName;

public class FrachiseSummaryEnt {
    @SerializedName("summary")
    SummaryPOJO summaryPOJO;

    public SummaryPOJO getSummaryPOJO() {
        return summaryPOJO;
    }

    public void setSummaryPOJO(SummaryPOJO summaryPOJO) {
        this.summaryPOJO = summaryPOJO;
    }
}
