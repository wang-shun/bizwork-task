package com.sogou.bizwork.task.api.core.task.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sogou.bizwork.task.api.core.tasklog.service.TaskLogService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sogou.bizdev.emer.provider.http.MailSendUtils;
import com.sogou.bizdev.emer.provider.http.MultipartEntityBuilderWrapper;
import com.sogou.bizdev.emer.provider.http.SendMode;
import com.sogou.bizwork.api.exception.ApiTException;
import com.sogou.bizwork.api.message.BriefTypeMessage;
import com.sogou.bizwork.api.xiaop.message.XiaoPMessage;
import com.sogou.bizwork.api.xiaop.message.service.XiaoPMessageTService;
import com.sogou.bizwork.task.api.common.util.MessageUtils;
import com.sogou.bizwork.task.api.core.activiti.bo.ActInfo;
import com.sogou.bizwork.task.api.core.activiti.dao.WorkflowDao;
import com.sogou.bizwork.task.api.core.score.bo.ScoreHistory;
import com.sogou.bizwork.task.api.core.tag.service.TagService;
import com.sogou.bizwork.task.api.core.task.dao.TaskMapper;
import com.sogou.bizwork.task.api.core.task.dto.TaskDTO;
import com.sogou.bizwork.task.api.core.task.po.AuditScore;
import com.sogou.bizwork.task.api.core.task.po.TaskVo;
import com.sogou.bizwork.task.api.core.task.service.TaskService;
import com.sogou.bizwork.task.api.core.taskfollow.dao.TaskFollowMapper;
import com.sogou.bizwork.task.api.core.taskfollow.dto.TaskFollowDTO;
import com.sogou.bizwork.task.api.core.taskfollow.service.TaskFollowService;
import com.sogou.bizwork.task.api.core.tasklog.dao.TaskLogMapper;
import com.sogou.bizwork.task.api.core.tasklog.dto.TaskLogDTO;
import com.sogou.bizwork.task.api.core.user.dao.UserMapper;
import com.sogou.bizwork.task.api.core.user.dto.GroupDTO;
import com.sogou.bizwork.task.api.core.user.dto.GroupInfoDto;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.service.GroupService;
import com.sogou.bizwork.task.api.core.user.service.UserService;

import net.sf.json.JSONObject;

@Service("taskService")
@SuppressWarnings("unchecked")
public class TaskServiceImpl implements TaskService {

	private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

	@Autowired
	private TaskMapper taskDao;
	@Autowired
	private TaskFollowMapper taskFollowDao;
	@Autowired
	private WorkflowDao workflowDao;
	@Autowired
	private TaskLogMapper taskLogDao;
	@Autowired
	private TaskFollowService taskFollowService;
	@Autowired
	private TagService tagService;
	@Autowired
	private UserService userService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private XiaoPMessageTService.Iface xiaoPMessageTService;

	@Autowired
	private TaskLogService taskLogService;
	@Value("${CLIENT_EN_NAME}")
	private String CLIENT_EN_NAME;
	@Value("${CLIENT_KEY}")
	private String CLIENT_KEY;
	@Value("${mail_from}")
	private String mail_from;
	@Value("${mail_host}")
	private String mail_host;

	private static final byte TASK_TODO = 0;
	private static final byte TASK_DOING = 1;
	private static final byte TASK_DONE = 2;

	// @Autowired
	// private EmerService emerService;

	@Override
	public TaskDTO queryTaskInfo(long taskId) {
		return taskDao.queryTaskInfo(taskId);
	}

	@Override
	public List<TaskDTO> queryTasksWithUserType(long userId, Integer userType) {
		List<TaskDTO> taskDtos = null;
		if (TaskDTO.USER_TYPE_CHARGE.equals(userType)) { // 按负责人类型查询
			taskDtos = taskDao.queryChargeTasks(userId);
		} else if (TaskDTO.USER_TYPE_FOLLOW.equals(userType)) { // 按照关注人类型查询
			// 根据userid获取所属组的id
			UserDTO user = userService.getUserById((int) userId);
			taskDtos = taskDao.queryFollowTasks(userId, user.getGroupId());
		} else {
			logger.error("query condition ERROR, userType not equal 0 or 1");
			throw new RuntimeException("query condition ERROR, userType only equal 0 or 1");
		}
		if (CollectionUtils.isEmpty(taskDtos))
			return Collections.emptyList();

		return taskDtos;
	}

	@Override
	public List<TaskDTO> queryDemos(long userId) {
		List<TaskDTO> taskDtos = null;
		taskDtos = taskDao.queryDemo(userId);
		return taskDtos;
	}

	@Override
	public long addTask(TaskDTO taskDTO, long operateUser) {
		// insert task and get 'task_id'
		taskDao.addTask(taskDTO);

		// insert task_follow
		StringBuilder builder = new StringBuilder();
		List<TaskFollowDTO> followUserDTOs = taskDTO.getFollowUsers();

		if (CollectionUtils.isNotEmpty(followUserDTOs)) {
			followUserDTOs.remove(null);
			for (TaskFollowDTO followUserDTO : followUserDTOs) {
				followUserDTO.setTaskId(taskDTO.getId());
				followUserDTO.setStatus((byte) 0);
				builder.append(followUserDTO.getFollowUser()).append(";");
			}
			if (builder.length() > 0) {
				builder.deleteCharAt(builder.length() - 1);
			}
			//taskFollowDao.addTaskFollows(followUserDTOs);
			taskFollowService.addTaskFollows(followUserDTOs);
		}

		// write log
		TaskLogDTO taskLogDTO = new TaskLogDTO();
		assignTaskDTO2TaskLogDTO(taskDTO, taskLogDTO);
		taskLogDTO.setOperateType((byte) 0);
		taskLogDTO.setOperateUser(operateUser);
		taskLogDTO.setTaskFollows(builder.toString());
		taskLogService.addTaskLog(taskLogDTO);
		return taskDTO.getId();
	}

