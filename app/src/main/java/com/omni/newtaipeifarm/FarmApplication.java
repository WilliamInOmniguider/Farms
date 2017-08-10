package com.omni.newtaipeifarm;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by wiliiamwang on 09/08/2017.
 */

public class FarmApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
