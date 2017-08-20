package com.attinad.analyticsengine.core.crashhandler;

import android.util.Log;


/**
 * Created by arun.chand on 07-12-2016.
 */
public class CrashTracker implements CrashListener {
    @Override
    public void crashEvent(String timestamp, String stacktrace) {
        Log.e("CrashTracker", "Stack Trace :-" + timestamp + stacktrace);
    }
}
