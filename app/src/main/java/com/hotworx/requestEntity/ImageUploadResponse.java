package com.hotworx.requestEntity  ;


import java.io.Serializable;

public class ImageUploadResponse extends BaseModel<ImageUploadResponse> implements Serializable {

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    private String filepath;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    private String success;

}
