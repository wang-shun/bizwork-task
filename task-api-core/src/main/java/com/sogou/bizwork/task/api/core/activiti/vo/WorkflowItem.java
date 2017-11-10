package com.sogou.bizwork.task.api.core.activiti.vo;

public class WorkflowItem {
	private String flowId;
	private String flowName;
//	private String taskId;
	private String employee;
	private String startTime;
	private String endTime;
	private String status;    
	private String group;
	
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
//	public String getTaskId() {
//		return taskId;
//	}
//	public void setTaskId(String taskId) {
//		this.taskId = taskId;
//	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
}
