package com.attinad.analyticsengine.core.manager;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.attinad.analyticsengine.core.initializer.DataEngine;

import java.util.List;

/**
 * Manager to detect changes in activity stack
 *
 * @author Unnikrishnan SR (unnikrishnan.sr@attinadsoftware.com) on 17-08-2017.
 * @since Media Proto
 * Copyright (c) 2017 Attinad Software Pvt Ltd. All rights reserved.
 */
public class ActivityMonitor {

    private Context mContext;
    private String mActivityName = "";
    private String TAG = "ActivityMonitor";


    public ActivityMonitor(Context mContext) {
        this.mContext = mContext;
    }

    public void start() {
        Log.e("MONITOR", "Started");
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(mContext.getPackageName())) {
                        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
                        ActivityManager.RunningTaskInfo task = tasks.get(0);
                        String rootActivity = task.topActivity.getClassName();
                        if (!mActivityName.equalsIgnoreCase(rootActivity)) {
                            mActivityName = rootActivity;
                            Log.e(TAG, "=> " + rootActivity);
                            DataEngine.getInstance().autoScreenEvent(rootActivity);
                        } else {
                            Log.e(TAG, "No activity change");
                        }
                    }
                }
            }
        }
    }
}
