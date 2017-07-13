package com.omni.newtaipeifarm;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.OmniEvent;
import com.omni.newtaipeifarm.model.SearchFarmResult;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.tool.DialogTools;
import com.omni.newtaipeifarm.tool.FarmText;
import com.omni.newtaipeifarm.view.FarmInfoFragment;
import com.omni.newtaipeifarm.view.SearchResultFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wiliiamwang on 03/07/2017.
 */

public class MainActivity extends AppCompatActivity implements IARegion.Listener,
        IALocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    LayoutInflater inflater;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPager mFarmContentVP;
    private TabLayout mFarmContentTL;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    private IALocationManager mIALocationManager;
    private IAResourceManager mIAResourceManager;

    private boolean mIsIndoor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_view);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }

        inflater = LayoutInflater.from(this);

        mViewPager = (ViewPager) findViewById(R.id.main_activity_view_vp_farm_pic);
        mViewPager.setAdapter(new PicPagerAdapter(this));

        mTabLayout = (TabLayout) findViewById(R.id.main_activity_view_tl);
        mTabLayout.setupWithViewPager(mViewPager, true);

        mFarmContentVP = (ViewPager) findViewById(R.id.main_activity_view_vp_farm_content);
        mFarmContentVP.setAdapter(new FarmContentPagerAdapter(this, new FarmContentPagerAdapter.FarmContentPagerAdapterListener() {
            @Override
            public void onClickFarmList(Farm farm) {
                MainActivity.this.getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_activity_view_fl, FarmInfoFragment.newInstance(farm), FarmInfoFragment.TAG).addToBackStack(null).commit();
            }

            @Override
            public void onClickMore(Farm farm) {
                MainActivity.this.getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_activity_view_fl, FarmInfoFragment.newInstance(farm), FarmInfoFragment.TAG).addToBackStack(null).commit();
            }

            @Override
            public void onClickSearch(String selectedAreaId, String selectedCategoryId, int searchRange) {

                FarmApi.getInstance().searchFarms(MainActivity.this, selectedCategoryId, mLastLocation.getLatitude(), mLastLocation.getLongitude(), searchRange, selectedAreaId,
                        new NetworkManager.NetworkManagerListener<SearchFarmResult[]>() {
                            @Override
                            public void onSucceed(SearchFarmResult[] results) {
                                MainActivity.this.getSupportFragmentManager().beginTransaction()
                                        .add(R.id.main_activity_view_fl, SearchResultFragment.newInstance(results, mLastLocation), SearchResultFragment.TAG)
                                        .addToBackStack(null)
                                        .commit();
                            }

                            @Override
                            public void onFail(VolleyError error, boolean shouldRetry) {
                                DialogTools.getInstance().showErrorMessage(MainActivity.this, R.string.dialog_title_api_error, error.getMessage());
                            }
                        });
            }
        }));

        mFarmContentTL = (TabLayout) findViewById(R.id.main_activity_view_tl_farm_content);
        mFarmContentTL.setupWithViewPager(mFarmContentVP, true);
        for (int i = 0; i < mFarmContentTL.getTabCount(); i++) {
            TabLayout.Tab tab = mFarmContentTL.getTabAt(i);
            setFarmContentTabView(tab, i == 0);
        }
        mFarmContentTL.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setFarmContentTabView(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setFarmContentTabView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        checkLocationService();
    }

    private void setFarmContentTabView(TabLayout.Tab tab, boolean isSelected) {
        boolean shouldSetView = (tab.getCustomView() == null);

        TextView tv = shouldSetView ?
                (TextView) inflater.inflate(R.layout.pager_farm_content_tab_view, null, false) :
                (TextView) tab.getCustomView();

        tv.setTextColor(ContextCompat.getColor(this, isSelected ? android.R.color.white : R.color.farm_green));
        tv.setText(tab.getText());

        if (tab.getPosition() == mFarmContentTL.getTabCount() - 1) {
            tv.setBackgroundResource(isSelected ? R.drawable.tab_right_farm_green_selected : R.drawable.tab_right_farm_green_default);
        } else if (tab.getPosition() == 0) {
            tv.setBackgroundResource(isSelected ? R.drawable.tab_left_farm_green_selected : R.drawable.tab_left_farm_green_default);
        } else {
            tv.setBackgroundResource(isSelected ? R.drawable.tab_center_farm_green_selected : R.drawable.tab_center_farm_green_default);
        }

        if (shouldSetView) {
            tab.setCustomView(tv);
        }
    }

    private void checkLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ensurePermissions();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("位置服務尚未開啟，請設定");
            dialog.setPositiveButton("open settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    DialogTools.getInstance().showErrorMessage(MainActivity.this,
                            getString(R.string.dialog_title_text_normal_error),
                            "沒有開啟位置服務，你要的地圖定位我給不起");
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

//            requestIndoorPosition();
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
                        "不給我權限，你要的地圖定位我給不起");
            }
        } else if (requestCode == FarmText.REQUEST_FINE_LOCATION) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    // TODO change user location getting method from GoogleApi
//                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    getLocationFromGoogle();

//                    requestIndoorPosition();
                }
            } else {
                Log.e("@W@", "request permission result FINE_LOCATION deny");
                DialogTools.getInstance().showErrorMessage(this,
                        getString(R.string.dialog_title_text_normal_error),
                        "不給我權限，你要的地圖定位我給不起");
            }
        }
    }

    /**
     * Google map GoogleApiClient.ConnectionCallbacks callback
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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
