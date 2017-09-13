package com.omni.newtaipeifarm.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.omni.newtaipeifarm.R;

/**
 * Created by wiliiamwang on 12/09/2017.
 */

public class WebTutoView extends DialogFragment {

    public static final String TAG = "tag_web_tuto_view";

    private View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.web_tuto_view, null, false);

            mView.findViewById(R.id.web_tuto_view_tv_get_in).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebTutoView.this.dismiss();
                }
            });
        }

        return mView;
    }
}
