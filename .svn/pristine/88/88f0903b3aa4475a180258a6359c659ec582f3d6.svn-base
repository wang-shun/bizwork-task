package com.sogou.bizwork.task.api.common.result;

import java.io.Serializable;

/**
 * 与ZNode中存储的数据对应
 * 
 * @author liquancai
 * 
 */
public class ZNodeDataResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private int serviceNodeId; // 服务节点ID
	private String serviceNodeName; // 服务节点名称
	private int serviceNodeType; // 节点类型(2-线上应用,3-线上数据库,4-线下应用,5-线下数据库,6-资源池)
	private String updateTime; // 更新时间

	public int getServiceNodeId() {
		return serviceNodeId;
	}

	public void setServiceNodeId(int serviceNodeId) {
		this.serviceNodeId = serviceNodeId;
	}

	public String getServiceNodeName() {
		return serviceNodeName;
	}

	public void setServiceNodeName(String serviceNodeName) {
		this.serviceNodeName = serviceNodeName;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getServiceNodeType() {
		return serviceNodeType;
	}

	public void setServiceNodeType(int serviceNodeType) {
		this.serviceNodeType = serviceNodeType;
	}
}
