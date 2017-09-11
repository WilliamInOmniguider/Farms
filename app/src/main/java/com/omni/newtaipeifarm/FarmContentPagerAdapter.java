package com.omni.newtaipeifarm;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.SearchFarmResult;
import com.omni.newtaipeifarm.view.FarmFoodFragment;
import com.omni.newtaipeifarm.view.FarmListFragment;

/**
 * Created by wiliiamwang on 28/06/2017.
 */

public class FarmContentPagerAdapter extends FragmentPagerAdapter {

    public interface FarmContentPagerAdapterListener {
        void onClickFarmList(Farm farm);

        void onClickMore(Farm farm);

        void onClickSearch(String selectedAreaId, String selectedCategoryId, int searchRange, String keywords);

        void onPOIItemClick(SearchFarmResult result);

        void onPOIItemMoreClick(SearchFarmResult result);
    }

    private Context mContext;
    private FarmContentPagerAdapterListener mListener;

    public FarmContentPagerAdapter(FragmentManager manager, Context context, FarmContentPagerAdapterListener listener) {
        super(manager);
        mContext = context;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return FarmContentPagerModule.values().length;
    }

    @Override
    public Fragment getItem(int position) {

//        if (position == FarmContentPagerModule.MAP.ordinal()) {
//
//            return FarmMapFragment.newInstance(new FarmMapFragment.FarmMapListener() {
//                @Override
//                public void onPOIItemClick(SearchFarmResult result) {
//                    mListener.onPOIItemClick(result);
//                    Log.e("@W@", "  ");
//                }
//
//                @Override
//                public void onPOIItemMoreClick(SearchFarmResult result) {
//                    mListener.onPOIItemMoreClick(result);
//                }
//            });
//        } else if (position == FarmContentPagerModule.LIST.ordinal()) {
//
//            return FarmListFragment.newInstance(new FarmListFragment.FarmListListener() {
//                @Override
//                public void onFarmsItemClick(Farm farm) {
//                    mListener.onClickFarmList(farm);
//                }
//
//                @Override
//                public void onItemMoreClick(Farm farm) {
//                    mListener.onClickMore(farm);
//                }
//            });
//        } else if (position == FarmContentPagerModule.SEARCH.ordinal()) {
//
//            return FarmSearchFragment.newInstance(new FarmSearchFragment.FarmSearchListener() {
//                @Override
//                public void onClickSearch(String areaId, String categoryId, int range, String keywords) {
//                    mListener.onClickSearch(areaId, categoryId, range, keywords);
//                }
//            });
//        } else {
//            return null;
//        }

        if (position == FarmContentPagerModule.LIST.ordinal()) {

            return FarmListFragment.newInstance(new FarmListFragment.FarmListListener() {
                @Override
                public void onFarmsItemClick(Farm farm) {
                    mListener.onClickFarmList(farm);
                }

                @Override
                public void onItemMoreClick(Farm farm) {
                    mListener.onClickMore(farm);
                }
            });
        } else if (position == FarmContentPagerModule.FIND_FOOD.ordinal()) {

            return FarmFoodFragment.newInstance();
        }
        else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(FarmContentPagerModule.values()[position].getTitleResId());
    }
}
