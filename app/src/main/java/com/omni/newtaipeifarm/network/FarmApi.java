package com.omni.newtaipeifarm.network;

import android.content.Context;
import android.provider.Settings;

import com.android.volley.Request;
import com.omni.newtaipeifarm.model.AddFavoriteResponse;
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

    public static class SortMode {
        /** Sort by last update time */
        public static final String DEFAULT = "0";
        /** Sort by popular sum */
        public static final String POPULAR = "1";
    }

    private static FarmApi mFarmApi;

    public static FarmApi getInstance() {
        if (mFarmApi == null) {
            mFarmApi = new FarmApi();
        }
        return mFarmApi;
    }

    public void getAllFarms(Context context, String sortMode, NetworkManager.NetworkManagerListener<Farm[]> listener) {
//        DialogTools.getInstance().showProgress(context);

        String url = NetworkManager.DOMAIN_NAME + "api/get_store";
        Map<String, String> params = new HashMap<>();
        params.put("circle", CIRCLE);
        params.put("poplist", sortMode);
        params.put("device_id", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        NetworkManager.getInstance().addJsonRequestToCommonObj(context, Request.Method.GET, url, params, Farm[].class, TIMEOUT, listener);
    }

    public void getAllCities(Context context, NetworkManager.NetworkManagerListener<City[]> listener) {
        DialogTools.getInstance().showProgress(context);

        String url = NetworkManager.DOMAIN_NAME + "api/get_city";

        NetworkManager.getInstance().addJsonRequestToCommonObj(context, Request.Method.GET, url, null, City[].class, TIMEOUT, listener);
    }

    public void getAllAreas(Context context, NetworkManager.NetworkManagerListener<Area[]> listener) {
        DialogTools.getInstance().showProgress(context);

        String url = NetworkManager.DOMAIN_NAME + "api/get_area";
        Map<String, String> params = new HashMap<>();
        params.put("circle", CIRCLE);

        NetworkManager.getInstance().addJsonRequestToCommonObj(context, Request.Method.GET, url, params, Area[].class, TIMEOUT, listener);
    }

    public void getAllCategories(Context context, NetworkManager.NetworkManagerListener<FarmCategory[]> listener) {
        DialogTools.getInstance().showProgress(context);

        String url = NetworkManager.DOMAIN_NAME + "api/get_category";
        Map<String, String> params = new HashMap<>();
        params.put("circle", CIRCLE);

        NetworkManager.getInstance().addJsonRequestToCommonObj(context, Request.Method.GET, url, params, FarmCategory[].class, TIMEOUT, listener);
    }

    public void searchFarms(Context context, String categoryId, String areaId, String keywords,
                            NetworkManager.NetworkManagerListener<SearchFarmResult[]> listener) {

        searchFarms(context, categoryId, 0, 0, 0, areaId, keywords, listener);
    }

    public void searchFarms(Context context, String categoryId, double lat, double lng, int range, String areaId, String keywords,
                            NetworkManager.NetworkManagerListener<SearchFarmResult[]> listener) {
        DialogTools.getInstance().showProgress(context);

        String url = NetworkManager.DOMAIN_NAME + "api/get_store";
        Map<String, String> params = new HashMap<>();
        params.put("circle", CIRCLE);
        params.put("category_id", categoryId);
        params.put("lat", String.valueOf(lat));
        params.put("lng", String.valueOf(lng));
        params.put("range", String.valueOf(range));
        params.put("a_id", areaId);
        params.put("keyword", keywords);
        params.put("device_id", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        NetworkManager.getInstance().addJsonRequestToCommonObj(context, Request.Method.GET, url, params, SearchFarmResult[].class, TIMEOUT, listener);
    }

    public void addToFavorite(Context context, String farmId,
                              NetworkManager.NetworkManagerListener<AddFavoriteResponse> listener) {
        DialogTools.getInstance().showProgress(context);

        String url = NetworkManager.DOMAIN_NAME + "api/favorite";
        Map<String, String> params = new HashMap<>();
        params.put("sid", farmId);
        params.put("device_id", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        NetworkManager.getInstance().addJsonRequest(context, Request.Method.GET, url, params, AddFavoriteResponse.class, TIMEOUT, listener);
    }

}
