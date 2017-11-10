package com.sogou.bizwork.task.api.web.common.util;

import com.sogou.bizwork.task.api.web.user.vo.UserInfo;

public class UserHolder {

    private static ThreadLocal<UserInfo> userHolder = new ThreadLocal<UserInfo>();

    public static UserInfo getUser() {
        return userHolder.get();
    }

    public static void setUser(UserInfo user) {
        userHolder.set(user);
    }

    public static long getUserId() {
        return userHolder.get().getUserId();
    }

    public static void remove() {
        userHolder.remove();
    }
}
