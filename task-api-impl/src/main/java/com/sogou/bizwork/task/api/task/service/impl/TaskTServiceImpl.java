package com.sogou.bizwork.task.api.task.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.common.ResultUtils;
import com.sogou.bizwork.task.api.common.exception.ApiTException;
import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.core.task.dto.TaskDTO;
import com.sogou.bizwork.task.api.core.task.service.TaskService;
import com.sogou.bizwork.task.api.task.convertor.TaskConvertor;
import com.sogou.bizwork.task.api.task.result.TaskResult;
import com.sogou.bizwork.task.api.task.service.TaskTService;
import com.sogou.bizwork.task.api.task.to.TaskTo;
import com.sogou.bizwork.task.api.task.util.TaskResultUtil;
import com.sogou.bizwork.task.api.task.validator.TaskValidator;

@Service("taskTService")
public class TaskTServiceImpl implements TaskTService.Iface {

    private static final Logger logger = LoggerFactory.getLogger(TaskTServiceImpl.class);
    @Autowired
    private TaskService taskService;

    @Override
    public TaskResult queryTaskInfo(long taskId) throws ApiTException, TException {
        TaskResult taskResult = new TaskResult();
        TaskDTO taskDTO = null;
        try {
            taskDTO = taskService.queryTaskInfo(taskId);
        } catch (DataAccessException e) {
            String errorMsg = "get task info with taskId [" + taskId + "] ERROR";
            logger.error(errorMsg, e);
            throw ResultUtils.newApiTException(BizErrorEnum.SYSTEM_ERROR.getCode(), errorMsg);
        } catch (Exception e) {
            String errorMsg = "unknown ERROR for taskId [" + taskId + "]";
            logger.error(errorMsg, e);
            throw ResultUtils.newApiTException(BizErrorEnum.SYSTEM_ERROR.getCode(), errorMsg);
        }
        if (taskDTO == null) {
            logger.error("invalid taskId [{}], not exist in db", taskId);
            return TaskResultUtil.newResult(BizErrorEnum.TASK_TASK_ID_INVALID);
        }

        List<TaskTo> taskTOs = new ArrayList<TaskTo>(1);
        taskTOs.add(TaskConvertor.convertDto2To(taskDTO));
        taskResult.setTaskToList(taskTOs);
        taskResult.setTotalNumber(1);

        return taskResult;
    }

    @Override
    public TaskResult queryUserTaskByStatusOrTag(long userId, int viewType, int userType) throws ApiTException,
            TException {
        TaskResult taskResult = new TaskResult();
        List<TaskDTO> taskDTOs = null;
        try {
            // taskDTOs = taskService.queryTasksWithViewAndType(userId,
            // viewType, userType);
        } catch (DataAccessException e) {
            logger.error("query Tasks DB ERROR, userId=" + userId + ",viewType=" + viewType + ",userType=" + userType,
                    e);
            taskResult = TaskResultUtil.newResult(BizErrorEnum.SYSTEM_ERROR);
            return taskResult;
        } catch (Exception e) {
            logger.error("query Tasks system ERROR, userId=" + userId + ",viewType=" + viewType + ",userType="
                    + userType, e);
            throw ResultUtils.newApiTException(BizErrorEnum.SYSTEM_ERROR.getCode());
        }

        taskResult.setTaskToList(TaskConvertor.convertDtos2Tos(taskDTOs));
        taskResult.setTotalNumber(taskDTOs == null ? 0 : taskDTOs.size());

        return taskResult;
    }

    @Override
    public TaskResult addTask(TaskTo taskTo, long operateUser) throws ApiTException, TException {
        TaskResult taskResult = TaskValidator.validateTaskDto(taskTo);
        if (!taskResult.status) {
            return taskResult;
        }

        TaskDTO taskDTO = TaskConvertor.convertToDTO(taskTo);
        try {
            long taskId = taskService.addTask(taskDTO, operateUser);
            taskTo.setId(taskId);
            List<TaskTo> taskTosTmp = new ArrayList<TaskTo>();
            taskTosTmp.add(taskTo);
            taskResult.setTaskToList(taskTosTmp);
        } catch (Exception e) {
            logger.error("addTask ERROR", e);
            ApiTException ate = new ApiTException();
            ate.setErrorCode(e.hashCode());
            ate.initCause(e);
            ate.setMessage(e.getMessage());
            ate.setStackTrace(e.getStackTrace());

            throw ate;
        }

        return taskResult;
    }

    @Override
    public TaskResult updateTask(TaskTo taskTo, long operateUser) throws ApiTException, TException {
        TaskResult taskResult = TaskValidator.validateTaskDto(taskTo);
        if (!taskResult.status) {
            return taskResult;
        }
        try {
            taskService.updateTask(TaskConvertor.convertToDTO(taskTo), operateUser);
        } catch (Exception e) {
            logger.error("updateTask for task {} ERROR", taskTo.getId(), e);
            ApiTException ate = new ApiTException();
            ate.setErrorCode(e.hashCode());
            ate.initCause(e);
            ate.setMessage(e.getMessage());
            ate.setStackTrace(e.getStackTrace());

            throw ate;
        }
        return taskResult;
    }

    @Override
    public TaskResult deleteTask(TaskTo taskTo, long operateUser) throws ApiTException, TException {
        TaskResult taskResult = new TaskResult();
        try {
            taskService.deleteTask(TaskConvertor.convertToDTO(taskTo), operateUser);
        } catch (Exception e) {
            logger.error("deleteTask for task {} ERROR", taskTo.getId(), e);
            ApiTException ate = new ApiTException();
            ate.setErrorCode(e.hashCode());
            ate.initCause(e);
            ate.setMessage(e.getMessage());
            ate.setStackTrace(e.getStackTrace());

            throw ate;
        }

        return taskResult;
    }

}
