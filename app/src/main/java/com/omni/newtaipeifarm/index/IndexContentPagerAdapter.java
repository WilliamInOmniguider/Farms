package com.omni.newtaipeifarm.index;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.omni.newtaipeifarm.model.IndexPagerModule;

/**
 * Created by wiliiamwang on 07/09/2017.
 */

public class IndexContentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public IndexContentPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        mContext = context;
    }

    @Override
    public int getCount() {
        return IndexPagerModule.values().length;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == IndexPagerModule.INDEX_FARM.ordinal()) {
            return IndexFarmFragment.newInstance();
        } else if (position == IndexPagerModule.INDEX_FOOD.ordinal()) {
            return IndexFoodFragment.newInstance();
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(IndexPagerModule.values()[position].getTitleResId());
    }
}