	@Override
	public boolean updateTask(TaskDTO taskDTO, long operateUser) {
		TaskDTO oldTaskDTO = queryTaskInfo(taskDTO.getId());
		boolean result = taskDao.updateTask(taskDTO) > 0;
		if (result) {
			boolean isEqual = compareFollows(oldTaskDTO.getFollowUsers(), taskDTO.getFollowUsers());
			if (!isEqual) {// 如果关注人更改，则重新更新关注人数据库
				// 先删除原始关注人信息
				taskFollowService.delTaskFollowsByTaskId(taskDTO.getId());
				// 再添加新的关注人列表
				if (taskDTO.getFollowUsers() != null && taskDTO.getFollowUsers().size() != 0) {
					for (TaskFollowDTO followUserDTO : taskDTO.getFollowUsers()) {
						followUserDTO.setTaskId(taskDTO.getId());
						followUserDTO.setStatus((byte) 0);
						followUserDTO.setCreateTime(taskDTO.getCreateTime());
						followUserDTO.setChangeTime(taskDTO.getChangeTime());
					}
					taskFollowService.addTaskFollows(taskDTO.getFollowUsers());
				}
			}
			TaskLogDTO taskLogDTO = new TaskLogDTO();
			assignTaskDTO2TaskLogDTO(taskDTO, taskLogDTO);
			taskLogDTO.setOperateType((byte) 1);
			taskLogDTO.setOperateUser(operateUser);
			StringBuilder builder = new StringBuilder();
			if (taskDTO.getFollowUsers() != null && taskDTO.getFollowUsers().size() != 0) {
				for (TaskFollowDTO followUserDTO : taskDTO.getFollowUsers()) {
					builder.append(followUserDTO.getFollowUser()).append(";");
				}
			}
			if (builder.length() > 0) {
				builder.deleteCharAt(builder.length() - 1);
			}
			taskLogDTO.setTaskFollows(builder.toString());

			taskLogDao.addTaskLog(taskLogDTO);
		}

		return result;
	}

	@Override
	public ScoreHistory getScoreHistory(TaskVo scheduledTask, int opearatorId) {
		ScoreHistory scoreHistory = new ScoreHistory();
		if (scheduledTask.getScoreId() > 0) {
			scoreHistory.setScoreId(scheduledTask.getScoreId());
		}
		if (scheduledTask.getScore() != null) {
			scoreHistory.setScoreTypeId(scheduledTask.getScore().getTypeId());  // typeid means
			scoreHistory.setScore(scheduledTask.getScore().getValue());
		}
		scoreHistory.setAcceptPeopleId(Integer.valueOf(scheduledTask.getChargeUser() + ""));
		UserDTO userDTO = userService.getUserByEmployeeId(Integer.valueOf(scheduledTask.getChargeUser() + ""));
		scoreHistory.setAcceptPeople(userDTO.getChineseName());
		UserDTO leaderDTO = userService.getUserByEmployeeId(userDTO.getLeaderId());
		scoreHistory.setLeaderId(userDTO.getLeaderId());
		scoreHistory.setLeader(leaderDTO.getChineseName());
		UserDTO opUserDTO = userService.getUserByEmployeeId(opearatorId);
		if (opearatorId == scheduledTask.getChargeUser()) {
			opUserDTO = leaderDTO;
		}
		scoreHistory.setGivePeople(opUserDTO.getChineseName());
		scoreHistory.setGivePeopleId(opUserDTO.getEmployeeId());
		scoreHistory.setGroup(userDTO.getGroupName());
		scoreHistory.setScoreFrom(1);
		return scoreHistory;
	}

