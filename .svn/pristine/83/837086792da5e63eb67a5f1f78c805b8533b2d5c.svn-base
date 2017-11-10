package com.sogou.bizwork.task.api.core.activiti.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sogou.bizwork.task.api.core.activiti.bo.ActInfo;
import com.sogou.bizwork.task.api.core.activiti.bo.BusinessKeyAndFlowInfo;
import com.sogou.bizwork.task.api.core.activiti.bo.FlowTypeRelation;
import com.sogou.bizwork.task.api.core.activiti.bo.WorkflowInitMap;
import com.sogou.bizwork.task.api.core.activiti.vo.QueryConditionVo;
import com.sogou.bizwork.task.api.core.common.dto.QueryCondition;

public interface WorkflowDao {
	public List<FlowTypeRelation> getFlowTypes();
	
	public void addActivitiInfo(ActInfo actInfo);
	
	public List<ActInfo> getActivitiInfos(List<String> businessKeys);
	
	public ActInfo getActivitiInfo(String businessKey);
	
	public List<String> getBusinessKeysByConditions(QueryConditionVo queryConditionVo);
	
	public List<WorkflowInitMap> getWorkFLowInitMaps(Integer flowTypeId);
	
	public void updateFormdataByBusinessKey(@Param(value = "businessKey")String businessKey, @Param(value = "formData" )String formData,@Param(value="flowName") String flowName);
	
	public void deleteWorkflowByBusinessKey(String businessKey);
}
