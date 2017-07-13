package com.omni.newtaipeifarm;

/**
 * Created by wiliiamwang on 28/06/2017.
 */

public enum FarmContentPagerModule {

    LIST(R.string.farm_content_pager_title_list, R.layout.pager_farm_list_layout),
//    CENTER_TEST(R.string.more, R.layout.pager_farm_search_layout),
    SEARCH(R.string.farm_content_pager_title_search, R.layout.pager_farm_search_layout);

    private int mTitleResId;
    private int mLayoutResId;

    FarmContentPagerModule(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
