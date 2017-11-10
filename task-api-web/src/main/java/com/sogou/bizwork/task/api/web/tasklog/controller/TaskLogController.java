package com.sogou.bizwork.task.api.web.tasklog.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.bizwork.task.api.common.exception.ApiTException;
import com.sogou.bizwork.task.api.core.dto.Result;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.service.UserService;
import com.sogou.bizwork.task.api.tasklog.result.TaskLogResult;
import com.sogou.bizwork.task.api.tasklog.service.TaskLogTService;
import com.sogou.bizwork.task.api.tasklog.to.TaskLogTo;
import com.sogou.bizwork.task.api.web.common.exception.BizException;
import com.sogou.bizwork.task.api.web.task.controller.TaskController;
import com.sogou.bizwork.task.api.web.tasklog.vo.TaskLogVo;

/**
 * @description 任务日志控制器
 * @author liquancai
 * @date 2016年8月8日
 */
@Controller
@RequestMapping("/tasklog")
public class TaskLogController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    TaskLogTService.Iface taskLogTService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/getTaskLogs.do")
    @ResponseBody
    public Result getTaskLogsByTaskId(@RequestBody String param) {
        Result result = new Result();

        if (StringUtils.isEmpty(param)) {
            result.setErrorMsg("参数为空");
            return result;
        }

        JSONObject json = JSONObject.fromObject(param);
        long taskId = Long.parseLong(json.getString("taskId"));
        try {
            TaskLogResult taskLogResult = taskLogTService.getTaskLogsByTaskId(taskId);
            if (!taskLogResult.status) {
                result.setErrorMsg(taskLogResult.getErrorCode().getMessage());
                return result;
            }
            List<TaskLogTo> taskLogTos = taskLogResult.getTaskLogToList();
            if (taskLogTos == null || taskLogTos.size() == 0) {
                result.setErrorMsg("invalid task id");
                return result;
            }
            List<TaskLogVo> taskLogVos = convertTaskLogTos2Vos(taskLogTos);
            result.setData(taskLogVos);
        } catch (ApiTException ate) {
            result.setSuccess(Result.FAILED);
            logger.error("getTaskLogs for {} error", taskId, ate);
        } catch (TException te) {
            result.setSuccess(Result.FAILED);
            logger.error("getTaskLogs for {} Thrift error", taskId, te);
        }

        return result;
    }

    /**
     * Convert TaskLogList from To to Vo
     * @param taskLogTos
     * @return
     * @throws ApiTException
     * @throws TException
     */
    private List<TaskLogVo> convertTaskLogTos2Vos(List<TaskLogTo> taskLogTos) throws ApiTException, TException {
        List<TaskLogVo> taskLogVos = new ArrayList<TaskLogVo>();
        if (taskLogTos == null)
            return taskLogVos;

        String CREATE_TASK_DESC = "创建事件";
        String CHANGE_CHARGE_USER = "修改负责人为";
        String ADD_FOLLOW_USER = "添加关注人";
        String DELETE_FOLLOW_USER = "删除关注人";
        String CHANGE_END_TIME = "修改截止时间为";

        boolean isFirst = true;
        TaskLogTo taskLogToPre = new TaskLogTo();
        for (TaskLogTo taskLogTo : taskLogTos) {
            if (isFirst) {
                TaskLogVo taskLogVo = convertTaskLogTo2Vo(taskLogTo);
                taskLogVo.setOperateDescription(CREATE_TASK_DESC);
                taskLogVos.add(taskLogVo);

                isFirst = false;
                taskLogToPre = taskLogTo;
            } else {
                // compare taskLogDTO with taskLogDTOPre
                // change charge user
                if (taskLogTo.getChargeUser() != taskLogToPre.getChargeUser()) {
                    TaskLogVo taskLogVo = convertTaskLogTo2Vo(taskLogTo);
                    taskLogVo.setOperateDescription(CHANGE_CHARGE_USER + ": "
                            + getUserChineseNameById(taskLogTo.getChargeUser()));
                    taskLogVos.add(taskLogVo);
                }
                // change follow user
                if (!taskLogTo.getTaskFollows().equals(taskLogToPre.getTaskFollows())) {
                    // 利用集合取并集再取差集,对比出任务关注者的变化
                    Set<String> userSet = new HashSet<String>();
                    Set<String> userSetPre = new HashSet<String>();
                    if (!taskLogTo.getTaskFollows().equals("")) {
                        for (String userId : taskLogTo.getTaskFollows().split(";")) {
                            userSet.add(userId);
                        }
                    }
                    if (!taskLogToPre.getTaskFollows().equals("")) {
                        for (String userId : taskLogToPre.getTaskFollows().split(";")) {
                            userSetPre.add(userId);
                        }
                    }
                    Set<String> unionSet = new HashSet<String>();
                    unionSet.addAll(userSet);
                    unionSet.addAll(userSetPre);
                    Set<String> addUserSet = new HashSet<String>();
                    unionSet.removeAll(userSetPre);
                    addUserSet.addAll(unionSet);
                    unionSet.addAll(userSetPre);
                    unionSet.removeAll(userSet);
                    Set<String> deleteUserSet = new HashSet<String>();
                    deleteUserSet.addAll(unionSet);

                    if (addUserSet.size() != 0) {
                        TaskLogVo taskLogVo = convertTaskLogTo2Vo(taskLogTo);
                        List<Long> userIdList = new ArrayList<Long>();
                        for (String userId : addUserSet) {
                            userIdList.add((long) Integer.parseInt(userId));
                        }
                        List<String> userChineseNameList = getUserChineseNamesByIdList(userIdList);
                        StringBuilder builder = new StringBuilder(ADD_FOLLOW_USER).append(": ");
                        for (String userName : userChineseNameList) {
                            builder.append(userName).append(";");
                        }
                        builder.deleteCharAt(builder.length() - 1);
                        taskLogVo.setOperateDescription(builder.toString());
                        taskLogVos.add(taskLogVo);
                    }

                    if (deleteUserSet.size() != 0) {
                        TaskLogVo taskLogVo = convertTaskLogTo2Vo(taskLogTo);
                        List<Long> userIdList = new ArrayList<Long>();
                        for (String userId : deleteUserSet) {
                            userIdList.add((long) Integer.parseInt(userId));
                        }
                        List<String> userChineseNameList = getUserChineseNamesByIdList(userIdList);
                        StringBuilder builder = new StringBuilder(DELETE_FOLLOW_USER).append(": ");
                        for (String userName : userChineseNameList) {
                            builder.append(userName).append(";");
                        }
                        builder.deleteCharAt(builder.length() - 1);
                        taskLogVo.setOperateDescription(builder.toString());
                        taskLogVos.add(taskLogVo);
                    }
                }

                // change task end time
                // 日期格式: "yyyy-MM-dd"
                String endDate = taskLogTo.getEndTime().substring(0, 10);
                String endDatePre = taskLogToPre.getEndTime().substring(0, 10);
                if (!endDate.equals(endDatePre)) {
                    TaskLogVo taskLogVo = convertTaskLogTo2Vo(taskLogTo);
                    taskLogVo.setOperateDescription(CHANGE_END_TIME + ": " + endDate);
                    taskLogVos.add(taskLogVo);
                }

                taskLogToPre = taskLogTo;
            }
        }

        return taskLogVos;
    }

    /**
     * Convert taskLogTo to Vo
     * @param taskLogTo
     * @return
     * @throws ApiTException
     * @throws TException
     */
    private TaskLogVo convertTaskLogTo2Vo(TaskLogTo taskLogTo) throws ApiTException, TException {
        TaskLogVo taskLogVo = new TaskLogVo();
        if (taskLogTo != null) {
            taskLogVo.setCreateTime(taskLogTo.getCreateTime().substring(0, 10));
            taskLogVo.setOperateUserName(getUserChineseNameById(taskLogTo.getOperateUser()));
        }

        return taskLogVo;
    }

    /**
     * 根据用户id获取用户名
     * @param userId
     * @return
     * @throws ApiTException 
     * @throws TException 
     */
    private String getUserChineseNameById(long userId) throws ApiTException, TException {
        UserDTO user = userService.getUserById((int) userId);
        if (null == user)
            throw new BizException("该用户信息不存在");
        return user.getChineseName();
    }

    /**
     * 根据多个用户ID批量获取用户名
     * @param userIdList
     * @return
     */
    private List<String> getUserChineseNamesByIdList(List<Long> userIdList) {
        if (userIdList == null || userIdList.size() == 0) {
            throw new RuntimeException("userIdList can not be null or empty");
        }
        List<UserDTO> users = userService.getUsersByIds(userIdList);
        if (users == null || users.size() == 0) {
            throw new BizException("用户信息不存在");
        }
        List<String> userChineseNames = new ArrayList<String>();
        for (UserDTO user : users) {
            userChineseNames.add(user.getChineseName());
        }

        return userChineseNames;
    }
}
