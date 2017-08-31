package com.omni.newtaipeifarm.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.omni.newtaipeifarm.FarmContentPagerAdapter;
import com.omni.newtaipeifarm.PicPagerAdapter;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.SearchFarmResult;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.tool.DialogTools;
import com.omni.newtaipeifarm.view.FarmInfoFragment;
import com.omni.newtaipeifarm.view.SearchResultFragment;

/**
 * Created by wiliiamwang on 28/08/2017.
 */

public class HomeFragment extends Fragment {

    private LayoutInflater mInflater;

    private Context mContext;
    private View mView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPager mFarmContentVP;
    private TabLayout mFarmContentTL;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.home_fragment_view, null, false);

            mInflater = inflater;

            Toolbar toolbar = (Toolbar) mView.findViewById(R.id.home_fragment_view_action_bar);
            TextView titleTV = (TextView) toolbar.findViewById(R.id.farm_action_bar_tv_title);
            titleTV.setText(R.string.action_bar_title_home);

            mViewPager = (ViewPager) mView.findViewById(R.id.home_fragment_view_vp_farm_pic);
            mViewPager.setAdapter(new PicPagerAdapter(mContext));

            mTabLayout = (TabLayout) mView.findViewById(R.id.home_fragment_view_tl);
            mTabLayout.setupWithViewPager(mViewPager, true);

            mFarmContentVP = (ViewPager) mView.findViewById(R.id.home_fragment_view_vp_farm_content);
            mFarmContentVP.setAdapter(new FarmContentPagerAdapter(getChildFragmentManager(), mContext, new FarmContentPagerAdapter.FarmContentPagerAdapterListener() {
                @Override
                public void onClickFarmList(Farm farm) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.home_fragment_view_fl, FarmInfoFragment.newInstance(farm), FarmInfoFragment.TAG).addToBackStack(null).commit();
                }

                @Override
                public void onClickMore(Farm farm) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.home_fragment_view_fl, FarmInfoFragment.newInstance(farm), FarmInfoFragment.TAG).addToBackStack(null).commit();
                }

                @Override
                public void onClickSearch(String selectedAreaId, String selectedCategoryId, int searchRange, String keywords) {

//                FarmApi.getInstance().searchFarms(getActivity(), selectedCategoryId, mLastLocation.getLatitude(), mLastLocation.getLongitude(), searchRange, selectedAreaId,
//                        new NetworkManager.NetworkManagerListener<SearchFarmResult[]>() {
//                            @Override
//                            public void onSucceed(SearchFarmResult[] results) {
//                                getActivity().getSupportFragmentManager().beginTransaction()
//                                        .add(R.id.main_activity_view_fl, SearchResultMapFragment.newInstance(results, mLastLocation), SearchResultMapFragment.TAG)
//                                        .addToBackStack(null)
//                                        .commit();
//                            }
//
//                            @Override
//                            public void onFail(VolleyError error, boolean shouldRetry) {
//                                DialogTools.getInstance().showErrorMessage(getActivity(), R.string.dialog_title_api_error, error.getMessage());
//                            }
//                        });
                    FarmApi.getInstance().searchFarms(getActivity(), selectedCategoryId, selectedAreaId, keywords,
                            new NetworkManager.NetworkManagerListener<SearchFarmResult[]>() {
                                @Override
                                public void onSucceed(SearchFarmResult[] results) {
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                        .add(R.id.home_fragment_view_fl, SearchResultFragment.newInstance(results), SearchResultFragment.TAG)
                                        .addToBackStack(null)
                                        .commit();
                                }

                                @Override
                                public void onFail(VolleyError error, boolean shouldRetry) {
                                    DialogTools.getInstance().showErrorMessage(getActivity(), R.string.dialog_title_api_error, error.getMessage());
                                }
                            });
                }

                @Override
                public void onPOIItemClick(SearchFarmResult result) {
                    Log.e("@W@", "onPOIItemClick");
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.home_fragment_view_fl, FarmInfoFragment.newInstance(result.toFarm()), FarmInfoFragment.TAG).addToBackStack(null).commit();
                }

                @Override
                public void onPOIItemMoreClick(SearchFarmResult result) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.home_fragment_view_fl, FarmInfoFragment.newInstance(result.toFarm()), FarmInfoFragment.TAG).addToBackStack(null).commit();
                }
            }));

            mFarmContentTL = (TabLayout) mView.findViewById(R.id.home_fragment_view_tl_farm_content);
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

        return mView;
    }

    private void setFarmContentTabView(TabLayout.Tab tab, boolean isSelected) {
        boolean shouldSetView = (tab.getCustomView() == null);

        TextView tv = shouldSetView ?
                (TextView) mInflater.inflate(R.layout.pager_farm_content_tab_view, null, false) :
                (TextView) tab.getCustomView();

        tv.setTextColor(ContextCompat.getColor(mContext, isSelected ? R.color.farm_pager_title_color : R.color.farm_pager_title_color));
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
}
