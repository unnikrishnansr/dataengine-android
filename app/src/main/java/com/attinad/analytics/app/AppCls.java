package com.attinad.analytics.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.attinad.analyticsengine.core.appstate.AppStateListener;
import com.attinad.analyticsengine.core.initializer.DataEngine;

/**
 * Created by unnikrishanansr on 14/8/17.
 */

public class AppCls extends Application implements AppStateListener {

    @Override
    public void onCreate() {
        super.onCreate();

        DataEngine.with(this).setAutoScreenCapture(true).setAppStateMonitor(this).doCaptureCrash().init();
        DataEngine.with(this).identifyEvent();
    }


    @Override
    public void onAppDidEnterForeground() {
        Log.e("APPSTATE","onAppDidEnterForeground");

    }

    @Override
    public void onAppDidEnterBackground() {
        Log.e("APPSTATE","onAppDidEnterBackground");
    }
}
