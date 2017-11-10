package com.sogou.bizwork.task.api.common.constants;

/**
 * 定义服务树二级节点类型
 * 
 * @author liquancai
 * 
 */
public enum ServiceNodeTypeEnum {
	ONLINE_APP("线上应用", 2),
	ONLINE_DB("线上数据库", 3),
	OFFLINE_APP("线下应用", 4),
	OFFLINE_DB("线下数据库", 5),
	RESOURCE_POOL("资源池", 6);

	// 节点名称,与数据库中的服务名称一致
	private String serviceNodeTypeName;
	// 节点Id,与数据库中的服务ID一致
	private int serviceNodeTypeCode;

	private ServiceNodeTypeEnum(String name, int code) {
		this.serviceNodeTypeName = name;
		this.serviceNodeTypeCode = code;
	}

	public String getServiceNodeTypeName() {
		return serviceNodeTypeName;
	}

	public int getServiceNodeTypeCode() {
		return serviceNodeTypeCode;
	}

	public static ServiceNodeTypeEnum getServiceNodeTypeEnum(int code) {
		for (ServiceNodeTypeEnum type : ServiceNodeTypeEnum.values()) {
			if (type.getServiceNodeTypeCode() == code) {
				return type;
			}
		}

		return null;
	}
}
