package com.omni.newtaipeifarm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.omni.newtaipeifarm.view.WebTutoView;

/**
 * Created by wiliiamwang on 03/07/2017.
 */

public class WebViewActivity extends FragmentActivity {

    public static final String INTENT_KEY_WEB_URL = "intent_key_web_url";

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity_view);

        Intent intent = getIntent();
        final String webUrl = intent.getStringExtra(INTENT_KEY_WEB_URL);

        final WebTutoView webTutoView = new WebTutoView();
        if (!DataCacheManager.getInstance().isVRTutoShowed) {
            DataCacheManager.getInstance().isVRTutoShowed = true;
            webTutoView.show(getSupportFragmentManager(), WebTutoView.TAG);
        }

        WebView wv = (WebView) findViewById(R.id.webview);
        WebViewClient wc = new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(WebViewActivity.this, R.style.AppCompatAlertDialogStyle);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setMessage(getString(R.string.dialog_message_loading));
                }
                if (!mProgressDialog.isShowing() && !webTutoView.isVisible()) {
                    mProgressDialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        };

        wv.setWebViewClient(wc);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(webUrl);
    }
}
