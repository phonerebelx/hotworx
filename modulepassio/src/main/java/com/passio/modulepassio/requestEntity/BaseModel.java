package com.passio.modulepassio.requestEntity;

import java.io.Serializable;
import java.util.List;

public class BaseModel<T> implements Serializable {
    private boolean responseError;
    private String msg;
    private String error;
    private Integer code;

    private List<T> data;


    public List<T> getAllData() {
        return data;
    }

    public void setAllData(List<T> data) {
        this.data = data;
    }

//    public boolean isStatus() {
//        return status;
//    }
//
//    public void setStatus(boolean status) {
//        this.status = status;
//    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isResponseError() {
        return responseError;
    }

    public void setResponseError(boolean responseError) {
        this.responseError = responseError;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

