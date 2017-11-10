package com.sogou.bizwork.task.api.core.activiti.po;

import java.util.List;

public class SvnDistribution {
	private List<String> managers;
	private String repositorys;
	private List<Object> persons;
	private String remark;
	
	public List<String> getManagers() {
		return managers;
	}
	public void setManagers(List<String> managers) {
		this.managers = managers;
	}
	public String getRepositorys() {
		return repositorys;
	}
	public void setRepositorys(String repositorys) {
		this.repositorys = repositorys;
	}
	public List<Object> getPersons() {
		return persons;
	}
	public void setPersons(List<Object> persons) {
		this.persons = persons;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
