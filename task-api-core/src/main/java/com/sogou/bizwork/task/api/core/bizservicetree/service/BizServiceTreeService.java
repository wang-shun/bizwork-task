package com.sogou.bizwork.task.api.core.bizservicetree.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sogou.bizwork.task.api.core.bizservicetree.GroupAndSvn;

public interface BizServiceTreeService {
	public Map<String, Set<String>> getServiceTreeInfoByEmployeeId(int employeeId);
	public List<GroupAndSvn> getServiceTreeInfos(int employeeId);
	public void addServiceTreeInfo();
}
