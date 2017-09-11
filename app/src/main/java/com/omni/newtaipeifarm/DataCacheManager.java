package com.omni.newtaipeifarm;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.omni.newtaipeifarm.model.Area;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.FarmCategory;
import com.omni.newtaipeifarm.model.OmniFloor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wiliiamwang on 05/07/2017.
 */

public class DataCacheManager {

    private static DataCacheManager sDataCacheManager;

    public static DataCacheManager getInstance() {
        if (sDataCacheManager == null) {
            sDataCacheManager = new DataCacheManager();
        }
        return sDataCacheManager;
    }

    private Area[] mAllAreas = null;
    private FarmCategory[] mAllCategories = null;

    // key : floorNumber, value : route point list on this floor
    private Map<String, List<LatLng>> mFloorRoutePointsMap;

    // key : floorNumber, value : arrow markers on this floor
    private Map<String, List<Marker>> mFloorArrowMarkersMap;

    private String mUserCurrentFloorLevel;
    private String mUserCurrentFloorPlanId;
    private OmniFloor mCurrentShowFloor;

    public Area[] getAllAreas() {
        return mAllAreas;
    }

    public void setAllAreas(Area[] allAreas) {
        this.mAllAreas = allAreas;
    }

    public FarmCategory[] getAllCategories() {
        return mAllCategories;
    }

    public void setAllCategories(FarmCategory[] allCategories) {
        this.mAllCategories = allCategories;
    }

    public void setFloorRoutePointsMap(String floorNumber, List<LatLng> routePointList) {
        if (mFloorRoutePointsMap == null) {
            mFloorRoutePointsMap = new HashMap<>();
        }
        List<LatLng> list = new ArrayList<>(routePointList);
        mFloorRoutePointsMap.put(floorNumber, list);
    }

    @Nullable
    public List<LatLng> getFloorRoutePointList(String floorNumber) {
        return mFloorRoutePointsMap == null ? null : mFloorRoutePointsMap.get(floorNumber);
    }

    @Nullable
    public Map<String, List<LatLng>> getFloorRoutePointsMap() {
        return mFloorRoutePointsMap;
    }

    @Nullable
    public List<LatLng> getUserCurrentFloorRoutePointList() {
        return mFloorRoutePointsMap == null ? null : mFloorRoutePointsMap.get(mUserCurrentFloorLevel);
    }

    @Nullable
    public List<Marker> getFloorArrowMarkerList(String floorNumber) {
        return mFloorArrowMarkersMap == null ? null : mFloorArrowMarkersMap.get(floorNumber);
    }

    @Nullable
    public List<Marker> getCurrentFloorArrowMarkerList() {
        return mFloorArrowMarkersMap == null ? null : mFloorArrowMarkersMap.get(mUserCurrentFloorLevel);
    }

    public Map<String, List<Marker>> getFloorArrowMarkersMap() {
        if (mFloorArrowMarkersMap == null) {
            mFloorArrowMarkersMap = new HashMap<>();
        }
        return mFloorArrowMarkersMap;
    }

    public void setFloorArrowMarkersMap(String floorNumber, List<Marker> markerList) {
        if (mFloorArrowMarkersMap == null) {
            mFloorArrowMarkersMap = new HashMap<>();
        }
        mFloorArrowMarkersMap.put(floorNumber, markerList);
    }

    @NonNull
    public String getUserCurrentFloorLevel() {
        return (mUserCurrentFloorLevel == null) ? "1" : mUserCurrentFloorLevel;
    }

    public void setUserCurrentFloorLevel(String userCurrentFloorLevel) {
        mUserCurrentFloorLevel = userCurrentFloorLevel;
    }

    public void setUserCurrentFloorPlanId(String planId) {
        mUserCurrentFloorPlanId = planId;
    }

    @Nullable
    public String getUserCurrentFloorPlanId() {
        return mUserCurrentFloorPlanId;
    }

    @Nullable
    public OmniFloor getCurrentShowFloor() {
        return mCurrentShowFloor;
    }

    public void setCurrentShowFloor(OmniFloor floor) {
        mCurrentShowFloor = floor;
    }
}
