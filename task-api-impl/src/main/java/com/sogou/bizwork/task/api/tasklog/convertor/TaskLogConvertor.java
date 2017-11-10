package com.sogou.bizwork.task.api.tasklog.convertor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sogou.bizwork.task.api.common.util.BeanUtils;
import com.sogou.bizwork.task.api.core.tasklog.dto.TaskLogDTO;
import com.sogou.bizwork.task.api.tasklog.to.TaskLogTo;

/**
 * @description TaskLogDTO与TaskLogTo之间的转换
 * @author liquancai
 * @date 2016年7月26日
 */
public class TaskLogConvertor {
	
	/**
	 * @description convert TaskLogDTO list to TaskLogTo list
	 * @param taskLogDTOs
	 * @return
	 */
	public static List<TaskLogTo> convertTaskLogDTOs2Tos(List<TaskLogDTO> taskLogDTOs){
		List<TaskLogTo> taskLogTos = new ArrayList<TaskLogTo>();
		if(taskLogDTOs != null){
			for(TaskLogDTO taskLogDTO : taskLogDTOs){
				taskLogTos.add(convertTaskLogDTO2To(taskLogDTO));
			}
		}
		
		return taskLogTos;
	}
	
	/**
	 * @description convertTaskLogDTO to TaskLogTo
	 * @param taskLogDTO
	 * @return
	 */
	private static TaskLogTo convertTaskLogDTO2To(TaskLogDTO taskLogDTO){
		TaskLogTo taskLogTo = new TaskLogTo();
		if(taskLogDTO != null){
			BeanUtils.copy(taskLogDTO, taskLogTo);
		}
		
		return taskLogTo;
	}
}
