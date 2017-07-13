package com.omni.newtaipeifarm.model;

import android.os.Bundle;

/**
 * Created by wiliiamwang on 10/07/2017.
 */

public class OmniEvent {

    public static final int TYPE_USER_LOCATION_CHANGED = 19;

    private int mType;
    private String mContent;
    private Object mObj;
    private Bundle mArgs;

    public OmniEvent(int type, String content) {
        mType = type;
        mContent = content;
    }

    public OmniEvent(int type, Object obj) {
        mType = type;
        mObj = obj;
    }

    public OmniEvent(int type, Bundle args) {
        mType = type;
        mArgs = args;
    }

    public int getType() {
        return mType;
    }

    public String getContent() {
        return mContent;
    }

    public Object getObj() {
        return mObj;
    }

    public Bundle getArguments() { return mArgs; }
}
