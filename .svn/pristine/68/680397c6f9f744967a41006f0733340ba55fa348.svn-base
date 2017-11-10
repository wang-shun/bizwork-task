package com.sogou.bizwork.task.api.web.activiti.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.sogou.bizwork.task.api.core.task.msg.service.BriefTaskMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.response.ResponseResult;
import com.sogou.bizwork.task.api.core.activiti.bo.ActInfo;
import com.sogou.bizwork.task.api.core.activiti.bo.WorkflowInitMap;
import com.sogou.bizwork.task.api.core.activiti.service.RepositoryService;
import com.sogou.bizwork.task.api.core.activiti.service.WorkflowService;
import com.sogou.bizwork.task.api.core.activiti.vo.ApprovalVo;
import com.sogou.bizwork.task.api.core.activiti.vo.QueryConditionVo;
import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowDetailVo;
import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowFormVo;
import com.sogou.bizwork.task.api.core.dto.Result;
import com.sogou.bizwork.task.api.core.task.service.TaskService;
import com.sogou.bizwork.task.api.core.user.dao.UserMapper;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.web.common.util.UserHolder;

@Controller
@RequestMapping("/workflow")
@SuppressWarnings("unchecked")
public class WorkFlowController {

	@Autowired
	private WorkflowService workflowService;       // 封装了activiti SOA方法
	@Autowired
	private RepositoryService repositoryService;  // 都是封装了activiti的
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private BriefTaskMessage briefTaskMessage;

	/**
	 * 获取所有工作流程类型
	 * 
	 * @return
	 */
	@RequestMapping("/getFlowTypes.do")
	@ResponseBody
	public Result getFlowTypes() {
		Result result = new Result();
		result.setData(workflowService.getFlowTypes());
		return result;
	}

