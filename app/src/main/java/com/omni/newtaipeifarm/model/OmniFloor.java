package com.omni.newtaipeifarm.model;

/**
 * Created by wiliiamwang on 10/07/2017.
 */

public class OmniFloor {

    private String mFloorLevel;
    private String mFloorPlanId;

    public OmniFloor(String floorLevel, String floorPlanId) {
        mFloorLevel = floorLevel;
        mFloorPlanId = floorPlanId;
    }

    public String getFloorLevel() {
        return mFloorLevel;
    }

    public String getFloorPlanId() {
        return mFloorPlanId;
    }

    public void setFloorLevel(String floorLevel) {
        mFloorLevel = floorLevel;
    }

    public void setFloorPlanId(String floorPlanId) {
        mFloorPlanId = floorPlanId;
    }
}
