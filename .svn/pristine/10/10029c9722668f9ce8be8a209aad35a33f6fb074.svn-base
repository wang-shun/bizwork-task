package com.sogou.bizwork.task.api.core.scheduled.dao;

import java.util.List;
import java.util.Map;

import com.sogou.bizwork.task.api.core.activiti.po.TaskIdAndNewTaskId;
import com.sogou.bizwork.task.api.core.activiti.po.TaskIdAndTimeStamp;
import com.sogou.bizwork.task.api.core.scheduled.bo.ScheduledTaskBo;

public interface ScheduledTaskDao {

	public void addScheduledTask(ScheduledTaskBo scheduledTaskBo);

	public List<ScheduledTaskBo> getValidTasks();

	public void updateToDelByTaskIds(List<Long> taskIds);

	public List<ScheduledTaskBo> getScheduledTasksByIds(List<Integer> ids);

	public ScheduledTaskBo getTaskByTaskId(long taskId);

	public void deleTaskByTaskId(long taskId);

	public int updateTaskIds(List<TaskIdAndNewTaskId> taskIds);

	public void updateDate(List<TaskIdAndTimeStamp> needUpdateDate);

}
