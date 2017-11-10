package com.sogou.bizwork.task.api.core.task.dto;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sogou.bizwork.task.api.core.score.bo.Score;
import com.sogou.bizwork.task.api.core.tag.dto.TagDto;
import com.sogou.bizwork.task.api.core.taskfollow.dto.TaskFollowDTO;

/**
 * @description TaskAPI调用时返回的任务实体，相对于PO的Task额外增加了“关注人”字段
 * @author yangbing
 * @date 2016-7-21
 * @version 1.0.0
 */
public class TaskDTO {

    public static final Integer VIEW_TYPE_STATUS = 0; // 视图类型：按状态类型查询
    public static final Integer VIEW_TYPE_TAG = 1; // 视图类型：按标签类型查询
    public static final Integer VIEW_TYPE_CHARGEUSER = 2; // 视图类型：按负责人类型查询

    public static final Integer USER_TYPE_CHARGE = 0; // 用户查询职责类型：负责人
    public static final Integer USER_TYPE_FOLLOW = 1; // 用户查询职责类型：关注人

    public static final Integer STATUS_TODO = 0; // 状态：TODO
    public static final Integer STATUS_DOING = 1; // 状态：DOING
    public static final Integer STATUS_DONE = 2; // 状态：DONE
    public static final Integer STATUS_DEL = -1; // 状态删除

    private long id;
    private long createUser; // 创建人ID
    private long chargeUser; // 负责人ID
    private List<TaskFollowDTO> followUsers; // 关注人列表
    private List<TagDto> tags; // 标签列表
    private String startTime; // 任务开始时间
    private String endTime; // 任务结束时间
    private String taskName; // 任务名称
    private String description; // 任务描述
    private String attachment; // 附件地址
    private byte status; // 任务状态，0：todo；1：doing；2：done；-1：删除
    private String createTime; // 任务创建时间
    private String changeTime; // 任务修改时间

    private String lastOpenTime; // 最后查看时间
    private Score score;
    private int scoreId;
    private int scoreTypeId;
    private int scoreValue;
    private int scoreFrom;
    private int scoreStatus;
    
    
    
    



	public int getScoreTypeId() {
		return scoreTypeId;
	}

	public void setScoreTypeId(int scoreTypeId) {
		this.scoreTypeId = scoreTypeId;
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}

	public int getScoreFrom() {
		return scoreFrom;
	}

	public void setScoreFrom(int scoreFrom) {
		this.scoreFrom = scoreFrom;
	}

	public int getScoreStatus() {
		return scoreStatus;
	}

	public void setScoreStatus(int scoreStatus) {
		this.scoreStatus = scoreStatus;
	}

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

	public String getLastOpenTime() {
        return lastOpenTime;
    }

    public void setLastOpenTime(String lastOpenTime) {
        this.lastOpenTime = lastOpenTime;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(long createUser) {
        this.createUser = createUser;
    }

    public long getChargeUser() {
        return chargeUser;
    }

    public void setChargeUser(long chargeUser) {
        this.chargeUser = chargeUser;
    }

    public List<TaskFollowDTO> getFollowUsers() {
        return followUsers;
    }

    public void setFollowUsers(List<TaskFollowDTO> followUsers) {
        this.followUsers = followUsers;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public String toString() {
        return JSONObject.fromObject(this).toString();
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
