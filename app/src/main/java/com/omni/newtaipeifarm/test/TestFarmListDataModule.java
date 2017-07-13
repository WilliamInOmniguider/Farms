package com.omni.newtaipeifarm.test;

import com.omni.newtaipeifarm.R;

/**
 * Created by wiliiamwang on 28/06/2017.
 */

public enum TestFarmListDataModule {

    FIRST("內湖清香農場", "A story of beauty and illusion", R.mipmap.farm_icon_1),
    SECOND("內湖草莓園", "A snowboarding odyssey", R.mipmap.farm_icon_2),
    THIRD("野草花果有機農場", "Every choice matters", R.mipmap.farm_icon_3),
    FOURTH("莓圃觀光休閒農園", "Time-bending challenges", R.mipmap.farm_icon_4),
    THIRD_T("野草花果有機農場", "Every choice matters", R.mipmap.farm_icon_3),
    SECOND_T("內湖草莓園", "A snowboarding odyssey", R.mipmap.farm_icon_2),
    FIRST_T("內湖清香農場", "A story of beauty and illusion", R.mipmap.farm_icon_1),
    THIRD_TT("野草花果有機農場", "Every choice matters", R.mipmap.farm_icon_3),
    SECOND_TT("內湖草莓園", "A snowboarding odyssey", R.mipmap.farm_icon_2),
    FIRST_TT("內湖清香農場", "A story of beauty and illusion", R.mipmap.farm_icon_1),
    THIRD_TTT("野草花果有機農場", "Every choice matters", R.mipmap.farm_icon_3),
    SECOND_TTT("內湖草莓園", "A snowboarding odyssey", R.mipmap.farm_icon_2),
    FIRST_TTT("內湖清香農場", "A story of beauty and illusion", R.mipmap.farm_icon_1);

    private String mTitle;
    private String mSubtitle;
    private int mPicResId;

    TestFarmListDataModule(String title, String subtitle, int picResId) {
        mTitle = title;
        mSubtitle = subtitle;
        mPicResId = picResId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public int getPicResId() {
        return mPicResId;
    }
}
