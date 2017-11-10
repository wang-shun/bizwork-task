package com.sogou.bizwork.task.api.tasklog.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.common.exception.ApiTException;
import com.sogou.bizwork.task.api.common.to.PaginationTo;
import com.sogou.bizwork.task.api.core.task.dto.PaginationDTO;
import com.sogou.bizwork.task.api.core.tasklog.dto.TaskLogDTO;
import com.sogou.bizwork.task.api.core.tasklog.service.TaskLogService;
import com.sogou.bizwork.task.api.task.convertor.PaginationConvertor;
import com.sogou.bizwork.task.api.tasklog.convertor.TaskLogConvertor;
import com.sogou.bizwork.task.api.tasklog.result.TaskLogResult;
import com.sogou.bizwork.task.api.tasklog.service.TaskLogTService;
import com.sogou.bizwork.task.api.tasklog.to.TaskLogTo;

@Service("taskLogTService")
public class TaskLogTServiceImpl implements TaskLogTService.Iface {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskLogTServiceImpl.class);
	
	@Autowired
	TaskLogService taskLogService;
	
	@Override
    public TaskLogResult getTaskLogsByTaskId(long taskId) throws ApiTException, TException {
		TaskLogResult taskLogResult = new TaskLogResult();
	    List<TaskLogDTO> taskLogDTOs = new ArrayList<TaskLogDTO>();
	    try{
	    	taskLogDTOs = taskLogService.getTaskLogsByTaskId(taskId);
	    	taskLogResult.setTaskLogToList(TaskLogConvertor.convertTaskLogDTOs2Tos(taskLogDTOs));
	    	taskLogResult.setTotalNumber(taskLogDTOs == null ? 0:taskLogDTOs.size());
	    }
	    catch(Exception e){
	    	logger.error("getTaskLogsByTaskId for task {} ERROR", taskId, e);
    		ApiTException ate = new ApiTException();
    		ate.initCause(e);
    		ate.setErrorCode(e.hashCode());
    		ate.setMessage(e.getMessage());
    		ate.setStackTrace(e.getStackTrace());
    		throw ate;
	    }
		
	    return taskLogResult;
    }

	@Override
    public TaskLogResult deleteTaskLog(long taskId) throws ApiTException, TException {
		TaskLogResult taskLogResult = new TaskLogResult();
		
		try{
			taskLogService.deleteTaskLogsByTaskId(taskId);
		}
		catch(Exception e){
			logger.error("deleteTaskLog for task {} ERROR", taskId, e);
	    	ApiTException ate = new ApiTException();
    		ate.initCause(e);
    		ate.setErrorCode(e.hashCode());
    		ate.setMessage(e.getMessage());
    		ate.setStackTrace(e.getStackTrace());
    		throw ate;
	    }
		
	    return taskLogResult;
    }
}
