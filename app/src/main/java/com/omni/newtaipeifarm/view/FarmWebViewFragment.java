package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.omni.newtaipeifarm.R;

/**
 * Created by wiliiamwang on 04/09/2017.
 */

public class FarmWebViewFragment extends Fragment {

    public static final String TAG = "tag_farm_web_view_fragment";

    private static final String KEY_WEB_VIEW_URL = "key_web_view_url";

    private Context mContext;
    private View mView;

    public static FarmWebViewFragment newInstance(String url) {
        FarmWebViewFragment fragment = new FarmWebViewFragment();

        Bundle bundle = new Bundle();
        bundle.putString(KEY_WEB_VIEW_URL, url);
        fragment.setArguments(bundle);

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
            mView = inflater.inflate(R.layout.farm_web_view_fragment_view, container, false);

            String url = getArguments().getString(KEY_WEB_VIEW_URL);

            WebView webView = (WebView) mView.findViewById(R.id.farm_web_view_fragment_view_wv);
            WebSettings setting = webView.getSettings();
            setting.setJavaScriptEnabled(true);

            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
        }

        return mView;
    }
}
