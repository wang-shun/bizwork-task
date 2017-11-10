package com.sogou.bizwork.task.api.core.activiti.po;

import java.sql.Timestamp;

public class TaskIdAndTimeStamp {
	private long taskId;
	private Timestamp updateTime;
	
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
}
