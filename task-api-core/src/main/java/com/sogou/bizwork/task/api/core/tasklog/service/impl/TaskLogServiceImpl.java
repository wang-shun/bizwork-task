package com.sogou.bizwork.task.api.core.tasklog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.core.task.dto.PaginationDTO;
import com.sogou.bizwork.task.api.core.tasklog.dao.TaskLogMapper;
import com.sogou.bizwork.task.api.core.tasklog.dto.TaskLogDTO;
import com.sogou.bizwork.task.api.core.tasklog.service.TaskLogService;

@Service("taskLogService")
public class TaskLogServiceImpl implements TaskLogService {
	@Autowired
	private TaskLogMapper taskLogMapper;
	
	@Override
	public void addTaskLog(TaskLogDTO taskLogDTO) {
		taskLogMapper.addTaskLog(taskLogDTO);
	}

	@Override
    public List<TaskLogDTO> getTaskLogsByTaskId(long taskId) {
	    return taskLogMapper.getTaskLogsByTaskId(taskId);
    }

	@Override
    public int deleteTaskLogsByTaskId(long taskId) {
	    return taskLogMapper.deleteTaskLogsByTaskId(taskId);
    }
}