	/**
	 * 提交表单，启动工作流,svn仓库申请表单提交
	 * 
	 * @param workflowFormVo
	 * @param request
	 * @return
	 */
	@RequestMapping("/submitWorkflow.do")
	@ResponseBody
	public Result submitTable(@RequestBody WorkflowFormVo workflowFormVo, HttpServletRequest request) {
		ArrayList<WorkflowFormVo>  allTasks = new ArrayList<WorkflowFormVo>();
 		Result result = new Result();
		Long userId = UserHolder.getUserId();  //intercept get
		if (userId == null || userId == 0) {
			result.setErrorCodeAndMsg(421, "userId cannot be null");
			return result;
		}
		if (workflowFormVo.getFlowTypeId() == null) {    //svn 申请的类型
			result.setErrorCodeAndMsg(421, "flowTypeId cannot be null");
		}
		if (StringUtils.isBlank(this.strictTrimStr(workflowFormVo.getFlowName()))) {  // 仓库名称
			String flowName = workflowFormVo.getFlowName();
			for (int i = 0; i < flowName.length(); i++) {
				if (Character.isWhitespace(flowName.charAt(i))) {
					result.setErrorCodeAndMsg(431, "仓库名称中不能包含空白字符！请修改后再提交！");
				}
			}
			result.setErrorCodeAndMsg(421, "flowName cannot be null");
			return result;
		} else {
		}
		if (StringUtils.isBlank(workflowFormVo.getFormData())) {
			result.setErrorCodeAndMsg(421, "formData cannot be null");
			return result;
		}
		Map<String, Object> formParams = new HashMap<String, Object>();
		Map<String, Object> orginalFormParams = new HashMap<String, Object>();
		Map<String, Object> details22 = new HashMap<String, Object>();
		List<String> needMailManagers = new ArrayList<String>();
		try {
			formParams = (Map<String, Object>) JSONUtil.deserialize(workflowFormVo.getFormData());
			needMailManagers = (List<String>) formParams.get("needEmailManagers");
			orginalFormParams = (Map<String, Object>) JSONUtil.deserialize(workflowFormVo.getFormData());
//			String mapToJSON = JSONUtil.serialize(formParams);
//			formParams = (Map<String, Object>)JSONUtil.deserialize(mapToJSON);
//			System.out.println(mapToJSON);
			if (workflowFormVo.getFlowTypeId() == 102){  //
				List list = (List) formParams.get("repositories"); //

				for (int i = 0; i < list.size(); i++){
					List list1 = new ArrayList<Map>();
					Map reposito = (Map) list.get(i);
					String path = (String) reposito.get("path");
					List<String> managers = (List<String>) reposito.get("managers");
					Collections.sort(managers);
					String manas = StringUtils.join(managers.toArray(), ",");
					if (details22.containsKey(manas)){
						List tempData = (List) details22.get(manas);
						Map<String, Object> details23 = new HashMap<String, Object>();
						String[] manaStrs = manas.split(",");
						List temManas = Arrays.asList(manaStrs);
						details23.put("managers", temManas);
						details23.put("path", path);
						tempData.add(details23);
						details22.put(manas, tempData);
					}else {
						Map<String, Object> details23 = new HashMap<String, Object>();
						String[] manaStrs = manas.split(",");
						List temManas = Arrays.asList(manaStrs);
						details23.put("managers", temManas);
						details23.put("path", path);
						List<Object> temRespo = new ArrayList<Object>();
						temRespo.add(details23);
						details22.put(manas, temRespo);
					}



////					details22.put(, ","), )
//					for (Object manager : managers){
//						String manag = (String) manager;
//						if (forms.containsKey(manag)){
//							List<Object> list2 = (List<Object>) forms.get(manag);
//							List<Object> tempM = new ArrayList<Object>();
//							tempM.add(manag);
//							details = (Map<String, Object>) list2.get(0);
//							Map<String, Object> details1 = new HashMap<String, Object>();
//							details1.put("path", path);
//							details1.put("managers", managers);
//							list2.add(details1);
//							forms.put(manag, list2);
//						}else {
//							details = new HashMap<String, Object>();
//							List<Object> respos = new ArrayList<Object>();
//							List<String> data = new ArrayList<String>();
//							data.add(manag);
//							details.put("path", path);
//							details.put("managers", data);
//							respos.add(details);
//							forms.put(manag, respos);
//						}
//					}
				}
				for (Map.Entry<String, Object> entry : details22.entrySet()){
					List<Object> value = (List<Object>) entry.getValue();
					formParams = (Map<String, Object>) JSONUtil.deserialize(workflowFormVo.getFormData());
					Map<String, Object> temp = (Map<String, Object>) value.get(0);
					formParams.put("repositories", value);
					formParams.put("needEmailManagers", needMailManagers);
					formParams.put("managers", temp.get("managers"));
					WorkflowFormVo workflowFormVo1 = new WorkflowFormVo();
					workflowFormVo1.setFlowName(workflowFormVo.getFlowName());
					workflowFormVo1.setMessage(workflowFormVo.getMessage());
					workflowFormVo1.setSid(workflowFormVo.getSid());
					workflowFormVo1.setFlowTypeId(workflowFormVo.getFlowTypeId());
					workflowFormVo1.setFormData(JSONUtil.serialize(formParams));
					allTasks.add(workflowFormVo1);
				}

			}
			else {
				allTasks.add(workflowFormVo);
			}
			//allTasks.add(null);
//			ArrayList<workflowFormVo>
//			//Map<>			formParams.get("repositories");
//     		List list = (List) formParams.get("repositories");
//     		HashMap<List<String>,List<String>> pathAndManagers = new HashMap<List<String>, List<String>>();
//			if (list.size() > 1){
//				for (Object map : list){
//					Map map1 = (Map<String,Object>) map;
//					for ()
//				}
//			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		List<UserDTO> userDTOs = userMapper.getAllUsers();
		Set<String> usernameSet = new HashSet<String>();
		for (UserDTO userDTO : userDTOs) {   // 纯粹为了去重
			usernameSet.add(userDTO.getUserName());
		}

		if (workflowFormVo.getFlowTypeId() == 101) { // 对svn仓库申请表单数据进行校验
			result = checkSvnApplication(orginalFormParams, userId);  //表单参数 和用户  检查管理员是否存在以及一些输入规范
		} else if (workflowFormVo.getFlowTypeId() == 102) { // 对svn权限分配表单数据进行校验
			result = checkSvnDistribution(orginalFormParams);
		}
		if (result.getSuccess() == 0) {
			return result;
		}
		int taskNum = allTasks.size();
		 //String data = workflowFormVo.getFormData();
		for (WorkflowFormVo task : allTasks){

		String canCompleteWorkflowType = ""; // 判断申请人是否可以直接完成整个流程
		ResponseResult responseResult = workflowService.addApplicationToBizflow(task, userId,
				canCompleteWorkflowType);
		taskNum -= 1;
		if (!responseResult.isSuccess) {
			result.setData(responseResult.getData());
			return result;
		}
		}
		//briefTaskMessage.updateTasksAndMessageToBizwork();
		return result;
	}

	private String strictTrimStr(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		return str.trim();
	}

	private Result checkSvnApplication(Map<String, Object> formParams, long userId) {
		Result result = new Result();
		String repositoryName = (String) formParams.get("repositoryName");
		if (repositoryService.checkSvnName(repositoryName)) {
			result.setErrorCodeAndMsg(431, "仓库名称已存在，无法新增！");
			return result;
		}
		String managerStr = (String) formParams.get("manager");
		if (StringUtils.isBlank(managerStr)) {
			result.setErrorCodeAndMsg(431, "管理员不能为空");
			return result;
		}
		List<UserDTO> userDTOs = userMapper.getAllUsers();
		Set<String> usernameSet = new HashSet<String>();
		for (UserDTO userDTO : userDTOs) {
			usernameSet.add(userDTO.getUserName());
		}
		String unExistedUsers = "";
		String[] managers = managerStr.split(",");
		List<Map<String, String>> permissions = (List<Map<String, String>>) formParams.get("persons");
		if (CollectionUtils.isNotEmpty(permissions)) { // 如果给谁添加文本不为空，那么检查操作人是否为管理员，如果操作人不是管理员，则权限分配失败
			UserDTO userDTO = userMapper.getUserByEmployeeId(Integer.parseInt(userId + ""));
			if (managerStr.contains("404")) {
				List<WorkflowInitMap> workflowInitMaps = workflowService.getWorkFLowInitMaps(101);
				for (WorkflowInitMap work : workflowInitMaps) {
					if (work.getParamKey().equals("opLeaderId")) {
						if (!work.getParamValue().equals(userDTO.getUserName())) {
						}
					}
				}
			} else if (!managerStr.contains(userDTO.getUserName())) {
			}
		}
		if (managers.length > 0) {
			int count = 0;
			for (String manager : managers) {  // 遍历管理员
				if (!manager.contains("404") && !usernameSet.contains(manager)) {
					if (count == 0) {
						count++;
					} else {
						unExistedUsers += "、";
					}
					unExistedUsers += manager;
				}
			}
			if (StringUtils.isNotBlank(unExistedUsers)) {
				result.setErrorCodeAndMsg(431, "仓库管理员中，以下管理员未登记：" + unExistedUsers + "，请核对后重试！");
				return result;
			}
		}
		if (StringUtils.isBlank(repositoryName)) {
			result.setErrorCodeAndMsg(421, "仓库名称不能为空！");
		} else {
			Pattern p = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-]*[a-zA-Z0-9(?!-)]]*$");
			Matcher m = p.matcher(repositoryName);
			if (!m.matches()) {
				result.setErrorCodeAndMsg(431, "仓库名称必须包含字母，以字母开头。纯字母、字母数字、字母数字-的组合，如bizwork-crm1，且不能以-结尾。");
				return result;
			}
		}
		if (StringUtils.isNotBlank(unExistedUsers)) {
			result.setErrorCodeAndMsg(431, "以下用户：" + unExistedUsers + "不合法，无法开通权限，请核对后重试！");
			return result;
		}

