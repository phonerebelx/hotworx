package com.hotworx.requestEntity  ;
/*
 * Created by Aamir Saleem on 11/2/2017.
   Email ID: aamirsaleem06@gmail.com
   Skype ID: aamirsaleem07
*/

import java.io.Serializable;

public class BodyFatGraphModel implements Serializable {

    private String day;
    private String body_fat;
    private String weight;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBody_fat() {

        if (body_fat != null)
            body_fat = body_fat.substring(0,body_fat.length() - 1);

        return body_fat;
    }

    public void setBody_fat(String body_fat) {
        this.body_fat = body_fat;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

}
