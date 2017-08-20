package com.attinad.analyticsengine.core.dataprovider;

import android.content.Context;

import com.attinad.analyticsengine.core.DataEngineApplication;

/**
 * Created by unnikrishanansr on 11/8/17.
 */

public class ContextProvider {

    private static DataEngineApplication mApplication;

    public static Context appContext() {
        return mApplication;
    }

    public static void setApplication(DataEngineApplication app) {
        mApplication = app;
    }
}
