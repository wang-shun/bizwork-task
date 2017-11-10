package com.sogou.bizwork.task.api.teleport;

public class ApiUserHolder {

    private static ThreadLocal<ApiUser> apiUserHolder = new ThreadLocal<ApiUser>();

    public static ApiUser getApiUser() {
        return apiUserHolder.get();
    }

    public static void setApiUser(ApiUser apiUser) {
        apiUserHolder.set(apiUser);
    }

    public static void remove() {
        apiUserHolder.remove();
    }

}
