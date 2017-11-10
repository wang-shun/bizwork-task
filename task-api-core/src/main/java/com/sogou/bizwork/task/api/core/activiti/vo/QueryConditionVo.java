package com.sogou.bizwork.task.api.core.activiti.vo;

public class QueryConditionVo {
	private String sid;
	private int typeId;
	private Integer flowTypeId;
	private String flowName;
	private int status;
	private String startTime;
	private String startTimeBegin;
	private String startTimeEnd;
	private String endTime;
	private String endTimeBegin;
	private String endTimeEnd;
	private String employee;   //员工中文名字
	private boolean isDone;
	private Integer pageNo;
	private Integer pageSize;
	private Long employeeId;
	
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public Integer getFlowTypeId() {
		return flowTypeId;
	}
	public void setFlowTypeId(Integer flowTypeId) {
		this.flowTypeId = flowTypeId;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public void setIsDone(boolean isDone) {
		this.isDone = isDone;
	}
	public boolean getIsDone() {
		return isDone;
	}
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public String getStartTimeBegin() {
		return startTimeBegin;
	}
	public void setStartTimeBegin(String startTimeBegin) {
		this.startTimeBegin = startTimeBegin;
	}
	public String getStartTimeEnd() {
		return startTimeEnd;
	}
	public void setStartTimeEnd(String startTimeEnd) {
		this.startTimeEnd = startTimeEnd;
	}
	public String getEndTimeBegin() {
		return endTimeBegin;
	}
	public void setEndTimeBegin(String endTimeBegin) {
		this.endTimeBegin = endTimeBegin;
	}
	public String getEndTimeEnd() {
		return endTimeEnd;
	}
	public void setEndTimeEnd(String endTimeEnd) {
		this.endTimeEnd = endTimeEnd;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
}
