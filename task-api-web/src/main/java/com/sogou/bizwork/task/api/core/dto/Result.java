package com.sogou.bizwork.task.api.core.dto;

/**
 * @description api调用返回结果
 * @author liquancai
 * @date 2016年8月8日
 */
public class Result {

	public static final int SUCCESS = 1;
	public static final int FAILED = 0;

	private int success = 1; // 1,success;0,failed
	private Object data; // 返回数据
	private int errorCode;
	private String errorMsg; // 错误信息

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
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
		this.success = 0;
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.success = 0;
		this.errorMsg = errorMsg;
	}
	
	public void setErrorCodeAndMsg(int errorCode, String errorMsg) {
		this.success = 0;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
}
