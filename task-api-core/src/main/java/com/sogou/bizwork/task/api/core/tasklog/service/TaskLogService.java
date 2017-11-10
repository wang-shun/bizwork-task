package com.sogou.bizwork.task.api.core.tasklog.service;

import java.util.List;

import com.sogou.bizwork.task.api.core.tasklog.dto.TaskLogDTO;

public interface TaskLogService {
	/**
	 * @description 添加任务操作日志
	 * @param taskLogDTO
	 * @return
	 */
	public void addTaskLog(TaskLogDTO taskLogDTO);
	
	/**
	 * @description 查询任务操作日志
	 * @param taskId 查询任务的ID
	 * @return 
	 */
	public List<TaskLogDTO> getTaskLogsByTaskId(long taskId);
	
	/**
	 * @description 删除任务操作日志
	 * @param taskId 删除任务的ID
	 * @return
	 */
	public int deleteTaskLogsByTaskId(long taskId);
}
