package com.attinad.analyticsengine.core.crashhandler;

/**
 * Created by arun.chand on 07-12-2016.
 */
public interface CrashListener {
    /**
     * Execute the crash event
     *
     * @param timestamp
     * @param stacktrace
     */
    void crashEvent(String timestamp, String stacktrace);
}
