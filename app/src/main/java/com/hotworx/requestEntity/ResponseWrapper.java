package com.hotworx.requestEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseWrapper<T> {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    @Expose
    private T Result;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getResult() {
        return Result;
    }

    public void setResult(T result) {
        Result = result;
    }
}
