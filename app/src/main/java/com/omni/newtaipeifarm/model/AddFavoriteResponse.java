package com.omni.newtaipeifarm.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wiliiamwang on 29/08/2017.
 */

public class AddFavoriteResponse implements Serializable {

    public class FavoriteStatus {
        public static final String INSERT_SUCCESS = "INSERT SUCCESS";
        public static final String DELETE_SUCCESS = "DELETE SUCCESS";
        public static final String WRITE_FAILED = "WRITE FAILED";
    }

    @SerializedName("result")
    private String result;
    @SerializedName("status")
    private String status;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
