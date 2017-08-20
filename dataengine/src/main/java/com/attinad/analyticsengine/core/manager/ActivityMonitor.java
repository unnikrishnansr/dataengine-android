package com.attinad.analyticsengine.core.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Manager to detect changes in activity stack
 *
 * @author Unnikrishnan SR (unnikrishnan.sr@attinadsoftware.com) on 17-08-2017.
 * @since Media Proto
 * Copyright (c) 2017 Attinad Software Pvt Ltd. All rights reserved.
 */
public class ActivityMonitor implements Runnable {

    private Context mContext;

    public ActivityMonitor(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void run() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(mContext.getPackageName())) {
                        Log.e("MONITOR","=> "+activeProcess);
                    }
                }
            }
        }
    }
}
