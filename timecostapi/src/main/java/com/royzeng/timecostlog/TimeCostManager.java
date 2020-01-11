package com.royzeng.timecostlog;

import android.os.Looper;

public class TimeCostManager {

    private static ITimeCostLogger mlogger;

    public static void setLogger(ITimeCostLogger logger) {
        mlogger = logger;
    }

    //call in plugin
    public static void logTimeCost(String method, long time) {
        System.out.println("=======logTimeCost=========");
        if (isMainThread() && mlogger != null) {
            mlogger.logger(method, time);
        }
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
