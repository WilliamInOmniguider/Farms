package com.omni.newtaipeifarm.test;

import com.omni.newtaipeifarm.R;

/**
 * Created by wiliiamwang on 27/06/2017.
 */

public enum TestModule {

    RED("Red", R.layout.red_item, R.mipmap.banner, "武陵農場櫻花季"),
    BLUE("Blue", R.layout.blue_item, R.mipmap.banner, "武陵農場感謝季"),
    GREEN("Green", R.layout.green_item, R.mipmap.banner, "武陵農場季季季");

    private String mTitle;
    private int mLayoutResId;
    private int mPicResId;
    private String mFarmTitle;

    TestModule(String titleText, int layoutResId, int picResId, String farmTitle) {
        mTitle = titleText;
        mLayoutResId = layoutResId;
        mPicResId = picResId;
        mFarmTitle = farmTitle;
    }

    public String getTitleText() {
        return mTitle;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

    public int getPicResId() { return mPicResId; }

    public String getFarmTitle() { return mFarmTitle; }
}
