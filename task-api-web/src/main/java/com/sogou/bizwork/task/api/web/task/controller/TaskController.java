package com.sogou.bizwork.task.api.web.task.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sogou.bizwork.api.xiaop.message.XiaoPMessage;
import com.sogou.bizwork.api.xiaop.message.service.XiaoPMessageTService;
import com.sogou.bizwork.task.api.common.exception.ApiTException;
import com.sogou.bizwork.task.api.common.exception.DFSException;
import com.sogou.bizwork.task.api.common.util.DFSUtils;
import com.sogou.bizwork.task.api.core.bizservicetree.service.BizServiceTreeService;
import com.sogou.bizwork.task.api.core.dto.Result;
import com.sogou.bizwork.task.api.core.message.dto.MessageDTO;
import com.sogou.bizwork.task.api.core.message.service.MessageService;
import com.sogou.bizwork.task.api.core.scheduled.bo.ScheduledTaskBo;
import com.sogou.bizwork.task.api.core.score.bo.Score;
import com.sogou.bizwork.task.api.core.score.bo.ScoreHistory;
import com.sogou.bizwork.task.api.core.score.bo.ScoreStatus;
import com.sogou.bizwork.task.api.core.score.service.ScoreService;
import com.sogou.bizwork.task.api.core.tag.dto.TagDto;
import com.sogou.bizwork.task.api.core.tag.dto.TagOrderDto;
import com.sogou.bizwork.task.api.core.tag.service.TagService;
import com.sogou.bizwork.task.api.core.task.dto.TaskDTO;
import com.sogou.bizwork.task.api.core.task.msg.service.BriefTaskMessage;
import com.sogou.bizwork.task.api.core.task.po.AttachmentInfo;
import com.sogou.bizwork.task.api.core.task.po.AuditScore;
import com.sogou.bizwork.task.api.core.task.po.FollowUserVo;
import com.sogou.bizwork.task.api.core.task.po.MessageVO;
import com.sogou.bizwork.task.api.core.task.po.Scheduled;
import com.sogou.bizwork.task.api.core.task.po.TagVo;
import com.sogou.bizwork.task.api.core.task.po.TaskVo;
import com.sogou.bizwork.task.api.core.task.service.ScheduledTaskService;
import com.sogou.bizwork.task.api.core.task.service.TaskService;
import com.sogou.bizwork.task.api.core.taskfollow.dto.TaskFollowDTO;
import com.sogou.bizwork.task.api.core.user.dto.GroupDTO;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.service.GroupService;
import com.sogou.bizwork.task.api.core.user.service.UserService;
import com.sogou.bizwork.task.api.tag.result.TagResult;
import com.sogou.bizwork.task.api.tag.service.TagTService;
import com.sogou.bizwork.task.api.tag.to.TagTo;
import com.sogou.bizwork.task.api.web.common.exception.BizException;
import com.sogou.bizwork.task.api.web.common.util.UserHolder;
import com.sogou.bizwork.task.api.web.message.vo.MessageConvertor;
import com.sogou.bizwork.task.api.web.message.vo.MessageProxyVO;
import com.sogou.bizwork.task.api.web.tag.TagConvertor;
import com.sogou.bizwork.task.api.web.task.convertor.TaskConvertor;
import com.sogou.bizwork.task.api.web.task.util.TaskUtil;
import com.sogou.bizwork.task.api.web.task.validator.TaskValidator;
import com.sogou.bizwork.task.api.web.task.vo.TagOrderProxyVo;
import com.sogou.bizwork.task.api.web.task.vo.TagOrderVo;
import com.sogou.bizwork.task.api.web.task.vo.TaskFrameVo;
import com.sogou.bizwork.task.api.web.task.vo.TaskProxyVo;

import net.sf.json.JSONObject;

/**
 * @description task控制器
 * @author liquancai
 * @date 2016-8-8
 */

@Controller
@RequestMapping("/task")
public class TaskController {

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private TagTService.Iface tagTService;
	// @Autowired
	// private TaskTService.Iface taskTService;
	@Autowired
	private UserService userService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private TagService tagService;
	@Autowired
	private BriefTaskMessage briefTaskMessage;
	@Autowired
	private ScheduledTaskService scheduledTaskService;
	@Autowired
	private BizServiceTreeService bizServiceTreeService;
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private XiaoPMessageTService.Iface xiaoPMessageTService;

	// 任务状态
	private static final byte TASK_TODO = 0;
	private static final byte TASK_DOING = 1;
	private static final byte TASK_DONE = 2;

	// 没有标签的任务的分组名称
	private static final String NAME_FOR_NO_TAG = "未分组";

	/**
	 * 添加消息
	 */
	@RequestMapping(value = "/addMessage.do")
	@ResponseBody
	public Result addMessage(@RequestBody MessageProxyVO messageProxyVO) {
		Result result = new Result();
		if (messageProxyVO == null) {
			return result;
		}
		MessageVO messageVO = messageProxyVO.getMessage();
		final long operateUserId = UserHolder.getUserId();
		messageVO.setUserId(operateUserId);
		try {
			final MessageDTO messageDTO = MessageConvertor.convertVo2Dto(messageVO);

			messageService.addMessage(messageDTO);

			// 更新查询任务详情页最新时间
			messageService.updateReadTime(messageVO.getTaskId(), operateUserId);
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					messageService.sentMessageToXiaoP(messageDTO, operateUserId);
//
//					briefTaskMessage.updateTasksAndMessageToBizwork();
//				}
//			}).start();
		} catch (Exception e) {
			logger.error("addMessage ERROR", e);
			result.setErrorMsg("addMessage ERROR");
		}
		return result;
	}

	private String addMessage(final MessageVO messageVO) {
		try {
			final MessageDTO messageDTO = MessageConvertor.convertVo2Dto(messageVO);

			messageService.addMessage(messageDTO);

			// 更新查询任务详情页最新时间
			messageService.updateReadTime(messageVO.getTaskId(), messageVO.getUserId());
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					messageService.sentMessageToXiaoP(messageDTO, messageVO.getUserId());
//
//					briefTaskMessage.updateTasksAndMessageToBizwork();
//				}
//			}).start();
		} catch (Exception e) {
			logger.error("addMessage ERROR", e);
			return "addMessage ERROR";
		}
		return null;
	}

