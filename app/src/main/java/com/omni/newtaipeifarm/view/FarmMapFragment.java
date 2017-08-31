package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.maps.android.SphericalUtil;
import com.omni.newtaipeifarm.DataCacheManager;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.Area;
import com.omni.newtaipeifarm.model.FarmCategory;
import com.omni.newtaipeifarm.model.NavigationMode;
import com.omni.newtaipeifarm.model.OmniEvent;
import com.omni.newtaipeifarm.model.SearchFarmResult;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.tool.DialogTools;
import com.omni.newtaipeifarm.tool.FarmText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by wiliiamwang on 18/07/2017.
 */

public class FarmMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowCloseListener {

    public interface FarmMapListener {
        void onPOIItemClick(SearchFarmResult result);

        void onPOIItemMoreClick(SearchFarmResult result);
    }

    private static FarmMapListener mListener = null;

    private Context mContext;
    private View mView;
    private SupportMapFragment mMapFragment;
    private FloatingActionButton mCurrentPositionFAB;
    private BottomSheetBehavior mBottomSheetBehavior;
    private RelativeLayout mPoiInfoLayout;
    private TextView mPOIInfoMoreTV;
    private NetworkImageView mPOIInfoIconNIV;
    private TextView mPOIInfoTitleTV;
    private TextView mPOIInfoSubtitleTV;
    private SearchFarmResult mCurrentResult;

    private GoogleMap mMap;
    private EventBus mEventBus;
    private boolean mIsInit = true;
    private SearchFarmResult[] mResults;
    private int mNavigationMode = NavigationMode.NOT_NAVIGATION;

    private Marker mUserMarker;
    private Circle mUserAccuracyCircle;
    private Marker mNavigationMarker;
    private Location mUserLocation;

    public static FarmMapFragment newInstance(FarmMapListener listener) {
        FarmMapFragment fragment = new FarmMapFragment();

        mListener = listener;

        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OmniEvent event) {
        switch (event.getType()) {
            case OmniEvent.TYPE_USER_LOCATION_CHANGED:
                Object obj = event.getObj();
                if (obj != null && (obj instanceof Location)) {
                    mUserLocation = (Location) obj;

                    if (mIsInit) {
                        mIsInit = false;
                        getFarms(mUserLocation);
                    }
                }
                break;
        }
    }

    private void getFarms(final Location userLocation) {

        FarmApi.getInstance().searchFarms(mContext,
                FarmCategory.ALL_CATEGORY_ID,
                userLocation.getLatitude(),
                userLocation.getLongitude(),
                50,
                Area.ALL_AREA_ID,
                "",
                new NetworkManager.NetworkManagerListener<SearchFarmResult[]>() {
                    @Override
                    public void onSucceed(SearchFarmResult[] results) {
                        mResults = results;

                        mMapFragment.getMapAsync(FarmMapFragment.this);
                    }

                    @Override
                    public void onFail(VolleyError error, boolean shouldRetry) {
                        DialogTools.getInstance().showErrorMessage(mContext, R.string.dialog_title_api_error, error.getMessage());
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mEventBus != null) {
            mEventBus.unregister(this);
        }

        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("@W@", "FarmMapFragment onSaveInstanceState : ");
        for (String key : outState.keySet()) {
            Log.e("@W@", "key : " + key + ", value : " + outState.get(key));
        }
        Log.e("@W@", "#");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.pager_farm_map_layout, null, false);

            mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.pager_farm_map_layout_map);

            mCurrentPositionFAB = (FloatingActionButton) mView.findViewById(R.id.pager_farm_map_layout_fab_current_position);
            mCurrentPositionFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("@W@", "mUserLocation == null ? " + (mUserLocation == null));
                    if (mMap != null && mUserLocation != null) {
//                        LatLng current = new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude());
//                        addUserMarker(current, mUserLocation);
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, FarmText.MAP_ZOOM_LEVEL));

                        getFarms(mUserLocation);
                    }
                }
            });

