package com.omni.newtaipeifarm;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omni.newtaipeifarm.test.TestModule;

/**
 * Created by wiliiamwang on 27/06/2017.
 */

public class PicPagerAdapter extends PagerAdapter {

    private Context mContext;

    public PicPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return TestModule.values().length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TestModule testModule = TestModule.values()[position];

        LayoutInflater inflater = LayoutInflater.from(mContext);

//        ViewGroup layout = (ViewGroup) inflater.inflate(testModule.getLayoutResId(), container, false);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.farm_pic_layout, container, false);
        layout.setBackgroundResource(testModule.getPicResId());

        TextView titleTV = (TextView) layout.findViewById(R.id.farm_pic_layout_tv_title);
        titleTV.setText(testModule.getFarmTitle());

        container.addView(layout);

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
//        super.destroyItem(container, position, view);
        container.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return TestModule.values()[position].getTitleText();
        return "";
    }
}
