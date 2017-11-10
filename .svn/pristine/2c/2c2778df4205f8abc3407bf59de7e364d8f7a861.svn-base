package com.sogou.bizwork.task.api.core.tasklog.dto;

import net.sf.json.JSONObject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @description TaskLogAPI调用时返回的任务操作日志实体
 * @author liquancai
 * @date 2016年7月21日
 * @version 1.0.0
 */
public class TaskLogDTO {
	private long id;
	private long taskId;				//任务ID
	private long createUser;			//任务创建人ID
	private long chargeUser;			//任务负责人ID
	private String startTime;			//任务开始时间
	private String endTime;				//任务结束时间
	private String description;			//任务描述
	private String attachment;			//任务附件地址
	private byte status;				//任务状态(0:todo,1:doing,2:done，-1:删除)
	private long operateUser;			//操作人ID
	private byte operateType;			//操作类型(0:新建，1：修改，-1:删除)
	private String createTime;			//操作日志创建时间
	private String taskFollows;			//任务关注者
	
	public long getId() {
    	return id;
    }
	public void setId(long id) {
    	this.id = id;
    }
	public long getTaskId() {
    	return taskId;
    }
	public void setTaskId(long taskId) {
    	this.taskId = taskId;
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
	public long getOperateUser() {
    	return operateUser;
    }
	public void setOperateUser(long operateUser) {
    	this.operateUser = operateUser;
    }
	public byte getOperateType() {
    	return operateType;
    }
	public void setOperateType(byte operateType) {
    	this.operateType = operateType;
    }
	public String getCreateTime() {
    	return createTime;
    }
	public void setCreateTime(String createTime) {
    	this.createTime = createTime;
    }
	public String getTaskFollows() {
    	return taskFollows;
    }
	public void setTaskFollows(String taskFollows) {
    	this.taskFollows = taskFollows;
    }
	public String toString(){
		return JSONObject.fromObject(this).toString();
	}
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}
	public boolean equals(Object obj){
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
