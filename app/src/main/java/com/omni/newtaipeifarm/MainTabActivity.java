package com.omni.newtaipeifarm;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.omni.newtaipeifarm.home.HomeFragment;
import com.omni.newtaipeifarm.index.IndexFragment;
import com.omni.newtaipeifarm.model.OmniEvent;
import com.omni.newtaipeifarm.popularity.PopularityFragment;
import com.omni.newtaipeifarm.search.SearchFragment;
import com.omni.newtaipeifarm.shop.ShopFragment;
import com.omni.newtaipeifarm.tool.DialogTools;
import com.omni.newtaipeifarm.tool.FarmText;
import com.omni.newtaipeifarm.tool.Tools;
import com.omni.newtaipeifarm.view.FarmInfoFragment;
import com.omni.newtaipeifarm.view.FarmWebViewFragment;
import com.omni.newtaipeifarm.view.SearchResultFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wiliiamwang on 28/08/2017.
 */

public class MainTabActivity extends AppCompatActivity implements TabSelectListener,
        IARegion.Listener,
        IALocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private FragmentTabHost mTabHost;
//    private EventBus mEventBus;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    private IALocationManager mIALocationManager;
    private IAResourceManager mIAResourceManager;

//    private FirebaseAnalytics mFirebaseAnalytics;

    private boolean mIsIndoor = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (mEventBus != null) {
//            mEventBus.unregister(this);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkLocationService();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (mEventBus == null) {
//            mEventBus = EventBus.getDefault();
//        }
//        mEventBus.register(this);

//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.main_tab_activity_view);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);

        mTabHost.addTab(mTabHost.newTabSpec(FarmText.TAB_NAME_HOME).setIndicator(generateTabView(R.string.action_bar_title_home, R.mipmap.button_home)),
                HomeFragment.class,
                null);

        mTabHost.addTab(mTabHost.newTabSpec(FarmText.TAB_NAME_POPULARITY).setIndicator(generateTabView(R.string.action_bar_title_popularity, R.mipmap.button_hot)),
                PopularityFragment.class,
                null);

        mTabHost.addTab(mTabHost.newTabSpec(FarmText.TAB_NAME_SEARCH).setIndicator(generateTabView(R.string.action_bar_title_search, R.mipmap.button_search)),
                SearchFragment.class,
                null);

        mTabHost.addTab(mTabHost.newTabSpec(FarmText.TAB_NAME_SHOP).setIndicator(generateTabView(R.string.action_bar_title_shop, R.mipmap.button_ec)),
                ShopFragment.class,
                null);

        mTabHost.addTab(mTabHost.newTabSpec(FarmText.TAB_NAME_INDEX).setIndicator(generateTabView(R.string.action_bar_title_index, R.mipmap.button_more)),
                IndexFragment.class,
                null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                updateTabView(mTabHost);

                if (getSupportFragmentManager().findFragmentByTag(FarmWebViewFragment.TAG) != null) {
                    getSupportFragmentManager().popBackStack();
                } else if (getSupportFragmentManager().findFragmentByTag(FarmInfoFragment.TAG) != null) {
                    getSupportFragmentManager().popBackStack();
                } else if (getSupportFragmentManager().findFragmentByTag(SearchResultFragment.TAG) != null) {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        mTabHost.onTabChanged(FarmText.TAB_NAME_HOME);
    }

    private View generateTabView(@StringRes int strId, @DrawableRes int iconResId) {
        TextView tv = new TextView(this);
        tv.setHeight(Tools.getInstance().getTabBarHeight(this));
        tv.setPadding(0, Tools.getInstance().dpToIntPx(this, 5), 0, Tools.getInstance().dpToIntPx(this, 5));
        tv.setText(getString(strId));
        tv.setGravity(Gravity.CENTER);
        tv.setCompoundDrawablesWithIntrinsicBounds(null, Tools.getInstance().getDrawable(this, iconResId), null, null);
        tv.setBackgroundResource(R.color.farm_color);

        return tv;
    }

    private void updateTabView(TabHost tabHost) {
        TextView tv;
        int currentIndex = tabHost.getCurrentTab();
        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            tv = (TextView) tabHost.getTabWidget().getChildTabViewAt(i);
            tv.setCompoundDrawablesWithIntrinsicBounds(null, Tools.getInstance().getDrawable(this, getTabIcon(i, (currentIndex == i))), null, null);
        }
    }

    private
    @DrawableRes
    int getTabIcon(int tabIndex, boolean isSelected) {
        switch (tabIndex) {
            case 0:
                return isSelected ? R.mipmap.button_home : R.mipmap.button_home;

            case 1:
                return isSelected ? R.mipmap.button_hot : R.mipmap.button_hot;

            case 2:
                return isSelected ? R.mipmap.button_search : R.mipmap.button_search;

            case 3:
                return isSelected ? R.mipmap.button_ec : R.mipmap.button_ec;

            case 4:
                return isSelected ? R.mipmap.button_more : R.mipmap.button_more;

//            case 0:
//                return isSelected ? R.mipmap.button_home : R.mipmap.button_home;
//
//            case 1:
//                return isSelected ? R.mipmap.button_search : R.mipmap.button_search;
//
//            case 2:
//                return isSelected ? R.mipmap.button_ec : R.mipmap.button_ec;

            default:
                return isSelected ? R.mipmap.button_more : R.mipmap.button_more;
        }
    }

    @Override
    public void onSwitchTab(String tabName) {
        if (mTabHost != null) {
            mTabHost.setCurrentTabByTag(tabName);
        }
    }

    private void checkLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ensurePermissions();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("位置服務尚未開啟，請設定");
            dialog.setPositiveButton(R.string.open_settings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
//                    DialogTools.getInstance().showErrorMessage(MainTabActivity.this,
//                            getString(R.string.dialog_title_text_normal_error),
//                            getString(R.string.not_open_location_setting_message));
                }
            });
            dialog.show();
        }
    }

    private void ensurePermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FarmText.REQUEST_FINE_LOCATION);
        } else {
            getLocationFromGoogle();

            // TODO: has leak here
//            requestIndoorPosition();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FarmText.REQUEST_COARSE_LOCATION) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //                initIALocationManager();
                getLocationFromGoogle();

//                requestIndoorPosition();
            } else {
                DialogTools.getInstance().showErrorMessage(this,
                        getString(R.string.dialog_title_text_normal_error),
                        getString(R.string.not_open_location_setting_message));
            }
        } else if (requestCode == FarmText.REQUEST_FINE_LOCATION) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    // TODO change user location getting method from GoogleApi
