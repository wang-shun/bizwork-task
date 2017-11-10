package com.sogou.bizwork.task.api.core.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sogou.bizwork.api.message.BriefTypeMessage;
import com.sogou.bizwork.task.api.core.message.dto.MessageDTO;
import com.sogou.bizwork.task.api.core.task.msg.po.TaskIdAndFollower;
import com.sogou.bizwork.task.api.core.task.msg.po.TaskIdAndMsgTime;
import com.sogou.bizwork.task.api.core.task.msg.po.TaskIdAndOpenTime;

public interface MessageMapper {

    public void addMessage(MessageDTO messageDTO);

    public List<MessageDTO> getMessages(@Param("taskId") long taskId);

    public List<MessageDTO> queryChargeMessages(@Param("userId") long userId);

    public List<MessageDTO> queryFollowMessages(@Param("userId") long userId);

    public void deleteMessage(@Param("messageId") long messageId, @Param("userId") long userId);

    public void updateReadTime(@Param("taskId") long taskId, @Param("userId") long userId);

    public String getReadTime(@Param("taskId") long taskId, @Param("userId") long userId);
    
    public List<BriefTypeMessage> getChargeBriefTypeMessages(long employeeId);
    
    public List<BriefTypeMessage> getFollowBriefTypeMessages(long employeeId);
    
    public List<BriefTypeMessage> getAllChargeBriefTypeMessages();
    
    public List<TaskIdAndMsgTime> getTaskIdAndMsgTimes();
    
    public List<TaskIdAndOpenTime> getTaskIdAndOpenTime();
//    
    public List<TaskIdAndFollower> getTaskIdAndFollowers();
    
}