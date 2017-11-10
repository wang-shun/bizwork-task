package com.sogou.bizwork.task.api.core.activiti.po;

public enum ErrorEnum {
    
    PARAM_ERROR(1, "参数错误"),
    
    USERNAME_PASSWORD_ERROR(101, "用户名或密码错误"),
    
    CRM_AUTH_ERROR(102, "没有系统权限"),
    
    LOGIN_EXPIRE_ERROR(103, "登录异常，请重新登录"),
    
    USER_OFFLINE_ERROR(104, "用户已被强制下线"),

    SYSTEM_ERROR(999, "系统异常");

    private int code;
    private String message;

    private ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (ErrorEnum em : ErrorEnum.values()) {
            if (em.getCode() == code) {
                return em.getMessage();
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
