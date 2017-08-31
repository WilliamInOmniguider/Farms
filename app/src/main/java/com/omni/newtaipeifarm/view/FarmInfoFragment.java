package com.omni.newtaipeifarm.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.omni.newtaipeifarm.ARActivity;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.tool.FarmText;

/**
 * Created by wiliiamwang on 30/06/2017.
 */

public class FarmInfoFragment extends Fragment {

    public static final String TAG = "tag_farm_info_fragment";
    private static final String S_KEY_FARM = "s_key_farm";

    private View mView;
    private Context mContext;
    private LayoutInflater mInflater;
    private TabLayout mDetailTL;

    public static FarmInfoFragment newInstance(@NonNull Farm farm) {
        FarmInfoFragment fragment = new FarmInfoFragment();

        Bundle arg = new Bundle();
        arg.putSerializable(S_KEY_FARM, farm);
        fragment.setArguments(arg);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.farm_info_fragment_view, container, false);

            mInflater = inflater;

            final Farm farm = (Farm) getArguments().getSerializable(S_KEY_FARM);

//            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) mView.findViewById(R.id.farm_info_fragment_view_ctl);
//            collapsingToolbarLayout.setTitle(farm.getName());
//            collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getActivity(), android.R.color.white));
//            collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));

            TextView actionBarTitleTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_action_bar_title);
            actionBarTitleTV.setText(farm.getName());

            TextView backTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_back);
            backTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(FarmInfoFragment.this).commit();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });

            NetworkImageView picNIV = (NetworkImageView) mView.findViewById(R.id.farm_info_fragment_view_niv_photo);
            NetworkManager.getInstance().setNetworkImage(getActivity(), picNIV, farm.getLogo(), R.mipmap.test_farm_pic);

//            TextView popularTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_popular);
//            popularTV.setText(getString(R.string.popular_sum, farm.getPopular()));

            NetworkImageView iconNIV = (NetworkImageView) mView.findViewById(R.id.farm_info_fragment_view_niv);
            NetworkManager.getInstance().setNetworkImage(mContext, iconNIV, farm.getIcon());

            TextView titleTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_title);
            titleTV.setText(farm.getName());

            TextView phoneTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_phone);
            phoneTV.setText(farm.getTel());

            TextView mailTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_mail);
            mailTV.setText(farm.getEmail());

            /** If farm.threeDURL isEmpty, hide this view. */
            LinearLayout vrLL = (LinearLayout) mView.findViewById(R.id.farm_info_fragment_view_ll_vr);

//            LinearLayout threeDLL = (LinearLayout) mView.findViewById(R.id.farm_info_fragment_view_ll_3d);

            LinearLayout ecLL = (LinearLayout) mView.findViewById(R.id.farm_info_fragment_view_ll_ec);
            if (TextUtils.isEmpty(farm.getEcURL())) {
                ecLL.setVisibility(View.GONE);
            }

            LinearLayout lineLL = (LinearLayout) mView.findViewById(R.id.farm_info_fragment_view_ll_line);

            LinearLayout websiteLL = (LinearLayout) mView.findViewById(R.id.farm_info_fragment_view_ll_website);

            ViewPager detailVP = (ViewPager) mView.findViewById(R.id.farm_info_fragment_view_vp_farm_content);
            detailVP.setAdapter(new FarmInfoPagerAdapter(getChildFragmentManager(), mContext, farm));
//            detailVP.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    return false;
//                }
//            });

            mDetailTL = (TabLayout) mView.findViewById(R.id.farm_info_fragment_view_tl);
            mDetailTL.setupWithViewPager(detailVP);
            for (int i = 0; i < mDetailTL.getTabCount(); i++) {
                TabLayout.Tab tab = mDetailTL.getTabAt(i);
                setFarmContentTabView(tab, i == 0);
            }
            mDetailTL.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    setFarmContentTabView(tab, true);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    setFarmContentTabView(tab, false);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

//            ImageView iv360 = (ImageView) mView.findViewById(R.id.farm_info_fragment_view_iv_360);
//            iv360.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
////                intent.putExtra(WebViewActivity.INTENT_KEY_WEB_URL, "https://walkinto.in/tour/-kphFz-0DzZyea2FMZAvz/_______");
//                    intent.putExtra(WebViewActivity.INTENT_KEY_WEB_URL, farm.getThreeDURL());
//                    getActivity().startActivity(intent);
//                }
//            });
//
//            ImageView arIV = (ImageView) mView.findViewById(R.id.farm_info_fragment_view_iv_ar);
//            arIV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ensurePermission();
//                }
//            });

//            OmniIconItem phoneOII = (OmniIconItem) mView.findViewById(R.id.farm_info_fragment_view_oii_phone);
//            phoneOII.setTitleText(farm.getTel());
//
//            OmniIconItem websiteOII = (OmniIconItem) mView.findViewById(R.id.farm_info_fragment_view_oii_website);
//            websiteOII.setTitleText(farm.getWebURL());
//
//            OmniIconItem placeOII = (OmniIconItem) mView.findViewById(R.id.farm_info_fragment_view_oii_place);
//            placeOII.setTitleText(farm.getAddress());
//
//            OmniIconItem timeOII = (OmniIconItem) mView.findViewById(R.id.farm_info_fragment_view_oii_time);
//            timeOII.setTitleText(farm.getOpenTime());
        }

        return mView;
    }

    private void ensurePermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    FarmText.REQUEST_CAMERA);
        } else {
            showUnity();
        }
    }

    private void showUnity() {
        getActivity().startActivity(new Intent(getActivity(), ARActivity.class));
    }

    private void setFarmContentTabView(TabLayout.Tab tab, boolean isSelected) {
        boolean shouldSetView = (tab.getCustomView() == null);

        TextView tv = shouldSetView ?
                (TextView) mInflater.inflate(R.layout.pager_farm_content_tab_view, null, false) :
                (TextView) tab.getCustomView();

        tv.setTextColor(ContextCompat.getColor(mContext, isSelected ? R.color.farm_pager_title_color : R.color.farm_pager_title_color));
        tv.setText(tab.getText());

        if (tab.getPosition() == mDetailTL.getTabCount() - 1) {
            tv.setBackgroundResource(isSelected ? R.drawable.tab_right_farm_green_selected : R.drawable.tab_right_farm_green_default);
        } else if (tab.getPosition() == 0) {
            tv.setBackgroundResource(isSelected ? R.drawable.tab_left_farm_green_selected : R.drawable.tab_left_farm_green_default);
        } else {
            tv.setBackgroundResource(isSelected ? R.drawable.tab_center_farm_green_selected : R.drawable.tab_center_farm_green_default);
        }

        if (shouldSetView) {
            tab.setCustomView(tv);
        }
    }
}
