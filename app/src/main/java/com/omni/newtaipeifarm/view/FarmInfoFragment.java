package com.omni.newtaipeifarm.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.omni.newtaipeifarm.ARActivity;
import com.omni.newtaipeifarm.R;
import com.omni.newtaipeifarm.WebViewActivity;
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

    public static FarmInfoFragment newInstance(@NonNull Farm farm) {
        FarmInfoFragment fragment = new FarmInfoFragment();

        Bundle arg = new Bundle();
        arg.putSerializable(S_KEY_FARM, farm);
        fragment.setArguments(arg);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.farm_info_fragment_view, null, false);
        }

        final Farm farm = (Farm) getArguments().getSerializable(S_KEY_FARM);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) mView.findViewById(R.id.farm_info_fragment_view_ctl);
        collapsingToolbarLayout.setTitle(farm.getName());
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));

        TextView backTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_back);
        backTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(FarmInfoFragment.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        NetworkImageView picNIV = (NetworkImageView) mView.findViewById(R.id.farm_info_fragment_view_niv_toolbar_photo);
        NetworkManager.getInstance().setNetworkImage(getActivity(), picNIV, farm.getLogo(), R.mipmap.test_farm_pic);

        ImageView iv360 = (ImageView) mView.findViewById(R.id.farm_info_fragment_view_iv_360);
        iv360.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                intent.putExtra(WebViewActivity.INTENT_KEY_WEB_URL, "https://walkinto.in/tour/-kphFz-0DzZyea2FMZAvz/_______");
                intent.putExtra(WebViewActivity.INTENT_KEY_WEB_URL, farm.getThreeDURL());
                getActivity().startActivity(intent);
            }
        });

        ImageView arIV = (ImageView) mView.findViewById(R.id.farm_info_fragment_view_iv_ar);
        arIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ensurePermission();
            }
        });

        ImageView shopIV = (ImageView) mView.findViewById(R.id.farm_info_fragment_view_iv_shop);
        shopIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageView starIV = (ImageView) mView.findViewById(R.id.farm_info_fragment_view_iv_star);
        starIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        OmniIconItem phoneOII = (OmniIconItem) mView.findViewById(R.id.farm_info_fragment_view_oii_phone);
        phoneOII.setTitleText(farm.getTel());

        OmniIconItem websiteOII = (OmniIconItem) mView.findViewById(R.id.farm_info_fragment_view_oii_website);
        websiteOII.setTitleText(farm.getWebURL());

        OmniIconItem placeOII = (OmniIconItem) mView.findViewById(R.id.farm_info_fragment_view_oii_place);
        placeOII.setTitleText(farm.getAddress());

        OmniIconItem timeOII = (OmniIconItem) mView.findViewById(R.id.farm_info_fragment_view_oii_time);
        timeOII.setTitleText(farm.getOpenTime());

        TextView infoTV = (TextView) mView.findViewById(R.id.farm_info_fragment_view_tv_info);
        infoTV.setText(farm.getIntro());
//        infoTV.setText("莓圃觀光果園位於台北市內湖的碧山 ( 舊稱白石湖 ) ，約離市中心 10 分鐘車程 ( 近日湖百貨 ) ，不但交通方便，更可享受都市所未有的清新空氣與田園氣息，讓您在平日、假日能擁有一份悠閒時光。幸運時更可觀察到天空中敖翔的大冠鷲、正在偷吃水果的五色鳥、優雅的台灣藍鵲在滑翔 ...\\n\\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。莓圃觀光果園位於台北市內湖的碧山 ( 舊稱白石湖 ) ，約離市中心 10 分鐘車程 ( 近日湖百貨 ) ，不但交通方便，更可享受都市所未有的清新空氣與田園氣息，讓您在平日、假日能擁有一份悠閒時光。幸運時更可觀察到天空中敖翔的大冠鷲、正在偷吃水果的五色鳥、優雅的台灣藍鵲在滑翔 ...\n" +
//                "\n" +
//                "\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。\n" +
//                "\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。\n" +
//                "\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。\n" +
//                "\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。\n" +
//                "\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。\n" +
//                "\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。\n" +
//                "\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。\n" +
//                "\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。\n" +
//                "\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。\n" +
//                "\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。\n" +
//                "\n我們提供的不光是大又甜的水果、用心煮的咖啡、點心，更希望提供一個誠心招待每位來此朋友休息的好去處，歡迎您一起上山來交朋友。!!");

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
}
