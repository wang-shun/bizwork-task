package com.sogou.bizwork.task.api.core.tasklog.dao;

import java.util.List;

import com.sogou.bizwork.task.api.core.tasklog.dto.TaskLogDTO;

public interface TaskLogMapper {
	public void addTaskLog(TaskLogDTO taskLogDTO);
	
	public List<TaskLogDTO> getTaskLogsByTaskId(long taskId);
	
	public int deleteTaskLogsByTaskId(long taskId);
}
