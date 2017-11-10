package com.sogou.bizwork.task.api.core.activiti.bo;

import java.sql.Timestamp;

public class ActInfo {
	private Long id;
	private String businessKey;
	private Long employeeId;
	private String ChineseName;   //员工中文姓名
	private Integer flowTypeId;
	private String flowName;
	private String message;
	private String formData;
	private Timestamp  createTime;
	private int errorCode;
	private String errorMsg;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getCreateTime() {
		String createTimeStr = createTime.toString();
		if (createTimeStr.endsWith(".0")) {
			createTimeStr = createTimeStr.substring(0, createTimeStr.length() - 2);
		}
		return createTimeStr;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Integer getFlowTypeId() {
		return flowTypeId;
	}
	public void setFlowTypeId(Integer flowTypeId) {
		this.flowTypeId = flowTypeId;
	}
	public String getFormData() {
		return formData;
	}
	public void setFormData(String formData) {
		this.formData = formData;
	}
	public String getChineseName() {
		return ChineseName;
	}
	public void setChineseName(String chineseName) {
		ChineseName = chineseName;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public void setErrorCodeAndMsg(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	@Override
	public String toString() {
		return "ActInfo [id=" + id + ", businessKey=" + businessKey + ", employeeId=" + employeeId + ", ChineseName="
				+ ChineseName + ", flowTypeId=" + flowTypeId + ", flowName=" + flowName + ", message=" + message
				+ ", formData=" + formData + ", createTime=" + createTime + ", errorCode=" + errorCode + ", errorMsg="
				+ errorMsg + "]";
	}
	
}
