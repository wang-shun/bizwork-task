package com.sogou.bizwork.task.api.core.task.service;

import com.sogou.bizwork.task.api.core.scheduled.bo.ScheduledTaskBo;
import com.sogou.bizwork.task.api.core.task.po.Result;
import com.sogou.bizwork.task.api.core.task.po.TaskVo;

public interface ScheduledTaskService {
	
	public Result addTask(TaskVo taskVo, long operateUserId);

	public void addScheduledTask(TaskVo scheduledTask, long operateUserId);

	public void updateScheduledTasks();
	
	public ScheduledTaskBo getScheduledTaskByTaskId(long taskId);

	public void setTaskToDelete(long taskId);

	public void deleteTaskByTaskId(long taskId);
}
