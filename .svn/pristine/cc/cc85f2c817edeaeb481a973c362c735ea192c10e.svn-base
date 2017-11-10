package com.sogou.bizwork.task.api.core.taskfollow.dto;

/**
 * @description
 * @author liquancai
 * @date 2016年7月21日
 */
public class TaskFollowDTO {

    private long taskId; // 任务ID
    private long followUser; // 任务关注人ID
    private byte status; // 状态(0有效,-1删除)
    private String createTime; // 创建时间
    private String changeTime; // 修改时间
    private int type;// 关注者类型，0：User 1：group

    public TaskFollowDTO() {}

    public TaskFollowDTO(long followUser, int type) {
        this.followUser = followUser;
        this.type = type;
    }

    public TaskFollowDTO(long taskId, long followUser, int type) {
        this.taskId = taskId;
        this.followUser = followUser;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getFollowUser() {
        return followUser;
    }

    public void setFollowUser(long followUser) {
        this.followUser = followUser;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (int) (prime * result + taskId);
        result = (int) (prime * result + followUser);
        result = (int) (prime * result + taskId);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaskFollowDTO other = (TaskFollowDTO) obj;
        if (taskId != other.taskId)
            return false;
        else if (followUser != other.followUser)
            return false;
        else if (type != other.type)
            return false;
        return true;
    }
}
