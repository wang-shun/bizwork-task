package com.sogou.bizwork.task.api.common.exception;

import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;

/**
 * 业务异常类
 * 
 * @author qianlei
 */
public class BizException extends Exception {

    private static final long serialVersionUID = 1L;

    private Integer errorCode;
    private String message;

    public BizException(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public BizException(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public BizException(BizErrorEnum en) {
        this.errorCode = en.getCode();
        this.message = en.getMessage();
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
