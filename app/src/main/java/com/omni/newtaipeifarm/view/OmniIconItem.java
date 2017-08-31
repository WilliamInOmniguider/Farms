package com.omni.newtaipeifarm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omni.newtaipeifarm.R;

/**
 * Created by wiliiamwang on 03/07/2017.
 */

public class OmniIconItem extends RelativeLayout {

    ImageView mIconIV;
    TextView mTitleTV;

    public OmniIconItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OmniIconItem);

        // TODO change default icon
        int iconRes = typedArray.getResourceId(R.styleable.OmniIconItem_icon_src, R.mipmap.ic_launcher);
        String titleText = typedArray.getString(R.styleable.OmniIconItem_icon_title_text);
//        float titleTextSize = typedArray.getDimension(R.styleable.OmniIconItem_icon_title_text_size, 14);
        int titleTextColor = typedArray.getColor(R.styleable.OmniIconItem_icon_title_text_color, ContextCompat.getColor(context, android.R.color.black));
        int autoLink = typedArray.getInt(R.styleable.OmniIconItem_icon_title_text_auto_link, -1);

        typedArray.recycle();

        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.omni_icon_item_view, this, true);

        mIconIV = (ImageView) view.findViewById(R.id.omni_icon_item_view_iv_icon);
        mIconIV.setImageResource(iconRes);

        mTitleTV = (TextView) view.findViewById(R.id.omni_icon_item_view_tv_title);
        mTitleTV.setText(titleText);
//        mTitleTV.setTextSize(titleTextSize / getResources().getDisplayMetrics().density);
        mTitleTV.setTextColor(titleTextColor);
        mTitleTV.setLinkTextColor(ContextCompat.getColor(context, R.color.farm_color));
        if (autoLink != -1) {
            mTitleTV.setAutoLinkMask(autoLink);
        }
    }

    public void setTitleText(String text) {
        mTitleTV.setText(text);
    }
}
