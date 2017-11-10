package com.sogou.bizwork.task.api.core.activiti.po;

public class RepositoryResult {

    private boolean success = true;
    private Object data = null;
    private String errorMsg;
    private int errorCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        this.success = false;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.success = false;
        this.errorCode = errorCode;
    }

    public void setError(int errorCode, String errorMsg) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public void setError(SvnRepositoryErrorEnum error) {
        this.success = false;
        this.errorCode = error.getCode();
        this.errorMsg = error.getMessage();
    }

}
