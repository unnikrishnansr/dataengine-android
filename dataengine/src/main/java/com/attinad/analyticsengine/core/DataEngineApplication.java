package com.attinad.analyticsengine.core;

import android.app.Application;

import com.attinad.analyticsengine.core.dataprovider.ContextProvider;

/**
 * Created by unnikrishanansr on 10/8/17.
 */

public class DataEngineApplication extends Application{


    @Override
    public void onCreate() {
        super.onCreate();
        ContextProvider.setApplication(this);
    }

}
