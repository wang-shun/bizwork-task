package com.sogou.bizwork.task.api.core.message.service;

import java.util.List;

import com.sogou.bizwork.api.message.BriefTypeMessage;
import com.sogou.bizwork.task.api.core.message.dto.MessageDTO;

public interface MessageService {

    /**
     * 添加消息
     */
    public void addMessage(MessageDTO messageDTO);

    /**
     * push message to xiaoP
     * @param messageDTO
     */
    public void sentMessageToXiaoP(MessageDTO messageDTO, long operateUserId);
    
    /**
     * 获取某个任务的所有消息
     */
    public List<MessageDTO> getMessages(long taskId);

    /**
     * 获取某个用户 负责（关注）的所有任务的最新一条消息
     */
    public List<MessageDTO> queryMessagesWithUserType(long userId, int userType);

    /**
     * 删除消息
     */
    public void deleteMessage(long messageId, long userId);

    /**
     *  更新进入任务详情页最新一次时间
     */
    public void updateReadTime(long taskId, long userId);

    /**
     *  获取进入任务详情页最新一次时间
     */
    public String getReadTime(long taskId, long userId);

    public List<BriefTypeMessage> getChargeBriefTypeMessages(long employeeId);
    
    public List<BriefTypeMessage> getFollowBriefTypeMessages(long employeeId);

    public List<BriefTypeMessage> getAllFollowBriefBypeMessages();

    public List<BriefTypeMessage> getAllChargeBriefTypeMessages();
}
