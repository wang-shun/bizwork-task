package com.sogou.bizwork.task.api.taskfollow.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.common.exception.ApiTException;
import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.core.taskfollow.po.TaskFollow;
import com.sogou.bizwork.task.api.core.taskfollow.service.TaskFollowService;
import com.sogou.bizwork.task.api.taskfollow.result.TaskFollowResult;
import com.sogou.bizwork.task.api.taskfollow.service.TaskFollowTService;
import com.sogou.bizwork.task.api.taskfollow.service.util.TaskFollowResultUtil;

@Service("taskFollowTService")
public class TaskFollowTServiceImpl implements TaskFollowTService.Iface {

    private static Logger logger = Logger.getLogger(TaskFollowTServiceImpl.class);

    @Autowired
    private TaskFollowService taskFollowService;

    @Override
    public TaskFollowResult updateNewTaskFollowsByTaskId(List<Long> NewTaskFollows, long taskId) throws ApiTException,
            TException {
        TaskFollowResult result = new TaskFollowResult();
        try {
            taskFollowService.delTaskFollowsByTaskId(taskId);
            if (NewTaskFollows != null && NewTaskFollows.size() > 0) {
                List<TaskFollow> follows = new ArrayList<TaskFollow>();
                for (Long follow : NewTaskFollows) {
                    TaskFollow newTaskFollow = new TaskFollow();
                    newTaskFollow.setTaskId(taskId);
                    newTaskFollow.setStatus((byte) 0);
                    newTaskFollow.setFollowUser(follow);
                    follows.add(newTaskFollow);
                }
                // taskFollowService.addTaskFollows(follows);//添加新的关注人列表
            }
        } catch (DataAccessException e) {
            logger.error("updateNewTaskFollowsByTaskId DataAccessException", e);
            result = TaskFollowResultUtil.newResult(BizErrorEnum.SYSTEM_ERROR);
        } catch (Exception e) {
            logger.error("updateNewTaskFollowsByTaskId ERROR", e);
            throw new ApiTException(e.hashCode()).setMessage("updateNewTaskFollowsByTaskId ERROR " + e.getMessage());
        }
        return result;
    }

}