	/**
	 * @description 更新任务详情
	 * @param taskVo
	 *            更新后的任务Vo
	 * @return
	 */
	@RequestMapping(value = "/updateTask.do")
	@ResponseBody
	public Result updateTask(@RequestBody TaskProxyVo taskProxyVo) {
		Result result = new Result();
		if (taskProxyVo == null || taskProxyVo.getTask() == null) {
			result.setSuccess(Result.FAILED);
			return result;
		}
		com.sogou.bizwork.task.api.core.task.po.TaskVo taskVo = taskProxyVo.getTask();
		TaskValidator.validateTask(result, taskVo);
		long operateUser = UserHolder.getUserId();
		TaskDTO taskDTOPre = null;
		try {
			if (taskVo.getTaskId() == 0) {
				result.setErrorMsg("task id can not be null");
				return result;
			}
			// 判断数据是否变化
			taskDTOPre = taskService.queryTaskInfo(taskVo.getTaskId());
			com.sogou.bizwork.task.api.core.task.po.TaskVo taskVoPre = TaskConvertor.convertTaskDto2Vo(taskDTOPre);
			if (!TaskUtil.compareTaskVo(taskVoPre, taskVo)) {
				taskVo.setStatus(taskVoPre.getStatus());
				// 任务负责人发生变更,自动更新状态为“待跟进”, 同时将原负责人加为关注人
				if (taskVoPre.getChargeUser() != taskVo.getChargeUser()) {
					taskVo.setStatus(TASK_TODO);

					List<FollowUserVo> followUsers = taskVo.getFollowUsers();
					FollowUserVo followUserVo = new FollowUserVo(taskVoPre.getChargeUser(), 0);
					if (!followUsers.contains(followUserVo)) {
						followUsers.add(followUserVo);
					}
					taskVo.setFollowUsers(followUsers);
				}

				TaskDTO taskDTO = TaskConvertor.convertTaskVo2Dto(taskVo);
				boolean res = taskService.updateTask(taskDTO, operateUser);
				if (!res) {
					result.setErrorMsg("更新失败！");
					return result;
				}
				// 任务负责人发生变更，给所有人发送邮件
				if (taskVoPre.getChargeUser() != taskVo.getChargeUser()) {
					String response = taskService.sendMailChargeUserChange(operateUser, taskDTO);
					if (!response.equals(""))
						result.setErrorMsg("发送邮件失败");
				}
			}

			// 判断tag是否变化
			List<TagVo> tagVos = taskVo.getTags() == null ? new ArrayList<TagVo>() : taskVo.getTags();
			TagResult tagResult = tagTService.getTagsByTaskIdAndUserId(taskVo.getTaskId(), operateUser);
			List<TagVo> tagVosPre = TagConvertor.convertTos2Vos(tagResult.getTagTo());

			List<List<TagVo>> TagVosDeleteAndAdd = getTagVosDeleteAndAdd(tagVos, tagVosPre);
			List<TagVo> tagVosDelete = TagVosDeleteAndAdd.get(0);
			List<TagVo> tagVosAdd = TagVosDeleteAndAdd.get(1);
			long taskId = taskVo.getTaskId();
			if (!CollectionUtils.isEmpty(tagVosDelete)) {
				for (TagVo tagVo : tagVosDelete) {
					List<TagDto> tagDtos = tagService.getTagByUserIdAndTagName(operateUser, tagVo.getName());
					tagService.deleteTagsByTaskIdAndTagId(taskId, tagDtos.get(0).getId());
				}
			}
			if (!CollectionUtils.isEmpty(tagVosAdd)) {
				for (TagVo tagVo : tagVosAdd) {
					TagDto tagDto = new TagDto();
					tagDto.setUserId(operateUser);
					tagDto.setName(tagVo.getName());
					tagDto.setColor(tagVo.getColor());
					tagService.addTaskTagByName(taskId, tagDto);
				}

			}
		} catch (ApiTException ate) {
			logger.error("updateTask for {} error", taskVo.getTaskId(), ate);
			result.setErrorMsg("updateTask Error");
		} catch (TException te) {
			logger.error("update task for {} Thrift error", taskVo.getTaskId(), te);
			result.setErrorMsg("updateTask Error");
		}

		if (StringUtils.isNotBlank(taskVo.getMessageContent())) {
			MessageVO messageVO = new MessageVO();
			messageVO.setContent(taskVo.getMessageContent());
			messageVO.setUserId(operateUser);
			messageVO.setTaskId(taskVo.getTaskId());

			String res = addMessage(messageVO);
			if (StringUtils.isNotBlank(res)) {
				result.setErrorMsg(res);
			}
		}
		scheduledTaskService.deleteTaskByTaskId(taskVo.getTaskId());
		if (taskVo.getScheduled() != null) {
			scheduledTaskService.addScheduledTask(taskVo, operateUser);
		}
		if (taskDTOPre.getScoreId() != 0) {
			scoreService.deleteScoreByScoreId(taskDTOPre.getScoreId());
		}
		if (taskVo.getScore() != null) {
			ScoreHistory scoreHistory = taskService.getScoreHistory(taskVo, Integer.valueOf(operateUser + ""));
			scoreService.addScore(scoreHistory);
			taskService.updateScoreIdByTaskId(taskVo.getTaskId(), scoreHistory.getScoreId());
		}
		return result;
	}

	private List<List<TagVo>> getTagVosDeleteAndAdd(List<TagVo> tagVos1, List<TagVo> tagVos2) {
		List<List<TagVo>> res = new ArrayList<List<TagVo>>();
		List<TagVo> tagVosDelete = new ArrayList<TagVo>();
		List<TagVo> tagVosAdd = new ArrayList<TagVo>();
		res.add(tagVosDelete);
		res.add(tagVosAdd);
		if (tagVos1 == null && tagVos2 == null)
			return res;
		else if (tagVos2 == null) {
			tagVosAdd = tagVos1;
			return res;
		} else if (tagVos1 == null) {
			tagVosDelete = tagVos2;
			return res;
		} else {
			Set<TagVo> set = new HashSet<TagVo>();
			set.addAll(tagVos1);
			for (TagVo tagVo : tagVos2) {
				if (!set.contains(tagVo)) {
					tagVosDelete.add(tagVo);
				}
			}
			set.clear();
			set.addAll(tagVos2);
			for (TagVo tagVo : tagVos1) {
				if (!set.contains(tagVo)) {
					tagVosAdd.add(tagVo);
				}
			}
		}
		return res;
	}

	/**
	 * 更新任务状态
	 * 
	 * @param request
	 * @param taskId
	 *            任务ID
	 * @param statusName
	 *            状态名称(ToDO, DOING, DONE)
	 * @return
	 */
	@RequestMapping(value = "/updateTaskStatus.do")
	@ResponseBody
	public Result updateTaskStatus(@RequestBody String param) {
		Result result = new Result();
		if (StringUtils.isEmpty(param)) {
			result.setErrorMsg("参数为空");
			return result;
		}
		JSONObject json = JSONObject.fromObject(param);
		long taskId = Long.parseLong(json.getString("taskId"));
		String statusName = json.getString("statusName");

		long operateUser = UserHolder.getUserId();

		byte status = -2;
		if (statusName == null || StringUtils.isEmpty(statusName)) {
			result.setErrorMsg("task status can not be null or empty");
			return result;
		}

		if (statusName.equalsIgnoreCase("TODO")) {
			status = TASK_TODO;
		} else if (statusName.equalsIgnoreCase("DOING")) {
			status = TASK_DOING;
		} else if (statusName.equalsIgnoreCase("DONE")) {
			status = TASK_DONE;
		} else {
			result.setErrorMsg("invalid task status");
			return result;
		}

		try {
			TaskDTO taskDTO = taskService.queryTaskInfo(taskId);
			if (taskDTO == null) {
				result.setErrorMsg("invalid task id");
				return result;
			}
			if (taskDTO.getStatus() == status) {
				result.setErrorMsg("task status is the same");
				return result;
			}
			taskDTO.setStatus(status);
			// if (taskDTO.)
			taskService.updateTask(taskDTO, operateUser);

			// 任务状态发生变更，给所有人发送邮件
			 String response = taskService.sendMailStatusChange(operateUser,
			 taskDTO);

			//briefTaskMessage.updateTasksAndMessageToBizwork();
			// if (!response.equals(""))
			// result.setErrorMsg("发送邮件失败");
			Map<String, Boolean> params = new HashMap<String, Boolean>();
			if (taskDTO.getScoreStatus() == 0 && status == TASK_DONE && taskDTO.getScoreId() > 0) {
				params.put("needScoreConfirm", true);
			} else {
				params.put("needScoreConfirm", false);
			}
			result.setData(params);
		} catch (Exception e) {
			logger.error("updateTaskStatus Error", e);
			result.setErrorMsg("updateTaskStatus Error");
		}

		return result;
	}

	@RequestMapping(value = "/confirmScore.do")
	@ResponseBody
	public Result confirmScore(@RequestBody Map<String, String> params)
			throws com.sogou.bizwork.api.exception.ApiTException, TException {
		Result result = new Result();
		taskService.updateScoreStatusByTaskId(Long.parseLong(params.get("taskId")), ScoreStatus.WAIT_CONFIRM);
		TaskDTO taskDTO = taskService.queryTaskInfo(Long.parseLong(params.get("taskId")));
		UserDTO userDTO = userService.getUserByEmployeeId(Integer.parseInt(taskDTO.getChargeUser() + ""));
		ScoreHistory scoreHistory = scoreService.getScoreByScoreId(taskDTO.getScoreId());
		UserDTO givePeopleDTO = userService.getUserByEmployeeId(scoreHistory.getGivePeopleId());
		List<XiaoPMessage> xiaoPMessages = new ArrayList<XiaoPMessage>();
		XiaoPMessage xiaoPMessage = new XiaoPMessage();
		xiaoPMessage.setType("0");
		xiaoPMessage.setReceivers(givePeopleDTO.getUserName());
		xiaoPMessage.setContent(userDTO.getChineseName() + "已完成您发起的积分任务：【" + taskDTO.getTaskName() + " 】。将得到“"
				+ scoreHistory.getScoreType() + "”类型的积分" + scoreHistory.getScore()
				+ "分。点击链接：http://api.task.bizwork.sogou/task/confirmScore.action?" + "taskId=" + taskDTO.getId()
				+ "，即可确认给分。如需修改分数，请在PC端任务详情中修改。（PC端确认分数入口。系统：bizwork首页-系统服务-部门通用-bizwork任务管理。菜单：部门积分）");
		xiaoPMessages.add(xiaoPMessage);
		xiaoPMessageTService.sendMessageToXiaoP(xiaoPMessages);
		return result;
	}

