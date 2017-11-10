package com.sogou.bizwork.task.api.core.task.po;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sogou.bizwork.task.api.common.util.BeanUtils;
import com.sogou.bizwork.task.api.core.task.dto.TaskDTO;

/**
 * @description
 * @author liquancai
 * @date 2016年8月8日
 */
public class TaskConvertor {

    private static final Logger logger = LoggerFactory.getLogger(TaskConvertor.class);

    public static TaskVo convertTaskDto2Vo(TaskDTO taskDto) {
        TaskVo taskVo = new TaskVo();
        if (taskDto == null)
            return taskVo;
        BeanUtils.copy(taskDto, taskVo);
        taskVo.setTaskId(taskDto.getId());
        if (taskDto.getStartTime() != null)
            taskVo.setStartTime(taskDto.getStartTime().substring(0, 10)); // 只截取"yyyy-MM-dd"
        if (taskDto.getEndTime() != null)
            taskVo.setEndTime(taskDto.getEndTime().substring(0, 10));

        taskVo.setFollowUsers(FollowUserConvertor.convertTaskFollowDTOs2Vos(taskDto.getFollowUsers()));
        taskVo.setTags(TagConvertor.convertDtos2Vos(taskDto.getTags()));
        return taskVo;
    }

    public static TaskDTO convertTaskVo2Dto(TaskVo taskVo) {
        TaskDTO taskDTO = new TaskDTO();
        if (taskVo == null)
            return taskDTO;
        BeanUtils.copy(taskVo, taskDTO);
        taskDTO.setId(taskVo.getTaskId());
        if (taskVo.getStartTime() != null && taskVo.getStartTime() != "")
            taskDTO.setStartTime(taskVo.getStartTime() + " 00:00:00");
        else
            taskDTO.setStartTime("0000-00-00 00:00:00");
        if (taskVo.getEndTime() != null && taskVo.getEndTime() != "")
            taskDTO.setEndTime(taskVo.getEndTime() + " 00:00:00");
        else
            taskDTO.setEndTime("0000-00-00 00:00:00");
        taskDTO.setFollowUsers(FollowUserConvertor.convertFollowUserVos2Dtos(taskVo.getFollowUsers()));
        return taskDTO;
    }

    /**
     * 获取当前日期字符串，格式:yyyy-MM-dd
     * @return
     */
    public static String getCurrentDateStr() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
