package com.sogou.bizwork.task.api.tasklog.util;

import java.util.List;

import com.sogou.bizwork.task.api.common.ResultUtils;
import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.tasklog.result.TaskLogResult;
import com.sogou.bizwork.task.api.tasklog.to.TaskLogTo;

/**
 * 
 * @author liquancai
 * @date 2016-7-26
 */
public class TaskLogResultUtil {
	
	public static TaskLogResult newResult(List<TaskLogTo> taskLogTos, BizErrorEnum be){
		TaskLogResult taskLogResult = new TaskLogResult();
		if(be != null){
			taskLogResult.setStatus(false);
			taskLogResult.setErrorCode(ResultUtils.newErrorCode(be));
		}
		else{
			taskLogResult.setStatus(true);
		}
		taskLogResult.setTaskLogToList(taskLogTos);
		
		return taskLogResult;
	}
	
	public static TaskLogResult newResult(BizErrorEnum be){
		return newResult(null, be);
	}
}