	@RequestMapping(value = "/confirmScore.action")
	@ResponseBody
	public void confirmScoreAction(HttpServletRequest request, HttpServletResponse response, Writer writer)
			throws IOException, com.sogou.bizwork.api.exception.ApiTException, TException {
		String taskId = request.getParameter("taskId");
		TaskDTO taskDTO = taskService.queryTaskInfo(Long.parseLong(taskId));
		String status = request.getParameter("status");

		if (StringUtils.isNotBlank(status)) {
			response.sendRedirect("/view/confirmScore.jsp?status=" + status + "&taskId=" + taskId);
		}
		if (StringUtils.isNotBlank(taskId)) {
			ScoreHistory scoreHistory = scoreService.getScoreByScoreId(taskDTO.getScoreId());
			if (scoreHistory.getStatus() == 1) {
				scoreService.updateScoreIdByScoreId(scoreHistory.getScoreId(), ScoreStatus.AGREE_SCORE);
				scoreService.getScoreByScoreId(Integer.parseInt(taskId));
				List<XiaoPMessage> xiaoPMessages = new ArrayList<XiaoPMessage>();
				XiaoPMessage xiaoPMessage = new XiaoPMessage();
				xiaoPMessage.setType("0");
				UserDTO userDTO = userService.getUserByEmployeeId(Integer.parseInt(taskDTO.getChargeUser() + ""));
				UserDTO leaderDTO = userService.getUserByEmployeeId(userDTO.getLeaderId());
				UserDTO givePeopleDTO = userService.getUserByEmployeeId(scoreHistory.getGivePeopleId());
				xiaoPMessage.setReceivers(leaderDTO.getUserName());
				xiaoPMessage.setContent(userDTO.getChineseName() + "已完成" + givePeopleDTO.getChineseName() + "发起的积分任务：【"
						+ taskDTO.getTaskName() + "】" + "已得到“" + scoreHistory.getScoreType() + "”类型的积分"
						+ scoreHistory.getScore() + "分。请知晓");
				xiaoPMessages.add(xiaoPMessage);
				xiaoPMessageTService.sendMessageToXiaoP(xiaoPMessages);
				response.sendRedirect(
						"/view/confirmScore.jsp?status=" + scoreHistory.getStatus() + "&taskId=" + taskId);
			} else if (scoreHistory.getStatus() == 2) {
				response.sendRedirect(
						"/view/confirmScore.jsp?status=" + scoreHistory.getStatus() + "&taskId=" + taskId);
			}
		}
	}

	@RequestMapping(value = "/cancelScore.action")
	@ResponseBody
	public void cancelScoreAction(HttpServletRequest request, HttpServletResponse response, Writer writer)
			throws IOException, com.sogou.bizwork.api.exception.ApiTException, TException {
		String taskId = request.getParameter("taskId");
		TaskDTO taskDTO = taskService.queryTaskInfo(Long.parseLong(taskId));
		if (StringUtils.isNotBlank(taskId)) {

			ScoreHistory scoreHistory = scoreService.getScoreByScoreId(taskDTO.getScoreId());
			if (scoreHistory.getStatus() == 2) {
				scoreService.updateScoreIdByScoreId(scoreHistory.getScoreId(), ScoreStatus.WAIT_CONFIRM);
				scoreService.getScoreByScoreId(Integer.parseInt(taskId));
				List<XiaoPMessage> xiaoPMessages = new ArrayList<XiaoPMessage>();
				XiaoPMessage xiaoPMessage = new XiaoPMessage();
				UserDTO userDTO = userService.getUserByEmployeeId(Integer.parseInt(taskDTO.getChargeUser() + ""));
				UserDTO leaderDTO = userService.getUserByEmployeeId(userDTO.getLeaderId());
				UserDTO givePeopleDTO = userService.getUserByEmployeeId(scoreHistory.getGivePeopleId());
				xiaoPMessage.setReceivers(leaderDTO.getUserName());
				xiaoPMessage.setType("0");
				xiaoPMessage.setContent(givePeopleDTO.getChineseName() + "已撤回由" + userDTO.getChineseName() + "负责的积分任务：【"
						+ taskDTO.getTaskName() + "】" + "撤回“" + scoreHistory.getScoreType() + "”类型积分"
						+ scoreHistory.getScore() + "分。请知晓");
				xiaoPMessages.add(xiaoPMessage);
				xiaoPMessageTService.sendMessageToXiaoP(xiaoPMessages);
			}
			response.sendRedirect("/view/confirmScore.jsp?status=5");
		}
	}

	/**
	 * 更新标签名称
	 * 
	 * @param taskId
	 *            任务ID
	 * @param tagNameSrc
	 *            标签更新前名称
	 * @param tagNameDes
	 *            标签更新后名称
	 * @return
	 */
	@RequestMapping(value = "/updateTagName.do")
	@ResponseBody
	public Result updateTagName(@RequestBody String param) {
		Result result = new Result();
		if (StringUtils.isEmpty(param)) {
			result.setErrorMsg("参数为空");
			return result;
		}
		JSONObject json = JSONObject.fromObject(param);
		long taskId = Long.parseLong(json.getString("taskId"));
		String tagNameSrc = json.getString("tagNameSrc");
		String tagNameDes = json.getString("tagNameDes");
		String tagColorDes = json.getString("colorDes");

		long operateUser = UserHolder.getUserId();
		if (StringUtils.isEmpty(tagNameSrc) || StringUtils.isEmpty(tagNameDes)) {
			result.setErrorMsg("Tag prameters can not be null or empty");
			return result;
		}
		if (tagNameSrc.equals(tagNameDes)) {
			result.setErrorMsg("tagNameSrc and tagNameDes can not be the same value");
			return result;
		}

		try {
			if (tagNameSrc.equals(NAME_FOR_NO_TAG)) {
				if (StringUtils.isEmpty(tagColorDes)) {
					result.setErrorMsg("Tag color can not be null or empty");
					return result;
				}
				// add tag
				TagDto tagDto = new TagDto();
				tagDto.setUserId(operateUser);
				tagDto.setName(tagNameDes);
				tagDto.setColor(tagColorDes);
				tagService.addTaskTagByName(taskId, tagDto);
			} else if (tagNameDes.equals(NAME_FOR_NO_TAG)) {
				// 获取所有指定User指定task的所有tag
				List<TagDto> tagDtos = tagService.getTagsByTaskIdAndUserId(taskId, operateUser);
				for (TagDto tagDto : tagDtos) {
					tagService.deleteTagsByTaskIdAndTagId(taskId, tagDto.getId());
				}
			} else {
				// 删除旧的
				List<TagDto> tagDtos = tagService.getTagByUserIdAndTagName(operateUser, tagNameSrc);
				tagService.deleteTagsByTaskIdAndTagId(taskId, tagDtos.get(0).getId());
				// add 新的
				TagDto tagDto = new TagDto();
				tagDto.setUserId(operateUser);
				tagDto.setName(tagNameDes);
				tagDto.setColor(tagColorDes);
				tagService.addTaskTagByName(taskId, tagDto);
			}
		} catch (Exception e) {
			logger.error("updateTagName ERROR", e);
			result.setErrorMsg("updateTagName ERROR");
		}

		return result;
	}

