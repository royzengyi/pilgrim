package com.royzeng.plugin;

public class Setting {
    public static final String SYSTEM_CLOCK_PACKAGE = "android/os/SystemClock";
    public static final String SYSTEM_CLOCK_METHOD = "elapsedRealtime";
    public static final String SYSTEM_CLOCK_METHOD_SIGNATURE = "()J";
    public static final String ITIMELOGGER_CLASS = "com/royzeng/timecostlog/ITimeCostLogger";
    public static final String TIMEDEBUGERMANAGER_CLASS = "com/royzeng/timecostlog/TimeCostManager";
    public static final String TIMEDEBUGERMANAGER_METHOD = "logTimeCost";
    public static final String TIMEDEBUGERMANAGER_METHOD_SIGNATURE = "(Ljava/lang/String;J)V";
    public static final String PACAKAGE_CONFIG = "timeCostLogPkgNames";
}
