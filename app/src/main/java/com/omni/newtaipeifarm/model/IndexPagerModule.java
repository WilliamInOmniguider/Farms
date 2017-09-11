package com.omni.newtaipeifarm.model;

import com.omni.newtaipeifarm.R;

/**
 * Created by wiliiamwang on 07/09/2017.
 */

public enum IndexPagerModule {

    INDEX_FARM(R.string.index_pager_title_farm),
    INDEX_FOOD(R.string.index_pager_title_food);

    private int mTitleResId;

    IndexPagerModule(int titleResId) {
        mTitleResId = titleResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }
}
