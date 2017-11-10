package com.sogou.bizwork.task.api.core.activiti.vo;

import java.io.Serializable;

public class ApprovalVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sid;
	private Integer flowTypeId;
	private String flowId;
	private String taskId;
	private boolean isPassed;
	private String message;
	private String formData;
	private String flowName;
	
	public boolean getIsPassed() {
		return isPassed;
	}
	public void setIsPassed(boolean isPassed) {
		this.isPassed = isPassed;
	}
	public Integer getFlowTypeId() {
		return flowTypeId;
	}
	public void setFlowTypeId(Integer flowTypeId) {
		this.flowTypeId = flowTypeId;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFormData() {
		return formData;
	}
	public void setFormData(String formData) {
		this.formData = formData;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
}
