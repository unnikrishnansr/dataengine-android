package com.attinad.analyticsengine.core.utils;

import android.util.Log;

public class DebugLog {
    /**
     * Different logs wrapped under this class.
     */
    private DebugLog() {
    }

    /**
     * Send a debug log message
     *
     * @param tag     Source of a log message. It usually identifies the class or activity where the log
     *                call occurs.
     * @param message The message you would like logged.
     */
    public static void log(String tag, String message) {
        Log.d(tag, message);
    }
}
