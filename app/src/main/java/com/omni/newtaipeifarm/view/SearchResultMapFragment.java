package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.omni.newtaipeifarm.adapter.SearchFarmResultAdapter;
import com.omni.newtaipeifarm.model.NavigationMode;
import com.omni.newtaipeifarm.model.OmniEvent;
import com.omni.newtaipeifarm.model.SearchFarmResult;
import com.omni.newtaipeifarm.tool.FarmText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by wiliiamwang on 07/07/2017.
 */

public class SearchResultMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowCloseListener {

    public static final String TAG = "tag_search_result_map_fragment";
    private static final String S_KEY_SEARCH_FARM_MAP_RESULT = "s_key_search_farm_map_result";
    private static final String S_KEY_USER_LOCATION = "s_key_user_location";

    private int mNavigationMode = NavigationMode.NOT_NAVIGATION;

    private Context mContext;
    private View mView;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private RecyclerView mRV;
    private SearchFarmResult[] mResults;
    private EventBus mEventBus;

    private Marker mUserMarker;
    private Circle mUserAccuracyCircle;
    private Marker mNavigationMarker;
    private Location mUserLocation;

    public static SearchResultMapFragment newInstance(SearchFarmResult[] results, Location userLocation) {
        SearchResultMapFragment fragment = new SearchResultMapFragment();

        Bundle arg = new Bundle();
        arg.putSerializable(S_KEY_SEARCH_FARM_MAP_RESULT, results);
        arg.putParcelable(S_KEY_USER_LOCATION, userLocation);
        fragment.setArguments(arg);

        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OmniEvent event) {
        switch (event.getType()) {
            case OmniEvent.TYPE_USER_LOCATION_CHANGED:
                Object obj = event.getObj();
                if (obj != null && (obj instanceof Location)) {
                    mUserLocation = (Location) obj;
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mEventBus == null) {
            mEventBus = EventBus.getDefault();
        }
        mEventBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mEventBus != null) {
            mEventBus.unregister(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.search_result_map_fragment_view, null, false);
        }

        mResults = (SearchFarmResult[]) getArguments().getSerializable(S_KEY_SEARCH_FARM_MAP_RESULT);
        mUserLocation = getArguments().getParcelable(S_KEY_USER_LOCATION);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.search_result_map_fragment_view_map);
        mMapFragment.getMapAsync(this);

        TextView backTV = (TextView) mView.findViewById(R.id.search_result_map_fragment_view_tv_back);
        backTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(SearchResultMapFragment.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mRV = (RecyclerView) mView.findViewById(R.id.search_result_map_fragment_view_rv);
        mRV.setLayoutManager(new LinearLayoutManager(mContext));
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.rv_divider_farm_green));
        mRV.addItemDecoration(divider);
        mRV.setAdapter(new SearchFarmResultAdapter(mContext, mResults, new SearchFarmResultAdapter.SFRViewHolderListener() {
            @Override
            public void onClickItem(View itemView, int position) {
                ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.search_result_map_fragment_view_fl, FarmInfoFragment.newInstance(mResults[position].toFarm()), FarmInfoFragment.TAG)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onClickMore(TextView moreView, int position) {
                ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.search_result_map_fragment_view_fl, FarmInfoFragment.newInstance(mResults[position].toFarm()), FarmInfoFragment.TAG)
                        .addToBackStack(null)
                        .commit();
            }
        }));

        return mView;
    }

    /**
     * OnMapReadyCallback callback
     **/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowCloseListener(this);

        mMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest pointOfInterest) {

            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

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

    /**
     * GoogleMap.OnInfoWindowCloseListener callback
     **/
    @Override
    public void onInfoWindowClose(Marker marker) {

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

        mMap.addMarker(new MarkerOptions()
                .flat(false)
                .anchor(0.5f, 0.5f)
                .position(new LatLng(Double.valueOf(result.getLat()),
                        Double.valueOf(result.getLng())))
                .icon(BitmapDescriptorFactory.fromResource(iconResId))
                .title(result.getName())
                .zIndex(FarmText.MARKER_Z_INDEX));
    }

    private void addUserMarker(LatLng position, Location location) {
//        if (mUserMarker != null) {
//            mUserMarker.remove();
//        }

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

//        mUserMarker.setVisible(false);

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
}
