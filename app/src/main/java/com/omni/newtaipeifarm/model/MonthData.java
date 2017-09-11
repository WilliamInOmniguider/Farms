package com.omni.newtaipeifarm.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wiliiamwang on 08/09/2017.
 */

public class MonthData implements Serializable {

    @SerializedName("number")
    private int number;
    @SerializedName("title")
    private String title;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
