package com.sogou.bizwork.task.api.core.activiti.service;

import com.sogou.adm.bizdev.bizflowapi.thrift.dto.bizflow.HistoricDetailTEntity;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.response.ResponseTask;
import com.sogou.bizwork.task.api.core.activiti.bo.Approval;
import com.sogou.bizwork.task.api.core.activiti.bo.SpecialReturnData;
import com.sogou.bizwork.task.api.core.activiti.vo.ApprovalVo;

public interface SvnApplicationService {

	public ResponseTask  queryMyApplication(String appid, String processDefintionKey, String businessKey, String userId, int i,
			int j);

	public String addApprove(ApprovalVo approvalVo, Long userId, String canCompleteWorkflowType);

	public Object getDataByTaskInitCode(String code, Object data);
	
	public SpecialReturnData doSomeBusinessWithMethodCode(Approval approval, HistoricDetailTEntity data);

	public void approveLeader(String businessKey, Long userId, Integer flowTypeId, String formData, String canCompleteWorkflowType); 
	
}
