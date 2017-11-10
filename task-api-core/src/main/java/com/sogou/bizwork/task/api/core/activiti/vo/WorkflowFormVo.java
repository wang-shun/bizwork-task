package com.sogou.bizwork.task.api.core.activiti.vo;

import java.io.Serializable;

public class WorkflowFormVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sid;
	private Integer flowTypeId;  // 流程类型,这里是svn的权限分配还是申请
	private String flowName;     //流程名
	private String formData;    //
	private String message;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
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
	@Override
	public String toString() {
		return "WorkflowFormVo [sid=" + sid + ", flowTypeId=" + flowTypeId + ", flowName=" + flowName + ", formData="
				+ formData + ", message=" + message + "]";
	}
	
}
