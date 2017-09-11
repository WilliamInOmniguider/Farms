package com.omni.newtaipeifarm.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.omni.newtaipeifarm.ARActivity;
import com.omni.newtaipeifarm.PicPagerAdapter;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.WebViewActivity;
import com.omni.newtaipeifarm.model.AddFavoriteResponse;
import com.omni.newtaipeifarm.model.BannerObj;
import com.omni.newtaipeifarm.model.Farm;
import com.omni.newtaipeifarm.model.OmniEvent;
import com.omni.newtaipeifarm.network.FarmApi;
import com.omni.newtaipeifarm.network.NetworkManager;
import com.omni.newtaipeifarm.tool.FarmText;

import org.greenrobot.eventbus.EventBus;

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
    private int tempFavoriteCount;

    private FirebaseAnalytics mFirebaseAnalytics;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Farm farm = (Farm) getArguments().getSerializable(S_KEY_FARM);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        sendTrack("id-農場介紹" + farm.getName(), "name-農場介紹" + farm.getName());
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
//                    getActivity().getSupportFragmentManager().beginTransaction().remove(FarmInfoFragment.this).commit();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });

            RelativeLayout picPagerLayout = (RelativeLayout) mView.findViewById(R.id.farm_info_fragment_view_pic_pager);
            ViewPager viewPager = (ViewPager) picPagerLayout.findViewById(R.id.pic_pager_layout_vp_farm_pic);
            BannerObj[] bannerObjs = new BannerObj[farm.getBannerArray().length];
            for (int i = 0; i < farm.getBannerArray().length; i++) {
                bannerObjs[i] = new BannerObj("", farm.getBannerArray()[i]);
            }
            viewPager.setAdapter(new PicPagerAdapter(mContext, bannerObjs));

            TabLayout tabLayout = (TabLayout) picPagerLayout.findViewById(R.id.pic_pager_layout_tl);
            tabLayout.setupWithViewPager(viewPager, true);

            final TextView popularTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_popular);
            popularTV.setText(getString(R.string.popular_sum, farm.getPopular()));
            tempFavoriteCount = farm.getPopular();

            final ImageView likeIV = (ImageView) mView.findViewById(R.id.farm_info_fragment_view_iv_like);
            likeIV.setImageResource(farm.getFavorite() == 1 ? R.mipmap.button_heart_r : R.mipmap.button_heart_w);
            likeIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FarmApi.getInstance().addToFavorite(mContext, farm.getSid(), new NetworkManager.NetworkManagerListener<AddFavoriteResponse>() {
                        @Override
                        public void onSucceed(AddFavoriteResponse response) {
                            if (response.getStatus().equals(AddFavoriteResponse.FavoriteStatus.INSERT_SUCCESS)) {
                                likeIV.setImageResource(R.mipmap.button_heart_r);
                                popularTV.setText(getString(R.string.popular_sum, ++tempFavoriteCount));

                                EventBus.getDefault().post(new OmniEvent(OmniEvent.TYPE_REFRESH_FARM_LIST_DATA, ""));
                            } else if (response.getStatus().equals(AddFavoriteResponse.FavoriteStatus.DELETE_SUCCESS)) {
                                likeIV.setImageResource(R.mipmap.button_heart_w);
                                popularTV.setText(getString(R.string.popular_sum, --tempFavoriteCount));

                                EventBus.getDefault().post(new OmniEvent(OmniEvent.TYPE_REFRESH_FARM_LIST_DATA, ""));
                            }
                        }

                        @Override
                        public void onFail(VolleyError error, boolean shouldRetry) {

                        }
                    });
                }
            });

            NetworkImageView iconNIV = (NetworkImageView) mView.findViewById(R.id.farm_info_fragment_view_niv);
            NetworkManager.getInstance().setNetworkImage(mContext, iconNIV, farm.getIcon(), R.mipmap.ntpc_icon);

            TextView titleTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_title);
            titleTV.setText(farm.getName());

            TextView phoneTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_phone);
            phoneTV.setText(farm.getTel());

            TextView mailTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_mail);
            mailTV.setText(farm.getEmail());

            /** If farm.threeDURL isEmpty, hide this view. */
            LinearLayout vrLL = (LinearLayout) mView.findViewById(R.id.farm_info_fragment_view_ll_vr);
            vrLL.setVisibility(TextUtils.isEmpty(farm.getThreeDURL()) ? View.GONE : View.VISIBLE);
            vrLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendTrack("id-VR環景" + farm.getName(), "name-VR環景" + farm.getName());

                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                intent.putExtra(WebViewActivity.INTENT_KEY_WEB_URL, "https://walkinto.in/tour/-kphFz-0DzZyea2FMZAvz/_______");
                    intent.putExtra(WebViewActivity.INTENT_KEY_WEB_URL, farm.getThreeDURL());
                    getActivity().startActivity(intent);
                }
            });

//            LinearLayout threeDLL = (LinearLayout) mView.findViewById(R.id.farm_info_fragment_view_ll_3d);

            LinearLayout ecLL = (LinearLayout) mView.findViewById(R.id.farm_info_fragment_view_ll_ec);
            ecLL.setVisibility(TextUtils.isEmpty(farm.getEcURL()) ? View.GONE : View.VISIBLE);
            ecLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendTrack("id-農場商城" + farm.getName(), "name-農場商城" + farm.getName());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.farm_info_fragment_view_fl, FarmWebViewFragment.newInstance(farm.getEcURL()), FarmWebViewFragment.TAG)
                            .addToBackStack(null)
                            .commit();
                }
            });

            LinearLayout lineLL = (LinearLayout) mView.findViewById(R.id.farm_info_fragment_view_ll_line);
            lineLL.setVisibility(TextUtils.isEmpty(farm.getLineId()) ? View.GONE : View.VISIBLE);
            lineLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(farm.getLineId()));
                    startActivity(browserIntent);
                }
            });

            LinearLayout websiteLL = (LinearLayout) mView.findViewById(R.id.farm_info_fragment_view_ll_website);
            websiteLL.setVisibility(TextUtils.isEmpty(farm.getWebURL()) ? View.GONE : View.VISIBLE);
            websiteLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendTrack("id-農場微官網" + farm.getName(), "name-農場微官網" + farm.getName());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.farm_info_fragment_view_fl, FarmWebViewFragment.newInstance(farm.getWebURL()), FarmWebViewFragment.TAG)
                            .addToBackStack(null)
                            .commit();
                }
            });

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

//            ImageView arIV = (ImageView) mView.findViewById(R.id.farm_info_fragment_view_iv_ar);
//            arIV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ensurePermission();
//                }
//            });
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

    private void sendTrack(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "cont_android");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
