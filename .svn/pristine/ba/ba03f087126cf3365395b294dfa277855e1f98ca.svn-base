package com.sogou.bizwork.task.api.core.activiti.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sogou.bizwork.api.message.BriefTypeMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.bizflow.HistoricDetailTEntity;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.bizflow.HistoricUserTaskInstanceTEntity;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.bizflow.ProcessInstanceTEntity;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.bizflow.TaskTEntity;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.query.ProcessQueryTEntity;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.query.TaskQueryTEntity;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.response.ResponseHistoricUserTaskInstance;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.response.ResponseProcessInstance;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.response.ResponseResult;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.response.ResponseTask;
import com.sogou.adm.bizdev.bizflowapi.thrift.iface.process.ProcessInfoTService;
import com.sogou.adm.bizdev.bizflowapi.thrift.iface.task.TaskTService;
import com.sogou.bizwork.task.api.core.activiti.bo.ActInfo;
import com.sogou.bizwork.task.api.core.activiti.bo.Approval;
import com.sogou.bizwork.task.api.core.activiti.bo.FlowTypeRelation;
import com.sogou.bizwork.task.api.core.activiti.bo.WorkflowInitMap;
import com.sogou.bizwork.task.api.core.activiti.dao.WorkflowDao;
import com.sogou.bizwork.task.api.core.activiti.po.FlowNode;
import com.sogou.bizwork.task.api.core.activiti.po.FlowType;
import com.sogou.bizwork.task.api.core.activiti.service.EntryService;
import com.sogou.bizwork.task.api.core.activiti.service.SvnApplicationService;
import com.sogou.bizwork.task.api.core.activiti.service.WorkflowService;
import com.sogou.bizwork.task.api.core.activiti.vo.ApprovalVo;
import com.sogou.bizwork.task.api.core.activiti.vo.FlowTypeRelaionVo;
import com.sogou.bizwork.task.api.core.activiti.vo.QueryConditionVo;
import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowDetailVo;
import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowFormVo;
import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowItem;
import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowResVo;
import com.sogou.bizwork.task.api.core.task.service.TaskService;
import com.sogou.bizwork.task.api.core.user.dao.UserMapper;
import com.sogou.bizwork.task.api.core.user.dto.GroupInfoDto;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.po.ChineseNameAndGroup;
import com.sogou.bizwork.task.api.core.user.service.UserService;

@Service("workflowService")
@SuppressWarnings("unchecked")
public class WorkflowServiceImpl implements WorkflowService {
    private static final Logger logger = LoggerFactory.getLogger(SvnApplicationServiceImpl.class);

	@Autowired
	private SvnApplicationService svnApplicationService;
	@Autowired
	private WorkflowDao workflowDao;
	@Autowired(required = false)
	private UserMapper userMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private ProcessInfoTService.Iface processInfoTService;  //soa service ,这个是在 xml中定义的
	@Autowired
	private TaskTService.Iface taskTService;  // workflow soa xml 中
	@Autowired
	private TaskService taskService;
	@Autowired
	private EntryService entryService;

