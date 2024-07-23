package com.hotworx.requestEntity  ;
/*
 * Created by Aamir Saleem on 10/25/2017.
   Email ID: aamirsaleem06@gmail.com
   Skype ID: aamirsaleem07
*/


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetMonthDataResp extends BaseModel<MonthDataLayer> implements Serializable {

    public MonthDataLayer getMonthData() {
        return data;
    }


    @SerializedName("MonthLayerData")
    private MonthDataLayer data;
    public void setMonthData(MonthDataLayer data) {
        this.data = data;
    }

}

//
//    public GetMonthDataModel getData() {
//        return data;
//    }
//
//    public void setData(GetMonthDataModel data) {
//        this.data = data;
//    }
//
//    @SerializedName("MonthData")
//    private GetMonthDataModel data;


