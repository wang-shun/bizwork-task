package com.sogou.bizwork.task.api.core.task.po;

import java.util.List;

import com.sogou.bizwork.task.api.core.score.bo.Score;


/**
 * @description 任务Vo(相比于To,增加了isToday来标记是否是紧急任务)
 * @author liquancai
 * @date 2016年8月8日
 */
public class TaskVo {

    private long taskId;
    private String taskName; // 任务名称
    private long createUser; // 创建人ID
    private String createTime; // 任务创建时间
    private long chargeUser; // 负责人ID
    private String startTime; // 任务开始时间
    private String endTime; // 任务结束时间
    private boolean warning = false; // 是否是紧急任务(任务的截止期在今天之前，且任务状态是TODO或者DOING)
    private boolean deleteAuthority = false;
    private String description; // 任务描述
    private String attachment; // 附件地址
    private byte status; // 任务状态,0：todo;1：doing;2：done;-1：删除
    private List<FollowUserVo> followUsers; // 关注人ID列表(用于输出展示)
    private String messageContent;  //消息内容
    // private String followUserIds; // 所有关注人ID(用于输入参数)
    private Scheduled scheduled;     //定时任务
    private Score score;    //积分
    private int scoreId;        //积分id
    
    
    

    public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public int getScoreId() {
		return scoreId;
	}

	public void setScoreId(int scoreId) {
		this.scoreId = scoreId;
	}

	private List<TagVo> tags; // 当前用户的任务标签

    private List<AttachmentInfo> fileList; // 文件信息集合
    private String chargeUserName; // 负责人姓名，(用于前端展示)

    private MessageVO message;// 当前用户的最后一条未读消息

	public Scheduled getScheduled() {
		return scheduled;
	}

	public void setScheduled(Scheduled scheduled) {
		this.scheduled = scheduled;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public boolean isDeleteAuthority() {
        return deleteAuthority;
    }

    public void setDeleteAuthority(boolean deleteAuthority) {
        this.deleteAuthority = deleteAuthority;
    }

    public MessageVO getMessage() {
        return message;
    }

    public void setMessage(MessageVO message) {
        this.message = message;
    }

    public String getChargeUserName() {
        return chargeUserName;
    }

    public void setChargeUserName(String chargeUserName) {
        this.chargeUserName = chargeUserName;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(long createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getChargeUser() {
        return chargeUser;
    }

    public void setChargeUser(long chargeUser) {
        this.chargeUser = chargeUser;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
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

    public List<AttachmentInfo> getFileList() {
        return fileList;
    }

    public void setFileList(List<AttachmentInfo> fileList) {
        this.fileList = fileList;
    }

}
