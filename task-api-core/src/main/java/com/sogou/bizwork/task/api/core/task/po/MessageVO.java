package com.sogou.bizwork.task.api.core.task.po;

/**
 * 跟DTO比，多了userName,少了status
 * @author wangjunsi3019
 *
 */
public class MessageVO {

    private int id;

    // 任务Id
    private long taskId;
    // 消息拥有者id
    private long userId;
    private String userName;
    // 消息内容
    private String content;
    // 创建时间
    private String createTime;
    // 是否是本人(1：是本人，0：不是本人)
    private int isOperateUser;

    public int getIsOperateUser() {
        return isOperateUser;
    }

    public void setIsOperateUser(int isOperateUser) {
        this.isOperateUser = isOperateUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
