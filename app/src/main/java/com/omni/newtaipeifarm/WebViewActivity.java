package com.omni.newtaipeifarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by wiliiamwang on 03/07/2017.
 */

public class WebViewActivity extends Activity {

    public static final String INTENT_KEY_WEB_URL = "intent_key_web_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity_view);

        Intent intent = getIntent();
        String url = intent.getStringExtra(INTENT_KEY_WEB_URL);

        WebView wv = (WebView) findViewById(R.id.webview);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(url);

//        wv.setWebViewClient(new MyWebViewClient());
//        wv.loadUrl("http://goo.gl/"+url);
    }

//    private class MyWebViewClient extends WebViewClient {
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            return super.shouldOverrideUrlLoading(view, request);
//        }
//    }
}