//                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    getLocationFromGoogle();

                    requestIndoorPosition();
                }
            } else {
                Log.e("@W@", "request permission result FINE_LOCATION deny");
                DialogTools.getInstance().showErrorMessage(this,
                        getString(R.string.dialog_title_text_normal_error),
                        getString(R.string.not_open_location_setting_message));
            }
        }
    }

    private void getLocationFromGoogle() {
        Log.e("@W@", "getLocationFromGoogle mGoogleApiClient == null : " + (mGoogleApiClient == null));
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private void requestIndoorPosition() {

        initIALocationManager();
        IALocationRequest request = IALocationRequest.create();
        request.setFastestInterval(1000);
        request.setSmallestDisplacement(0.6f);

        mIALocationManager.removeLocationUpdates(this);
        mIALocationManager.requestLocationUpdates(request, this);
    }

    private void initIALocationManager() {
        if (mIALocationManager == null) {
            mIALocationManager = IALocationManager.create(this);
        } else {
            mIALocationManager.unregisterRegionListener(this);
        }
        mIALocationManager.registerRegionListener(this);
        if (mIAResourceManager == null) {
            mIAResourceManager = IAResourceManager.create(this);
        }
    }

    /**
     * Google map GoogleApiClient.ConnectionCallbacks callback
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    FarmText.REQUEST_FINE_LOCATION);
        } else {

            if (mLocationRequest == null) {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(1000);
                mLocationRequest.setFastestInterval(1000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("@W@", "Google onConnectedSuspended");
    }

    /**
     * Google map GoogleApiClient.OnConnectionFailedListener callback
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        DialogTools.getInstance().showErrorMessage(this, R.string.dialog_title_text_normal_error, connectionResult.getErrorMessage());
    }

    /**
     * GPS LocationListener callback
     */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        EventBus.getDefault().post(new OmniEvent(OmniEvent.TYPE_USER_LOCATION_CHANGED, location));
    }

    /**
     * IndoorAtlas IARegion.Listener callback
     */
    @Override
    public void onEnterRegion(IARegion iaRegion) {
//        Tools.shouldReloadMapWhenReconnected = false;
        Log.e("@W@", "onEnterRegion : " + iaRegion);
        DialogTools.getInstance().showTestMessage(this, R.string.dialog_title_text_log, "onEnterRegion iaRegion type === " + iaRegion.getType());
        if (iaRegion.getType() == IARegion.TYPE_UNKNOWN) {

//            mIndoorTypeTV.setText("type : TYPE_UNKNOWN, id : " + iaRegion.getId());
            Log.e("@W@", "onEnterRegion unknown id === " + iaRegion.getId());
            mIsIndoor = false;
        } else if (iaRegion.getType() == IARegion.TYPE_FLOOR_PLAN) {

//            mIndoorTypeTV.setText("type : TYPE_FLOOR_PLAN, id : " + iaRegion.getId());
            Log.e("@W@", "onEnterRegion floor plan id === " + iaRegion.getId());
            mIsIndoor = true;

//            fetchFloorPlan(iaRegion.getId(), true);
        } else if (iaRegion.getType() == IARegion.TYPE_VENUE) {

//            mIndoorTypeTV.setText("type : TYPE_VENUE, id : " + iaRegion.getId());
            Log.e("@W@", "onEnterRegion venue id === " + iaRegion.getId());
            mIsIndoor = false;
        }
    }

    @Override
    public void onExitRegion(IARegion iaRegion) {
        if (iaRegion.getType() == IARegion.TYPE_UNKNOWN) {
            Log.e("@W@", "onExitRegion unknown id === " + iaRegion.getId());
            mIsIndoor = false;
        } else if (iaRegion.getType() == IARegion.TYPE_FLOOR_PLAN) {
            Log.e("@W@", "onExitRegion floor plan id === " + iaRegion.getId());
        } else if (iaRegion.getType() == IARegion.TYPE_VENUE) {
            Log.e("@W@", "onExitRegion venue id === " + iaRegion.getId());
        }
    }

    /**
     * IndoorAtlas IALocationListener callback
     */
    @Override
    public void onLocationChanged(IALocation iaLocation) {
        Log.e("@W@", "indoorAtlas LocationChanged");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
}
