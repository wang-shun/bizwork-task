package com.sogou.bizwork.task.api.core.message.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.api.exception.ApiTException;
import com.sogou.bizwork.api.message.BriefTypeMessage;
import com.sogou.bizwork.api.xiaop.message.XiaoPMessage;
import com.sogou.bizwork.api.xiaop.message.service.XiaoPMessageTService;
import com.sogou.bizwork.task.api.core.message.dao.MessageMapper;
import com.sogou.bizwork.task.api.core.message.dto.MessageDTO;
import com.sogou.bizwork.task.api.core.message.service.MessageService;
import com.sogou.bizwork.task.api.core.task.dao.TaskMapper;
import com.sogou.bizwork.task.api.core.task.dto.TaskDTO;
import com.sogou.bizwork.task.api.core.task.msg.po.TaskIdAndFollower;
import com.sogou.bizwork.task.api.core.task.msg.po.TaskIdAndMsgTime;
import com.sogou.bizwork.task.api.core.task.msg.po.TaskIdAndOpenTime;
import com.sogou.bizwork.task.api.core.task.po.TaskFollow;
import com.sogou.bizwork.task.api.core.taskfollow.dao.TaskFollowMapper;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.service.UserService;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskFollowMapper taskFollowMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private XiaoPMessageTService.Iface XiaoPMessageTService;
    
    @Override
    public void addMessage(MessageDTO messageDTO) {
        messageMapper.addMessage(messageDTO);
    }

	@Override
	public void sentMessageToXiaoP(MessageDTO messageDTO, long operateUserId) {
        List<TaskFollow> taskFollows = taskFollowMapper.getTaskFollowsById(messageDTO.getTaskId());
        TaskDTO taskDTO = taskMapper.queryTaskInfo(messageDTO.getTaskId());
		Set<Long> employeeIdSet = new HashSet<Long>();
        Set<Long> groupIdSet = new HashSet<Long>();
        if (CollectionUtils.isNotEmpty(taskFollows)) {
            taskFollows.remove(null);
            for (TaskFollow t : taskFollows) {
                if (t.getType() == 0) {
                    employeeIdSet.add(t.getFollowUser());
                } else if (t.getType() == 1) {
                    groupIdSet.add(t.getFollowUser());
                }
            }
        }
        
        if (CollectionUtils.isNotEmpty(groupIdSet)) {
            List<Long> employeeIds = userService.getEmployeeIdsByGroupIds(new ArrayList<Long>(groupIdSet));
            if (CollectionUtils.isNotEmpty(employeeIds)) {
                for (Long eid :employeeIds) {
                    employeeIdSet.add(eid);
                }
            }
        }
        
        StringBuilder receivers = new StringBuilder();
		List<XiaoPMessage> xiaoPMessages = new ArrayList<XiaoPMessage>();
		XiaoPMessage chargeMessage = new XiaoPMessage();
		StringBuilder chargeContent = new StringBuilder();
		XiaoPMessage followMessage = new XiaoPMessage();
		StringBuilder followContent = new StringBuilder();
        if (CollectionUtils.isNotEmpty(employeeIdSet)) {
            employeeIdSet.remove(null);
            employeeIdSet.remove(operateUserId);
            if (CollectionUtils.isNotEmpty(employeeIdSet)) employeeIdSet.remove(taskDTO.getChargeUser());
            if (CollectionUtils.isNotEmpty(employeeIdSet)) {
	            List<String> userNames = userService.getUserNamesByEmployeeIds(new ArrayList<Long>(employeeIdSet));
	            if (CollectionUtils.isNotEmpty(userNames)) {
	            	receivers.append(userNames.get(0));
	            	for (int i = 1; i < userNames.size(); i++) {
	            		receivers.append(",").append(userNames.get(i));
	            	}
	            }
            }
        }
		UserDTO userDTO = userService.getUserById((int) messageDTO.getUserId());
        if (receivers.length() > 1) {
			followContent.append("[bizwork]您关注的任务“").append(taskDTO.getTaskName())
				.append("”有新留言：").append(messageDTO.getContent());
			
			if (userDTO != null && StringUtils.isNotBlank(userDTO.getChineseName())) {
				followContent.append("。来自：").append(userDTO.getChineseName());
			}
			followMessage.setContent(followContent.toString());
			followMessage.setReceivers(receivers.toString());
			followMessage.setType("0");
			xiaoPMessages.add(followMessage);
        }
        UserDTO chargeDto = userService.getUserById((int)taskDTO.getChargeUser());
        if (chargeDto != null && StringUtils.isNotBlank(chargeDto.getChineseName())
        		&& StringUtils.isNotBlank(chargeDto.getUserName())
        		&& chargeDto.getEmployeeId() != (int) operateUserId) {
        	chargeContent.append("[bizwork]您负责的任务“").append(taskDTO.getTaskName())
			.append("”有新留言：").append(messageDTO.getContent());			
			if (userDTO != null && StringUtils.isNotBlank(userDTO.getChineseName())) {
				chargeContent.append("。来自：").append(userDTO.getChineseName());
			}
			chargeMessage.setContent(chargeContent.toString());
			chargeMessage.setReceivers(chargeDto.getUserName());
			chargeMessage.setType("0");
			xiaoPMessages.add(chargeMessage);
        }
        if (CollectionUtils.isNotEmpty(xiaoPMessages)) {
			try {
				XiaoPMessageTService.sendMessageToXiaoP(xiaoPMessages);
			} catch (ApiTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
	}

    @Override
    public List<MessageDTO> getMessages(long taskId) {
        return messageMapper.getMessages(taskId);
    }

    @Override
    public List<MessageDTO> queryMessagesWithUserType(long userId, int userType) {
        List<MessageDTO> messageDTOs = null;
        if (TaskDTO.USER_TYPE_CHARGE.equals(userType)) { // 按负责人类型查询
            messageDTOs = messageMapper.queryChargeMessages(userId);
        } else if (TaskDTO.USER_TYPE_FOLLOW.equals(userType)) { // 按照关注人类型查询
            messageDTOs = messageMapper.queryFollowMessages(userId);
        } else {
            logger.error("query condition ERROR, userType not equal 0 or 1");
            throw new RuntimeException("query condition ERROR, userType only equal 0 or 1");
        }
        if (CollectionUtils.isEmpty(messageDTOs))
            return Collections.emptyList();

        return messageDTOs;
    }

    @Override
    public void deleteMessage(long messageId, long userId) {
        messageMapper.deleteMessage(messageId, userId);
    }

    @Override
    public void updateReadTime(long taskId, long userId) {
        messageMapper.updateReadTime(taskId, userId);
    }

    @Override
    public String getReadTime(long taskId, long userId) {
        return messageMapper.getReadTime(taskId, userId);
    }

    @Override
    public List<BriefTypeMessage> getChargeBriefTypeMessages(long employeeId) {
        // TODO Auto-generated method stub
        return messageMapper.getChargeBriefTypeMessages(employeeId);
    }

    @Override
    public List<BriefTypeMessage> getFollowBriefTypeMessages(long employeeId) {
        // TODO Auto-generated method stub
        return messageMapper.getFollowBriefTypeMessages(employeeId);
    }
    @Override
    public List<BriefTypeMessage> getAllFollowBriefBypeMessages() {
        List<TaskIdAndMsgTime> maxMsgList = messageMapper.getTaskIdAndMsgTimes();
        
        List<TaskIdAndOpenTime> lastOpenTimeList = messageMapper.getTaskIdAndOpenTime();
        List<TaskIdAndFollower> listTemp =  messageMapper.getTaskIdAndFollowers();
        List<TaskIdAndFollower> taskFollowers = new ArrayList<TaskIdAndFollower>();
        if (CollectionUtils.isNotEmpty(listTemp)) {
            for (TaskIdAndFollower t : listTemp) {
                if (t.getType() == 0) {
                    taskFollowers.add(t);
                } else {
                    List<Long> groupIds = new ArrayList<Long>();  //divide groupId into userIds
                    groupIds.add(t.getUserId());
                    List<Long> userIds = userService.getEmployeeIdsByGroupIds(groupIds);
                    if (CollectionUtils.isNotEmpty(userIds)) {
                        for (Long userId : userIds) {
                            TaskIdAndFollower tf = new TaskIdAndFollower();
                            tf.setTaskId(t.getTaskId());
                            tf.setUserId(userId);
                            taskFollowers.add(tf);
                        }
                    }
                }
            }
        }
        
        Map<Long, Timestamp> maxMsgMap = new HashMap<Long, Timestamp>();
        Map<String, Timestamp> lastOpenTimeMap = new HashMap<String, Timestamp>();
        
        
        List<BriefTypeMessage> result = new ArrayList<BriefTypeMessage>();
        Map<Long, Integer> resultMap = new HashMap<Long, Integer>();
        Set<String> set = new HashSet<String>();
        
        if (CollectionUtils.isNotEmpty(taskFollowers) && CollectionUtils.isNotEmpty(maxMsgList)
        		&& CollectionUtils.isNotEmpty(lastOpenTimeList)) {
        	for (TaskIdAndMsgTime ml :maxMsgList) {
        		if (!maxMsgMap.containsKey(ml.getTaskId())) {
        			maxMsgMap.put(ml.getTaskId(), ml.getMaxMsgTime());
        		}
        	}
        	for (TaskIdAndOpenTime to : lastOpenTimeList) {
        		if (!lastOpenTimeMap.containsKey(to.getTaskAtUser())) {
        			lastOpenTimeMap.put(to.getTaskAtUser(), to.getLastOpenTime());
        		}
        	}
        	
            for (TaskIdAndFollower tf : taskFollowers) {
            	String key = tf.getTaskId() + "@" + tf.getUserId();
            	
	                if (set.add(key)) {
	                    if (!org.springframework.util.CollectionUtils.isEmpty(lastOpenTimeMap) &&
	                    	!org.springframework.util.CollectionUtils.isEmpty(maxMsgMap) &&
	                    		(!lastOpenTimeMap.containsKey(key) ||
	                    		(lastOpenTimeMap.get(key).before(maxMsgMap.get(tf.getTaskId())))
	                    				)) {
	                    	if (!resultMap.containsKey(tf.getUserId())) {
	                    		resultMap.put(tf.getUserId(), 1);
	                    	} else {
	                    		resultMap.put(tf.getUserId(), resultMap.get(tf.getUserId()) + 1);
	                    	}
	                    }
	                }
            	
            }
            for (Entry<Long, Integer> en : resultMap.entrySet()) {
        		BriefTypeMessage bf = new BriefTypeMessage();
        		bf.setNum((long)en.getValue());
        		bf.setEmployeeId(en.getKey());
        		bf.setMesTypeId((short) 8);
        		result.add(bf);
            }
        }
        
        return result;
    }

    @Override
    public List<BriefTypeMessage> getAllChargeBriefTypeMessages() {  
        return messageMapper.getAllChargeBriefTypeMessages();
    }

}