	private String bsk = "";


	
	@Override
	public ResponseResult addApplicationToBizflow(WorkflowFormVo workflowFormVo, Long userId, String canCompleteWorkflowType) {

		ResponseResult responseResult = new ResponseResult();
		Map<String, String> parameters = new HashMap<String, String>();
		Long leaderId = userService.getLeaderIdByEmployeeId(userId); // leaderId  公用的
		if (leaderId == null) {
			leaderId = 204516l;
			logger.error("get empty leaderId error! the userId=" + userId);
		}
		if (workflowFormVo.getFlowTypeId() == 101) {    //如果该人为404管理员（即opLeader），则可以直接完成整个流程
			List<WorkflowInitMap> workflowInitMaps = workflowDao.getWorkFLowInitMaps(101); // 初始化流的参数，流类型
			if (CollectionUtils.isNotEmpty(workflowInitMaps)) {
				WorkflowInitMap workflowInitMap = workflowInitMaps.get(0); // 得到第一个 数据库存在该类型的流
				if (workflowInitMap != null) {
					String opLeader = workflowInitMap.getParamValue();    //取出opleader 3113
					if ((userId + "").equals(opLeader)) {         //如果操作人为opleader，那么可以顺利通过所有流程
						canCompleteWorkflowType = "opLeader";    //将canCompleteWorkflowType赋值为opLeader
					}
				}
			}
		} else if (workflowFormVo.getFlowTypeId() == 102) { //当前流程是svn权限分配    //如果该人为svn仓库管理员（即opLeader），则可以直接完成整个流程
			UserDTO userDTO = userMapper.getUserByEmployeeId(Integer.parseInt(userId + ""));
			try {
				Map<String, Object> formMap = (Map<String, Object>) JSONUtil.deserialize(workflowFormVo.getFormData());
				List<String> managers = (List<String>) formMap.get("managers");    //取出仓库管理员,从提交的得到
				if (CollectionUtils.isNotEmpty(managers)) {
					for (String manager: managers) {
						if (manager.equals(userDTO.getUserName())) {    //如果操作人为仓库管理员（svnManager），那么可以顺利通过所有流程
							canCompleteWorkflowType = "svnManager";    //将canCompleteWorkflowType赋值为svnManager
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		int count = userMapper.getGroupLeaderCount(userId);    //如果count>0，那么该操作人为leader
		if ("opLeader".equals(canCompleteWorkflowType)
				|| "svnManager".equals(canCompleteWorkflowType)
				|| count > 0) {    //opleader、svn仓库管理员或leader本人发起的申请。需要将leaderId置为操作人本人
			parameters.put("leaderId", userId + "");
		} else {   // 不是leader 就使用真正的leader
			parameters.put("leaderId", leaderId + "");  // 我不是leader
		}
		
		List<WorkflowInitMap> workflowInitMaps =
				workflowDao.getWorkFLowInitMaps(workflowFormVo.getFlowTypeId());    //本地mysql  svn仓库节点审批人 从数据库取出仓库管理员
		List<String> candidateUserIds = new ArrayList<String>();
		String businessKey = "";
		if (CollectionUtils.isNotEmpty(workflowInitMaps)) {
			if ("svnManager".equals(canCompleteWorkflowType)) {    //svn仓库管理员在在权限分配审批节点候选人设为自己
				candidateUserIds.add(userId + ""); // 添加候选人
			} else {            //wo不是manager
				for (WorkflowInitMap w : workflowInitMaps) {
					if (StringUtils.isNotBlank(w.getParamKey()) && StringUtils.isNotBlank(w.getParamKey())) {
						Object objectData =    // 一个数
								svnApplicationService.getDataByTaskInitCode(w.getParamValue(), workflowFormVo);  // 返回的是 manager id
						if (w.getParamValue().contains("_")) {    // 这是权限分配
							if (w.getParamValue().equals("102_2")) {       //得到候选人节点参数
								candidateUserIds = (List<String>) objectData;  // 候选人 ,管理员
							}
							parameters.put(w.getParamKey(), objectData.toString());    //将候选人参数放入  candidateUserIds 为key
						} else {
							parameters.put(w.getParamKey(), w.getParamValue());  // opleader 或者 candidateUserIds
						}
					}
				}
			}
			/* svnDistribution   */
			String initBusinessKey = workflowInitMaps.get(0).getBusinessKey();    //提交表单之前要到数据库中取出businesskey  流类型
			try {

				//-------------------------------------------
				//-------------------------------
				//-------------------------------
				//启动工作流
				//-------------------------------
				//-------------------------------
				//-------------------------------------------
				// 这个就是远程的方法了
				responseResult = processInfoTService.submitStartFormByProcessDefinitionKey(initBusinessKey,
						"", parameters, userId + "");     //启动流程,实例化一个流程,该流程是什么业务
							//权限分配的时候就是提交的初始化参数是leader  和manager
				List<String> businessKeys = new ArrayList<String>();

				businessKey = responseResult.getData();  //远程的business key 唯一标示,根据业务的key封装成唯一key
				
				//-------------------------------------------
				//-------------------------------
				//-------------------------------
				//将表单记录至数据库
				//-------------------------------
				//-------------------------------
				//-------------------------------------------
				this.addActivitiInfo(workflowFormVo, responseResult.getData(), userId, canCompleteWorkflowType); // 工作流程记录到本地数据库
				if (count == 0 
						&& !"svnManager".equals(canCompleteWorkflowType)
						&& !"opLeader".equals(canCompleteWorkflowType)) {    //给上级发邮件，提醒上级审批
					//busindessKey.append(responseResult.getData());
					//bsk.append(responseResult.getData());
//					bsk += responseResult.getData() + ",";
////					if (args.length != 0 && args[0] == 0){
//						taskService.sendSvnApproveMail(bsk.split(","), leaderId); //我不是leader
//					}
//					if (args.length == 0)
					taskService.sendSvnApproveMail(businessKey, leaderId); //我不是leader
				}
				
				
				if (StringUtils.isNotBlank(businessKey)) {
					businessKeys.add(businessKey);
					TaskQueryTEntity taskQueryTEntity = new TaskQueryTEntity();
					taskQueryTEntity.setBusinessKeys(businessKeys);  // 远程activiti的
					ResponseTask responseTask = 
							taskTService.getTaskList("bizwork", taskQueryTEntity, 0, -1);  // 待
					if (CollectionUtils.isNotEmpty(candidateUserIds)) {    //判断该人是否
						List<TaskTEntity> taskTEntities = responseTask.getData();
						if (CollectionUtils.isNotEmpty(taskTEntities)) {
							TaskTEntity taskTEntity = taskTEntities.get(0);
							if (taskTEntity != null) {    //设置当前任务的待审批人
								taskTService.setTaskAssignList(taskTEntity.getId()
										, "candidateUserIds", candidateUserIds); //之前设置了leader,这里设置另一个
							}
						}
					}
				}
			} catch (TException e) {
				e.printStackTrace();
			}
		} else {
			responseResult.isSuccess = false;
			responseResult.setData("Submit error! For workflowType is not registered!");
		}
		if ("opLeader".equals(canCompleteWorkflowType) && workflowFormVo.getFlowTypeId() == 101) {
			svnApplicationService.approveLeader(businessKey, userId, 101, workflowFormVo.getFormData(), "opLeader");
		} else if ("svnManager".equals(canCompleteWorkflowType) && workflowFormVo.getFlowTypeId() == 102) {
			svnApplicationService.approveLeader(businessKey, userId, 102, workflowFormVo.getFormData(), "svnManager");
		} else if (count > 0 && StringUtils.isNotBlank(businessKey)) {    //如果数量大于0，那么该申请人为小组leader，leader审批自动通过
			svnApplicationService.approveLeader(businessKey, userId,
					workflowFormVo.getFlowTypeId(), workflowFormVo.getFormData(), "");
		} 
		
		return responseResult;
	}
	
	
	@Override
	public WorkflowResVo addApplications(QueryConditionVo queryConditionVo, Long userId) {
		WorkflowResVo workflowResVo = new WorkflowResVo();
		List<WorkflowItem> list = new ArrayList<WorkflowItem>();
		List<String> businessKeys = new ArrayList<String>();
		boolean queryAll = false;            //queryAll没实际作用，可 增强可读性
		String userIdStr = "";

		if (StringUtils.isBlank(queryConditionVo.getFlowName()) &&
				StringUtils.isBlank(queryConditionVo.getEmployee()) &&
				queryConditionVo.getFlowTypeId() == 0 &&
				StringUtils.isBlank(queryConditionVo.getStartTime())) {   //检查查询条件
			queryAll = true;
			workflowResVo.setTotalNumber(10);
			workflowResVo.setList(list);
			return workflowResVo;   // 这样就是返回的是空了
		}
		entryService.updateEntryProcess();     //启动新员工入职流程
		List<String> businessKeyList = new ArrayList<String>();
		if (queryConditionVo.getTypeId() == 1) {   //1、待处理的申请 2、我的申请 3、所有人的申请
			userIdStr = userId + "";
		} else if (queryConditionVo.getTypeId() == 2) {
			queryConditionVo.setEmployeeId(userId);
		} else if (queryConditionVo.getTypeId() == 3) {
			
		}
		if (CollectionUtils.isEmpty(businessKeyList)) {
			if (!queryAll) {
				if (StringUtils.isNotBlank(queryConditionVo.getStartTime())) {
					String[] startTime = queryConditionVo.getStartTime().split(",");
					if (startTime != null) {
						if (startTime.length == 1) {
							queryConditionVo.setStartTimeBegin(startTime[0]);
						} else if (startTime.length == 2) {
							if (StringUtils.isNotBlank(startTime[0])) {
								queryConditionVo.setStartTimeBegin(startTime[0]);
							}
							if (StringUtils.isNotBlank(startTime[1])) {
								queryConditionVo.setStartTimeEnd(startTime[1]);
							}
						}
					}
				}
				
				if (StringUtils.isNotBlank(queryConditionVo.getEndTime())) {
					String[] endTime = queryConditionVo.getEndTime().split(",");
					if (endTime != null) {
						if (endTime.length == 1) {
							queryConditionVo.setStartTimeBegin(endTime[0]);
						} else if (endTime.length == 2) {
							if (StringUtils.isNotBlank(endTime[0])) {
								queryConditionVo.setEndTimeBegin(endTime[0]);
							}
							if (StringUtils.isNotBlank(endTime[1])) {
//								queryConditionVo.setEndTimeEnd(endTime[1]);
								  
						        Calendar c = Calendar.getInstance();  
						        Date date = null;  
						        try {  
						            date = new SimpleDateFormat("yy-MM-dd").parse(endTime[1]);  
						        } catch (ParseException e) {  
						            e.printStackTrace();  
						        }  
						        c.setTime(date);  
						        int day = c.get(Calendar.DATE);  
						        c.set(Calendar.DATE, day + 1);  
						  
						        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")  
						                .format(c.getTime()); 
						        queryConditionVo.setEndTime(dayAfter);
							}
						}
					}
				}
				queryConditionVo.setFlowName(this.getQueryStr(queryConditionVo.getFlowName()));
				queryConditionVo.setEmployee(this.getQueryStr(queryConditionVo.getEmployee()));
				businessKeys = workflowDao.getBusinessKeysByConditions(queryConditionVo); // get all businesskeys
			}
		} else {
			businessKeys = businessKeyList;
		}
		
		ProcessQueryTEntity processQueryTEntity = new ProcessQueryTEntity();
		processQueryTEntity.setProcessDefinitionKey("");
		processQueryTEntity.setBusinessKeys(businessKeys);
		
		if (queryConditionVo.getStatus() == 0) {  //全部
			processQueryTEntity.setStatus(0);
			if (queryConditionVo.getTypeId() == 1) {
				processQueryTEntity.setStatus(1);
			}
		} else if (queryConditionVo.getStatus() == 1) {  //已完成
			processQueryTEntity.setStatus(-1);
		} else if (queryConditionVo.getStatus() == 2) {  //进行中
			processQueryTEntity.setStatus(1);
		} else if (queryConditionVo.getStatus() == 3) {  //已取消
			
		}
		
		processQueryTEntity.setEndStartTime(queryConditionVo.getEndTimeBegin());
		processQueryTEntity.setEndEndTime(queryConditionVo.getEndTimeEnd());
		if (!queryAll && CollectionUtils.isEmpty(businessKeys)) {
			return workflowResVo;
		}
		processQueryTEntity.setBusinessKeys(businessKeys);
		TaskQueryTEntity taskQueryTEntity = new TaskQueryTEntity();
		taskQueryTEntity.setBusinessKeys(businessKeys);
		taskQueryTEntity.setUserId(userIdStr);
		if (queryConditionVo.getTypeId() == 1) {  //查询待处理申请
			return this.addWorkflowItems(taskQueryTEntity, userIdStr,
					queryConditionVo.getPageSize(), queryConditionVo.getPageNo());
		}
			return this.addWorkflowItems(businessKeys, processQueryTEntity, userIdStr,
					queryConditionVo.getPageNo(), queryConditionVo.getPageSize(), workflowResVo);
		
	}
	
	private WorkflowResVo addWorkflowItems(TaskQueryTEntity taskQueryTEntity,
			String userId, int pageSize, int pageNum) {
		WorkflowResVo workflowResVo = new WorkflowResVo();
		Map<String, WorkflowItem> map = new HashMap<String, WorkflowItem>();
		List<String> businessKeys = new ArrayList<String>();
		List<WorkflowItem> res = new ArrayList<WorkflowItem>();
		try {
			ResponseTask responseTask = 
					taskTService.getTaskList("bizwork", taskQueryTEntity, 0, -1);    //待处理的task
			List<TaskTEntity> taskTEntities = responseTask.getData();
			if (!CollectionUtils.isEmpty(taskTEntities)) {
				for (TaskTEntity taskTEntity : taskTEntities) {
					WorkflowItem workflowItem = new WorkflowItem();
					String businessKey = taskTEntity.getBusinessKey();
					if (StringUtils.isNotBlank(businessKey)) {
						businessKeys.add(businessKey);
						workflowItem.setFlowId(businessKey);
						if (!StringUtils.isBlank(taskTEntity.getAssignee())) {
							UserDTO userDTO = userMapper.getUserByEmployeeId(Integer.parseInt(taskTEntity.getAssignee()));
							workflowItem.setStatus("状态：【" + taskTEntity.getName() + "】，等待" + userDTO.getChineseName() + "处理");
						} else {
							List<String> userIds = taskTEntity.getCandidateUserIds();
							String usernames = "";
							if (CollectionUtils.isNotEmpty(userIds)) {
								for (int i = 0; i < userIds.size(); i++) {
									UserDTO userDTO = userMapper.getUserByEmployeeId(Integer.parseInt(userIds.get(i)));
									if (userDTO != null && StringUtils.isNotBlank(userDTO.getChineseName())) {
										if (i == 0) {
											usernames += userDTO.getChineseName();
										} else {
											usernames += "、" + userDTO.getChineseName();
										}
									}
								}
							}
							workflowItem.setStatus("状态：【" + taskTEntity.getName() + "】，等待" + usernames + "处理");
						}
						map.put(businessKey, workflowItem);
						res.add(workflowItem);
					}
				}
			}
			if (CollectionUtils.isNotEmpty(businessKeys)) {
				Set<String> businessKeySet = new HashSet<String>(businessKeys);
				
				Set<Long> employeeIdSet = new HashSet<Long>();
				if (CollectionUtils.isNotEmpty(businessKeySet)) {  //add flowname to workflowItem
					List<ActInfo> actInfos = workflowDao.getActivitiInfos(new ArrayList<String>(businessKeySet));
					if (CollectionUtils.isNotEmpty(actInfos)
							&& !org.springframework.util.CollectionUtils.isEmpty(map)) {
						for (ActInfo act : actInfos) {
							WorkflowItem wItem = map.get(act.getBusinessKey());
							wItem.setStartTime(act.getCreateTime().toString());
							wItem.setFlowName(act.getFlowName());
							wItem.setEmployee(act.getEmployeeId() + "");
							employeeIdSet.add(act.getEmployeeId());
						}
					}
				}

				if (CollectionUtils.isNotEmpty(employeeIdSet)) {  //add chinesename and group to workflowItem
					List<ChineseNameAndGroup> cng = 
							userMapper.getChineseNameAndGroup(new ArrayList<Long>(employeeIdSet));
					if (CollectionUtils.isNotEmpty(cng) &&
							CollectionUtils.isNotEmpty(res)) {
						for (WorkflowItem w : res) {
							for (ChineseNameAndGroup c : cng) {
								if (w.getEmployee().equals(c.getEmployeeId() + "")) {
									w.setEmployee(c.getChineseName());
									w.setGroup(c.getGroupName());
									break;
								}
							}
						}
					}
				}
			}
			workflowResVo.setTotalNumber(res.size());
//			workflowResVo.setList(res);
			int start = (pageNum - 1) * pageSize;    //分页
			int end = start + pageSize;
			List<WorkflowItem> workflowItems = new ArrayList<WorkflowItem>();
			for (int i = res.size() - 1; i >= 0; i--) {
				workflowItems.add(res.get(i));
			}
			if (start >= res.size()) {
//				workflowResVo.setList(res);
			} else if (start < res.size() && res.size() < end) {
				workflowResVo.setList(workflowItems.subList(start, res.size()));
			} else {
				workflowResVo.setList(workflowItems.subList(start, end));
			}
		} catch (TException e) {
			e.printStackTrace();
		}
		return workflowResVo;
	}
	
	private WorkflowResVo addWorkflowItems(List<String> businessKeys, ProcessQueryTEntity processQueryTEntity,
			String userIdStr, int pageNum, int pageSize, WorkflowResVo workflowResVo) {
		List<WorkflowItem> res = new ArrayList<WorkflowItem>();
		ResponseProcessInstance responseProcessInstance = null;
		try {
//			processQueryTEntity.setBusinessKeys(null);   //清空所有过滤条件
			responseProcessInstance = processInfoTService.queryProcessInstanceList("bizwork", processQueryTEntity, pageNum, pageSize);
			Iterator<ProcessInstanceTEntity> process = responseProcessInstance.getDataIterator();
			if (process == null) {
				return workflowResVo;
			}
			Map<String, WorkflowItem> map = new HashMap<String, WorkflowItem>();
			Set<Long> employeeIdSet = new HashSet<Long>();
			Set<String> businessKeySet = new HashSet<String>();
			while (process.hasNext()) {
				ProcessInstanceTEntity p = process.next();
				WorkflowItem workflowItem = new WorkflowItem();
				if (StringUtils.isNotBlank(p.getStartUserId())) {
					employeeIdSet.add(Long.parseLong(p.getStartUserId()));
				}
				workflowItem.setEmployee(p.getStartUserId());
				workflowItem.setStartTime(p.getBeginTime());
				workflowItem.setEndTime(p.getEndTime());
				workflowItem.setFlowId(p.getBusinessKey());
				workflowItem.setStatus(p.getStatus());
				businessKeySet.add(p.getBusinessKey());
				map.put(p.getBusinessKey(), workflowItem);
				res.add(workflowItem);
			}
			
			if (CollectionUtils.isNotEmpty(businessKeySet)) {  //add flowname to workflowItem
				List<ActInfo> actInfos = workflowDao.getActivitiInfos(new ArrayList<String>(businessKeySet));
				if (CollectionUtils.isNotEmpty(actInfos)
						&& !org.springframework.util.CollectionUtils.isEmpty(map)) {
					for (ActInfo act : actInfos) {
						WorkflowItem wItem = map.get(act.getBusinessKey());
						wItem.setFlowName(act.getFlowName());
					}
				}
			}
			
			if (CollectionUtils.isNotEmpty(employeeIdSet)) {  //add chinesename and group to workflowItem
				List<ChineseNameAndGroup> cng = 
						userMapper.getChineseNameAndGroup(new ArrayList<Long>(employeeIdSet));
				if (CollectionUtils.isNotEmpty(cng) &&
						CollectionUtils.isNotEmpty(res)) {
					for (WorkflowItem w : res) {
						for (ChineseNameAndGroup c : cng) {
							if (w.getEmployee().equals(c.getEmployeeId() + "")) {
								w.setEmployee(c.getChineseName());
								w.setGroup(c.getGroupName());
								break;
							}
						}
					}
				}
			}
		} catch (TException e) {
			e.printStackTrace();
		}
		workflowResVo.setList(res);
		workflowResVo.setTotalNumber((int)responseProcessInstance.getTotalNum());
		return workflowResVo;
	}
	
	private String getQueryStr(String str) {
		if (StringUtils.isBlank(str) || str.trim().equals("")) {
			return null;
		}
		return str.trim();
	}
	

	@Override
	public List<FlowTypeRelaionVo> getFlowTypes() {
		List<FlowTypeRelation> flowTypeRelations = workflowDao.getFlowTypes();
		List<FlowTypeRelaionVo> res = new ArrayList<FlowTypeRelaionVo>();
		Set<Integer> parentIds = new HashSet<Integer>();
		if (CollectionUtils.isNotEmpty(flowTypeRelations)) {
			for (FlowTypeRelation f : flowTypeRelations) {
				FlowType ft = new FlowType();
				ft.setFlowTypeId(f.getFlowTypeId());
				ft.setFlowTypeName(f.getFlowTypeName());
				if (parentIds.add(f.getParentFlowId())) {
					FlowTypeRelaionVo fvo = new FlowTypeRelaionVo();
					fvo.setFlowTypeId(f.getParentFlowId());
					fvo.setFlowTypeName(f.getParentFlowTypeName());
					List<FlowType> fts = new ArrayList<FlowType>();
					fts.add(ft);
					fvo.setSubFlowTypeList(fts);
					res.add(fvo);
				} else {
					List<FlowType> fts = res.get(res.size() - 1).getSubFlowTypeList();
					fts.add(ft);
				}
			}
		}
		return res;
	}

	@Override
	public WorkflowDetailVo getWorkflowDetail(String businessKey, Long userId) {
		WorkflowDetailVo workflowDetailVo = new WorkflowDetailVo();
		if (businessKey.endsWith(", pubid=bizwork")) {    //小P中访问会带上", pubid=bizwork"，需要把这个后缀去了，手机端才可以正确访问
			businessKey = businessKey.split(",")[0];
		}
		workflowDetailVo.setBusinessKey(businessKey);
		ActInfo actInfo = workflowDao.getActivitiInfo(businessKey);
		if (actInfo != null) {
			workflowDetailVo.setFormData(actInfo.getFormData());
			workflowDetailVo.setFlowName(actInfo.getFlowName());
		}
		workflowDetailVo.setFlowTypeId(actInfo.getFlowTypeId());
		try {    //获取流程图片链接
			ResponseResult runningInfo = processInfoTService.getProcessRunningInfo(businessKey);
			workflowDetailVo.setFlowImageUrl(runningInfo.getData());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		List<FlowNode> flowNodes = new ArrayList<FlowNode>();
		
		//加入发起人节点
		FlowNode firstNode = new FlowNode();
		if (actInfo.getCreateTime() != null) {
			firstNode.setDateTime(actInfo.getCreateTime().toString());
		}
		firstNode.setEmployee(actInfo.getChineseName());
		firstNode.setName("发起申请");
		firstNode.setRemarks(actInfo.getMessage());
		firstNode.setStatus("已完成");
		flowNodes.add(firstNode);
		
		
		//已经完成的节点
		try {
			ResponseHistoricUserTaskInstance instances = 
					taskTService.getHistoricUserTaskInstanceList(businessKey);
			List<HistoricUserTaskInstanceTEntity> userTasks = instances.getData();
			if (CollectionUtils.isNotEmpty(userTasks)) {
				for (HistoricUserTaskInstanceTEntity userTask : userTasks) {
					if (StringUtils.isBlank(userTask.getAssignee()))  //跳过不是userTask的及assignee为空的
						continue;
					
					//取出userTask
					FlowNode flowNode = new FlowNode();
					String[] assignees = userTask.getAssignee().split(",");
					for (int i = 0; i < assignees.length; i++) {
						UserDTO userDTO = userMapper.getUserByEmployeeId(Integer.parseInt(assignees[i]));
						if (userDTO != null) {
							if (i == 0) {
								flowNode.setEmployee(userDTO.getChineseName());
							} else {
								flowNode.setEmployee(flowNode.getEmployee() + "、" + userDTO.getChineseName());
							}
						}
					}
					flowNode.setDateTime(userTask.getEndTime());
					flowNode.setName(userTask.getActivityName());
					List<HistoricDetailTEntity> hDetails = userTask.getParamDetails();
					Approval approval = new Approval();
					for (HistoricDetailTEntity hDetail : hDetails) {
						String paramName = hDetail.getName();
						if (paramName.endsWith("IsPassed")) {
							if (hDetail.getValue().equals("true")) {
								flowNode.setStatus("同意");
								approval.setPassed(true);
							} else if (hDetail.getValue().equals("false")) {
								flowNode.setStatus("拒绝");
								approval.setPassed(false);
							}
						} else if (paramName.endsWith("Comment")) {
							flowNode.setRemarks(hDetail.getValue());
							approval.setComment(hDetail.getValue());
						}
					}
					flowNodes.add(flowNode);
				}
				workflowDetailVo.setNodeList(flowNodes);
			}
		} catch (TException e1) {
			e1.printStackTrace();
		}
		
		//当前待处理的节点
		List<String> businessKeys = new ArrayList<String>();
		businessKeys.add(businessKey);
		try {
			TaskQueryTEntity taskQueryTEntity = new TaskQueryTEntity();
			taskQueryTEntity.setBusinessKeys(businessKeys);
			ResponseTask responseTask = 
					taskTService.getTaskList("bizwork", taskQueryTEntity, 0, -1);
			List<TaskTEntity> taskTEntities = responseTask.getData();
			boolean mayDelete = false;    //可能拥有删除权限
			if (CollectionUtils.isNotEmpty(taskTEntities)) {   //判断是否能审批
				TaskTEntity taskTEntity = taskTEntities.get(0); 
				if (taskTEntity.getTaskDefinitionKey().contains("reApply")) {  //如果包含重新提交，那么可能有重新提交权限
					mayDelete = true;
				}
				if (taskTEntity != null) {
					FlowNode flowNode = new FlowNode();
					if (StringUtils.isNotBlank(taskTEntity.getAssignee())) {
						if (taskTEntity.getAssignee().equals(userId + "")) {
							workflowDetailVo.setCanAuthorize(true);
						}
					} else {
						if (CollectionUtils.isNotEmpty(taskTEntity.getCandidateUserIds())) {    //如果候选审批人不为空
							taskTEntity.getCandidateUserIds().remove("[]");
							if (CollectionUtils.isNotEmpty(taskTEntity.getCandidateUserIds())) {
								if (taskTEntity.getCandidateUserIds().contains(userId + "")) {    //如果候选审批人包含操作人，那么操作人就有审批权限
									workflowDetailVo.setCanAuthorize(true);
								}
								for (String uId : taskTEntity.getCandidateUserIds()) {    //获取待处理人的姓名
										UserDTO userDTO = userMapper.getUserByEmployeeId(Integer.parseInt(uId));
										if (userDTO != null) {
											if (StringUtils.isBlank(flowNode.getEmployee())) {
												flowNode.setEmployee(userDTO.getChineseName());
											} else {
												flowNode.setEmployee(flowNode.getEmployee() + "、" + userDTO.getChineseName());
											}
										}
								}
							}
						} else if (CollectionUtils.isNotEmpty(taskTEntity.getCandidateGroupIds())) {    //如果候选组不为空
							if (taskTEntity.getCandidateGroupIds().contains(userId + "")) {
								workflowDetailVo.setCanAuthorize(true);
							}
							for (String uId : taskTEntity.getCandidateGroupIds()) {
									UserDTO userDTO = userMapper.getUserByEmployeeId(Integer.parseInt(uId));
									if (userDTO != null) {
										if (StringUtils.isNotBlank(flowNode.getEmployee())) {
											flowNode.setEmployee(userDTO.getChineseName());
										} else {
											flowNode.setEmployee(flowNode.getEmployee() + "、" + userDTO.getChineseName());
										}
									}
							}
						} else {
							workflowDetailVo.setCanAuthorize(false);
						}
						flowNode.setName(taskTEntity.getName());
						flowNodes.add(flowNode);    //将没有assign的这个节点加入
						
					}
					if (workflowDetailVo.isCanAuthorize()) {  //判断是否能编辑
						ProcessQueryTEntity processQueryTEntity = new ProcessQueryTEntity();
						processQueryTEntity.setBusinessKeys(businessKeys);
						processQueryTEntity.setStartUserId(userId + "");
						ResponseProcessInstance responseProcessInstance =
								processInfoTService.queryProcessInstanceList
								("bizwork", processQueryTEntity, 1, -1);
						if (responseProcessInstance != null &&
								responseProcessInstance.totalNum > 0) {
							workflowDetailVo.setCanEdit(true);
						} else {
							List<WorkflowInitMap> workflowInitMaps = workflowDao.getWorkFLowInitMaps(101);
							if (CollectionUtils.isNotEmpty(workflowInitMaps)) {
								WorkflowInitMap workflowInitMap = workflowInitMaps.get(0);
								if (workflowInitMap != null) {
									String editUserId = workflowInitMap.getParamValue();
									if ((userId + "").equals(editUserId)) {
										workflowDetailVo.setCanEdit(true);
									}
								}
							}
						}
						if (mayDelete && workflowDetailVo.isCanEdit()) {    //如果可编辑，那么判断是否可挂起流程
							if (actInfo.getEmployeeId().equals(userId)) {
								workflowDetailVo.setCanDelete(true);
							}
						}
					}
//					flowNode.setName(taskTEntity.getName());
//					flowNode.setStatus("待操作");
//					flowNodes.add(flowNode);
				}
				workflowDetailVo.setTaskId(taskTEntity.getId());
			}
			if (workflowDetailVo.getFlowTypeId() == 201) {    //新员工入职流程的流程类型id为201，由于每个节点的表单数据不同，所以要特殊处理
				String formData = workflowDetailVo.getFormData();
				Map<String, Object> formDataMap = null;
				try {
					formDataMap = (Map<String, Object>) JSONUtil.deserialize(formData);
					formDataMap.put("name", actInfo.getChineseName());
					UserDTO userDTO = userService.getUserByEmployeeId(Integer.parseInt((actInfo.getEmployeeId() + "")));
					UserDTO leaderDTO = userService.getUserByEmployeeId(userDTO.getLeaderId());
					if (formDataMap.get("step") == null || (Long)formDataMap.get("step") <= 1) {
						formDataMap.put("step", 1);
						formDataMap.put("leader", leaderDTO.getChineseName());
					} else if ((Long)formDataMap.get("step") == 2) {
						formDataMap.put("email", userDTO.getEmail());
						formDataMap.put("groupId", userDTO.getGroupId());
					} else if ((Long)formDataMap.get("step") == 3) {
						GroupInfoDto groupInfoDto = taskService.getGroupInfoByGroupname(userDTO.getGroupName());
						
						if (groupInfoDto != null) {
							groupInfoDto = taskService.getGroupInfoByGroupname(groupInfoDto.getGroupname());
							formDataMap.put("depInfo", groupInfoDto.getDeptIntro());
							formDataMap.put("groupInfo", groupInfoDto.getGroupIntro());
							formDataMap.put("groupSpec", groupInfoDto.getSpecialInfo());
							formDataMap.put("groupDoc", groupInfoDto.getTechStandard());
							formDataMap.put("software", groupInfoDto.getSoftwareEnv());
						}
						if (formDataMap.get("depInfo") == null) {
							formDataMap.put("depInfo", "http://biztech.sogou-inc.com/");
						}
						if (formDataMap.get("groupDoc") == null) {
							formDataMap.put("groupDoc", "http://biztech.sogou-inc.com/wiki/index.php/相关规范");
						}
					}
					workflowDetailVo.setFormData(JSONUtil.serialize(formDataMap));
					if (userId == actInfo.getEmployeeId()) {
						workflowDetailVo.setCanAuthorize(true);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (TException e1) {
			e1.printStackTrace();
		}
		
		return workflowDetailVo;
	}


	@Override
	public String addApprove(ApprovalVo approvalVo, Long userId, String canCompleteWorkflowType) {
//		Integer flowTypeId = approvalVo.getFlowTypeId();
//		if (flowTypeId == WorkflowType.SVN_TABLE_TYPE) {
			return svnApplicationService.addApprove(approvalVo, userId, canCompleteWorkflowType);
//		}
//		return "";
	}
	@Override
	public void addActivitiInfo(WorkflowFormVo workflowFormVo, String businessKey, Long userId, String canCompleteWorkflowType) {
		ActInfo actInfo = new ActInfo();
		actInfo.setBusinessKey(businessKey);
		actInfo.setEmployeeId(userId);
		actInfo.setFlowName(workflowFormVo.getFlowName().trim());
		actInfo.setFlowTypeId(workflowFormVo.getFlowTypeId());
		actInfo.setFormData(workflowFormVo.getFormData());
		actInfo.setMessage(workflowFormVo.getMessage());
		UserDTO userDTO = 
				userService.getUserByEmployeeId(Integer.parseInt(userId + ""));
		if (userDTO != null) {
			actInfo.setChineseName(userDTO.getChineseName());
		}
		logger.info("add activitiinfo to bizflow " + actInfo);
		workflowDao.addActivitiInfo(actInfo);
	}

	@Override
	public void updateFormdataByBusinessKey(String businessKey, String formData,String flowName) {
		workflowDao.updateFormdataByBusinessKey(businessKey, formData,flowName);
	}


	@Override
	public List<WorkflowInitMap> getWorkFLowInitMaps(int flowTypeId) {
		return workflowDao.getWorkFLowInitMaps(flowTypeId);
	}


	@Override
	public ActInfo getActInfoByBusinessKey(String businessKey) {
		return workflowDao.getActivitiInfo(businessKey);
	}

	@Override
	public List<BriefTypeMessage> getAllFlowTodeal() {   // get
		return null;
	}


}
