package com.sogou.bizwork.task.api.web.common.exception;

import java.util.List;
import java.util.Map;

/**
 * 业务异常类
 * 
 * @author qianlei
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errorMsg;// 单条全局错误信息
    private List<String> errors = null;// 多条全局错误信息
    private Map<String, String> fieldErrors = null;// 字段错误信息

    public BizException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public BizException(Throwable cause, String errorMsg) {
        super(cause);
        this.errorMsg = errorMsg;
    }

    public BizException(List<String> errors) {
        this.errors = errors;
    }

    public BizException(Throwable cause, List<String> errors) {
        super(cause);
        this.errors = errors;
    }

    public BizException(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public BizException(Throwable cause, Map<String, String> fieldErrors) {
        super(cause);
        this.fieldErrors = fieldErrors;
    }

    /******************get  and set **********************/
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

}
