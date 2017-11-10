package com.sogou.bizwork.task.api.core.activiti.dto;

import java.util.List;
import java.util.Set;

public class RepositoryDto {
	private List<Repository> repositories;
	private Set<String> managerIntersection;
	
	
	public List<Repository> getRepositories() {
		return repositories;
	}
	public void setRepositories(List<Repository> repositories) {
		this.repositories = repositories;
	}
	public Set<String> getManagerIntersection() {
		return managerIntersection;
	}
	public void setManagerIntersection(Set<String> managerIntersection) {
		this.managerIntersection = managerIntersection;
	}
	
	
}
