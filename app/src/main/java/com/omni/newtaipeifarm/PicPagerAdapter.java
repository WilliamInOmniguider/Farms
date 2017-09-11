package com.omni.newtaipeifarm;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.omni.newtaipeifarm.model.BannerObj;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.test.TestModule;

/**
 * Created by wiliiamwang on 27/06/2017.
 */

public class PicPagerAdapter extends PagerAdapter {

    private Context mContext;
    private BannerObj[] mBanners;

    public PicPagerAdapter(Context context, BannerObj[] banners) {
        mContext = context;
        mBanners = banners;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mBanners.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        TestModule testModule = TestModule.values()[position];
        BannerObj banner = mBanners[position];

        LayoutInflater inflater = LayoutInflater.from(mContext);

//        ViewGroup layout = (ViewGroup) inflater.inflate(testModule.getLayoutResId(), container, false);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.farm_pic_layout, container, false);
//        layout.setBackgroundResource(testModule.getPicResId());

        NetworkImageView bannerNIV = (NetworkImageView) layout.findViewById(R.id.farm_pic_layout_niv_banner);
//        NetworkManager.getInstance().setNetworkImage(mContext, bannerNIV, banner.getImageURL());
        NetworkManager.getInstance().setNetworkImage(mContext, bannerNIV, banner.getImageURL(), R.mipmap.ntpc_icon);

        TextView titleTV = (TextView) layout.findViewById(R.id.farm_pic_layout_tv_title);
        titleTV.setText(banner.getTitle());

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
