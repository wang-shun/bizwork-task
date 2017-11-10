package com.sogou.bizwork.task.api.core.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.apache.zookeeper.server.quorum.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.bizflow.HistoricDetailTEntity;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.bizflow.TaskTEntity;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.query.TaskQueryTEntity;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.response.ResponseTask;
import com.sogou.adm.bizdev.bizflowapi.thrift.iface.process.ProcessInfoTService;
import com.sogou.adm.bizdev.bizflowapi.thrift.iface.task.TaskTService;
import com.sogou.bizwork.task.api.core.activiti.bo.ActInfo;
import com.sogou.bizwork.task.api.core.activiti.bo.Approval;
import com.sogou.bizwork.task.api.core.activiti.bo.PersonPermission;
import com.sogou.bizwork.task.api.core.activiti.bo.SpecialReturnData;
import com.sogou.bizwork.task.api.core.activiti.dao.WorkflowDao;
import com.sogou.bizwork.task.api.core.activiti.po.RepositoryResult;
import com.sogou.bizwork.task.api.core.activiti.po.SvnDistribution;
import com.sogou.bizwork.task.api.core.activiti.service.RepositoryService;
import com.sogou.bizwork.task.api.core.activiti.service.SvnApplicationService;
import com.sogou.bizwork.task.api.core.activiti.service.WorkflowService;
import com.sogou.bizwork.task.api.core.activiti.vo.ApprovalVo;
import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowFormVo;
import com.sogou.bizwork.task.api.core.task.service.TaskService;
import com.sogou.bizwork.task.api.core.user.dao.UserMapper;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.service.UserService;

@Service("svnApplicationService")
@SuppressWarnings("unchecked")
public class SvnApplicationServiceImpl implements SvnApplicationService {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(SvnApplicationServiceImpl.class);

	@Autowired
	private ProcessInfoTService.Iface processInfoTService;
	@Autowired
	private UserService userService;
	@Autowired
	private WorkflowDao workflowDao;
	@Autowired
	private TaskTService.Iface taskTService;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TaskService taskService;
	@Autowired
	private WorkflowService workflowService;

	@Override
	public ResponseTask queryMyApplication(String appid, String processDefintionKey, String businessKey, String userId,
			int i, int j) {
		ResponseTask res = null;
		if (StringUtils.isBlank(userId)) {
			userId = "";
		}
		return res;
	}

