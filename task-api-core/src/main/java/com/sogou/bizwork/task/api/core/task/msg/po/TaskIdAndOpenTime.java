package com.sogou.bizwork.task.api.core.task.msg.po;

import java.sql.Date;
import java.sql.Timestamp;

public class TaskIdAndOpenTime {
    private String taskAtUser;
    private Timestamp lastOpenTime;
    public String getTaskAtUser() {
		return taskAtUser;
	}
	public void setTaskAtUser(String taskAtUser) {
		this.taskAtUser = taskAtUser;
	}
	public Timestamp getLastOpenTime() {
		return lastOpenTime;
	}
	public void setLastOpenTime(Timestamp lastOpenTime) {
		this.lastOpenTime = lastOpenTime;
	}
}
