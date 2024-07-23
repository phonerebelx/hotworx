package com.hotworx.requestEntity;

import java.io.Serializable;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.List;

public class GetIntermittentFood extends BaseModel<GetIntermittentFood> implements Serializable {
    private List<Food_list> food_list;

    private String message;

    private Boolean status;

    public List<Food_list> getFood_list() {
        return this.food_list;
    }

    public void setFood_list(List<Food_list> food_list) {
        this.food_list = food_list;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static class Food_list implements Serializable {
        private String product_id;

        private Integer total_qty;

        private Integer calories;

        private String product_name;

        public String getProduct_id() {
            return this.product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public Integer getTotal_qty() {
            return this.total_qty;
        }

        public void setTotal_qty(Integer total_qty) {
            this.total_qty = total_qty;
        }

        public Integer getCalories() {
            return this.calories;
        }

        public void setCalories(Integer calories) {
            this.calories = calories;
        }

        public String getProduct_name() {
            return this.product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }
    }
}

