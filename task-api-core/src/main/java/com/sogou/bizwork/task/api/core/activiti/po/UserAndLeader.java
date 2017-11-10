package com.sogou.bizwork.task.api.core.activiti.po;

public class UserAndLeader {
	private String name;    //员工姓名
	private String leader;    //上级姓名
	private int employeeId;    //员工id
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	@Override
	public String toString() {
		return "UserAndLeader [name=" + name + ", leader=" + leader + ", employeeId=" + employeeId + "]";
	}
	
}
