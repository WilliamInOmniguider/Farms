package com.omni.newtaipeifarm.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wiliiamwang on 06/07/2017.
 */

public class Area implements Serializable {

    public static final String ALL_AREA_ID = "0";

    @SerializedName("a_id")
    private String id;
    @SerializedName("a_name")
    private String name;

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public static class Builder {

        private Area mArea;

        public Builder() {
            if (mArea == null) {
                mArea = new Area();
            }
        }

        public Builder setId(String id) {
            mArea.setId(id);
            return this;
        }

        public Builder setName(String name) {
            mArea.setName(name);
            return this;
        }

        public Area build() {
            return mArea;
        }

    }
}
