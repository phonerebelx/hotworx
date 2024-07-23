package com.hotworx.requestEntity  ;

public class FinalSummaryPojo extends BaseModel<FinalSummaryPojo> {
    CaloriesDetailPojo calorieDetails;

    public CaloriesDetailPojo getCalorieDetails() {
        return calorieDetails;
    }

    public void setCalorieDetails(CaloriesDetailPojo calorieDetails) {
        this.calorieDetails = calorieDetails;
    }
}
