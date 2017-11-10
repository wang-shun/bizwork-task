package com.sogou.bizwork.task.api.common.util;


public class ExecuteTimeHolder {

    private static ThreadLocal<Long> startTimeHolder = new ThreadLocal<Long>();

    public static long getStartTime() {
        return startTimeHolder.get();
    }

    public static void setStartTime(Long startTime) {
        startTimeHolder.set(startTime);
    }

}
