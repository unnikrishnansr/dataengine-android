package com.attinad.analyticsengine.core.appstate;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.attinad.analyticsengine.core.appstate.internal.AppStateEmitter;
import com.attinad.analyticsengine.core.appstate.internal.AppStateRecognizer;
import com.attinad.analyticsengine.core.appstate.internal.DefaultAppStateRecognizer;

import rx.Observable;

import static android.support.annotation.RestrictTo.Scope.TESTS;
import static rx.Emitter.BackpressureMode.LATEST;

/**
 * An app state monitor that keeps track of whenever the application
 * goes into background and comes back into foreground.
 */
@SuppressWarnings({"unused"})
public final class RxAppStateMonitor implements AppStateMonitor {

    @NonNull
    private final AppStateRecognizer recognizer;


    @NonNull
    public static Observable<AppState> monitor(@NonNull Application app) {
        return Observable.fromEmitter(new AppStateEmitter(new DefaultAppStateRecognizer(app)), LATEST);
    }


    @NonNull
    public static AppStateMonitor create(@NonNull Application app) {
        return new RxAppStateMonitor(app);
    }

    private RxAppStateMonitor(@NonNull Application app) {
        this.recognizer = new DefaultAppStateRecognizer(app);
    }

    @RestrictTo(TESTS)
    RxAppStateMonitor(@NonNull AppStateRecognizer recognizer) {
        this.recognizer = recognizer;
    }


    @Override
    public void start() {
        recognizer.start();
    }


    @Override
    public void stop() {
        recognizer.stop();
    }

    @Override
    public void addListener(@NonNull AppStateListener appStateListener) {
        recognizer.addListener(appStateListener);
    }


    @Override
    public void removeListener(@NonNull AppStateListener appStateListener) {
        recognizer.removeListener(appStateListener);
    }


    @Override
    public boolean isAppInForeground() {
        return recognizer.getAppState() == AppState.FOREGROUND;
    }


    @Override
    public boolean isAppInBackground() {
        return recognizer.getAppState() == AppState.BACKGROUND;
    }
}