	/**
	 * 比较两个关注人集合是否相等
	 * 
	 * @param oldTaskFollowDto
	 * @param newTaskFollowDto
	 * @return
	 */
	private boolean compareFollows(List<TaskFollowDTO> oldTaskFollowDto, List<TaskFollowDTO> newTaskFollowDto) {
		if (oldTaskFollowDto == null && newTaskFollowDto == null)
			return true;
		if (oldTaskFollowDto == null || newTaskFollowDto == null)
			return false;
		if (oldTaskFollowDto.size() != newTaskFollowDto.size())
			return false;
		if (oldTaskFollowDto.containsAll(newTaskFollowDto) && newTaskFollowDto.containsAll(oldTaskFollowDto)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean deleteTask(TaskDTO taskDTO, long operateUser) {
		// set task status to -1
		taskDTO.setStatus((byte) -1);
		int re = taskDao.updateTask(taskDTO); // 更新数据库task状态


		// set taskFollows status to -1
		taskFollowDao.updateTaskFollowsStatusDel(taskDTO.getId());

		// set task_tag status to 0
		tagService.updateTaskTagStatusDel(taskDTO.getId());

		TaskLogDTO taskLogDTO = new TaskLogDTO();
		assignTaskDTO2TaskLogDTO(taskDTO, taskLogDTO);
		taskLogDTO.setOperateType((byte) -1);
		taskLogDTO.setOperateUser(operateUser);
		StringBuilder builder = new StringBuilder();
		if (taskDTO.getFollowUsers() != null) {
			for (TaskFollowDTO followUserDTO : taskDTO.getFollowUsers()) {
				builder.append(followUserDTO.getFollowUser()).append(";");
			}
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		taskLogDTO.setTaskFollows(builder.toString());
		taskLogDao.addTaskLog(taskLogDTO);

		return re > 0;
	}

	/**
	 * @description 将Task中的属性赋值到TaskLog中
	 * @param taskDTO
	 * @param taskLogDTO
	 */
	private void assignTaskDTO2TaskLogDTO(TaskDTO taskDTO, TaskLogDTO taskLogDTO) {
		if (taskDTO == null)
			throw new RuntimeException("copy taskDTO to taskLogDTO fail ,as taskDTO is NULL");

		taskLogDTO.setTaskId(taskDTO.getId());
		taskLogDTO.setChargeUser(taskDTO.getChargeUser());
		taskLogDTO.setCreateUser(taskDTO.getCreateUser());
		taskLogDTO.setStartTime(taskDTO.getStartTime());
		taskLogDTO.setEndTime(taskDTO.getEndTime());
		taskLogDTO.setDescription(taskDTO.getDescription());
		taskLogDTO.setAttachment(taskDTO.getAttachment());
		taskLogDTO.setStatus(taskDTO.getStatus());
	}

	/**
	 * 发送提醒邮件到任务负责人
	 */
	@Override
	public String sendMailAlert(long operateUserId, TaskDTO taskDTO) {
		UserDTO operateUser = userService.getUserById((int) operateUserId);
		String mailBody = generateMailAlert(operateUser, taskDTO);
		String mailSubject = "Bizwork-日常任务“" + taskDTO.getTaskName() + "”跟进提醒";
		UserDTO chargeUser = userService.getUserById((int) taskDTO.getChargeUser());
		String mailTo = operateUser.getEmail() + ";" + chargeUser.getEmail();
		return sendMail(operateUserId, mailBody, mailSubject, mailTo);
	}

	/**
	 * 任务负责人发生变更，给所有人发送邮件
	 */
	@Override
	public String sendMailChargeUserChange(long operateUserId, TaskDTO taskDTO) {
		UserDTO operateUser = userService.getUserById((int) operateUserId);
		String mailBody = generateMailChargeUserChange(operateUser, taskDTO);
		String mailSubject = "Bizwork-日常任务[" + taskDTO.getTaskName() + "]负责人变更";
		UserDTO chargeUser = userService.getUserById((int) taskDTO.getChargeUser());
		String mailTo = operateUser.getEmail() + ";" + chargeUser.getEmail();
		List<TaskFollowDTO> followUsers = taskDTO.getFollowUsers();
		if (followUsers != null && followUsers.size() > 0)
			for (TaskFollowDTO followUser : followUsers) {
				mailTo += ";" + getFollowUserEmail(followUser);
			}
		return sendMail(operateUserId, mailBody, mailSubject, mailTo);
	}

	// 获取followUser的邮件
	private String getFollowUserEmail(TaskFollowDTO followUser) {
		String mailTo = "";
		if (followUser.getType() == 0) {
			UserDTO user = userService.getUserById((int) followUser.getFollowUser());
			mailTo = user.getEmail();
		} else if (followUser.getType() == 1) {
			GroupDTO group = groupService.getGroupById((int) followUser.getFollowUser());
			mailTo = group.getGroupEmail();
		}
		return mailTo;
	}

	/**
	 * 新建任务，给创建人和负责人发送邮件
	 */
	@Override
	public String sendMailCreatNewTask(long operateUserId, TaskDTO taskDTO) {
		UserDTO operateUser = userService.getUserById((int) operateUserId);
		String mailBody = generateMailCreatNewTask(operateUser, taskDTO);
		String mailSubject = "Bizwork-日常任务[" + taskDTO.getTaskName() + "]新建提醒";
		UserDTO chargeUser = userService.getUserById((int) taskDTO.getChargeUser());
		String mailTo = operateUser.getEmail() + ";" + chargeUser.getEmail();
		List<TaskFollowDTO> followUsers = taskDTO.getFollowUsers();
		if (followUsers != null && followUsers.size() > 0)
			for (TaskFollowDTO followUser : followUsers) {
				mailTo += ";" + getFollowUserEmail(followUser);
			}
		return sendMail(operateUserId, mailBody, mailSubject, mailTo);
	}

	@Override
	public void sendTaskToXiaoP(long operateUserId, TaskDTO taskDTO) {
		List<XiaoPMessage> xiaoPMessages = new ArrayList<XiaoPMessage>();
		XiaoPMessage chargeMessage = new XiaoPMessage();
		StringBuilder chargeContent = new StringBuilder();
		UserDTO operateDto = userService.getUserById((int) operateUserId);
		UserDTO chargeDto = userService.getUserById((int) taskDTO.getChargeUser());
		if (operateDto == null || chargeDto == null)
			return;
		chargeContent.append("[bizwork]您负责的项目“").append(taskDTO.getTaskName()).append("”已创建，请尽快跟进，创建人：")
				.append(operateDto.getChineseName());

		XiaoPMessage followMessage = new XiaoPMessage();
		StringBuilder followContent = new StringBuilder();
		followContent.append("[bizwork]").append(operateDto.getChineseName()).append("邀请您关注任务“")
				.append(taskDTO.getTaskName()).append("”");
		if (StringUtils.isNotBlank(taskDTO.getDescription())) {
			chargeContent.append("，任务描述：【").append(taskDTO.getDescription()).append("】");
			followContent.append("，任务描述：【").append(taskDTO.getDescription()).append("】");
		}

		try {
			String startTime = null;
			String endTime = null;
			if (StringUtils.isNotBlank(taskDTO.getStartTime()) && !taskDTO.getStartTime().startsWith(("0000-00-00"))) {
				startTime = taskDTO.getStartTime().substring(0, 10);
			}
			if (StringUtils.isNotBlank(taskDTO.getEndTime()) && !taskDTO.getEndTime().startsWith("0000-00-00")) {
				endTime = taskDTO.getEndTime().substring(0, 10);
			}
			if (startTime == null && endTime != null) {
				chargeContent.append("，截止时间：").append(endTime);
				followContent.append("，截止时间：").append(endTime);
			} else if (startTime != null && endTime == null) {
				chargeContent.append("，开始时间：").append(startTime);
				followContent.append("，开始时间：").append(startTime);
			} else if (startTime != null && endTime != null) {
				chargeContent.append("，任务时间：").append(startTime).append("至").append(endTime);
				followContent.append("，任务时间：").append(startTime).append("至").append(endTime);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			MessageUtils.printStackTrace(e);
		}

		chargeMessage.setReceivers(chargeDto.getUserName());
		chargeMessage.setType("0");
		chargeMessage.setContent(chargeContent.toString());
		List<TaskFollowDTO> taskFollowDTOs = taskDTO.getFollowUsers();
		StringBuilder receivers = new StringBuilder();

		Set<Long> receiverSet = new HashSet<Long>();
		Set<Long> groupIdSet = new HashSet<Long>();
		if (CollectionUtils.isNotEmpty(taskFollowDTOs)) {
			for (TaskFollowDTO t : taskFollowDTOs) {
				if (t != null) {
					if (t.getType() == 0) {
						receiverSet.add(t.getFollowUser());
					} else if (t.getType() == 1) {
						groupIdSet.add(t.getFollowUser());
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(groupIdSet)) {
			List<Long> employeeIds = userService.getEmployeeIdsByGroupIds(new ArrayList<Long>(groupIdSet));
			if (CollectionUtils.isNotEmpty(employeeIds)) {
				for (Long e : employeeIds) {
					receiverSet.add(e);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(receiverSet)) {
			receiverSet.remove(null);
			receiverSet.remove(operateUserId);
			if (CollectionUtils.isNotEmpty(receiverSet))
				receiverSet.remove(chargeDto.getId());
			if (CollectionUtils.isNotEmpty(receiverSet)) {
				List<String> userNames = userService.getUserNamesByEmployeeIds(new ArrayList<Long>(receiverSet));
				if (CollectionUtils.isNotEmpty(userNames)) {
					receivers.append(userNames.get(0));
					for (int i = 1; i < userNames.size(); i++) {
						receivers.append(",").append(userNames.get(i));
					}
				}
			}
		}

		if (taskDTO.getChargeUser() != operateUserId) {
			xiaoPMessages.add(chargeMessage);
		}
		if (receivers.length() > 0) {
			followMessage.setReceivers(receivers.toString());
			followMessage.setType("0");
			followMessage.setContent(followContent.toString());
			xiaoPMessages.add(followMessage);
		}
		if (CollectionUtils.isNotEmpty(xiaoPMessages)) {
			try {
				xiaoPMessageTService.sendMessageToXiaoP(xiaoPMessages);
			} catch (ApiTException e) {
				e.printStackTrace();
			} catch (TException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 任务状态发生变更，给所有人发送邮件
	 */
	public String sendMailStatusChange(long operateUserId, TaskDTO taskDTO) {
		UserDTO operateUser = userService.getUserById((int) operateUserId);
		String mailBody = generateMailStatusChange(operateUser, taskDTO);
		String mailSubject = "Bizwork-日常任务[" + taskDTO.getTaskName() + "]状态更新提醒";
		UserDTO chargeUser = userService.getUserById((int) taskDTO.getChargeUser());
		String mailTo = operateUser.getEmail() + ";" + chargeUser.getEmail();
		List<TaskFollowDTO> followUsers = taskDTO.getFollowUsers();
		if (followUsers != null && followUsers.size() > 0)
			for (TaskFollowDTO followUser : followUsers) {
				mailTo += ";" + getFollowUserEmail(followUser);
			}
		return sendMail(operateUserId, mailBody, mailSubject, mailTo);
	}

	/**
	 * 删除任务，给所有人发送邮件
	 */
	public String sendMailDeleteTask(long operateUserId, TaskDTO taskDTO) {
		UserDTO operateUser = userService.getUserById((int) operateUserId);
		String mailBody = generateMailDeleteTask(operateUser, taskDTO);
		String mailSubject = "Bizwork-日常任务[" + taskDTO.getTaskName() + "]删除提醒";
		UserDTO chargeUser = userService.getUserById((int) taskDTO.getChargeUser());
		String mailTo = operateUser.getEmail() + ";" + chargeUser.getEmail();
		List<TaskFollowDTO> followUsers = taskDTO.getFollowUsers();
		if (followUsers != null && followUsers.size() > 0)
			for (TaskFollowDTO followUser : followUsers) {
				mailTo += ";" + getFollowUserEmail(followUser);
			}
		return sendMail(operateUserId, mailBody, mailSubject, mailTo);
	}

	@Override
	public String sendMail(long operateUserId, String mailBody, String mailSubject, String mailTo) {

		MultipartEntityBuilderWrapper builder = MultipartEntityBuilderWrapper.create();
		builder.addClientEnName(CLIENT_EN_NAME).addClientKey(CLIENT_KEY).addMailFrom("bizmail@sogou-inc.com")
				.addMailTo(mailTo).addMailSubject(mailSubject).addMailBody(mailBody).addSendMode(SendMode.WHOLE.mode);

		String response = MailSendUtils.send(mail_host + "/sendMail.action", builder);
		String code = JSONObject.fromObject(response).getString("code");
		if (!code.equals("A00000")) {
			String err = JSONObject.fromObject(response).getString("msg");
			logger.error("send mail error", err);
			return err;
		} else
			return "";
	}

	@Override
	public void sendEntryEmail(String mailBody, String mailSubject, String mailTo) {
		Resource resource = new ClassPathResource("files/biztech内部系统使用手册.rar");
		if (resource.exists()) {
			MultipartEntityBuilderWrapper builder = MultipartEntityBuilderWrapper.create();
			try {
				builder.addClientEnName(CLIENT_EN_NAME).addClientKey(CLIENT_KEY).addMailFrom("bizmail@sogou-inc.com")
						.addMailTo(mailTo).addMailSubject(mailSubject).addMailBody(mailBody)
						.addSendMode(SendMode.WHOLE.mode).addAttachments(resource.getFile())
						;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String response = MailSendUtils.send(mail_host + "/sendMail.action", builder);
			String code = JSONObject.fromObject(response).getString("code");
		} else {
			
		}
	}

	/**
	 * 发送提醒邮件到任务负责人
	 */
	public String generateMailAlert(UserDTO operateUser, TaskDTO taskDTO) {
		UserDTO chargeUser = userService.getUserById((int) taskDTO.getChargeUser());

		StringBuilder builder = new StringBuilder();
		builder.append("<p>Hi,");
		builder.append(chargeUser.getChineseName());
		builder.append("</p><p>&nbsp;&nbsp;&nbsp;&nbsp;任务“");
		builder.append(taskDTO.getTaskName());
		builder.append("”原定");
		String time = taskDTO.getEndTime();
		if (time != null)
			builder.append(time.substring(0, time.length() / 2 + 1));
		builder.append("完成，请尽快跟进。</p>");
		if (!taskDTO.getDescription().equals("")) {
			builder.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;任务详情：");
			builder.append(taskDTO.getDescription());
			builder.append("</p>");
		}
		builder.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;任务链接：");
		builder.append("http://task.bizwork.sogou-inc.com/#task/myChargeTask/");
		builder.append("</p><p><br/></p><p>来自bizwork，提醒人：");
		builder.append(operateUser.getChineseName());
		builder.append("。<br/></p>");
		return builder.toString();
	}

	/**
	 * 任务负责人发生变更，给所有人发送邮件
	 */
	public String generateMailChargeUserChange(UserDTO operateUser, TaskDTO taskDTO) {
		UserDTO chargeUser = userService.getUserById((int) taskDTO.getChargeUser());

		StringBuilder builder = new StringBuilder();
		builder.append("<p>Hi,");
		builder.append(chargeUser.getChineseName());
		builder.append("</p><p>&nbsp;&nbsp;&nbsp;&nbsp;任务[");
		builder.append(taskDTO.getTaskName());
		builder.append("]负责人已变更为：");
		builder.append(chargeUser.getChineseName());
		builder.append("，请及时跟进。</p>");
		builder.append("<p><br/></p><p>操作人：");
		builder.append(operateUser.getChineseName());
		builder.append("。<br/></p>");
		return builder.toString();
	}

	/**
	 * 新建任务，给创建人和负责人发送邮件
	 */
	public String generateMailCreatNewTask(UserDTO operateUser, TaskDTO taskDTO) {
		UserDTO chargeUser = userService.getUserById((int) taskDTO.getChargeUser());

		StringBuilder builder = new StringBuilder();
		builder.append("<p>Hi,");
		builder.append(chargeUser.getChineseName());
		builder.append("</p><p>&nbsp;&nbsp;&nbsp;&nbsp;你负责的任务“");
		builder.append(taskDTO.getTaskName());
		builder.append("”已被创建，请及时跟进。</p>");
		if (StringUtils.isNotEmpty(taskDTO.getDescription())) {
			builder.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;任务详情：");
			builder.append(taskDTO.getDescription());
			builder.append("</p>");
		}
		builder.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;任务截止时间：");
		String time = taskDTO.getEndTime();
		if (time.equals("0000-00-00 00:00:00"))
			builder.append("待定");
		else
			builder.append(time.substring(0, time.length() / 2 + 1));
		builder.append("</p>");
		builder.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;任务链接：");
		builder.append("http://task.bizwork.sogou-inc.com/#task/myChargeTask/");
		builder.append("</p><p><br/></p><p>来自bizwork，操作人：");
		builder.append(operateUser.getChineseName());
		builder.append("。<br/></p>");
		return builder.toString();
	}

	/**
	 * 任务状态发生变更，给所有人发送邮件
	 */
	public String generateMailStatusChange(UserDTO operateUser, TaskDTO taskDTO) {

		StringBuilder builder = new StringBuilder();
		builder.append("<p>Hi,all</p><p>&nbsp;&nbsp;&nbsp;&nbsp;任务“");
		builder.append(taskDTO.getTaskName());
		builder.append("”状态更新为：");
		byte status = taskDTO.getStatus();
		switch (status) {
		case TASK_TODO:
			builder.append("待处理");
			break;
		case TASK_DOING:
			builder.append("进行中");
			break;
		case TASK_DONE:
			builder.append("已完成");
			break;
		}
		builder.append("，请周知。</p>");
		if (!taskDTO.getDescription().equals("")) {
			builder.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;任务详情：");
			builder.append(taskDTO.getDescription());
			builder.append("</p>");
		}
		builder.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;任务链接：");
		builder.append("http://task.bizwork.sogou-inc.com/#task/myAttentionTask/");
		builder.append("</p><p><br/></p><p>来自bizwork，操作人：");
		builder.append(operateUser.getChineseName());
		builder.append("。<br/></p>");
		return builder.toString();
	}

	/**
	 * 删除任务，给所有人发送邮件
	 */
	public String generateMailDeleteTask(UserDTO operateUser, TaskDTO taskDTO) {

		StringBuilder builder = new StringBuilder();
		builder.append("<p>Hi,all<p>&nbsp;&nbsp;&nbsp;&nbsp;任务[");
		builder.append(taskDTO.getTaskName());
		builder.append("]已被删除，请周知。</p>");
		builder.append("<p><br/></p><p>操作人：");
		builder.append(operateUser.getChineseName());
		builder.append("。<br/></p>");
		return builder.toString();
	}

	@Override
	public boolean deleteFollowUser(TaskDTO taskDTO, TaskFollowDTO operateUserDTO) {

		// set taskFollows status to -1
		int re = taskFollowDao.updateFollowUser(operateUserDTO);

		TaskLogDTO taskLogDTO = new TaskLogDTO();
		assignTaskDTO2TaskLogDTO(taskDTO, taskLogDTO);
		taskLogDTO.setOperateType((byte) 1);
		taskLogDTO.setOperateUser(operateUserDTO.getFollowUser());
		StringBuilder builder = new StringBuilder();
		if (taskDTO.getFollowUsers() != null) {
			for (TaskFollowDTO followUserDTO : taskDTO.getFollowUsers()) {
				if (!followUserDTO.equals(operateUserDTO)) {
					builder.append(followUserDTO.getFollowUser()).append(";");
				}
			}
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		taskLogDTO.setTaskFollows(builder.toString());
		taskLogDao.addTaskLog(taskLogDTO);

		return re > 0;
	}

	@Override
	public void updateChargeUser(long taskId, long chargeUserId, long operateUser) {
		taskDao.updateChargeUser(taskId, chargeUserId);

		// write log
		TaskDTO taskDTO = taskDao.queryTaskInfo(taskId);
		TaskLogDTO taskLogDTO = new TaskLogDTO();
		assignTaskDTO2TaskLogDTO(taskDTO, taskLogDTO);
		taskLogDTO.setOperateType((byte) 1);
		taskLogDTO.setOperateUser(operateUser);
		StringBuilder builder = new StringBuilder();
		if (taskDTO.getFollowUsers() != null && taskDTO.getFollowUsers().size() != 0) {
			for (TaskFollowDTO followUserDTO : taskDTO.getFollowUsers()) {
				builder.append(followUserDTO.getFollowUser()).append(";");
			}
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		taskLogDTO.setTaskFollows(builder.toString());

		taskLogDao.addTaskLog(taskLogDTO);
	}

	@Override
	public List<Long> getChargeUser() {
		List<Long> list = new ArrayList<Long>();
		list = taskDao.getChargeUser();
		return list;
	}

	@Override
	public List<TaskDTO> getDoneJob(Long chargeUser) {
		List<TaskDTO> list = new ArrayList<TaskDTO>();
		list = taskDao.getDoneJob(chargeUser);
		return list;
	}

	@Override
	public List<TaskDTO> getCommingJob(Long chargeUser) {
		List<TaskDTO> list = new ArrayList<TaskDTO>();
		list = taskDao.getCommingJob(chargeUser);
		return list;
	}

	@Override
	public List<TaskDTO> getTodoJob(Long chargeUser) {
		List<TaskDTO> list = new ArrayList<TaskDTO>();
		list = taskDao.getTodoJob(chargeUser);
		return list;
	}

	@Override
	public String sendMailAlert(long id, List<TaskDTO> list1, List<TaskDTO> list2, List<TaskDTO> list3) {
		// 表格全为空,不发送邮件
		if ((list1 == null || list1.size() == 0) && (list2 == null || list2.size() == 0)
				&& (list3 == null || list3.size() == 0)) {
			return "";
		}
		UserDTO chargeUser = userService.getUserByEmployeeId((int) id);
		String mailBody = generateMailAlert(chargeUser, list1, list2, list3);
		String mailSubject = "Bizwork-日常任务跟进提醒";
		String mailTo = chargeUser.getEmail();
		if (mailTo == null || "".equals(mailTo)) {
			return "";
		}
		return sendMail(id, mailBody, mailSubject, mailTo);
	}

	private String generateMailAlert(UserDTO chargeUser, List<TaskDTO> list1, List<TaskDTO> list2,
			List<TaskDTO> list3) {
		StringBuilder builder = new StringBuilder();
		builder.append("<p>Hi,");
		builder.append(chargeUser.getChineseName());
		builder.append("</p>");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String d = sdf.format(date);
		builder.append("截止到 ");
		builder.append(d);
		builder.append(" 日10点");
		if (list1 != null && list1.size() != 0) {
			builder.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">已逾期的任务如下</font></p>");
			builder.append(
					"<style type=\"text/css\">table.gridtable {font-family: verdana,arial,sans-serif;font-size:14px;color:#333333;border-width: 1px; border-color: #666666;border-collapse: collapse;}table.gridtable th {border-width: 1px;padding: 8px; border-style: solid;border-color: #666666;background-color: #039BE5;}table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}</style>");
			builder.append("<p><table class=\"gridtable\" >");
			builder.append("<tr><th>任务名</th><th>项目描述</th><th>任务截止时间</th></tr>");
			for (TaskDTO t : list1) {
				builder.append("<tr><td>");
				builder.append(t.getTaskName());
				builder.append("</td><td>");
				builder.append(t.getDescription());
				builder.append("</td><td>");
				String time = t.getEndTime().substring(0, 10);
				builder.append(time);
				builder.append("</td></tr>");
			}
			builder.append("</table></p>");
		}

		if (list2 != null && list2.size() != 0) {
			builder.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"red\">将到期的任务如下</font></p>");
			builder.append(
					"<style type=\"text/css\">table.gridtable {font-family: verdana,arial,sans-serif;font-size:14px;color:#333333;border-width: 1px; border-color: #666666;border-collapse: collapse;}table.gridtable th {border-width: 1px;padding: 8px; border-style: solid;border-color: #666666;background-color: #039BE5;}table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}</style>");
			builder.append("<p><table class=\"gridtable\" >");
			builder.append("<tr><th>任务名</th><th>项目描述</th><th>任务截止时间</th></tr>");
			for (TaskDTO t : list2) {
				builder.append("<tr><td>");
				builder.append(t.getTaskName());
				builder.append("</td><td>");
				builder.append(t.getDescription());
				builder.append("</td><td>");
				String time = t.getEndTime().substring(0, 10);
				builder.append(time);
				builder.append("</td></tr>");
			}
			builder.append("</table></p>");
		}

		if (list3 != null && list3.size() != 0) {
			builder.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"blue\">待跟进的任务如下</font></p>");
			builder.append(
					"<style type=\"text/css\">table.gridtable {font-family: verdana,arial,sans-serif;font-size:14px;color:#333333;border-width: 1px; border-color: #666666;border-collapse: collapse;}table.gridtable th {border-width: 1px;padding: 8px; border-style: solid;border-color: #666666;background-color: #039BE5;}table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}</style>");
			builder.append("<p><table class=\"gridtable\" >");
			builder.append("<tr><th>任务名</th><th>项目描述</th><th>任务截止时间</th></tr>");
			for (TaskDTO t : list3) {
				builder.append("<tr><td>");
				builder.append(t.getTaskName());
				builder.append("</td><td>");
				builder.append(t.getDescription());
				builder.append("</td><td>");
				String time = t.getEndTime().substring(0, 10);
				builder.append(time);
				builder.append("</td></tr>");
			}
			builder.append("</table></p>");
		}
		builder.append("</br>请尽快跟进");
		builder.append("</br><p>&nbsp;&nbsp;&nbsp;&nbsp;任务链接：");
		builder.append("<a href = \"http://task.bizwork.sogou-inc.com/#myChargeTask/\" />");
		builder.append("http://task.bizwork.sogou-inc.com/#myChargeTask/<br/></p>");
		return builder.toString();
	}

	@Override
	public List<BriefTypeMessage> updateAllChargeBriefMsg() {
		return taskDao.getAllChargeBriefTypeMessages();
	}

	@Override
	public List<BriefTypeMessage> getAllChargeBriefTypeTasks() {
		return taskDao.getAllChargeBriefTypeMessages();
	}

	@Override
	public List<TaskDTO> queryTaskByFullInfo(long createUser, long chargeUser, String startTime, String endTime,
			String taskName) {
		return taskDao.queryTaskByFullInfo(createUser, chargeUser, startTime, endTime, taskName);
	}

	@Override
	public void sendSvnApproveMail(String businessKey, long receiverUserId) {
		ActInfo actInfo = workflowDao.getActivitiInfo(businessKey);
		String mailBody = "您好，您有新的待办流程：";
		String mailSubject = "[bizwork]您有待审批的流程：" + actInfo.getFlowName() + "，请尽快处理。";
		UserDTO userDTO = userMapper.getUserByEmployeeId((int) receiverUserId);
		// UserDTO leaderDTO = null;
		String formData = null;
		if (actInfo.getFlowTypeId() == 101) {
			mailBody = this.getSvnApplicationMailBody(actInfo);
		} else if (actInfo.getFlowTypeId() == 102) {
			mailBody = this.getSvnDistributionMailBody(actInfo);
		} else if (actInfo.getFlowTypeId() == 201) {
			mailBody = "<p>" + actInfo.getChineseName() + "入职流程等待您的处理！</p>";
			formData = actInfo.getFormData();
			try {
				Map<String, Object> formDataMap = (Map<String, Object>) JSONUtil.deserialize(formData);
				String qq = (String) formDataMap.get("qq");
				String info = null;
				if (StringUtils.isNotBlank(qq)) {
					info = actInfo.getChineseName() + "的qq号为：" + qq;
				}
				String weixin = (String) formDataMap.get("weixin");
				if (StringUtils.isNotBlank(weixin)) {
					if (StringUtils.isBlank(info)) {
						info = actInfo.getChineseName() + "的微信号为：" + weixin;
					} else {
						info += "，微信号为：" + weixin;
					}
				}
				if (StringUtils.isNotBlank(info)) {
					info = "。请及时将" + actInfo.getChineseName() + "邀请至相关群组";
					mailBody += "<p>" + info + "</p>";
				}
				if (formDataMap.get("step") == null) {
					userDTO = userService.getUserByEmployeeId(Integer.parseInt(actInfo.getEmployeeId() + ""));
					formDataMap.put("step", 0);
					workflowDao.updateFormdataByBusinessKey(actInfo.getBusinessKey(), JSONUtil.serialize(formDataMap),
							actInfo.getFlowName());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mailBody += "<p>&nbsp;&nbsp;&nbsp;&nbsp;[bizwork]流程处理请进入：<a href = \"http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/\" >http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/</a></p>";
			// userDTO = userMapper.getUserByEmployeeId(userDTO.getLeaderId());
			// leaderDTO =
			// userMapper.getUserByEmployeeId(userDTO.getLeaderId());
			// userDTO.setChineseName(leaderDTO.getChineseName());
			// //此时的申请人要改为leader申请
		}

		// sb.append("<tr>");
		if (actInfo.getFlowTypeId() == 201 && StringUtils.isNotBlank(formData)) {
			return;
		}
		if ( userDTO != null && StringUtils.isNotBlank(userDTO.getEmail())) {
			sendMail(userDTO.getEmployeeId(), mailBody, mailSubject, userDTO.getEmail());
		}

		String xiaoPContent = actInfo.getFlowName() + "，申请人：" + actInfo.getChineseName() + "，请尽快处理。";
		this.sendToXiaoP(userDTO.getUserName(), xiaoPContent, businessKey);
	}


	private void sendToXiaoP(String userName, String content, String businessKey) {
		XiaoPMessage xiaoPMessage = new XiaoPMessage();
		xiaoPMessage.setContent(content);
		xiaoPMessage.setUrl("http://task.bizwork.sogou-inc.com/mobile#/workflowInfo/" + businessKey);
		xiaoPMessage.setTitle("[bizwork]您有待审批的流程");
		xiaoPMessage.setType("3");
		xiaoPMessage.setReceivers(userName);
		List<XiaoPMessage> xiaoPMessages = new ArrayList<XiaoPMessage>();
		xiaoPMessages.add(xiaoPMessage);
		try {
			xiaoPMessageTService.sendMessageToXiaoP(xiaoPMessages);
		} catch (ApiTException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
	}

	private String getSvnApplicationMailBody(ActInfo actInfo) {
		String formData = actInfo.getFormData();
		StringBuffer sb = new StringBuffer();
		sb.append("<p>您好，您有新的待办流程：【" + actInfo.getFlowName()
				+ "】，</br><style type=\"text/css\">table.gridtable {font-family: verdana,arial,sans-serif;font-size:14px;color:#333333;border-width: 1px; border-color: #666666;border-collapse: collapse;}table.gridtable th {border-width: 1px;padding: 8px; border-style: solid;border-color: #666666;background-color: #039BE5;}table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}</style><p><table class=\"gridtable\" ><tr><th>仓库名称</th><th>仓库功能描述</th><th>是否涉及广告发布</th><th>是否设计财务、计费</th><th>是否需要404审计</th><th>申请人</th><th>权限分配功能开通给哪位</th></tr>");
		try {
			Map<String, Object> params = (Map<String, Object>) JSONUtil.deserialize(formData);
			if (!org.springframework.util.CollectionUtils.isEmpty(params)) {
				sb.append("<tr><td>").append(params.get("repositoryName")).append("</td>");
				sb.append("<td>").append(params.get("description")).append("</td>");
				if ((Boolean) params.get("needADPublish")) {
					sb.append("<td>是</td>");
				} else {
					sb.append("<td>否</td>");
				}
				if ((Boolean) params.get("needFinance")) {
					sb.append("<td>是</td>");
				} else {
					sb.append("<td>否</td>");
				}
				if ((Boolean) params.get("need404")) {
					sb.append("<td>是</td>");
				} else {
					sb.append("<td>否</td>");
				}
				sb.append("<td>").append(actInfo.getChineseName()).append("</td>");
				sb.append("<td>").append(params.get("manager")).append("</td></tr></table></p></br>");
				sb.append(
						"&nbsp;&nbsp;&nbsp;&nbsp;[bizwork]审批请进入：<a href = \"http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/\" >http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/</a>");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String getSvnDistributionMailBody(ActInfo actInfo) {
		String formData = actInfo.getFormData();
		StringBuffer sb = new StringBuffer();
		sb.append("<p>您好，您有新的待办流程：【" + actInfo.getFlowName()
				+ "】，</br><style type=\"text/css\">table.gridtable {font-family: verdana,arial,sans-serif;font-size:14px;color:#333333;border-width: 1px; border-color: #666666;border-collapse: collapse;}table.gridtable th {border-width: 1px;padding: 8px; border-style: solid;border-color: #666666;background-color: #039BE5;}table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}</style><p><table class=\"gridtable\" ><tr><th>仓库url</th><th>给谁开通</th><th>申请人</th><th>仓库管理员</th></tr>");
		try {
			Map<String, Object> params = (Map<String, Object>) JSONUtil.deserialize(formData);
			String path = "";
			String assignTo = "";
			List<Map<String, String>> repositories = (List<Map<String, String>>) params.get("repositories");
			if (CollectionUtils.isNotEmpty(repositories)) {
				int i = 0;
				for (Map<String, String> repository : repositories) {
					if (i == 0) {
						i++;
					} else {
						path += "、";
					}
					path += repository.get("path");
				}
			}
			List<Map<String, Object>> persons = (List<Map<String, Object>>) params.get("persons");
			if (CollectionUtils.isNotEmpty(persons)) {
				int i = 0;
				for (Map<String, Object> person : persons) {
					if (i == 0) {
						i++;
					} else {
						assignTo += "、";
					}
					assignTo += person.get("email");
					try {
						long permission = Long.parseLong(person.get("permission") + "");
						if (permission == 1) {
							assignTo += "：读写";
						} else {
							assignTo += "：只读";
						}
					} catch (Exception e) {
						e.printStackTrace();
						if (person.get("permission").equals("1")) {
							assignTo += "：读写";
						} else {
							assignTo += "：只读";
						}
					}
				}
			}
			sb.append("<td>").append(path).append("</td>");
			sb.append("<td>").append(assignTo).append("</td>");
			sb.append("<td>").append(actInfo.getChineseName()).append("</td>");
			List<String> managerList = (List<String>) params.get("managers");
			if (CollectionUtils.isNotEmpty(managerList)) {
				String managers = managerList.toString();
				sb.append("<td>").append(managers.substring(1, managers.length() - 1))
						.append("</td></tr></table></p></br>");
			}
			sb.append(
					"&nbsp;&nbsp;&nbsp;&nbsp;[bizwork]审批请进入：<a href = \"http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/\" >http://task.bizwork.sogou-inc.com/#workflow/dailyWorkflow/</a>");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public void updateToDelByTaskIds(List<Long> taskIds) {
		taskDao.updateToDelByTaskIds(taskIds);     //周期任务仍然存在
		taskFollowDao.updateToDelByTaskIds(taskIds);
	}

	@Override
	public GroupInfoDto getGroupInfoByGroupname(String groupname) {
		return taskDao.getGroupInfoByGroupname(groupname);
	}

	@Override
	public List<AuditScore> getAuditScoresByEmployeeid(long employeeid) {
		return taskDao.getAuditScoresByEmployeeid(employeeid);

	}

	@Override
	public void updateScoreIdByTaskId(long taskId, int scoreStatus) {
		taskDao.updateScoreIdByTaskId(taskId, scoreStatus);
	}

	@Override
	public void updateScoreStatusByTaskId(long taskId, int scoreStatus) {
		taskDao.updateScoreStatusByTaskId(taskId, scoreStatus);
	}

}
