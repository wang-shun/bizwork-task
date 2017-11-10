package com.sogou.bizwork.task.api.core.activiti.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sogou.bizwork.task.api.core.task.po.Scheduled;
import com.sogou.bizwork.task.api.core.task.po.TaskVo;
import com.sogou.bizwork.task.api.core.task.service.ScheduledTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sogou.bizwork.task.api.core.activiti.po.UserAndLeader;
import com.sogou.bizwork.task.api.core.activiti.service.EntryService;
import com.sogou.bizwork.task.api.core.activiti.service.WorkflowService;
import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowFormVo;
import com.sogou.bizwork.task.api.core.task.service.TaskService;
import com.sogou.bizwork.task.api.core.user.dao.UserMapper;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;

@Service("entryService")
public class EntryServiceImpl implements EntryService {
    private static final Logger logger = LoggerFactory.getLogger(SvnApplicationServiceImpl.class);
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ScheduledTaskService scheduledTaskService;
	@Override
	public void updateEntryProcess() {
        List<UserAndLeader> userAndLeaders = userMapper.getUserAndLeader();  // get  User and leader this user is new classmate activte time is null
        if (!CollectionUtils.isEmpty(userAndLeaders)) {
	        userMapper.updateActiveTime(userAndLeaders);
	        // 下面写定时代码
	        for (UserAndLeader temp : userAndLeaders) {
				TaskVo scheduleTaskOfNewEmploye = new TaskVo();
				scheduleTaskOfNewEmploye.setTaskId(0);
				scheduleTaskOfNewEmploye.setTaskName("新人三天任务-第一天任务");
				scheduleTaskOfNewEmploye.setChargeUser(temp.getEmployeeId());
				scheduleTaskOfNewEmploye.setCreateUser(temp.getEmployeeId());
				scheduleTaskOfNewEmploye.setCreateTime(null);
				scheduleTaskOfNewEmploye.setStartTime(null);
				scheduleTaskOfNewEmploye.setWarning(false);
				scheduleTaskOfNewEmploye.setDeleteAuthority(false);
				scheduleTaskOfNewEmploye.setMessageContent(null);
				scheduleTaskOfNewEmploye.setAttachment(null);
				//scheduleTaskOfNewEmploye.setFollowUsers();
				Scheduled scheduled = new Scheduled();
				scheduled.setPeriodFrequency(1);
				scheduled.setPeriodSpecific(0);
				scheduled.setPeriodValid(3);
				scheduleTaskOfNewEmploye.setScheduled(scheduled);
				com.sogou.bizwork.task.api.core.task.po.Result res = scheduledTaskService.addTask(scheduleTaskOfNewEmploye,temp.getEmployeeId());
				scheduleTaskOfNewEmploye.setTaskId(Long.parseLong(res.getData() + ""));
				scheduledTaskService.addScheduledTask(scheduleTaskOfNewEmploye, temp.getEmployeeId());
				WorkflowFormVo workflowFormVo = new WorkflowFormVo();
	            Map<String, Object> formMap = new HashMap<String, Object>();
	            formMap.put("step", 1);
	            formMap.put("name", temp.getName());
	            formMap.put("leader", temp.getLeader());
	            workflowFormVo.setFlowName("新人入职-" + temp.getName());
	            workflowFormVo.setFlowTypeId(201);
	            try {
					workflowFormVo.setFormData(JSONUtil.serialize(temp));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            workflowService.addApplicationToBizflow(workflowFormVo, temp.getEmployeeId() + 0l, "");
	            String mailSubject = "欢迎加入biztech";
	            StringBuilder mailBody = new StringBuilder();
	            mailBody.append("Hi，" + temp.getName() + " 同学：");
	    		mailBody.append("<p>欢迎加入Sogou营销事业部！</p>");
	    		mailBody.append(
	    				"<p>商业平台研发部致力于打造安全、稳定、高效的商业服务平台。为打造搜狗一站式营销服务平台提供基础架构支撑，支持跨平台及不同终端上的广告主及代理商的接入，包括搜索推广、网盟推广、品牌推广等广告投放系统架构设计和研发 ，为广告主、合作伙伴及业务人员等提供安全、稳定可靠的商业平台，满足其精准投放、精细化管理以及快速决策需求。在技术上，致力于解决分布式、高并发、大数据，强一致性等带来的技术难题及挑战，构建和持续优化底层基础架构和基础服务，保证高可靠、高性能、高可扩展性快速支撑各项新业务。</p>");
	    		mailBody.append("<p>在这里有在业界峰会挥洒自如的技术大牛，也有学习切磋的技术分享会。</p>");
	    		mailBody.append(
	    				"<p style=\"font-family:verdana;color:red\">请进入：<a href=\"http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/\">http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/</a>完成入职引导</p>");

	    		mailBody.append(
	    				"<img src=\"http://img.store.sogou/app/a/100200003/6ef426dd-e658-43f5-a110-8fe445bd0ed3-20170515102935.tmp\" />");
	    		mailBody.append(
	    				"<img src=\"http://img.store.sogou/app/a/100200003/be896b29-d010-4779-97bb-87dd0be341f3-20170515103135.tmp\" />");
	    		mailBody.append("<p>同时，Biztech也是一个温暖大家庭，我们一起成长，一起欢乐，相互扶持相互鼓励，</p>");
	    		mailBody.append(
	    				"<img src=\"http://img.store.sogou/app/a/100200003/10d4c6d8-4308-4fce-938d-26d9394826bd-20170515103355.tmp\" />");
	    		mailBody.append("<p>更多精彩视频，请看<a href=\"http://fe.sogou/index.html\">http://fe.sogou/index.html</a></p>");
	    		mailBody.append("<p>热诚的欢迎你加入我们，平台研发部，因你而精彩！</p>");
	    		mailBody.append("<p>部门办公平台：<a href=\"http://bizwork.sogou-inc.com/\">http://bizwork.sogou-inc.com/</a></p>");
	            UserDTO userDTO = userMapper.getUserByEmployeeId(temp.getEmployeeId());  // 新同学信息

	    		Resource resource = new ClassPathResource("files/biztech内部系统使用手册.rar");
	    		if (resource.exists()) {
	    			taskService.sendEntryEmail(mailBody.toString(), mailSubject,  userDTO.getEmail());
	    		} else {
	    			taskService.sendMail(204516l, mailBody.toString(), mailSubject, userDTO.getEmail());
	    		}
	        }
        }
	}

}