		return result;
	}

	private Result checkSvnDistribution(Map<String, Object> formParams) {  // check formParas
		Result result = new Result();
		List<String> managers = (List<String>) formParams.get("managers");
		if (CollectionUtils.isEmpty(managers)) {
			result.setErrorCodeAndMsg(421, "管理员不能为空");
			return result;
		}
		List<UserDTO> userDTOs = userMapper.getAllUsers();
		Set<String> usernameSet = new HashSet<String>();
		for (UserDTO userDTO : userDTOs) {   // 去重
			usernameSet.add(userDTO.getUserName());
		}
		String unExistedUsers = "";    // 纯粹为了去重
		int count = 0;
		for (String manager : managers) {   // 管理员遍历
			if (!manager.contains("404") && !usernameSet.contains(manager)) {  // 校验 manager
				if (count == 0) {   // 纯粹为了加点
					count++;
				} else {
					unExistedUsers += "、";
				}
				unExistedUsers += manager;
			}
		}
		if (StringUtils.isNotBlank(unExistedUsers)) {
			result.setErrorCodeAndMsg(431, "以下管理员未登记：" + unExistedUsers + "，请核对后重试！");
			return result;
		}
		List<Map<String, String>> permissions = (List<Map<String, String>>) formParams.get("persons");  // 用户权限和邮箱
		if (CollectionUtils.isEmpty(permissions)) {
			result.setErrorCodeAndMsg(431, "请指定给谁开通权限！"); //
			return result;
		}
		count = 0;
		for (Map<String, String> map : permissions) {  // 判断申请人是否存在
			if (!usernameSet.contains(map.get("email"))) {
				if (count == 0) {
					count++;
				} else {
					unExistedUsers += "、";
				}
			}
		}
		if (StringUtils.isNotBlank(unExistedUsers)) {
			result.setErrorCodeAndMsg(431, "以下用户：" + unExistedUsers + "不合法，无法开通权限，请核对后重试！");
			return result;
		}
		return result;
	}

	@RequestMapping("/getApplications.do")
	@ResponseBody
	public Result getApplication(@RequestBody QueryConditionVo queryCondition, HttpServletRequest request) {
		Result result = new Result();
		Long userId = UserHolder.getUserId();  //
		if (userId == null || userId == 0) {
			result.setErrorCode(421);
			result.setErrorMsg("userId cannot be null");
			return result;
		}
		result.setData(workflowService.addApplications(queryCondition, userId));
		return result;
	}

	public static String stream2String(InputStream in, String charset) {
		StringBuffer sb = new StringBuffer();
		try {
			Reader r = new InputStreamReader(in, charset);
			int length = 0;
			for (char[] c = new char[1024]; (length = r.read(c)) != -1;) {
				sb.append(c, 0, length);
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@RequestMapping("/getApplicationDetail.do")
	@ResponseBody
	public Result getWorkFLowDetail(@RequestBody Map<String, String> params) {
		Result result = new Result();
		Long userId = UserHolder.getUserId();
		String businessKey = params.get("flowId");
		if (StringUtils.isBlank(businessKey)) {
			result.setErrorCode(421);
			result.setErrorMsg("businessKey非法");
		}

		WorkflowDetailVo workflowDetailVo = workflowService.getWorkflowDetail(businessKey, userId);
		result.setData(workflowDetailVo);
		return result;
	}

	@RequestMapping("/approve.do")
	@ResponseBody
	public Result addApprove(@RequestBody ApprovalVo approvalVo, HttpServletRequest request) {
		Result result = new Result();  // flowid 和业务有关的 工作流服务平台会将这个封装成全局唯一   taskid在工作流服务器段生成的
		Long userId = UserHolder.getUserId();

		if (userId == null || userId.equals(0)) {
			result.setErrorCode(421);
			result.setErrorMsg("userId cannot be null");
			return result;
		}
		if (approvalVo == null) {
			result.setErrorCode(421);
			result.setErrorMsg("approvalVo cannot be null");
			return result;
		}
		String data = workflowService.addApprove(approvalVo, userId, "");
		result.setData(data);
		if (StringUtils.isNotBlank(approvalVo.getFlowId()) && StringUtils.isNotBlank(approvalVo.getFormData())
				&& approvalVo.getIsPassed()) {
			Map<String, Object> params = new HashMap<String, Object>();
			try {
				params = (Map<String, Object>) JSONUtil.deserialize(approvalVo.getFormData());
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (approvalVo.getFlowId().equals(101)) { // 根据不同类型的流程进行相应的校验
				result = checkSvnApplication(params, userId);
			} else if (approvalVo.getFlowId().equals("102")) {
				result = checkSvnDistribution(params);
			}
			if (result.getSuccess() == 0) {
				return result;
			}

			ActInfo actInfo = workflowService.getActInfoByBusinessKey(approvalVo.getFlowId());
			if (actInfo.getFlowTypeId() == 201) { // 新人入职流程表单参数需要特殊处理
				String formData = approvalVo.getFormData();
				Map<String, Object> formDataMap = null;
				Map<String, Object> formDataMapOri = null;
				try {
					formDataMap = (Map<String, Object>) JSONUtil.deserialize(formData);
					formDataMapOri = (Map<String, Object>) JSONUtil.deserialize(actInfo.getFormData());
					formDataMap.putAll(formDataMapOri);
					if ((Long) formDataMap.get("step") <= 1) {
						formDataMap.put("step", 2);
					} else if ((Long) formDataMap.get("step") == 2) {
						formDataMap.put("step", 3);
					} else if ((Long) formDataMap.get("step") == 3) {
						// formDataMap.put("step", 4);
					}
					approvalVo.setFormData(JSONUtil.serialize(formDataMap));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			workflowService.updateFormdataByBusinessKey(approvalVo.getFlowId(), approvalVo.getFormData(),
					approvalVo.getFlowName());
		}

		//briefTaskMessage.updateTasksAndMessageToBizwork();
		return result;
	}

	@RequestMapping("/test.do")
	@ResponseBody
	public void test() {
		StringBuilder mailBody = new StringBuilder();
		taskService.sendEntryEmail(mailBody.toString(), "新人入职邮件测试", "chenxisi0848@sogou-inc.com");
	}

	@RequestMapping("/callBack.do")
	@ResponseBody
	public void callBack() {
		System.out.println("hello world!");
	}

}
