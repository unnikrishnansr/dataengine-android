package com.attinad.analyticsengine.core.manager;

import android.app.Application;
import android.content.Context;

import com.attinad.analyticsengine.core.appstate.AppState;
import com.attinad.analyticsengine.core.appstate.AppStateListener;
import com.attinad.analyticsengine.core.appstate.AppStateMonitor;
import com.attinad.analyticsengine.core.appstate.RxAppStateMonitor;

import rx.functions.Action1;


/**
 * Created by arun.chand on 23-03-2017.
 */

public class AppStateManager {
    private static final AppStateManager ourInstance = new AppStateManager();
    private AppStateMonitor appStateMonitor;

    public static AppStateManager getInstance() {
        return ourInstance;
    }

    private AppStateListener listener;

    private AppStateManager() {
    }

    public void initState(Context context, AppStateListener stateListener) {

        this.listener = stateListener;

        RxAppStateMonitor.monitor((Application) context).subscribe(new Action1<AppState>() {
            @Override
            public void call(AppState appState) {

            }
        });

        appStateMonitor = RxAppStateMonitor.create((Application) context);
        appStateMonitor.addListener(listener);
        appStateMonitor.start();
    }

    public void stopTrackingState() {
        if (appStateMonitor != null) appStateMonitor.stop();
    }

}
