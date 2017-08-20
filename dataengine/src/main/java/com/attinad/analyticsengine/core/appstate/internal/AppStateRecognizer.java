package com.attinad.analyticsengine.core.appstate.internal;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.attinad.analyticsengine.core.appstate.AppState;
import com.attinad.analyticsengine.core.appstate.AppStateListener;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;

@RestrictTo(LIBRARY)
public interface AppStateRecognizer {

  void addListener(@NonNull AppStateListener listener);

  void removeListener(@NonNull AppStateListener listener);

  void start();

  void stop();

  @NonNull
  AppState getAppState();
}
