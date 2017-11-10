package com.sogou.bizwork.task.api.core.task.msg.po;

public class TaskIdAndFollower {
    private Long taskId;
    private Long userId;
    private int type=0;  //0: user, 1: group
    public Long getTaskId() {
        return taskId;
    }
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
}