	public static void main(String[] args) {
		String json = "{\"sid\":\"bizwork_task_99bf56d9-5a38-40c0-8d01-0bfb128ad30b\",\"task\":{\"createUser\":12,\"taskName\":\"周期性任务\",\"chargeUser\":3113,\"followUsers\":[{\"id\":3113,\"type\":0}],\"scheduled\":{\"periodFrequency\":1,\"periodSpecific\":1,\"periodValid\":1},\"tags\":[],\"description\":\"周期性任务\"}}";
		try {
			JSONUtil.deserialize(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description 添加任务
	 * @param taskVo
	 * @return
	 */
	@RequestMapping(value = "/addTask.do")
	@ResponseBody
	public Result addTask(@RequestBody TaskProxyVo taskProxyVo) {

		long operateUserId = UserHolder.getUserId();
		Result result = new Result();
		if (taskProxyVo == null || taskProxyVo.getTask() == null) {
			result.setErrorMsg("Invalid Parameter");
			return result;
		}
		com.sogou.bizwork.task.api.core.task.po.TaskVo taskVo = taskProxyVo.getTask();
		com.sogou.bizwork.task.api.core.task.po.TaskVo scheduledTask = new com.sogou.bizwork.task.api.core.task.po.TaskVo();
		scheduledTask = convertWebToCore(taskVo);
		scheduledTask.setCreateUser(operateUserId);  // 新建一个对象
		ScoreHistory scoreHistory = taskService.getScoreHistory(scheduledTask, Integer.valueOf(operateUserId + ""));
        if (scoreHistory.getScore() != 0){
		scoreService.addScore(scoreHistory);   //new socre task
        }
		scheduledTask.setScoreId(scoreHistory.getScoreId());
		com.sogou.bizwork.task.api.core.task.po.Result res = scheduledTaskService.addTask(scheduledTask, operateUserId);

		scheduledTask.setTaskId(Long.parseLong(res.getData() + ""));
		scheduledTaskService.addScheduledTask(scheduledTask, operateUserId);
		return result;
	}

	private com.sogou.bizwork.task.api.core.task.po.TaskVo convertWebToCore(
			com.sogou.bizwork.task.api.core.task.po.TaskVo taskVo) {
		com.sogou.bizwork.task.api.core.task.po.TaskVo scheduled = new com.sogou.bizwork.task.api.core.task.po.TaskVo();
		scheduled.setAttachment(taskVo.getAttachment());
		scheduled.setChargeUser(taskVo.getChargeUser());
		scheduled.setChargeUserName(taskVo.getChargeUserName());
		scheduled.setCreateTime(taskVo.getCreateTime());
		scheduled.setCreateUser(taskVo.getCreateUser());
		scheduled.setDescription(taskVo.getDescription());
		scheduled.setEndTime(taskVo.getEndTime());
		scheduled.setFileList(taskVo.getFileList());
		scheduled.setFollowUsers(taskVo.getFollowUsers());
		scheduled.setMessage(taskVo.getMessage());
		scheduled.setMessageContent(taskVo.getMessageContent());
		scheduled.setScheduled(taskVo.getScheduled());
		scheduled.setStartTime(taskVo.getStartTime());
		scheduled.setStatus(taskVo.getStatus());
		scheduled.setTags(taskVo.getTags());
		scheduled.setTaskId(taskVo.getTaskId());
		scheduled.setTaskName(taskVo.getTaskName());
		scheduled.setScore(taskVo.getScore());
		scheduled.setScoreId(taskVo.getScoreId());
		return scheduled;
	}

	@RequestMapping("insertMsg.do")
	@ResponseBody
	public Result insertMessage() {
		Result result = new Result();
	//	briefTaskMessage.updateTasksAndMessageToBizwork();
		// long operateUser = 3112;
		// TaskDTO taskDTO = new TaskDTO();
		// List<TaskFollowDTO> followUsers = new ArrayList<TaskFollowDTO>();
		// TaskFollowDTO tf1 = new TaskFollowDTO();
		// tf1.setType(1);
		// tf1.setFollowUser(1);
		// tf1.setTaskId(999);
		// TaskFollowDTO tf2 = new TaskFollowDTO();
		// tf2.setTaskId(0);
		// tf2.setFollowUser(3113);
		// tf2.setTaskId(999);
		// followUsers.add(tf1);
		// followUsers.add(tf2);
		// taskDTO.setFollowUsers(followUsers);
		// taskDTO.setChargeUser(3115);
		// taskService.addTask(taskDTO, operateUser);
		return result;
	}

	@RequestMapping("/deleteTask.do")
	@ResponseBody
	public Result deleteTask(@RequestBody String param) {
		Result result = new Result();
		if (StringUtils.isEmpty(param)) {
			result.setErrorMsg("参数为空");
			return result;
		}
		JSONObject json = JSONObject.fromObject(param);
		long taskId = Long.parseLong(json.getString("taskId"));
		try {
			TaskDTO taskDTO = taskService.queryTaskInfo(taskId);
			if (taskDTO == null)
				throw new BizException("TaskId=" + taskId + " is not Exsits");

			long operateUser = UserHolder.getUserId();
			// 删除任务，给所有人发送邮件
			String response = taskService.sendMailDeleteTask(operateUser, taskDTO);
			if (!response.equals(""))
				result.setErrorMsg("发送邮件失败");

			taskService.deleteTask(taskDTO, operateUser);
			scheduledTaskService.setTaskToDelete(taskId);
			scoreService.setScoreToDelete(taskId);
		} catch (Exception e) {
			logger.error("deleteTask error, taskId=" + taskId, e);
			result.setErrorMsg("deleteTask error, taskId=" + taskId);
		}

		return result;
	}

	/**
	 * 删除当前用户对指定任务的关注
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("/deleteFollowUser.do")
	@ResponseBody
	public Result deleteFollowUser(@RequestBody String param) {
		Result result = new Result();
		if (StringUtils.isEmpty(param)) {
			result.setErrorMsg("参数为空");
			return result;
		}
		JSONObject json = JSONObject.fromObject(param);
		long taskId = Long.parseLong(json.getString("taskId"));

		TaskDTO taskDTO = taskService.queryTaskInfo(taskId);
		if (taskDTO == null)
			throw new BizException("TaskId=" + taskId + " is not Exsits");

		long operateUser = UserHolder.getUserId();
		List<TaskFollowDTO> followUsers = taskDTO.getFollowUsers();
		TaskFollowDTO operateUserDTO = new TaskFollowDTO(taskId, operateUser, 0);

		if (!followUsers.contains(operateUserDTO))
			throw new BizException("operateUser=" + operateUser + " is not followUsers of TaskId=" + taskId);

		taskService.deleteFollowUser(taskDTO, operateUserDTO);

		return result;
	}

	/**
	 * 获取任务详情
	 * 
	 * @param taskId
	 * @return 任务基本信息、用户标签
	 */
	@RequestMapping(value = "getTaskDetail.do")
	@ResponseBody
	public Result getTaskDetail(HttpServletRequest request, @RequestBody String param) {
		Result result = new Result();
		if (StringUtils.isEmpty(param)) {
			result.setErrorMsg("参数为空");
			return result;
		}
		JSONObject json = JSONObject.fromObject(param);
		long taskId = Long.parseLong(json.getString("taskId"));
		try {
			TaskDTO taskDTO = taskService.queryTaskInfo(taskId);
			if (taskDTO.getScoreTypeId() > 0) {
				Score score = new Score();
				score.setTypeId(taskDTO.getScoreTypeId());
				score.setValue(taskDTO.getScoreValue());
				taskDTO.setScore(score);
			}
			long userId = UserHolder.getUserId();
			TagResult tagResult = tagTService.getTagsByTaskIdAndUserId(taskId, userId);
			if (!tagResult.status) {
				result.setErrorMsg(tagResult.getErrorCode().getMessage());
				return result;
			}

			com.sogou.bizwork.task.api.core.task.po.TaskVo taskVo = TaskConvertor.convertTaskDto2Vo(taskDTO);
			taskVo.setChargeUserName(getUserChineseNameById(taskDTO.getChargeUser()));
			List<AttachmentInfo> fileList = TaskUtil.getFileLists(taskDTO.getAttachment(),
					getFileDownloadPath(request));
			taskVo.setFileList(fileList);
			for (FollowUserVo followUserVo : taskVo.getFollowUsers()) {
				if (followUserVo.getType() == 0)
					followUserVo.setName(getUserChineseNameById(followUserVo.getId()));
				if (followUserVo.getType() == 1)
					followUserVo.setName(getGroupNameById(followUserVo.getId()));
			}
			List<TagTo> tagTos = tagResult.getTagTo();
			if (CollectionUtils.isNotEmpty(tagTos)) {
				taskVo.setTags(TagConvertor.convertTos2Vos(tagTos));
			}
			ScheduledTaskBo scheduledTaskBo = scheduledTaskService.getScheduledTaskByTaskId(taskVo.getTaskId());
			if (scheduledTaskBo != null) {
				Scheduled scheduled = new Scheduled();
				scheduled.setPeriodFrequency(scheduledTaskBo.getPeriodFrequency());
				scheduled.setPeriodSpecific(scheduledTaskBo.getPeriodSpecific());
				scheduled.setPeriodValid(scheduledTaskBo.getPeriodValid());
				taskVo.setScheduled(scheduled);
			}
			result.setData(taskVo);

			// 更新查询任务详情页最新时间
			messageService.updateReadTime(taskId, userId);
		} catch (ApiTException e) {
			logger.error(String.format("query Task Detail ERROR, taskId=%d, ", taskId), e);
			result.setErrorMsg("系统异常");
		} catch (TException e) {
			logger.error("query Task Detail THRIFT Exception", e);
			result.setErrorMsg("系统异常");
		}
		return result;
	}

	/**
	 * 
	 * @param viewType
	 *            视图类型，0:状态视图，1：标签视图，2：负责人视图（只有在我关注的任务界面有该视图）
	 * @param userType
	 *            用户职责范围类型：0：我负责的任务，1：我关注的任务
	 * @return
	 */
	@RequestMapping(value = "/getTasks.do")
	@ResponseBody
	public Result getTasks(@RequestBody String param) {
		Result result = new Result();
		if (StringUtils.isEmpty(param)) {
			result.setErrorMsg("参数为空");
			return result;
		}

		JSONObject json = JSONObject.fromObject(param);
		int viewType = json.getInt("viewType"); // 负责人还是关注人
		int userType = json.getInt("userType");

		long userId = UserHolder.getUserId();
		try {
			List<TaskDTO> taskDTOs = taskService.queryTasksWithUserType(userId, userType);
			List<TaskDTO> demoTask = taskService.queryDemos(userId);

			if (CollectionUtils.isEmpty(taskDTOs) && userType == 0 && CollectionUtils.isEmpty(demoTask)) {// 添加一个示例任务
				taskDTOs = new ArrayList<TaskDTO>();
				TaskDTO taskSample = new TaskDTO();
				taskSample.setChargeUser(userId);
				taskSample.setCreateUser(userId);
				taskSample.setStartTime(TaskConvertor.getCurrentDateStr() + " 00:00:00");
				taskSample.setEndTime(TaskConvertor.getCurrentDateStr() + " 00:00:00");
				taskSample.setTaskName("示例任务");
				taskSample.setDescription("示例任务,请自行删除");
				taskSample.setStatus(TASK_TODO);
				List<TaskFollowDTO> followUsersSample = new ArrayList<TaskFollowDTO>();
				followUsersSample.add(new TaskFollowDTO(userId, 0));
				taskSample.setFollowUsers(followUsersSample);

				long taskId = taskService.addTask(taskSample, userId);
				TagDto tagDto = new TagDto();
				tagDto.setUserId(userId);
				tagDto.setName("示例任务");
				tagDto.setColor("#59C203");
				tagService.addTaskTagByName(taskId, tagDto);

				TaskDTO taskDTO = taskService.queryTaskInfo(taskId);
				List<TagDto> tagDTos = tagService.getTagsByTaskIdAndUserId(taskId, userId);
				if (CollectionUtils.isNotEmpty(tagDTos)) {
					taskDTO.setTags(tagDTos);
				}
				taskDTOs.add(taskDTO);
			}
			// 获取人员id name键值对
			Set<Long> userIdSet = new HashSet<Long>();
			for (TaskDTO taskDTO : taskDTOs) {
				com.sogou.bizwork.task.api.core.task.po.TaskVo taskVo = TaskConvertor.convertTaskDto2Vo(taskDTO);
				for (FollowUserVo followUserVo : taskVo.getFollowUsers()) {
					if (followUserVo.getType() == 0)
						userIdSet.add(followUserVo.getId());
				}
			}
			Map<Long, String> userNameMap = getUserChineseNameByIds(new ArrayList<Long>(userIdSet));
			// 获取任务消息键值对
			List<MessageDTO> messages = messageService.queryMessagesWithUserType(userId, userType);
			Map<Long, MessageVO> messageMap = new HashMap<Long, MessageVO>();
			for (MessageDTO messageDTO : messages) {
				MessageVO messageVO = MessageConvertor.convertDto2Vo(messageDTO);
				messageVO.setUserName(userNameMap.get(messageVO.getUserId()));
				messageMap.put(messageDTO.getTaskId(), messageVO);
			}

			List<TaskFrameVo> taskFrames = new ArrayList<TaskFrameVo>();
			if (TaskDTO.VIEW_TYPE_STATUS.equals(viewType)) {// 切换状态视图
				getViewOfStatusTask(taskDTOs, messageMap, userNameMap, taskFrames);
			} else if (TaskDTO.VIEW_TYPE_TAG.equals(viewType)) {// 切换标签视图
				getViewOfTagTask(taskDTOs, messageMap, userNameMap, taskFrames, userType);
			} else if (TaskDTO.VIEW_TYPE_CHARGEUSER.equals(viewType)) {// 负责人视图
				getViewOfChargeUserTask(taskDTOs, messageMap, userNameMap, taskFrames, 2);
			}

			result.setData(taskFrames);
//			new Thread(new Runnable() {   // 开启一个新线程更新messagebrief数据库
//
//				@Override
//				public void run() {
//					briefTaskMessage.updateTasksAndMessageToBizwork(); // update
//																		// task
//																		// and
//																		// message
//																		// to
//																		// bizwork!
//				}
//			}).start();

		} catch (ApiTException e) {
			logger.error(
					String.format("getTasks ERROR, userId=%d, viewType=%d, userType=%d", userId, viewType, userType),
					e);
			result.setErrorMsg("系统异常");
		} catch (TException e) {
			logger.error("query Task THRIFT Exception", e);
			result.setErrorMsg("系统异常");
		}
		return result;
	}

	@RequestMapping(value = "uploadFile.do")
	@ResponseBody
	public Result uploadFile(HttpServletRequest request, String fileName) {
		Result result = new Result();
		MultipartHttpServletRequest mutipartHttpServletRequest = (MultipartHttpServletRequest) request;
		// 获得文件：
		MultipartFile file = mutipartHttpServletRequest.getFile("file");

		if (file == null || file.isEmpty()) {
			result.setErrorMsg("上传文件为空");
			return result;
		}

		StringBuffer fileDownload = getFileDownloadPath(request);

		try {
			String fid = DFSUtils.writeToDFS(file.getInputStream());
			if (StringUtils.isNotEmpty(fid)) {
				fileDownload.append("filePath=" + fid).append("&fileName=" + fileName);
				logger.info("文件下载路径：" + fileDownload.toString());
				AttachmentInfo info = new AttachmentInfo(fileDownload.toString(), null);
				info.setFid(fid);
				result.setData(info);
			} else {
				result.setErrorMsg("上传文件失败");
			}
		} catch (IOException e) {
			logger.error("uploadFile Error", e);
			result.setErrorMsg("上传文件出错");
		}

		return result;
	}

	/**
	 * 文件下载
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/downloadFile.do")
	public void download(String filePath, String fileName, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		if (StringUtils.isEmpty(fileName))
			fileName = "default";
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
		try {
			InputStream inputStream = DFSUtils.readFromDFS(filePath);

			OutputStream os = response.getOutputStream();
			byte[] b = new byte[1024];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}

			os.close();
			inputStream.close();
		} catch (IOException e) {
			logger.error("downloadFile ERROR, filePath=" + filePath + ", fileName=" + fileName, e);
		} catch (DFSException e) {
			logger.error("读取DFS文件流失败,fid = " + filePath);
		}
	}

	/**
	 * 发送提醒邮件到任务负责人
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/sendMail.do")
	@ResponseBody
	public Result sendMail(@RequestBody String param) {
		Result result = new Result();
		if (StringUtils.isEmpty(param)) {
			result.setErrorMsg("参数为空");
			return result;
		}
		JSONObject json = JSONObject.fromObject(param);
		long taskId = Long.parseLong(json.getString("taskId"));
		long operateUserId = UserHolder.getUserId();
		try {
			TaskDTO taskDTO = taskService.queryTaskInfo(taskId);
			if (taskDTO == null) {
				result.setErrorMsg("invalid task id");
				return result;
			}
			String response = taskService.sendMailAlert(operateUserId, taskDTO);
			if (!response.equals(""))
				result.setErrorMsg("发送失败");
		} catch (Exception e) {
			logger.error("sendMail for {} error", taskId, e);
//			e.printStackTrace();
			result.setErrorMsg("sendMail Error");
		}

		return result;
	}

	/**
	 * 根据用户id获取用户名
	 */
	private String getUserChineseNameById(long userId) throws ApiTException, TException {
		UserDTO user = userService.getUserById((int) userId);
		if (null == user)
			throw new BizException("该用户信息不存在");
		return user.getChineseName();
	}

	/**
	 * 根据id获取Groupname
	 */
	private String getGroupNameById(long groupId) throws ApiTException, TException {
		GroupDTO group = groupService.getGroupById((int) groupId);
		if (null == group)
			throw new BizException("该组信息不存在");
		return group.getGroupName();
	}

	/**
	 * 根据id list获取Username键值对
	 */
	private Map<Long, String> getUserChineseNameByIds(List<Long> userIds) throws ApiTException, TException {
		List<UserDTO> users = userService.getUsersByIds(userIds);
		Map<Long, String> userNameMap = new HashMap<Long, String>();
		for (UserDTO user : users) {
			userNameMap.put((long) user.getEmployeeId(), user.getChineseName());
		}
		return userNameMap;
	}

	/**
	 * 根据用户id和标签名称获取tag id
	 */
	private long getTagIdByUserIdAndTagName(long userId, String tagName) throws ApiTException, TException {
		List<TagDto> tagDtos = tagService.getTagByUserIdAndTagName(userId, tagName);
		if (null == tagDtos || tagDtos.get(0) == null)
			throw new BizException("该标签信息不存在");
		return tagDtos.get(0).getId();
	}

	/**
	 * 将原始数据taskTos转换成状态视图结构化数据对象
	 * 
	 * @param taskTos
	 * @param taskFrames
	 * @throws TException
	 * @throws ApiTException
	 */
	private void getViewOfStatusTask(List<TaskDTO> taskDTOs, Map<Long, MessageVO> messageMap,
			Map<Long, String> userNameMap, List<TaskFrameVo> taskFrames) throws ApiTException, TException {
		TaskFrameVo taskFrameOfTODO = new TaskFrameVo("TODO");
		TaskFrameVo taskFrameOfDOING = new TaskFrameVo("DOING");
		TaskFrameVo taskFrameOfDONE = new TaskFrameVo("DONE");

		for (TaskDTO taskDTO : taskDTOs) {
			com.sogou.bizwork.task.api.core.task.po.TaskVo taskVo = TaskConvertor.convertTaskDto2Vo(taskDTO);
			taskVo.setStartTime(TaskUtil.parseDate(taskDTO.getStartTime()));
			taskVo.setEndTime(TaskUtil.parseDate(taskDTO.getEndTime()));
			String userName = getUserChineseNameById(taskVo.getChargeUser());
			if (userName != null) {
				taskVo.setChargeUserName(userName);
			}

			// 加入未读消息
			if (messageMap.containsKey(taskDTO.getId())) {
				String getLastOpenTime = taskDTO.getLastOpenTime();
				MessageVO messageVO = messageMap.get(taskDTO.getId());
				String lastMesssageTime = messageVO.getCreateTime();
				if (isBefor(getLastOpenTime, lastMesssageTime)) {
					taskVo.setMessage(messageVO);
				}
			}
			// 判断是否超时未完成
			if (taskDTO.getStatus() == TASK_TODO || taskDTO.getStatus() == TASK_DOING) {
				taskVo.setWarning(getTaskIsWarning(taskDTO.getEndTime()));
			}
			// 获取关注人，关注组名字
			boolean isUserFollow = getFollowUserName(taskVo, userNameMap);
			taskVo.setDeleteAuthority(isUserFollow);

			if (TaskDTO.STATUS_TODO == taskVo.getStatus()) {
				taskFrameOfTODO.getTaskList().add(taskVo);
			} else if (TaskDTO.STATUS_DOING == taskVo.getStatus()) {
				taskFrameOfDOING.getTaskList().add(taskVo);
			} else if (TaskDTO.STATUS_DONE == taskVo.getStatus()) {
				taskFrameOfDONE.getTaskList().add(taskVo);
			} else {
				continue;
			}
		}

		taskFrames.add(taskFrameOfTODO);
		taskFrames.add(taskFrameOfDOING);
		taskFrames.add(taskFrameOfDONE);
	}

	/**
	 * 获取关注人中文姓名和DisplayName
	 */
	private boolean getFollowUserName(com.sogou.bizwork.task.api.core.task.po.TaskVo taskVo,
			Map<Long, String> userNameMap) throws ApiTException, TException {
		boolean flag = false;
		long userId = UserHolder.getUserId();
		for (com.sogou.bizwork.task.api.core.task.po.FollowUserVo followUserVo : taskVo.getFollowUsers()) {
			String userName = null;
			if (followUserVo.getType() == 1) {
				userName = getGroupNameById(followUserVo.getId());
			} else if (followUserVo.getType() == 0) {
				userName = userNameMap.get(followUserVo.getId());
				if (followUserVo.getId() == userId) {
					flag = true;
				}
			}
			if (StringUtils.isEmpty(userName)) {
				logger.error("invalid user or group name" + followUserVo.getType() + followUserVo.getId());
				return false;
				// throw new RuntimeException("invalid user or group name" +
				// followUserVo.getType() + followUserVo.getId());
			}
			followUserVo.setName(userName);
			if (followUserVo.getType() == 0 || followUserVo.getType() == 1) {
				followUserVo.setDisplayName(userName.substring(userName.length() - 1));
			}

		}
		return flag;
	}

	/**
	 * 判断截止日期是否早于当前日期
	 * 
	 * @param endTime
	 * @return
	 */
	private boolean getTaskIsWarning(String endTime) {
		if (endTime == "" || endTime == null)
			return false;
		String curDateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date endDate = format.parse(endTime);
			Date curDate = format.parse(curDateStr);
			return endDate.before(curDate);
		} catch (ParseException e) {
			logger.error("解析截止日期异常");
			throw new RuntimeException("解析截止日期异常");
		}
	}

	private boolean isBefor(String getLastOpenTime, String lastMesssageTime) {
		if (getLastOpenTime == "" || getLastOpenTime == null)
			return true;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date getLastOpenDate = null;
			if (StringUtils.isNotEmpty(getLastOpenTime)) {
				getLastOpenDate = format.parse(getLastOpenTime);
			}
			Date lastMesssageDate = null;
			if (StringUtils.isNotEmpty(lastMesssageTime)) {
				lastMesssageDate = format.parse(lastMesssageTime);
			}
			if (null == getLastOpenTime || null == lastMesssageDate) {
				return false;
			}
			return getLastOpenDate.before(lastMesssageDate);
		} catch (ParseException e) {
			logger.error("解析日期异常");
			return false;
		}
	}

	/**
	 * 将原始数据taskTos转换成标签视图结构化数据对象
	 * 
	 * @param taskTos
	 * @param taskFrames
	 * @throws TException
	 * @throws ApiTException
	 */
	private void getViewOfTagTask(List<TaskDTO> taskDTOs,
			Map<Long, com.sogou.bizwork.task.api.core.task.po.MessageVO> messageMap, Map<Long, String> userNameMap,
			List<TaskFrameVo> taskFrames, int userType) throws ApiTException, TException {
		if (CollectionUtils.isEmpty(taskDTOs))
			return;

		Map<String, String> tagNameAndColor = new HashMap<String, String>();
		// key:标签名称 val:带有该标签的任务的list
		Map<String, List<TaskVo>> tagMap = new HashMap<String, List<TaskVo>>();
		List<TaskVo> noTagTaskList = new ArrayList<TaskVo>();
		for (TaskDTO taskDTO : taskDTOs) {
			TaskVo taskVo = TaskConvertor.convertTaskDto2Vo(taskDTO);
			taskVo.setStartTime(TaskUtil.parseDate(taskDTO.getStartTime()));
			taskVo.setEndTime(TaskUtil.parseDate(taskDTO.getEndTime()));
			taskVo.setChargeUserName(getUserChineseNameById(taskVo.getChargeUser()));

			// 加入未读消息
			if (messageMap.containsKey(taskDTO.getId())) {
				String getLastOpenTime = taskDTO.getLastOpenTime();
				com.sogou.bizwork.task.api.core.task.po.MessageVO messageVO = messageMap.get(taskDTO.getId());
				String lastMesssageTime = messageVO.getCreateTime();
				if (isBefor(getLastOpenTime, lastMesssageTime)) {
					taskVo.setMessage(messageVO);
				}
			}
			// 判断是否超时未完成
			if (taskDTO.getStatus() == TASK_TODO || taskDTO.getStatus() == TASK_DOING) {
				taskVo.setWarning(getTaskIsWarning(taskDTO.getEndTime()));
			}
			// 获取关注人，关注组名字
			boolean isUserFollow = getFollowUserName(taskVo, userNameMap);
			taskVo.setDeleteAuthority(isUserFollow);

			byte taskStatus = taskDTO.getStatus();
			List<TagVo> tagVosOfName = new ArrayList<TagVo>();
			if (taskStatus == TaskDTO.STATUS_TODO) {
				TagVo tagTmp = new TagVo();
				tagTmp.setName("TODO");
				tagVosOfName.add(tagTmp);
				taskVo.setTags(tagVosOfName);
			} else if (taskStatus == TaskDTO.STATUS_DOING) {
				TagVo tagTmp = new TagVo();
				tagTmp.setName("DOING");
				tagVosOfName.add(tagTmp);
				taskVo.setTags(tagVosOfName);
			} else if (taskStatus == TaskDTO.STATUS_DONE) {
				TagVo tagTmp = new TagVo();
				tagTmp.setName("DONE");
				tagVosOfName.add(tagTmp);
				taskVo.setTags(tagVosOfName);
			} else {
				continue;
			}

			List<TagDto> tags = taskDTO.getTags();
			// 此处要分为任务有标签和任务无标签两种
			if (CollectionUtils.isNotEmpty(tags)) {// 任务有标签
				for (TagDto tagTmp : tags) {
					String tagName = tagTmp.getName();
					if (tagMap.containsKey(tagName)) {
						List<TaskVo> orgTasks = tagMap.get(tagName);
						orgTasks.add(taskVo);
						tagMap.put(tagName, orgTasks);
					} else {
						List<TaskVo> tempTaskVo = new ArrayList<TaskVo>();
						tempTaskVo.add(taskVo);
						tagMap.put(tagName, tempTaskVo);
						tagNameAndColor.put(tagName, tagTmp.getColor());
					}
				}
			} else {// 任务无标签
				noTagTaskList.add(taskVo);
			}
		}
		long userId = UserHolder.getUserId();
		List<TaskFrameVo> taskFramesTemp = new ArrayList<TaskFrameVo>();
		for (Entry<String, List<TaskVo>> entry : tagMap.entrySet()) {
			TaskFrameVo taskFrameVo = new TaskFrameVo(entry.getKey());
			taskFrameVo.setId(getTagIdByUserIdAndTagName(userId, entry.getKey()));
			taskFrameVo.setTaskList(entry.getValue());
			taskFrameVo.setColor(tagNameAndColor.get(entry.getKey()));
			taskFramesTemp.add(taskFrameVo);
		}
		// 获取tagOrder,如果存在，则按顺序排列
		TagOrderDto tagOrderDto = tagService.getTagOrder(userId, userType);
		if (null != tagOrderDto && tagOrderDto.getOrder() != "") {
			String[] orders = tagOrderDto.getOrder().split(",");
			for (int i = 0; i < orders.length; i++) {
				int tagId = Integer.parseInt(orders[i]);
				if (tagId == 0 && CollectionUtils.isNotEmpty(noTagTaskList)) {// 未分组
					TaskFrameVo noNameFrame = new TaskFrameVo(NAME_FOR_NO_TAG);
					List<TaskVo> listtemp = new ArrayList<TaskVo>(noTagTaskList);
					noNameFrame.setTaskList(listtemp);
					taskFrames.add(noNameFrame);
					noTagTaskList.clear();
				} else {
					for (int j = 0; j < taskFramesTemp.size(); j++) {
						if (taskFramesTemp.get(j).getId() == tagId) {
							taskFrames.add(taskFramesTemp.get(j));
							taskFramesTemp.remove(j);
							continue;
						}
					}
				}
			}
			taskFrames.addAll(taskFramesTemp);
		} else {
			taskFrames.addAll(taskFramesTemp);
		}

		if (CollectionUtils.isNotEmpty(noTagTaskList)) {
			TaskFrameVo noNameFrame = new TaskFrameVo(NAME_FOR_NO_TAG);
			noNameFrame.setTaskList(noTagTaskList);
			taskFrames.add(noNameFrame);
		}
	}

	/**
	 * 将原始数据taskTos转换成负责人视图结构化数据对象
	 * 
	 * @param taskTos
	 * @param taskFrames
	 * @throws TException
	 * @throws ApiTException
	 */
	private void getViewOfChargeUserTask(List<TaskDTO> taskDTOs,
			Map<Long, com.sogou.bizwork.task.api.core.task.po.MessageVO> messageMap, Map<Long, String> userNameMap,
			List<TaskFrameVo> taskFrames, int userType) throws ApiTException, TException {

		if (CollectionUtils.isEmpty(taskDTOs))
			return;

		Map<Long, List<TaskVo>> chargeUserMap = new HashMap<Long, List<TaskVo>>();
		for (TaskDTO taskDTO : taskDTOs) {
			com.sogou.bizwork.task.api.core.task.po.TaskVo taskVo = TaskConvertor.convertTaskDto2Vo(taskDTO);
			taskVo.setStartTime(TaskUtil.parseDate(taskDTO.getStartTime()));
			taskVo.setEndTime(TaskUtil.parseDate(taskDTO.getEndTime()));
			String chargeUserName = getUserChineseNameById(taskVo.getChargeUser());
			taskVo.setChargeUserName(chargeUserName);
			// 加入标签
			List<TagVo> tagVos = taskVo.getTags();
			byte taskStatus = taskDTO.getStatus();
			TagVo tagTmp = new TagVo();
			if (taskStatus == TaskDTO.STATUS_TODO) {
				tagTmp.setName("TODO");
				tagVos.add(0, tagTmp);
				taskVo.setTags(tagVos);
			} else if (taskStatus == TaskDTO.STATUS_DOING) {
				tagTmp.setName("DOING");
				tagVos.add(0, tagTmp);
				taskVo.setTags(tagVos);
			} else if (taskStatus == TaskDTO.STATUS_DONE) {
				tagTmp.setName("DONE");
				tagVos.add(0, tagTmp);
				taskVo.setTags(tagVos);
			} else if (CollectionUtils.isNotEmpty(tagVos)) {
				taskVo.setTags(tagVos);
			}
			// 加入未读消息
			if (messageMap.containsKey(taskDTO.getId())) {
				String getLastOpenTime = taskDTO.getLastOpenTime();
				com.sogou.bizwork.task.api.core.task.po.MessageVO messageVO = messageMap.get(taskDTO.getId());
				String lastMesssageTime = messageVO.getCreateTime();
				if (isBefor(getLastOpenTime, lastMesssageTime)) {
					taskVo.setMessage(messageVO);
				}
			}
			// 判断是否超时未完成
			if (taskDTO.getStatus() == TASK_TODO || taskDTO.getStatus() == TASK_DOING) {
				taskVo.setWarning(getTaskIsWarning(taskDTO.getEndTime()));
			}

			// 获取关注人，关注组名字
			boolean isUserFollow = getFollowUserName(taskVo, userNameMap);
			taskVo.setDeleteAuthority(isUserFollow);

			if (chargeUserMap.containsKey(taskVo.getChargeUser())) {
				List<com.sogou.bizwork.task.api.core.task.po.TaskVo> orgTasks = chargeUserMap
						.get(taskVo.getChargeUser());
				orgTasks.add(taskVo);
				chargeUserMap.put(taskVo.getChargeUser(), orgTasks);
			} else {
				List<TaskVo> tempTaskVo = new ArrayList<TaskVo>();
				tempTaskVo.add(taskVo);
				chargeUserMap.put(taskVo.getChargeUser(), tempTaskVo);
			}
		}
		List<TaskFrameVo> taskFramesTemp = new ArrayList<TaskFrameVo>();
		for (Entry<Long, List<TaskVo>> entry : chargeUserMap.entrySet()) {
			TaskFrameVo taskFrameVo = new TaskFrameVo();
			taskFrameVo.setName(getUserChineseNameById(entry.getKey()));
			taskFrameVo.setId(entry.getKey());
			taskFrameVo.setTaskList(entry.getValue());
			taskFramesTemp.add(taskFrameVo);
		}
		// 获取tagOrder,如果存在，则按顺序排列
		long userId = UserHolder.getUserId();
		TagOrderDto tagOrderDto = tagService.getTagOrder(userId, userType);
		if (null != tagOrderDto && tagOrderDto.getOrder() != null && tagOrderDto.getOrder() != "") {
			String[] orders = tagOrderDto.getOrder().split(",");
			for (int i = 0; i < orders.length; i++) {
				int userOrderId = Integer.parseInt(orders[i]);
				for (int j = 0; j < taskFramesTemp.size(); j++) {
					if (taskFramesTemp.get(j).getId() == userOrderId) {
						taskFrames.add(taskFramesTemp.get(j));
						taskFramesTemp.remove(j);
						continue;
					}
				}
			}
			taskFrames.addAll(taskFramesTemp);
		} else {
			taskFrames.addAll(taskFramesTemp);
		}

	}

	/**
	 * 查询消息
	 */
	@RequestMapping(value = "/getMessages.do")
	@ResponseBody
	public Result getMessages(@RequestBody String param) {
		Result result = new Result();
		if (StringUtils.isEmpty(param)) {
			result.setErrorMsg("参数为空");
			return result;
		}
		JSONObject json = JSONObject.fromObject(param);
		long taskId = Long.parseLong(json.getString("taskId"));
		long operateUserId = UserHolder.getUserId();
		try {
			List<MessageDTO> messages = messageService.getMessages(taskId);
			List<MessageVO> resData = new ArrayList<MessageVO>();

			for (MessageDTO messageDTO : messages) {
				MessageVO messageVO = MessageConvertor.convertDto2Vo(messageDTO);
				messageVO.setUserName(getUserChineseNameById(messageDTO.getUserId()));
				if (operateUserId == messageVO.getUserId()) {
					messageVO.setIsOperateUser(1);
				}
				resData.add(messageVO);
			}

			result.setData(resData);
		//	briefTaskMessage.updateTasksAndMessageToBizwork(); // update task
																// and message
																// to
																// bizwork!
		} catch (Exception e) {
			logger.error("getMessage ERROR", e);
			result.setErrorMsg("getMessage ERROR");
		}
		return result;
	}

	/**
	 * 删除消息
	 */
	@RequestMapping("/deleteMessage.do")
	@ResponseBody
	public Result deleteMessage(@RequestBody String param) {
		Result result = new Result();
		if (StringUtils.isEmpty(param)) {
			result.setErrorMsg("参数为空");
			return result;
		}
		JSONObject json = JSONObject.fromObject(param);
		long messageId = Long.parseLong(json.getString("messageId"));
		long operateUserId = UserHolder.getUserId();
		try {
			messageService.deleteMessage(messageId, operateUserId);
		} catch (Exception e) {
			logger.error("deleteMessage ERROR", e);
			result.setErrorMsg("deleteMessage ERROR");
		}

		return result;
	}

	/**
	 * 获取用户现有标签
	 */
	@RequestMapping(value = "/getTags.do")
	@ResponseBody
	public Result getTags() {
		Result result = new Result();
		long operateUserId = UserHolder.getUserId();
		try {
			List<TagDto> tagDtos = tagService.getTags(operateUserId);
			List<TagVo> tagvos = TagConvertor.convertDtos2Vos(tagDtos);
			result.setData(tagvos);
		} catch (Exception e) {
			logger.error("getTags ERROR", e);
			result.setErrorMsg("getTags ERROR");
		}
		return result;
	}

	@RequestMapping(value = "/test.do")
	@ResponseBody
	public Result test() {
		Result result = new Result();
		// List<ServiceTreeInfo> svnServiceTrees =
		// bizServiceTreeService.getServiceTreeInfoByEmployeeId(204516);
		bizServiceTreeService.addServiceTreeInfo();
		result.setData("ok!");
		return result;
	}

	/**
	 * 更改负责人
	 */
	@RequestMapping(value = "/updateChargeUser.do")
	@ResponseBody
	public Result updateChargeUser(@RequestBody TaskProxyVo taskProxyVo) {
		Result result = new Result();
		if (taskProxyVo == null || taskProxyVo.getTask() == null) {
			result.setSuccess(Result.FAILED);
			return result;
		}
		com.sogou.bizwork.task.api.core.task.po.TaskVo taskVo = taskProxyVo.getTask();
		try {
			if (taskVo.getTaskId() == 0) {
				result.setErrorMsg("task id can not be null");
				return result;
			}
			if (taskVo.getChargeUser() == 0) {
				result.setErrorMsg("chageuser id can not be null");
				return result;
			}

			com.sogou.bizwork.task.api.core.task.po.TaskVo taskVoPre = TaskConvertor
					.convertTaskDto2Vo(taskService.queryTaskInfo(taskVo.getTaskId()));
			if (taskVoPre.getChargeUser() == taskVo.getChargeUser()) {
				result.setErrorMsg("chargeuser is same");
				return result;
			}
			taskVoPre.setChargeUser(taskVo.getChargeUser());
			taskProxyVo.setTask(taskVoPre);
			updateTask(taskProxyVo);

		} catch (Exception e) {
			logger.error("updatChargeUser ERROR", e);
			result.setErrorMsg("updatChargeUser ERROR");
		}

		return result;
	}

	/**
	 * 更改标签顺序，负责人顺序
	 */
	@RequestMapping(value = "/updateTagAndChargeUserOrder.do")
	@ResponseBody
	public Result updateTagOrder(@RequestBody TagOrderProxyVo tagOrderProxyVo) {
		Result result = new Result();
		if (tagOrderProxyVo == null || tagOrderProxyVo.getTagOrder() == null) {
			result.setSuccess(Result.FAILED);
			return result;
		}
		TagOrderVo tagOrderVo = tagOrderProxyVo.getTagOrder();
		try {
			if (tagOrderVo.getType() != 0 && tagOrderVo.getType() != 1 && tagOrderVo.getType() != 2) {
				result.setErrorMsg("type can only be 0,1 or 2");
				return result;
			}
			if (tagOrderVo.getOrder() == null || tagOrderVo.getOrder() == "") {
				result.setErrorMsg("order can not  be null or\"\"");
				return result;
			}
			long userId = UserHolder.getUserId();
			tagOrderVo.setUserId(userId);
			tagService.updateTagOrder(TagConvertor.convertTagOrderVo2Dto(tagOrderVo));

		} catch (Exception e) {
			logger.error("updatChargeUser ERROR", e);
			result.setErrorMsg("updatChargeUser ERROR");
		}
		return result;
	}

	/**
	 * 获取文件的下载地址
	 * 
	 * @param request
	 * @return
	 */
	private StringBuffer getFileDownloadPath(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ "/task/downloadFile.do?");
		return sb;
	}

	/**
	 * leader获取积分审核列表
	 */
	@RequestMapping(value = "/getAuditScores.do")
	@ResponseBody
	public Result getAuditScores(@RequestBody TagOrderProxyVo tagOrderProxyVo) {
		Result result = new Result();
		long employeeid = UserHolder.getUserId();
		List<AuditScore> auditScores = taskService.getAuditScoresByEmployeeid(employeeid);
		return result;
	}

}
