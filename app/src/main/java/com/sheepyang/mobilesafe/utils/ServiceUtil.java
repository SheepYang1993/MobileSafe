package com.sheepyang.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by SheepYang on 2016/6/11 23:05.
 */
public class ServiceUtil {
    private static List<ActivityManager.RunningServiceInfo> runningServices;

    public static boolean isRunningService(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        runningServices = activityManager.getRunningServices(1000);
        for (ActivityManager.RunningServiceInfo runningServiceInfo:
        runningServices) {
            ComponentName service = runningServiceInfo.service;
            String runningServicesName = service.getClassName();
            if (className.equals(runningServicesName)) {
                return true;
            }
        }
        return false;
    }
}
