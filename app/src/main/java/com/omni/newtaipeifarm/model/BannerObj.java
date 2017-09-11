package com.omni.newtaipeifarm.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wiliiamwang on 31/08/2017.
 */

public class BannerObj implements Serializable {

    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String imageURL;

    public BannerObj(String titleText, String url) {
        this.title = titleText;
        this.imageURL = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
