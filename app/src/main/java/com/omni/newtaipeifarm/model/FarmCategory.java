package com.omni.newtaipeifarm.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wiliiamwang on 06/07/2017.
 */

public class FarmCategory implements Serializable {

    public static final String ALL_CATEGORY_ID = "0";

    @SerializedName("category_id")
    private String id;
    @SerializedName("s_category")
    private String name;
    @SerializedName("s_category_en")
    private String nameEN;

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String getNameEN() {
        return nameEN;
    }

    private void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public static class Builder {

        private FarmCategory mFarmCategory;

        public Builder() {
            if (mFarmCategory == null) {
                mFarmCategory = new FarmCategory();
            }
        }

        public Builder setId(String id) {
            mFarmCategory.setId(id);
            return this;
        }

        public Builder setName(String name) {
            mFarmCategory.setName(name);
            return this;
        }

        public Builder setNameEN(String nameEN) {
            mFarmCategory.setNameEN(nameEN);
            return this;
        }

        public FarmCategory build() {
            return mFarmCategory;
        }

    }
}
