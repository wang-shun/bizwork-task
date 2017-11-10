package com.sogou.bizwork.task.api.core.task.msg.po;

import java.sql.Date;
import java.sql.Timestamp;

public class TaskIdAndMsgTime {
    private Long taskId;
    private Timestamp maxMsgTime;
    public Long getTaskId() {
        return taskId;
    }
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
	public Timestamp getMaxMsgTime() {
		return maxMsgTime;
	}
	public void setMaxMsgTime(Timestamp maxMsgTime) {
		this.maxMsgTime = maxMsgTime;
	}
}
