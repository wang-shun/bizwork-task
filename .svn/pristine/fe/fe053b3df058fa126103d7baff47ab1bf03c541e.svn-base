package com.sogou.bizwork.task.api.core.scheduled.bo;

import java.util.List;

import com.sogou.bizwork.task.api.core.task.po.FollowUserVo;
import com.sogou.bizwork.task.api.core.task.po.TagVo;
import com.sogou.bizwork.task.api.core.task.po.TaskVo;

public class ScheduledTask {
	private String taskName;
	private long chargeUser;
    private List<FollowUserVo> followUsers; // 关注人ID列表(用于输出展示)
    private List<TagVo> tags; // 当前用户的任务标签
    private String description; // 任务描述
    
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public long getChargeUser() {
		return chargeUser;
	}
	public void setChargeUser(long chargeUser) {
		this.chargeUser = chargeUser;
	}
	public List<FollowUserVo> getFollowUsers() {
		return followUsers;
	}
	public void setFollowUsers(List<FollowUserVo> followUsers) {
		this.followUsers = followUsers;
	}
	public List<TagVo> getTags() {
		return tags;
	}
	public void setTags(List<TagVo> tags) {
		this.tags = tags;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void convertTaskVoToTask(TaskVo taskVo) {
		this.taskName = taskVo.getTaskName();
		this.chargeUser = taskVo.getChargeUser();
		this.followUsers = taskVo.getFollowUsers();
		this.tags = taskVo.getTags();
		this.description = taskVo.getDescription();
	}
	public TaskVo convertTaskToTaskVo() {
		TaskVo taskVo = new TaskVo();
		taskVo.setTaskName(taskName);
		taskVo.setChargeUser(chargeUser);
		taskVo.setFollowUsers(followUsers);
		taskVo.setTags(tags);
		taskVo.setDescription(description);
		return taskVo;
	}
}
