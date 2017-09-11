package com.omni.newtaipeifarm.shop;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.omni.newtaipeifarm.R;

/**
 * Created by wiliiamwang on 28/08/2017.
 */

public class ShopFragment extends Fragment {

    private Context mContext;
    private View mView;
    private WebView mWV;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id-" + getString(R.string.action_bar_title_shop));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name-" + getString(R.string.action_bar_title_shop));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "cont_android");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.shop_fragment_view, null, false);

            Toolbar toolbar = (Toolbar) mView.findViewById(R.id.shop_fragment_view_action_bar);
            TextView titleTV = (TextView) toolbar.findViewById(R.id.farm_action_bar_tv_title);
            titleTV.setText(R.string.action_bar_title_shop);

            mWV = (WebView) mView.findViewById(R.id.shop_fragment_view_wv);
            mWV.setWebViewClient(new WebViewClient());
            mWV.getSettings().setJavaScriptEnabled(true);
            mWV.loadUrl("http://www.ntpcfarm.com.tw");
        }

        return mView;
    }
}
