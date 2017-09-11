package com.omni.newtaipeifarm.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wiliiamwang on 11/09/2017.
 */

public class AreaFarm implements Serializable {

    @SerializedName("a_id")
    private String areaId;
    @SerializedName("a_name")
    private String areaName;
    @SerializedName("store")
    private Farm[] stores;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Farm[] getStores() {
        return stores;
    }

    public void setStores(Farm[] stores) {
        this.stores = stores;
    }
}
