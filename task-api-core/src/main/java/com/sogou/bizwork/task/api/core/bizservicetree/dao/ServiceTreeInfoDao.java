package com.sogou.bizwork.task.api.core.bizservicetree.dao;

import java.util.List;

import com.sogou.bizwork.task.api.core.bizservicetree.ServiceTreeInfo;

public interface ServiceTreeInfoDao {
	public void addServiceTreeInfo(List<ServiceTreeInfo> serviceTreeInfos);

	public void clearServiceTreeInfo();

	public List<ServiceTreeInfo> getServiceTreeInfos();
}
