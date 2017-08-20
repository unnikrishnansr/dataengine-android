package com.attinad.analyticsengine.core.appstate.internal;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.attinad.analyticsengine.core.appstate.AppState;
import com.attinad.analyticsengine.core.appstate.AppStateListener;

import java.util.concurrent.atomic.AtomicBoolean;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.support.annotation.RestrictTo.Scope.LIBRARY;


@RestrictTo(LIBRARY)
public final class DefaultAppStateRecognizer implements AppStateRecognizer {

    @NonNull
    private final CompositeAppStateListener compositeListener = new CompositeAppStateListener();
    @NonNull
    private final ActivityLifecycleCallbacks activityStartedCallback = new ActivityStartedCallback();
    @NonNull
    private final ComponentCallbacks2 uiHiddenCallback = new UiHiddenCallback();
    @NonNull
    private final BroadcastReceiver screenOffBroadcastReceiver = new ScreenOffBroadcastReceiver();
    @NonNull
    private final AtomicBoolean isFirstLaunch = new AtomicBoolean(true);

    @NonNull
    private final Application app;

    @NonNull
    private AppState appState = AppState.BACKGROUND;

    public DefaultAppStateRecognizer(@NonNull Application app) {
        this.app = app;
    }

    @Override
    public void addListener(@NonNull AppStateListener listener) {
        compositeListener.addListener(listener);
    }

    @Override
    public void removeListener(@NonNull AppStateListener listener) {
        compositeListener.removeListener(listener);
    }

    @Override
    public void start() {
        app.registerActivityLifecycleCallbacks(activityStartedCallback);
        app.registerComponentCallbacks(uiHiddenCallback);
        app.registerReceiver(screenOffBroadcastReceiver, new IntentFilter(ACTION_SCREEN_OFF));
    }

    @Override
    public void stop() {
        app.unregisterActivityLifecycleCallbacks(activityStartedCallback);
        app.unregisterComponentCallbacks(uiHiddenCallback);
        app.unregisterReceiver(screenOffBroadcastReceiver);
    }

    @NonNull
    @Override
    public AppState getAppState() {
        return appState;
    }

    private boolean isAppInForeground() {
        return appState == AppState.FOREGROUND;
    }

    private boolean isAppInBackground() {
        return appState == AppState.BACKGROUND;
    }

    private void onAppDidEnterForeground() {
        appState = AppState.FOREGROUND;
        compositeListener.onAppDidEnterForeground();
    }

    private void onAppDidEnterBackground() {
        appState = AppState.BACKGROUND;
        compositeListener.onAppDidEnterBackground();
    }

    private class ActivityStartedCallback extends NoOpActivityLifecycleCallbacks {

        @Override
        public void onActivityStarted(Activity activity) {
            if (isFirstLaunch.compareAndSet(true, false)) {
                onAppDidEnterForeground();
                return;
            }

            if (isAppInBackground()) {
                onAppDidEnterForeground();
            }
        }
    }

    private class UiHiddenCallback extends NoOpComponentCallbacks2 {

        @Override
        public void onTrimMemory(int level) {
            if (level >= TRIM_MEMORY_UI_HIDDEN && isAppInForeground()) {
                onAppDidEnterBackground();
            }
        }
    }

    private class ScreenOffBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isAppInForeground()) {
                onAppDidEnterBackground();
            }
        }
    }
}
