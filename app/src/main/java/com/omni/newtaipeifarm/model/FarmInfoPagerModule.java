package com.omni.newtaipeifarm.model;

import com.omni.newtaipeifarm.R;

/**
 * Created by wiliiamwang on 31/08/2017.
 */

public enum FarmInfoPagerModule {

    CONTACT_INFO(R.string.farm_info_pager_title_contact_info),
    FARM_INTRO(R.string.farm_info_pager_title_farm_intro);

    private int mTitleResId;

    FarmInfoPagerModule(int titleResId) {
        mTitleResId = titleResId;
    }

    public int getmTitleResId() {
        return mTitleResId;
    }
}
