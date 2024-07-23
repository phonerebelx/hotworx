package com.hotworx.requestEntity;

import java.io.Serializable;
import java.util.List;

public class GetFoodResponse implements Serializable {

    public List<Common> getCommon() {
        return this.common;
    }

    public void setCommon(List<Common> common) {
        this.common = common;
    }

    List<Common> common;

    public List<Branded> getBranded() {
        return this.branded;
    }

    public void setBranded(List<Branded> branded) {
        this.branded = branded;
    }

    public List<Branded> getFoods() {
        return this.foods;
    }

    public void setFoods(List<Branded> foods) {
        this.foods = foods;
    }

    List<Branded> branded;
    List<Branded> foods;

}