//            TextView reGetFarmsTV = (TextView) mView.findViewById(R.id.pager_farm_map_layout_tv_search_farms);
//            reGetFarmsTV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

            mPoiInfoLayout = (RelativeLayout) mView.findViewById(R.id.pager_farm_map_layout_poi_info);
            mPoiInfoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onPOIItemClick(mCurrentResult);
                    }
                }
            });
            mPOIInfoIconNIV = (NetworkImageView) mPoiInfoLayout.findViewById(R.id.poi_info_view_niv);
            mPOIInfoTitleTV = (TextView) mPoiInfoLayout.findViewById(R.id.poi_info_view_tv_title);
            mPOIInfoSubtitleTV = (TextView) mPoiInfoLayout.findViewById(R.id.poi_info_view_tv_subtitle);
            mPOIInfoMoreTV = (TextView) mPoiInfoLayout.findViewById(R.id.poi_info_view_tv_more);
            mPOIInfoMoreTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onPOIItemMoreClick(mCurrentResult);
                    }
                }
            });

            mBottomSheetBehavior = BottomSheetBehavior.from((FrameLayout) mPoiInfoLayout.getParent());

            if (mEventBus == null) {
                mEventBus = EventBus.getDefault();
            }
            mEventBus.register(this);
        }

        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowCloseListener(this);

        mMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest pointOfInterest) {
                Log.e("@W@", "pointOfInterest name : " + pointOfInterest.name + ", placeId : " + pointOfInterest.placeId);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (TextUtils.isEmpty(marker.getTitle())) {
                    return true;
                } else {
                    showPOIInfo(marker);
                    return false;
                }
            }
        });

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (SearchFarmResult result : mResults) {
            addPOIMarker(result);

            builder.include(new LatLng(Double.valueOf(result.getLat()),
                    Double.valueOf(result.getLng())));
        }

        if (mUserLocation != null) {
            LatLng userPosition = new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude());
            addUserMarker(userPosition, mUserLocation);
            builder.include(userPosition);
        }

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(),
                width,
                height,
                padding/* offset from edges of the map in pixels*/);
        mMap.animateCamera(cu);
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        if (marker.getTag() == null) {
            return;
        }

        collapseBottomSheet();
    }

    private void addPOIMarker(SearchFarmResult result) {
        if (mMap == null) {
            return;
        }

        int iconResId;
        switch (result.getCatecoryEN()) {
            case FarmText.FARM_TYPE_LARGE:
                iconResId = R.mipmap.icon_fram_l;
                break;

            case FarmText.FARM_TYPE_MEDIUM:
                iconResId = R.mipmap.icon_fram_m;
                break;

            case FarmText.FARM_TYPE_LITTLE:
                iconResId = R.mipmap.icon_fram_s;
                break;

            default:
                iconResId = R.mipmap.icon_fram_s;
                break;
        }

        Marker marker = mMap.addMarker(new MarkerOptions()
                .flat(false)
                .anchor(0.5f, 0.5f)
                .position(new LatLng(Double.valueOf(result.getLat()),
                        Double.valueOf(result.getLng())))
                .icon(BitmapDescriptorFactory.fromResource(iconResId))
                .title(result.getName())
                .zIndex(FarmText.MARKER_Z_INDEX));
        marker.setTag(result);
    }

    private void addUserMarker(LatLng position, Location location) {

        if (mMap == null) {
            return;
        }

        if (mUserMarker == null) {
            mUserMarker = mMap.addMarker(new MarkerOptions()
                    .flat(true)
                    .rotation(location.getBearing())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location))
                    .anchor(0.5f, 0.5f)
                    .position(position)
                    .zIndex(FarmText.MARKER_Z_INDEX));

            mUserAccuracyCircle = mMap.addCircle(new CircleOptions()
                    .center(position)
                    .radius(location.getAccuracy())
//                    .fillColor(R.color.blue)
//                    .strokeColor(R.color.blue)
                    .strokeWidth(10)
                    .zIndex(FarmText.MARKER_Z_INDEX));
        } else {
            mUserMarker.setPosition(position);
            mUserMarker.setRotation(location.getBearing());

            mUserAccuracyCircle.setCenter(position);
            mUserAccuracyCircle.setRadius(location.getAccuracy());
        }

        if (mNavigationMode == NavigationMode.USER_IN_NAVIGATION) {
            List<LatLng> pointList = DataCacheManager.getInstance().getUserCurrentFloorRoutePointList();
            if (pointList != null) {
                testLa(pointList);
            }
        }
    }

    private void testLa(List<LatLng> pointList) {
        if (pointList != null) {
            LatLng userPosition = mUserMarker.getPosition();
            LatLng previousPoint = null;
            LatLng closestPoint = null;
            double closestDistance = -1;
            double heading = -1;

            double distanceLine;
            double distanceSegment;

            for (LatLng point : pointList) {

                if (previousPoint != null) {
//                    double distanceToLine = PolyUtil.distanceToLine(userPosition, previousPoint, point);
//                    if (closestDistance == -1 || closestDistance > distanceToLine) {
//                        closestDistance = distanceToLine;
//                        closestStartPoint = previousPoint;
//                        heading = SphericalUtil.computeHeading(previousPoint, point);
//                    }

                    double r_numerator = (userPosition.longitude - previousPoint.longitude) * (point.longitude - previousPoint.longitude) +
                            (userPosition.latitude - previousPoint.latitude) * (point.latitude - previousPoint.latitude);
                    double r_denominator = (point.longitude - previousPoint.longitude) * (point.longitude - previousPoint.longitude) +
                            (point.latitude - previousPoint.latitude) * (point.latitude - previousPoint.latitude);
                    double r = r_numerator / r_denominator;

                    double px = previousPoint.longitude + r * (point.longitude - previousPoint.longitude);
                    double py = previousPoint.latitude + r * (point.latitude - previousPoint.latitude);

                    double s = ((previousPoint.latitude - userPosition.latitude) * (point.longitude - previousPoint.longitude) -
                            (previousPoint.longitude - userPosition.longitude) * (point.latitude - previousPoint.latitude)) / r_denominator;

                    distanceLine = Math.abs(s) * Math.sqrt(r_denominator);

                    double xx = px;
                    double yy = py;

                    if ((r >= 0) && (r <= 1)) {
                        distanceSegment = distanceLine;
                    } else {

                        double dist1 = (userPosition.longitude - previousPoint.longitude) * (userPosition.longitude - previousPoint.longitude) +
                                (userPosition.latitude - previousPoint.latitude) * (userPosition.latitude - previousPoint.latitude);
                        double dist2 = (userPosition.longitude - point.longitude) * (userPosition.longitude - point.longitude) +
                                (userPosition.latitude - point.latitude) * (userPosition.latitude - point.latitude);
                        if (dist1 < dist2) {
                            xx = previousPoint.longitude;
                            yy = previousPoint.latitude;
                            distanceSegment = Math.sqrt(dist1);
                        } else {
                            xx = point.longitude;
                            yy = point.latitude;
                            distanceSegment = Math.sqrt(dist2);
                        }
                    }

                    if (closestDistance == -1 || closestDistance > distanceSegment) {
                        closestDistance = distanceSegment;
                        closestPoint = new LatLng(yy, xx);
                        heading = SphericalUtil.computeHeading(previousPoint, point);
                    }
                }
                previousPoint = point;
            }

            LatLng pointOnRoute = SphericalUtil.computeOffset(closestPoint, closestDistance, heading);
            if (mNavigationMarker == null) {
                mNavigationMarker = mMap.addMarker(new MarkerOptions().position(pointOnRoute)
                        .rotation((float) heading)
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_navigation_start)));
                mNavigationMarker.setVisible(DataCacheManager.getInstance().getCurrentShowFloor().getFloorLevel()
                        .equals(DataCacheManager.getInstance().getUserCurrentFloorLevel()));
            } else {
                mNavigationMarker.setPosition(pointOnRoute);
                mNavigationMarker.setRotation((float) heading);
                mNavigationMarker.setVisible(DataCacheManager.getInstance().getCurrentShowFloor().getFloorLevel()
                        .equals(DataCacheManager.getInstance().getUserCurrentFloorLevel()));
            }

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(pointOnRoute)
                    .zoom(FarmText.MAP_ZOOM_LEVEL)
                    .bearing((float) heading)
                    .tilt(20)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void showPOIInfo(Marker marker) {
        if (!TextUtils.isEmpty(marker.getTitle())) {
            mCurrentResult = (SearchFarmResult) marker.getTag();

            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED ||
                    mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING) {
                collapseBottomSheet();
            }
            mBottomSheetBehavior.setPeekHeight(mPoiInfoLayout.getHeight());
            mPoiInfoLayout.requestLayout();
            NetworkManager.getInstance().setNetworkImage(mContext, mPOIInfoIconNIV, mCurrentResult.getIcon());
            mPOIInfoTitleTV.setText(mCurrentResult.getName());
            mPOIInfoSubtitleTV.setText(mCurrentResult.getAddress());

            mCurrentPositionFAB.setTranslationY(-mPoiInfoLayout.getMeasuredHeight());
        }
    }

    private void collapseBottomSheet() {
        mBottomSheetBehavior.setPeekHeight(0);
        mCurrentPositionFAB.setTranslationY(0);

//        if (mOriginalPOIMarker != null) {
//            mOriginalPOIMarker = null;
//        }
    }
}
