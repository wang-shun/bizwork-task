package com.sogou.bizwork.task.api.core.taskfollow.dao;

import java.util.List;

import com.sogou.bizwork.api.message.BriefTypeMessage;
import com.sogou.bizwork.task.api.core.task.msg.po.MsgReceiver;
import com.sogou.bizwork.task.api.core.task.po.TaskFollow;
import com.sogou.bizwork.task.api.core.taskfollow.dto.TaskFollowDTO;

/**
 * @description 
 * @author liquancai
 * @date 2016年7月21日
 */
public interface TaskFollowMapper {

    public void addTaskFollows(List<TaskFollowDTO> taskFollows);

    public int updateTaskFollowsStatusDel(long taskId);

    public int updateFollowUser(TaskFollowDTO operateUserDTO);
    
    public List<BriefTypeMessage> getFollowBriefTypeMessages(List<Long> employeeIds);
    
    public List<TaskFollow> getTaskFollowsById(long taskId);
    
    public List<BriefTypeMessage> getAllTaskFollows();
    
    public List<BriefTypeMessage> getAllFollwBriefMessages();
    
    public List<MsgReceiver> getAllFollowers();

	public void updateToDelByTaskIds(List<Long> taskIds);
}
