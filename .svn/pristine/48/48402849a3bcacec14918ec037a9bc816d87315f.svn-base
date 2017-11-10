package com.sogou.bizwork.task.api.core.activiti.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.core.activiti.po.RepositoryResult;
import com.sogou.bizwork.task.api.core.activiti.po.ServiceResult;

@Service("repositoryService")
public interface RepositoryService {

    public ServiceResult isRepositoryExist(String username, String reponame);

    public RepositoryResult applyRepository(String businessKey);

    public ServiceResult createPath();

    public ServiceResult grantPermission();
    
    public  Map<String, Object> getManagersByPaths(String url, Map<String, String> params);

	public boolean checkSvnName(String reponame);
	
	public RepositoryResult distributePermission(String businessKey, long employeeId);
	
	public List<String> getRepositories(String username);

}
