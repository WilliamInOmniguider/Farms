package com.omni.newtaipeifarm.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.CommonResponse;
import com.omni.newtaipeifarm.tool.AeSimpleSHA1;
import com.omni.newtaipeifarm.tool.DialogTools;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by wiliiamwang on 05/07/2017.
 */

public class NetworkManager {

    public interface NetworkManagerListener<X> {
        void onSucceed(X object);

        void onFail(VolleyError error, boolean shouldRetry);
    }

    public static final String NETWORKMANAGER_TAG = NetworkManager.class.getSimpleName();
    public static final String DOMAIN_NAME = "http://loc.utobonus.com/";

    private final int DEFAULT_TIMEOUT = 30000;

    private static NetworkManager mNetworkManager;
    private RequestQueue mRequestQueue;
    private Gson mGson;

    public static NetworkManager getInstance() {
        if (mNetworkManager == null) {
            mNetworkManager = new NetworkManager();
        }
        return mNetworkManager;
    }

    public boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isNetworkEnable = (manager != null &&
                    manager.getActiveNetworkInfo() != null &&
                    manager.getActiveNetworkInfo().isConnectedOrConnecting());

            return isNetworkEnable;
        } else {
            return false;
        }
    }

//    public boolean hasActiveInternetConnection(final Context context) {
//        if (isNetworkAvailable(context)) {
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
//
//                    try {
//                        HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
//                        urlc.setRequestProperty("User-Agent", "Test");
//                        urlc.setRequestProperty("Connection", "close");
//                        urlc.setConnectTimeout(1500);
//                        urlc.connect();
//
//                        return (urlc.getResponseCode() == 200);
//                    } catch (IOException e) {
//                        DialogTools.getInstance().showErrorMessage(context, "No network connection", "Checking internet connection");
//                    }
//                }
//            });
//        } else {
//            DialogTools.getInstance().showErrorMessage(context, "No network connection", "Checking internet connection");
//        }
//        return false;
//    }

    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    public Gson getGson() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return mGson;
    }

    public void setNetworkImage(Context context, NetworkImageView networkImageView, String url) {

        setNetworkImage(context, networkImageView, url, R.mipmap.slime);
    }

    public void setNetworkImage(Context context, NetworkImageView networkImageView, String url, @DrawableRes int defaultIconResId) {

        if (TextUtils.isEmpty(url)) {
            networkImageView.setDefaultImageResId(R.mipmap.slime_dark);
            networkImageView.setErrorImageResId(R.mipmap.slime_dark);
        } else {
            LruImageCache lruImageCache = LruImageCache.getInstance();

            ImageLoader imageLoader = new ImageLoader(getRequestQueue(context), lruImageCache);

            networkImageView.setDefaultImageResId(defaultIconResId);
            networkImageView.setErrorImageResId(R.mipmap.slime_dark);
            networkImageView.setImageUrl(url, imageLoader);
        }
    }

    public <T> void addToRequestQueue(Request<T> req, String tag, Context context) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? NETWORKMANAGER_TAG : tag);
        getRequestQueue(context).add(req);
    }

    public <T> void addToRequestQueue(Request<T> req, Context context) {
        req.setTag(NETWORKMANAGER_TAG);
        getRequestQueue(context).add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public <T> void addJsonRequestToCommonObj(Context context,
                                              int requestMethod,
                                              final String url,
                                              Map<String, String> params,
                                              final Class<T[]> responseClass,
                                              final NetworkManagerListener<T[]> listener) {

        addJsonRequestToCommonObj(context, requestMethod, url, params, responseClass, DEFAULT_TIMEOUT, listener);
    }

    public <T> void addJsonRequestToCommonObj(final Context context,
                                              int requestMethod,
                                              final String url,
                                              Map<String, String> params,
                                              final Class<T[]> responseClass,
                                              int timeoutMs,
                                              final NetworkManagerListener<T[]> listener) {

        if (!isNetworkAvailable(context)) {
            DialogTools.getInstance().dismissProgress(context);
            listener.onFail(new VolleyError(context.getString(R.string.no_network_connection_message)), false);
            return;
        }

        String paramsString = "";
        if (params != null) {
            for (String key : params.keySet()) {
                if (!TextUtils.isEmpty(paramsString)) {
                    paramsString = paramsString + "&";
                }
                paramsString = paramsString + key + "=" + params.get(key);
            }
        }

        final String requestUrl = (TextUtils.isEmpty(paramsString)) ? url : url + "?" + paramsString;
        Log.e("@W@", "requestUrl : " + requestUrl);
        JsonObjectRequest request = new JsonObjectRequest(requestMethod,
                requestUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CommonResponse commonResponse = getGson().fromJson(response.toString(), CommonResponse.class);
                        if (commonResponse.getResult().equals("true")) {
                            String json = new Gson().toJson(commonResponse.getData());
                            T[] data = getGson().fromJson(json, responseClass);

                            listener.onSucceed(data);

                            DialogTools.getInstance().dismissProgress(context);
                        } else {
                            DialogTools.getInstance().dismissProgress(context);
                            if (commonResponse.getErrorMessage().equals("INVALID STORE")) {
                                DialogTools.getInstance().showErrorMessage(context, R.string.dialog_title_text_normal_error, R.string.dialog_msg_search_no_result);
                            } else {
                                DialogTools.getInstance().showErrorMessage(context, R.string.dialog_title_api_error, commonResponse.getErrorMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null && error.getClass().equals(TimeoutError.class)) {
                            Log.e("@W@", "*** Error NetworkResponse Timeout, timeMs : " + error.getNetworkTimeMs());
                            error = new VolleyError(context.getString(R.string.dialog_message_api_time_out));
                        }

                        listener.onFail(error, (TextUtils.isEmpty(error.getMessage()) && error.getCause() == null && error.networkResponse == null));

                        DialogTools.getInstance().dismissProgress(context);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                timeoutMs,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(request, context.getClass().getSimpleName(), context);
    }

    public <T> void addJsonRequest(Context context,
                                   int requestMethod,
                                   final String url,
                                   Map<String, String> params,
                                   final Class<T> responseClass,
                                   final NetworkManagerListener<T> listener) {

        addJsonRequest(context, requestMethod, url, params, responseClass, DEFAULT_TIMEOUT, listener);
    }

    public <T> void addJsonRequest(final Context context,
                                   int requestMethod,
                                   final String url,
                                   Map<String, String> params,
                                   final Class<T> responseClass,
                                   int timeoutMs,
                                   final NetworkManagerListener<T> listener) {

        if (!isNetworkAvailable(context)) {
            DialogTools.getInstance().dismissProgress(context);
            listener.onFail(new VolleyError(context.getString(R.string.no_network_connection_message)), false);
            return;
        }

        String paramsString = "";
        if (params != null) {
            for (String key : params.keySet()) {
                if (!TextUtils.isEmpty(paramsString)) {
                    paramsString = paramsString + "&";
                }
                paramsString = paramsString + key + "=" + params.get(key);
            }
        }

//        long currentTimestamp = System.currentTimeMillis();
//        String mac = getMacStr(currentTimestamp);
//        if (!TextUtils.isEmpty(paramsString)) {
//            paramsString = paramsString + "&";
//        }
//        paramsString = paramsString + "timestamp=" + currentTimestamp + "&mac=" + mac;

        final String requestUrl = (TextUtils.isEmpty(paramsString)) ? url : url + "?" + paramsString;
        Log.e("@W@", "requestUrl : " + requestUrl);
        JsonObjectRequest request = new JsonObjectRequest(requestMethod,
                requestUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        T object = getGson().fromJson(response.toString(), responseClass);
                        listener.onSucceed(object);

                        DialogTools.getInstance().dismissProgress(context);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null && error.getClass().equals(TimeoutError.class)) {
                            error = new VolleyError(context.getString(R.string.dialog_message_api_time_out));
                        }
                        listener.onFail(error, (TextUtils.isEmpty(error.getMessage()) && error.getCause() == null && error.networkResponse == null));

                        DialogTools.getInstance().dismissProgress(context);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                timeoutMs,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(request, context.getClass().getSimpleName(), context);
    }

    public <T> void addPostStringRequest(final Context context,
                                         final String url,
                                         final Map<String, String> params,
                                         final Class<T[]> responseClass,
                                         int timeoutMs,
                                         final NetworkManagerListener<T[]> listener) {
        if (!isNetworkAvailable(context)) {
            DialogTools.getInstance().dismissProgress(context);
            DialogTools.getInstance().showNoNetworkMessage(context);
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommonResponse commonResponse = getGson().fromJson(response.toString(), CommonResponse.class);
                        if (commonResponse.getResult().equals("true")) {
                            String json = new Gson().toJson(commonResponse.getData());
                            T[] data = getGson().fromJson(json, responseClass);

                            listener.onSucceed(data);

                            DialogTools.getInstance().dismissProgress(context);
                        } else {
                            DialogTools.getInstance().dismissProgress(context);
                            DialogTools.getInstance().showErrorMessage(context, R.string.dialog_title_api_error, commonResponse.getErrorMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("@W@", "Error NetworkResponse statusCode === " + error.networkResponse.statusCode);
                        } else {
                            if (error.getClass().equals(TimeoutError.class)) {
                                Log.e("@W@", "*** Error NetworkResponse Timeout, timeMs : " + error.getNetworkTimeMs());
                            }
                        }
                        listener.onFail(error, (TextUtils.isEmpty(error.getMessage()) && error.getCause() == null && error.networkResponse == null));

                        DialogTools.getInstance().dismissProgress(context);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                timeoutMs,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(request, context.getClass().getSimpleName(), context);
    }

    private String getMacStr(long currentTimestamp) {
        try {
            return AeSimpleSHA1.SHA1("nmpapp://" + currentTimestamp);
        } catch (NoSuchAlgorithmException e) {
            Log.e("@W@", "NoSuchAlgorithmException cause : " + e.getCause());
            return "";
        } catch (UnsupportedEncodingException e) {
            Log.e("@W@", "UnsupportedEncodingException cause : " + e.getCause());
            return "";
        }
    }
}
