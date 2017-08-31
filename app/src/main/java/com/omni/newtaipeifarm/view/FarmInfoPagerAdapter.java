package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.FarmInfoPagerModule;

/**
 * Created by wiliiamwang on 31/08/2017.
 */

public class FarmInfoPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private Farm mFarm;

    public FarmInfoPagerAdapter(FragmentManager manager, Context context, Farm farm) {
        super(manager);
        mContext = context;
        mFarm = farm;
    }

    @Override
    public int getCount() {
        return FarmInfoPagerModule.values().length;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == FarmInfoPagerModule.CONTACT_INFO.ordinal()) {
            return FarmContactInfoFragment.newInstance(mFarm);
        } else if (position == FarmInfoPagerModule.FARM_INTRO.ordinal()) {
            return FarmIntroFragment.newInstance(mFarm);
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(FarmInfoPagerModule.values()[position].getmTitleResId());
    }
}
