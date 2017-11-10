package com.sogou.bizwork.task.api.core.taskfollow.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.api.message.BriefTypeMessage;
import com.sogou.bizwork.task.api.common.util.MessageUtils;
import com.sogou.bizwork.task.api.core.task.msg.po.MsgReceiver;
import com.sogou.bizwork.task.api.core.taskfollow.dao.TaskFollowMapper;
import com.sogou.bizwork.task.api.core.taskfollow.dto.TaskFollowDTO;
import com.sogou.bizwork.task.api.core.taskfollow.service.TaskFollowService;
import com.sogou.bizwork.task.api.core.user.service.UserService;

@Service("taskFollowService")
public class TaskFollowServiceImpl implements TaskFollowService {

    @Autowired
    private TaskFollowMapper taskFollowMapper;
    @Autowired
    private UserService userService;

    @Override
    public void addTaskFollows(List<TaskFollowDTO> taskFollows) {
        taskFollowMapper.addTaskFollows(taskFollows);
    }

    @Override
    public void delTaskFollowsByTaskId(long taskId) {
        taskFollowMapper.updateTaskFollowsStatusDel(taskId);
    }

    @Override
    public List<BriefTypeMessage> updateAllTaskFollows() {
         return taskFollowMapper.getAllTaskFollows();
    }

    @Override
    public List<BriefTypeMessage> getAllFollwBriefTasks() {
        List<MsgReceiver> mrs = taskFollowMapper.getAllFollowers();
        
        Set<Long> allUserIdSet = new HashSet<Long>();
        Set<Long> groupIdSet = new HashSet<Long>();
        
        if (CollectionUtils.isNotEmpty(mrs)) {
            for (MsgReceiver m : mrs) {
                if (m.getType() == 0) {
                    allUserIdSet.add(m.getId());
                } else if (m.getType() == 1) {
                    groupIdSet.add(m.getId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(groupIdSet)) {
            List<Long> users = userService.getEmployeeIdsByGroupIds(new ArrayList<Long>(groupIdSet));
            if (CollectionUtils.isNotEmpty(users)) {
                for (Long u : users) {
                    allUserIdSet.add(u);
                }
            }
        }
        List<BriefTypeMessage> br = new ArrayList<BriefTypeMessage>();
        if (CollectionUtils.isNotEmpty(allUserIdSet)) {
            br = taskFollowMapper.getFollowBriefTypeMessages(new ArrayList<Long>(allUserIdSet));
        }
        return br;
    }
}
