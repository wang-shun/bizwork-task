package com.sogou.bizwork.task.api.core.task.po;

/**
 * @description api调用返回结果
 * @author liquancai
 * @date 2016年8月8日
 */
public class ResponResult {

	public final static boolean SUCCESS = true;
	public static final int FAILED = 0;

	private boolean success = true;
	private Object data; // 返回数据
	private int errorCode;
	private String errorMsg; // 错误信息



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

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.success = false;
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.success = false;
		this.errorMsg = errorMsg;
	}
	
	public void setErrorCodeAndMsg(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
}
