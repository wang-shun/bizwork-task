package com.sogou.bizwork.task.api.core.bizservicetree;

import com.sogou.bizservicetree.api.enums.ServiceStatus;
import com.sogou.bizwork.task.api.core.bizservicetree.ServiceTreeInfo;

public class ServiceTreeInfo {
	private String serviceName;
	private String description;
	private ServiceStatus serviceStatus;
	private String opLeader;
	private String devLeader;
	private String qaLeader;
	private String opGroup;
	private String devGroup;
	private String qaGroup;
	private String opCharger;
	private String devCharger;
	private String qaCharger;
	private int is404;    //0:不是404,1:是404
	private String dept;
	
	public ServiceStatus getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(ServiceStatus serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOpLeader() {
		return opLeader;
	}
	public void setOpLeader(String opLeader) {
		this.opLeader = opLeader;
	}
	public String getDevLeader() {
		return devLeader;
	}
	public void setDevLeader(String devLeader) {
		this.devLeader = devLeader;
	}
	public String getQaLeader() {
		return qaLeader;
	}
	public void setQaLeader(String qaLeader) {
		this.qaLeader = qaLeader;
	}
	public String getOpGroup() {
		return opGroup;
	}
	public void setOpGroup(String opGroup) {
		this.opGroup = opGroup;
	}
	public String getDevGroup() {
		return devGroup;
	}
	public void setDevGroup(String devGroup) {
		this.devGroup = devGroup;
	}
	public String getQaGroup() {
		return qaGroup;
	}
	public void setQaGroup(String qaGroup) {
		this.qaGroup = qaGroup;
	}
	public String getOpCharger() {
		return opCharger;
	}
	public void setOpCharger(String opCharger) {
		this.opCharger = opCharger;
	}
	public String getDevCharger() {
		return devCharger;
	}
	public void setDevCharger(String devCharger) {
		this.devCharger = devCharger;
	}
	public String getQaCharger() {
		return qaCharger;
	}
	public void setQaCharger(String qaCharger) {
		this.qaCharger = qaCharger;
	}
	public int getIs404() {
		return is404;
	}
	public void setIs404(int is404) {
		this.is404 = is404;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	@Override
	public String toString() {
		return "ServiceTreeInfo [serviceName=" + serviceName + ", description=" + description + ", serviceStatus="
				+ serviceStatus + ", opLeader=" + opLeader + ", devLeader=" + devLeader + ", qaLeader=" + qaLeader
				+ ", opGroup=" + opGroup + ", devGroup=" + devGroup + ", qaGroup=" + qaGroup + ", opCharger="
				+ opCharger + ", devCharger=" + devCharger + ", qaCharger=" + qaCharger + ", is404=" + is404 + ", dept="
				+ dept + "]";
	}
	
}
