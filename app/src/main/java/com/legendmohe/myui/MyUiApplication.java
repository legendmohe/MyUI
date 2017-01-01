package com.legendmohe.myui;

import android.app.Application;

import com.legendmohe.myui.navigator.ActivityNavigator;

/**
 * Created by legendmohe on 2016/12/26.
 */

public class MyUiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ActivityNavigator.init(this);
        ActivityNavigator.DEBUG = true;
    }
}
