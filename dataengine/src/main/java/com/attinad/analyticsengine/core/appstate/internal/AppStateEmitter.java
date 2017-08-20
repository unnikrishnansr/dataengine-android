package com.attinad.analyticsengine.core.appstate.internal;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.attinad.analyticsengine.core.appstate.AppState;
import com.attinad.analyticsengine.core.appstate.AppStateListener;

import rx.Emitter;
import rx.functions.Action1;
import rx.functions.Cancellable;

import static android.support.annotation.RestrictTo.Scope.LIBRARY;



@RestrictTo(LIBRARY)
public final class AppStateEmitter implements Action1<Emitter<AppState>> {

  @NonNull
  private final AppStateRecognizer recognizer;

  public AppStateEmitter(@NonNull AppStateRecognizer recognizer) {
    this.recognizer = recognizer;
  }

  @Override
  public void call(final Emitter<AppState> appStateEmitter) {
    final AppStateListener appStateListener = new AppStateListener() {
      @Override
      public void onAppDidEnterForeground() {
        appStateEmitter.onNext(AppState.FOREGROUND);
      }

      @Override
      public void onAppDidEnterBackground() {
        appStateEmitter.onNext(AppState.FOREGROUND);
      }
    };

    appStateEmitter.setCancellation(new Cancellable() {
      @Override
      public void cancel() throws Exception {
        recognizer.removeListener(appStateListener);
        recognizer.stop();
      }
    });

    recognizer.addListener(appStateListener);
    recognizer.start();
  }
}