	@Override
	public String addApprove(ApprovalVo approvalVo, Long userId, String canCompleteWorkflowType) {
		Map<String, String> params = new HashMap<String, String>();  //
		if (approvalVo == null)
			return "approvalVo cannot be null";
		List<String> businessKeys = new ArrayList<String>();
		String businessKey = approvalVo.getFlowId();  // 远程businesskey 和业务有关的
		businessKeys.add(businessKey);
		ActInfo actInfo = workflowDao.getActivitiInfo(businessKey);  // flow message
		approvalVo.setFlowTypeId(actInfo.getFlowTypeId());  //设置流类型
		TaskQueryTEntity taskQueryTEntity = new TaskQueryTEntity();
		try {
			taskQueryTEntity.setUserId(userId + "");
			taskQueryTEntity.setBusinessKeys(businessKeys);
			ResponseTask responseTask = taskTService.getTaskList("bizwork", taskQueryTEntity, 0, -1);    //获取当前需要审批的节点信息
			if (responseTask != null) {
				List<TaskTEntity> taskTEntities = responseTask.getData();
				if (CollectionUtils.isNotEmpty(taskTEntities)) {  //当前用户能审批
					TaskTEntity taskTEntity = taskTEntities.get(0);
					Approval approval = new Approval();   //新建审批意见
					approvalVo.setTaskId(taskTEntity.getId());
					if (taskTEntity != null) {
						String userTaskName = taskTEntity.getTaskDefinitionKey();      //获取usertask 获取当前是哪个节点,也就是流程当前状态
						if (userTaskName.equals("reApply") && approvalVo.getIsPassed()) { // 确定重新提交

							int count = userMapper.getGroupLeaderCount(userId);    //count > 0则为leader
							Map<String, String> reApplyMap = new HashMap<String, String>();
							reApplyMap.put("reApplyIsPassed", "true");
							reApplyMap.put("reApplyComment", approvalVo.getMessage());
							taskTService.completeTaskByTaskId(taskTEntity.getId(), userId + "", reApplyMap); //提交审批
							//如果操作人是leader/svn仓库管理员/opLeader，那么确定重新提交后，还需要回调，以便继续下一个节点审批
							if (count > 0 || "svnManager".equals(canCompleteWorkflowType)  // 审批完第一个节点,自己还能审批下一个节点
									|| "opLeader".equals(canCompleteWorkflowType)) {
								this.approveLeader(approvalVo.getFlowId(), userId, approvalVo.getFlowTypeId(),  // 继续审批
										approvalVo.getFormData(), canCompleteWorkflowType);
								return "";
							}
						} else if (userTaskName.equals("reApply") && !approvalVo.getIsPassed()) { // 放弃重新提交
							workflowDao.deleteWorkflowByBusinessKey(approvalVo.getFlowId());
							processInfoTService.hungUpProcessInstance(approvalVo.getFlowId());  //领导没通过挂起
						} else if (userTaskName.equals("opLeaderApproval") && approvalVo.getIsPassed()) { // opleader审批通过 第二阶段
							approval.setPassed(true);
							approval.setComment(approvalVo.getMessage());
							SpecialReturnData specialReturnData = this.opLeaderApprove(approval, taskTEntity,
									userId + "", approvalVo.getTaskId());   // opleader
							this.sendSvnBusinessEmail(taskQueryTEntity, approvalVo, null);
							return (String) specialReturnData.getReturnData();
						} else if (userTaskName.equals("svnAdminApproval") && approvalVo.getIsPassed()) {    //svn仓库管理员审批通过
							approval.setPassed(true);
							approval.setComment(approvalVo.getMessage());
							this.svnAdminApproval(approval, taskTEntity, Integer.parseInt(userId.toString()),
									approvalVo.getTaskId(), canCompleteWorkflowType);
							return "";
						}else {
							params.put(userTaskName + "IsPassed", approvalVo.getIsPassed() + ""); // 设置当前节点状态,pass and comment
							if (StringUtils.isNotBlank(approvalVo.getMessage())) {
								params.put(userTaskName + "Comment", approvalVo.getMessage());
							}
							Map<String, Object> formParams = new HashMap<String, Object>();
							try {
								formParams = (Map<String, Object>) JSONUtil.deserialize(actInfo.getFormData()); // 任务内容
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if (userTaskName.equals("leaderApproval") && approvalVo.getIsPassed()
									&& approvalVo.getFlowTypeId() == 102) { // 权限审批时，在leader审批节点，如果opleader中包含leader，那么可以直接完成所有流程
								List<String> managers = (List<String>) formParams.get("managers");  // leader approval next step is manager
								UserDTO userDTO = userService.getUserByEmployeeId(Integer.parseInt(userId + "")); // leader message
								for (String manager : managers) {    //如果该操作人为仓库管理员，那么可以直接结束完成所有节点
									if (manager.equals(userDTO.getUserName())) {  // 自己还是管理员
										canCompleteWorkflowType = "opLeader";
										break;
									}
								}
							} else if (userTaskName.equals("leaderApproval") && approvalVo.getIsPassed()
									&& approvalVo.getFlowTypeId() == 201) {   // 201类型的新人入职工作流，leader审批通过之后，发起svn权限分配
								Map<String, Object> formParameters = new HashMap<String, Object>();
								try {
									formParameters = (Map<String, Object>) JSONUtil.deserialize(approvalVo.getFormData());
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								List<String> svns = (List<String>) formParameters.get("svn");
								WorkflowFormVo workflowFormVo = new WorkflowFormVo();
								workflowFormVo.setFlowName("SVN权限分配-" + actInfo.getChineseName() + "入职");
								workflowFormVo.setFlowTypeId(201);
								Map<String, Object> formData = new HashMap<String, Object>();
								// formData.put("","")
								String url = "http://bizsvn.sogou-inc.com/svnadmin/if_accesspathinfo.php";
								UserDTO leaderDto = userMapper.getUserByEmployeeId(Integer.parseInt(userId + ""));
								Set<String> pathSet = new HashSet<String>();
								Map<String, Object> pathAndManagers = new HashMap<String, Object>();
								if (CollectionUtils.isNotEmpty(svns)) {
									for (String path : svns) {
										if (!path.contains(":/")) {
											path += ":/";
										}
										if (!pathSet.add(path)) {
											continue;
										}
										Map<String, String> queryParams = new HashMap<String, String>();
										queryParams.put("username", leaderDto.getUserName());
										queryParams.put("path", path);
										Map<String, Object> data = repositoryService.getManagersByPaths(url, queryParams);
										if (data.get("errorCode") != null || data.get("isExist") != null) {
											if (data.get("errorcode") != null
													&& Long.parseLong(data.get("errorCode") + "") > 0) {
	
											} else {
												if ((data.get("isExist") + "").equals("1")) {
													if (StringUtils.isNotBlank(data.get("managers").toString())) {
														pathAndManagers.put(path, (List<String>) data.get("managers"));
													}
												}
											}
										}
									}
								}
								
								Map<String, List<String>> managerAndPaths = getManagersAndPaths(pathAndManagers);

								if (!org.springframework.util.CollectionUtils.isEmpty(managerAndPaths)) {
									UserDTO applyUserDTO = userMapper
											.getUserByEmployeeId(Integer.parseInt(actInfo.getEmployeeId() + ""));
									for (Entry<String, List<String>> entry : managerAndPaths.entrySet()) {
										logger.info("entry={key:" + entry.getKey() + " value:" + entry.getValue() + "}");
										List<String> managers = new ArrayList<String>();
										managers.add(entry.getKey());
										formData.put("managers", managers);
										formData.put("needEmailManagers", managers);
										List<Map<String, Object>> persons = new ArrayList<Map<String, Object>>();
										Map<String, Object> person = new HashMap<String, Object>();
										person.put("email", applyUserDTO.getUserName());
										person.put("permission", 1);
										persons.add(person);
										formData.put("persons", persons);
										formData.put("remarks", "【系统提醒】" + "新人入职自动发起权限分配！");
										List<Map<String, Object>> repositories = new ArrayList<Map<String,Object>>();
										for (String val : entry.getValue()) {
											Map<String, Object> map = new HashMap<String, Object>();
											map.put("path", val);
											List<String> list = new ArrayList<String>();
											list.add(entry.getKey());
											map.put("managers", list);
											repositories.add(map);
										}
										formData.put("repositories", repositories);
										try {
											WorkflowFormVo workflowFormVo2 = new WorkflowFormVo();
											workflowFormVo2.setFormData(JSONUtil.serialize(formData));
											workflowFormVo2.setFlowTypeId(102);
											workflowFormVo2.setMessage("【系统提醒】新人" + actInfo.getChineseName() + "入职svn权限分配，请尽快完成审批！");
											workflowFormVo2.setFlowName("svn权限分配-" + actInfo.getChineseName() + "入职");
											logger.info(workflowFormVo2.toString());
											workflowService.addApplicationToBizflow(workflowFormVo2, userId, ""); // 新人入职发起权限分配流程
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
							}
						}

					}
				}
			}
		} catch (TException e1) {
			e1.printStackTrace();
		}

		try {
			taskTService.completeTaskByTaskId(approvalVo.getTaskId(), userId + "", params); //更新流程状态
			String leaderApproval = params.get("leaderApprovalIsPassed");
			if ("opLeader".equals(canCompleteWorkflowType)) { // opleader的申请不需要发邮件，且需要继续审批下一个节点
				addApprove(approvalVo, userId, canCompleteWorkflowType);
			} else if ("svnManager".equals(canCompleteWorkflowType)) {
				addApprove(approvalVo, userId, canCompleteWorkflowType);
			} else if ("noNeedEmail".equals(canCompleteWorkflowType)) {

			} else if (approvalVo.getFlowTypeId() == 102 && StringUtils.isNotBlank(leaderApproval)
					&& leaderApproval.equals("true")) { // 向svn权限分配申请人勾选的管理员发送邮件
				String formData = actInfo.getFormData();
				Map<String, Object> formMap = (Map<String, Object>) JSONUtil.deserialize(formData);
				List<String> needEmailManagers = (List<String>) formMap.get("needEmailManagers");
				Set<String> needEmailEmployeeIdSet = new HashSet<String>();
				if (CollectionUtils.isNotEmpty(needEmailManagers)) {
					for (String username : needEmailManagers) {
						UserDTO userDTO = userMapper.getUserByUserName(username);
						needEmailEmployeeIdSet.add(userDTO.getEmployeeId() + "");
					}
					sendSvnBusinessEmail(taskQueryTEntity, approvalVo, needEmailEmployeeIdSet);
				}
			} else {
				sendSvnBusinessEmail(taskQueryTEntity, approvalVo, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private Map<String, List<String>> getManagersAndPaths(Map<String, Object> pathAndManagers) {
		Map<String, List<String>> managerAndPaths = new HashMap<String, List<String>>();
		String managerAndPathStr = "";
		String pathAndManagerStr = "";
		while (!org.springframework.util.CollectionUtils.isEmpty(pathAndManagers)) {
			Map<String, Integer> managerAndCount = new HashMap<String, Integer>(); // 每个管理员对应的路径数量
			Set<String> managerSet = new HashSet<String>();
			for (Entry<String, Object> entity : pathAndManagers.entrySet()) {
				managerAndPathStr += "{" + entity.getKey() + ":" +  entity.getValue() + "}";
				List<String> managers = (List<String>) entity.getValue();
				if (CollectionUtils.isEmpty(managers))
					continue;
				for (String manager : managers) {
					if (manager != null) {
						if (managerSet.add(manager)) {
							managerAndCount.put(manager, 1);
						} else {
							managerAndCount.put(manager, managerAndCount.get(manager) + 1);
						}
					}
				}
			}
			String tempManager = "";
			int max = 0;
			for (Entry<String, Integer> entry : managerAndCount.entrySet()) {
				if (entry.getValue() > max) {
					max = entry.getValue();
					tempManager = entry.getKey();
				}
			}
			Map<String, Object> temp = new HashMap<String, Object>(pathAndManagers);
			for (Entry<String, Object> entry : temp.entrySet()) {
				List<String> managers = (List<String>) entry.getValue();
				if (CollectionUtils.isEmpty(managers)) {
					pathAndManagers.remove(entry.getKey());
					continue;
				}
				boolean containsManager = false;
				for (String manager : managers) {
					if (manager.equals(tempManager)) {
						containsManager = true;
						break;
					}
				}
				if (containsManager) {
					List<String> paths = managerAndPaths.get(tempManager);
					if (paths == null) {
						paths = new ArrayList<String>();
						managerAndPaths.put(tempManager, paths);
					}
					paths.add(entry.getKey());
					pathAndManagers.remove(entry.getKey());
				}
			}
		}
		for (Entry<String, List<String>> ele : managerAndPaths.entrySet()) {
			managerAndPathStr += "{" + ele.getKey() + ":" + ele.getValue() + "}";
		}
		return managerAndPaths;
	}

	private void sendSvnBusinessEmail(TaskQueryTEntity taskQueryTEntity, ApprovalVo approvalVo,
			Set<String> needEmailEmployeeIdSet) {
		taskQueryTEntity.setUserId("");
		ResponseTask responseTask;
		try {
			responseTask = taskTService.getTaskList("bizwork", taskQueryTEntity, 0, -1);
			if (responseTask != null) {
				List<TaskTEntity> taskTEntities = responseTask.getData();
				if (CollectionUtils.isNotEmpty(taskTEntities)) {
					TaskTEntity taskTEntity = taskTEntities.get(0);
					if (CollectionUtils.isNotEmpty(needEmailEmployeeIdSet)) {
						for (String employeeId : needEmailEmployeeIdSet) {
							taskService.sendSvnApproveMail(approvalVo.getFlowId(), Long.parseLong(employeeId));
						}
					} else if (StringUtils.isNotBlank(taskTEntity.getAssignee())) {
						taskService.sendSvnApproveMail(approvalVo.getFlowId(),
								Integer.parseInt(taskTEntity.getAssignee()));
					} else if (CollectionUtils.isNotEmpty(taskTEntity.getCandidateUserIds())) {
						for (String candidateUserId : taskTEntity.getCandidateUserIds()) {
							taskService.sendSvnApproveMail(approvalVo.getFlowId(), Long.parseLong(candidateUserId));
						}
					}
				}
			}
		} catch (TException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getDataByTaskInitCode(String code, Object data) { // 获取初始化数据
		if (code.equals("102_2")) {
			WorkflowFormVo workflowFormVo = (WorkflowFormVo) data;
			String formData = workflowFormVo.getFormData();
			if (StringUtils.isNotBlank(formData)) {
				try {
					List<String> candidateUserIds = new ArrayList<String>();
					Map<String, Object> map = (HashMap<String, Object>) JSONUtil.deserialize(formData);
					if (!org.springframework.util.CollectionUtils.isEmpty(map)) {
						List<String> managers = (List<String>) map.get("managers");
						for (String userName : managers) {
							UserDTO userDTO = userService.getUserByUserName(userName);
							if (userDTO != null && userDTO.getEmployeeId() != null) {
								candidateUserIds.add(userDTO.getEmployeeId() + "");
							}
						}
						return candidateUserIds;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} else if (code.equals("102_3")) {

		}
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public SpecialReturnData doSomeBusinessWithMethodCode(Approval approval, HistoricDetailTEntity hDetail) {
		String methodCode = approval.getMethodCode();
		if (methodCode.equals("102-1")) { // 权限分配方法码
			String businessKey = hDetail.getBusinessKey();
			if (StringUtils.isNotBlank(businessKey)) {
				ActInfo actInfo = workflowDao.getActivitiInfo(businessKey);
				Map<String, String> params = new HashMap<String, String>();
				String formData = actInfo.getFormData();

				try {
					SvnDistribution svnDistribution = (SvnDistribution) JSONUtil.deserialize(formData);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				params.put("reponame", "");
			}
		} else if (methodCode.equals("101-1")) { // svn申请opLeader审批
			if (approval.equals("false"))
				return null;

		}
		return null;
	}
	/*最后一步审批 opleader*/
	private SpecialReturnData opLeaderApprove(Approval approval, TaskTEntity taskTEntity, String userId,
			String taskId) {
		SpecialReturnData specialReturnData = new SpecialReturnData();
		Map<String, String> params = new HashMap<String, String>();
		String businessKey = taskTEntity.getBusinessKey();
		RepositoryResult repository = repositoryService.applyRepository(businessKey);     //svn仓库申请
		ActInfo actInfo = workflowDao.getActivitiInfo(businessKey);
		String extraStr = "";
		if (actInfo != null) {
			String formData = actInfo.getFormData();
			try {
				Map<String, Object> formMap = (Map<String, Object>) JSONUtil.deserialize(formData);
				List<PersonPermission> permissions = (List<PersonPermission>) formMap.get("persons");
				if (CollectionUtils.isNotEmpty(permissions)) {
					RepositoryResult repositoryResult = repositoryService.distributePermission(businessKey, -1); // 需要默认分配权限
					if (repositoryResult.isSuccess()) {
						extraStr = "您所指定默认svn权限分配结果如下： " + repositoryResult.getData();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (repository.isSuccess()) {
			specialReturnData.setNeedContinue(true);
			specialReturnData.setReturnData("");
			params.put("opLeaderApprovalIsPassed", "true");
			String comment = "";
			if (StringUtils.isNotBlank(approval.getComment())) {
				comment = approval.getComment();
			}
			params.put("opLeaderApprovalComment", comment + "【系统提醒】恭喜您，仓库已成功创建！" + extraStr);

			String mailSubject = "[bizwork]svn仓库申请成功通知";
			String mailBody = "恭喜您，您发起的【" + actInfo.getFlowName() + "】成功完结。";
			mailBody += "<br>点击可查看历史审批详情：<a href = \"http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/\" >http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/</a>";
			params.put("svnAdminApprovalComment", comment);
			UserDTO userDTO = userMapper.getUserByEmployeeId(Integer.parseInt(actInfo.getEmployeeId() + ""));
			taskService.sendMail(actInfo.getEmployeeId(), mailBody, mailSubject, userDTO.getEmail());
		} else {
			specialReturnData.setNeedContinue(false);
			specialReturnData.setReturnData(repository.getErrorMsg());
			params.put("opLeaderApprovalIsPassed", "false");
			String comment = "";
			if (StringUtils.isNotBlank(approval.getComment())) {
				comment = "【审批人留言】" + approval.getComment();
			}
			params.put("opLeaderApprovalComment", comment + "【系统提醒】opLeader已同意，但仓库存在重名，需更改后重新提交。");
		}
		try {
			taskTService.completeTaskByTaskId(taskId, userId, params);
		} catch (TException e) {
			e.printStackTrace();
		}
		return specialReturnData;
	}

	private SpecialReturnData svnAdminApproval(Approval approval, TaskTEntity taskTEntity, int employeeId,
			String taskId, String canCompleteWorkflowType) {
		SpecialReturnData specialReturnData = new SpecialReturnData();
		RepositoryResult res = repositoryService.distributePermission(taskTEntity.getBusinessKey(), employeeId);
		Map<String, String> params = new HashMap<String, String>();
		params.put("svnAdminApprovalIsPassed", "true");
		String comment = "";
		if (res.isSuccess()) {
			ActInfo actInfo = workflowDao.getActivitiInfo(taskTEntity.getBusinessKey());
			String mailBody = "恭喜您，您发起的【" + actInfo.getFlowName() + "】已完结，权限分配结果如下：";
			if ("svnManager".equals(canCompleteWorkflowType)) {
				comment += "【系统提醒】恭喜您，svn仓库管理员权限分配已自动完成！系统已为您自动关闭邮件提醒功能。权限分配结果如下：";
			} else {
				if (StringUtils.isNotBlank(approval.getComment())) {
					comment = "【审批人留言】" + approval.getComment();
				}
				comment += "【系统提醒】恭喜您，权限分配完成！权限分配结果如下：";
			}
			comment += res.getData();
			mailBody += res.getData();
			mailBody += "<br>点击可查看历史审批详情：<a href = \"http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/\" >http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/</a>";
			params.put("svnAdminApprovalComment", comment);
			UserDTO userDTO = userMapper.getUserByEmployeeId(Integer.parseInt(actInfo.getEmployeeId() + ""));
			String mailSubject = "[bizwork]svn权限分配成功通知";
			taskService.sendMail(actInfo.getEmployeeId(), mailBody, mailSubject, userDTO.getEmail());
		} else {
			params.put("svnAdminApprovalComment", comment + "【系统提醒】" + res.getErrorMsg());
		}
		try {
			taskTService.completeTaskByTaskId(taskId, employeeId + "", params);

		} catch (TException e) {
			e.printStackTrace();
		}

		specialReturnData.setNeedContinue(false);
		specialReturnData.setReturnData(comment);
		return specialReturnData;
	}

	/* 审批 */
	@Override
	public void approveLeader(String businessKey, Long userId, Integer flowTypeId, String formData,
			String canCompleteWorkflowType) {
		List<String> businessKeys = new ArrayList<String>();
		businessKeys.add(businessKey);
		ApprovalVo approvalVo = new ApprovalVo();
		approvalVo.setFlowTypeId(flowTypeId); //类型
		approvalVo.setFlowId(businessKey);  // 工作流服务平台的
		approvalVo.setIsPassed(true);
		if ("opLeader".equals(canCompleteWorkflowType)) {
			approvalVo.setMessage("opLeader的申请系统默认全部通过。");
		} else if ("svnManager".equals(canCompleteWorkflowType)) {
			approvalVo.setMessage("【系统提醒】svn仓库管理员的申请系统默认全部通过。");  // 审批意见
		} else {
			approvalVo.setMessage("【系统提醒】小组leader申请系统默认通过。");
		}
		approvalVo.setFormData(formData);
		TaskQueryTEntity taskQueryTEntity = new TaskQueryTEntity();
		taskQueryTEntity.setBusinessKeys(businessKeys);
		ResponseTask responseTask;
		try {
			responseTask = taskTService.getTaskList("bizwork", taskQueryTEntity, 0, -1); //get target task

			List<TaskTEntity> taskTEntities = responseTask.getData();
			if (CollectionUtils.isNotEmpty(taskTEntities)) { // 判断是否能审批
				TaskTEntity taskTEntity = taskTEntities.get(0);
				approvalVo.setTaskId(taskTEntity.getId());  //远程任务id
			}

			this.addApprove(approvalVo, userId, canCompleteWorkflowType);  //增加节点审批意见,同意或拒绝
		} catch (TException e) {
			e.printStackTrace();
		}
	}
}
