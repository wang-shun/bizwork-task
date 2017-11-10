package com.sogou.bizwork.task.api.common;

import com.sogou.bizwork.task.api.common.error.ErrorCode;
import com.sogou.bizwork.task.api.common.exception.ApiTException;
import com.sogou.bizwork.task.api.common.exception.BizException;
import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;

/**
 * 
 * @Title
 * @Description
 * @author fanjianjun
 * @date   2016年7月20日
 */
public class ResultUtils {

    public static ApiTException newApiTException(int code) {
        ApiTException apiEx = new ApiTException(code);
        apiEx.setMessage(BizErrorEnum.getMessage(code));
        return apiEx;
    }
    
    public static ApiTException newApiTException(int code, String extraMessage) {
        ApiTException apiEx = new ApiTException(code);
        apiEx.setMessage(BizErrorEnum.getMessage(code)+","+extraMessage);
        return apiEx;
    }

    public static ErrorCode newErrorCode(int code) {
        ErrorCode et = new ErrorCode();
        et.setErrorCode(code);
        et.setMessage(BizErrorEnum.getMessage(code));
        return et;
    }

    public static ErrorCode newErrorCode(BizErrorEnum be) {
        if (be == null) {
            return null;
        }
        ErrorCode et = new ErrorCode();
        et.setErrorCode(be.getCode());
        et.setMessage(be.getMessage());
        return et;
    }

    public static ErrorCode newErrorCode(BizErrorEnum be, String extraMessage) {
        if (be == null) {
            return null;
        }

        ErrorCode ec = new ErrorCode();
        ec.setErrorCode(be.getCode());
        ec.setMessage(be.getMessage() + " " + extraMessage);

        return ec;
    }

    public static ErrorCode newErrorCode(BizException ex) {
        if (ex == null) {
            return null;
        }

        ErrorCode ec = new ErrorCode();
        ec.setErrorCode(ex.getErrorCode());
        ec.setMessage(ex.getMessage());
        return ec;
    }
}
