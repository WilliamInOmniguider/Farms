package com.omni.newtaipeifarm.network;

import android.content.Context;

import com.android.volley.Request;
import com.omni.newtaipeifarm.model.Area;
import com.omni.newtaipeifarm.model.City;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.FarmCategory;
import com.omni.newtaipeifarm.model.SearchFarmResult;
import com.omni.newtaipeifarm.tool.DialogTools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wiliiamwang on 05/07/2017.
 */

public class FarmApi {

    private final int TIMEOUT = 15000;
    private final String CIRCLE = "farm";

    private static FarmApi mFarmApi;

    public static FarmApi getInstance() {
        if (mFarmApi == null) {
            mFarmApi = new FarmApi();
        }
        return mFarmApi;
    }

    public void getAllFarms(Context context, NetworkManager.NetworkManagerListener<Farm[]> listener) {
//        DialogTools.getInstance().showProgress(context);

        String url = "http://loc.utobonus.com/api/get_store";
        Map<String, String> params = new HashMap<>();
        params.put("circle", CIRCLE);

        NetworkManager.getInstance().addJsonRequest(context, Request.Method.GET, url, params, Farm[].class, TIMEOUT, listener);
    }

    public void getAllCities(Context context, NetworkManager.NetworkManagerListener<City[]> listener) {
        DialogTools.getInstance().showProgress(context);

        String url = "http://loc.utobonus.com/api/get_city";

        NetworkManager.getInstance().addJsonRequest(context, Request.Method.GET, url, null, City[].class, TIMEOUT, listener);
    }

    public void getAllAreas(Context context, NetworkManager.NetworkManagerListener<Area[]> listener) {
        DialogTools.getInstance().showProgress(context);

        String url = "http://loc.utobonus.com/api/get_area";
        Map<String, String> params = new HashMap<>();
        params.put("circle", CIRCLE);

        NetworkManager.getInstance().addJsonRequest(context, Request.Method.GET, url, params, Area[].class, TIMEOUT, listener);
    }

    public void getAllCategories(Context context, NetworkManager.NetworkManagerListener<FarmCategory[]> listener) {
        DialogTools.getInstance().showProgress(context);

        String url = "http://loc.utobonus.com/api/get_category";
        Map<String, String> params = new HashMap<>();
        params.put("circle", CIRCLE);

        NetworkManager.getInstance().addJsonRequest(context, Request.Method.GET, url, params, FarmCategory[].class, TIMEOUT, listener);
    }

    public void searchFarms(Context context, String categoryId, double lat, double lng, int range, String areaId,
                            NetworkManager.NetworkManagerListener<SearchFarmResult[]> listener) {
        DialogTools.getInstance().showProgress(context);

        String url = "http://loc.utobonus.com/api/get_store";
        Map<String, String> params = new HashMap<>();
        params.put("circle", CIRCLE);
        params.put("category_id", categoryId);
        params.put("lat", String.valueOf(lat));
        params.put("lng", String.valueOf(lng));
        params.put("range", String.valueOf(range));
        params.put("a_id", areaId);

        NetworkManager.getInstance().addJsonRequest(context, Request.Method.GET, url, params, SearchFarmResult[].class, TIMEOUT, listener);
    }

}
