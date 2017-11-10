package com.sogou.bizwork.task.api.core.activiti.service;

import java.util.List;

import com.sogou.adm.bizdev.bizflowapi.thrift.dto.response.ResponseResult;
import com.sogou.bizwork.api.message.BriefTypeMessage;
import com.sogou.bizwork.task.api.core.activiti.bo.ActInfo;
import com.sogou.bizwork.task.api.core.activiti.bo.WorkflowInitMap;
import com.sogou.bizwork.task.api.core.activiti.vo.ApprovalVo;
import com.sogou.bizwork.task.api.core.activiti.vo.FlowTypeRelaionVo;
import com.sogou.bizwork.task.api.core.activiti.vo.QueryConditionVo;
import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowDetailVo;
import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowFormVo;
import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowResVo;

public interface WorkflowService {

	public List<FlowTypeRelaionVo> getFlowTypes();
	/**
	 * 
	 * @param applicationTable    申请表单
	 * @param userId    操作人的employeeId
	 * @param canCompleteWorkflowType   可以在启动流程就完成所有流程的类型
	 * @return
	 */
	public ResponseResult addApplicationToBizflow(WorkflowFormVo applicationTable, Long userId, String canCompleteWorkflowType);
	
	public WorkflowResVo addApplications(QueryConditionVo queryCondition, Long userId);

	public WorkflowDetailVo getWorkflowDetail(String businessKey, Long userId);

//	public void addActivitiInfo(ActInfo actInfo);
	
	public String addApprove(ApprovalVo approvalVo, Long userId, String canCompleteWorkflowType);

	public void updateFormdataByBusinessKey(String businessKey, String formData,String flowName);
	
	public void addActivitiInfo(WorkflowFormVo workflowFormVo, String businessKey, Long userId, String canCompleteWorkflowType);

	public List<WorkflowInitMap> getWorkFLowInitMaps(int flowTypeId);

	public ActInfo getActInfoByBusinessKey(String flowId);
	public List<BriefTypeMessage> getAllFlowTodeal();
}
