package com.sogou.bizwork.task.api.web.tasklog.convertor;

import java.util.ArrayList;
import java.util.List;

import com.sogou.bizwork.task.api.tasklog.to.TaskLogTo;
import com.sogou.bizwork.task.api.web.tasklog.vo.TaskLogVo;

/**
 * @description 
 * @author liquancai
 * @date 2016年8月9日
 */
public class TaskLogConvertor {
	
	public static TaskLogVo convertTaskLogTo2Vo(TaskLogTo taskLogTo){
		TaskLogVo taskLogVo = new TaskLogVo();
		if(taskLogTo == null)
			return taskLogVo;
		
		taskLogVo.setCreateTime(taskLogTo.getCreateTime());
		
		return taskLogVo;
	}
	
	public static List<TaskLogVo> convertTaskLogTos2Vos(List<TaskLogTo> taskLogTos){
		List<TaskLogVo> taskLogVos = new ArrayList<TaskLogVo>();
		if(taskLogTos == null)
			return taskLogVos;
		
		for(TaskLogTo taskLogTo : taskLogTos){
			taskLogVos.add(convertTaskLogTo2Vo(taskLogTo));
		}
		
		return taskLogVos;
	}
}
