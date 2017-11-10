package com.sogou.bizwork.task.api.core.task.po;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import net.sf.json.JSONObject;


/**
 * @description 任务实体类，对应数据库表t_task
 * @author yangbing
 * @date 2016-7-20
 * @version 1.0.0
 */
public class Task {
	private long id;
	private long createUser;		//创建人ID
	private long chargeUser;		//负责人ID
	private String startTime;		//任务开始时间，Date不是thrift内置类型，避免要为此定义struct，故采用内置String类型
	private String endTime;			//任务结束时间
	private String taskName;		//任务名称
	private String description;		//任务描述
	private String attachment;		//附件地址
	private byte status;			//任务状态，0：todo；1：doing；2：done；-1：删除
	private String createTime;		//任务创建时间
	private String changeTime;		//任务修改时间
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
