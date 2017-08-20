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

    /**
     * Creates a new {@link Observable} that emits {@link AppState} items
     * whenever the app goes into background and comes back into foreground.
     *
     * @return a new {@link Observable}
     */
    @NonNull
    public static Observable<AppState> monitor(@NonNull Application app) {
        return Observable.fromEmitter(new AppStateEmitter(new DefaultAppStateRecognizer(app)), LATEST);
    }

    /**
     * Creates a new {@link RxAppStateMonitor} instance for the given {@link Application}.
     *
     * @return a new {@link RxAppStateMonitor} instance
     */
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

    /**
     * Starts monitoring the app for background / foreground changes.
     */
    @Override
    public void start() {
        recognizer.start();
    }

    /**
     * Stops monitoring the app for background / foreground changes.
     */
    @Override
    public void stop() {
        recognizer.stop();
    }

    /**
     * Adds a new {@link AppStateListener} to the app state monitor.
     */
    @Override
    public void addListener(@NonNull AppStateListener appStateListener) {
        recognizer.addListener(appStateListener);
    }

    /**
     * Removes the specified {@link AppStateListener} from the app state monitor.
     */
    @Override
    public void removeListener(@NonNull AppStateListener appStateListener) {
        recognizer.removeListener(appStateListener);
    }

    /**
     * Checks whether the app is currently in the foreground.
     *
     * @return {@code true} if the app is currently in the foreground, {@code false} otherwise
     */
    @Override
    public boolean isAppInForeground() {
        return recognizer.getAppState() == AppState.FOREGROUND;
    }

    /**
     * Checks whether the app is currently in the background.
     *
     * @return {@code true} if the app is currently in the background, {@code false} otherwise
     */
    @Override
    public boolean isAppInBackground() {
        return recognizer.getAppState() == AppState.BACKGROUND;
    }
}