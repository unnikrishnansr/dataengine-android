package com.attinad.analyticsengine.core.exception;


import com.attinad.analyticsengine.core.crashhandler.CrashListener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by arun.chand on 07-12-2016.
 */
public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;
    private CrashListener crashListener;

    /*
     * if any of the parameters is null, the respective functionality
     * will not be used
     */
    public CrashExceptionHandler(CrashListener crshListener) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.crashListener = crshListener;
    }

    public void uncaughtException(Thread t, Throwable e) {
        String timestamp = DateFormat.getDateTimeInstance().format(new Date());
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        crashListener.crashEvent(timestamp, stacktrace);
        defaultUEH.uncaughtException(t, e);
    }

}
