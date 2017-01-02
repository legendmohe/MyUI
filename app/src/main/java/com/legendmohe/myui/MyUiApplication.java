package com.legendmohe.myui;

import android.app.Application;

import com.legendmohe.myui.navigator.NavUtil;

/**
 * Created by legendmohe on 2016/12/26.
 */

public class MyUiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NavUtil.init(this);
        NavUtil.DEBUG = true;
    }
}
