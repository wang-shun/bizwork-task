package com.sogou.bizwork.task.api.core.activiti.bo;

public class FlowTypeRelation {
	private Integer parentFlowId;
	private String parentFlowTypeName;
	private Integer flowTypeId;
	private String flowTypeName;
	
	public Integer getParentFlowId() {
		return parentFlowId;
	}
	public void setParentFlowId(Integer parentFlowId) {
		this.parentFlowId = parentFlowId;
	}
	public String getParentFlowTypeName() {
		return parentFlowTypeName;
	}
	public void setParentFlowTypeName(String parentFlowTypeName) {
		this.parentFlowTypeName = parentFlowTypeName;
	}
	public Integer getFlowTypeId() {
		return flowTypeId;
	}
	public void setFlowTypeId(Integer flowTypeId) {
		this.flowTypeId = flowTypeId;
	}
	public String getFlowTypeName() {
		return flowTypeName;
	}
	public void setFlowTypeName(String flowTypeName) {
		this.flowTypeName = flowTypeName;
	}
}
