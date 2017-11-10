package com.sogou.bizwork.task.api.task.convertor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.sogou.bizwork.cas.utils.StringUtils;
import com.sogou.bizwork.task.api.common.util.BeanUtils;
import com.sogou.bizwork.task.api.core.tag.dto.TagDto;
import com.sogou.bizwork.task.api.core.task.dto.TaskDTO;
import com.sogou.bizwork.task.api.core.task.po.Task;
import com.sogou.bizwork.task.api.tag.to.TagTo;
import com.sogou.bizwork.task.api.task.to.TaskTo;

/**
 * @description thrift对象与TaskDTO的转换
 * @author yangbing
 * @date 2016-7-21
 * @version 1.0.0
 */
public class TaskConvertor {

    public static TaskDTO convertToDTO(TaskTo taskTo) {
        if (taskTo == null)
            return null;

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(taskTo.getId());
        taskDTO.setCreateUser(taskTo.getCreateUser());
        taskDTO.setChargeUser(taskTo.getChargeUser());

        List<Long> followUserIds = taskTo.getFollowUsers();
        if (CollectionUtils.isNotEmpty(followUserIds)) {
            // List<FollowUserDTO> followUsers = new
            // ArrayList<FollowUserDTO>(followUserIds.size());
            for (long followUserId : followUserIds) {
                // followUsers.add(new FollowUserDTO(taskTo.getId(),
                // followUserId));
            }
            // taskDTO.setFollowUsers(followUsers);
        }

        taskDTO.setStartTime(taskTo.getStartTime());
        taskDTO.setEndTime(taskTo.getEndTime());
        taskDTO.setTaskName(taskTo.getTaskName());
        taskDTO.setDescription(taskTo.getDescription());
        taskDTO.setAttachment(taskTo.getAttachment());
        taskDTO.setStatus(taskTo.getStatus());
        taskDTO.setCreateTime(taskTo.getCreateTime());
        taskDTO.setChangeTime(taskTo.getChangeTime());

        return taskDTO;
    }

    public static Task convertTo2Po(TaskTo taskTo) {
        if (taskTo == null)
            return null;

        Task task = new Task();
        BeanUtils.copy(taskTo, task);

        return task;
    }

    public static List<TaskTo> convertDtos2Tos(List<TaskDTO> tasks) {
        if (CollectionUtils.isEmpty(tasks))
            return Collections.emptyList();

        List<TaskTo> taskTos = new ArrayList<TaskTo>();
        for (TaskDTO taskDto : tasks) {
            taskTos.add(convertDto2To(taskDto));
        }

        return taskTos;
    }

    public static List<TaskDTO> convertTos2Dtos(List<TaskTo> taskTos) {
        if (taskTos == null)
            return Collections.emptyList();

        List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
        for (TaskTo taskTo : taskTos) {
            taskDTOs.add(convertToDTO(taskTo));
        }

        return taskDTOs;
    }

    public static TaskTo convertDto2To(TaskDTO taskDto) {
        if (taskDto == null)
            return null;

        TaskTo taskTo = new TaskTo();
        taskTo.setId(taskDto.getId());
        taskTo.setCreateUser(taskDto.getCreateUser());
        taskTo.setChargeUser(taskDto.getChargeUser());

        // List<FollowUserDTO> followUsers = taskDto.getFollowUsers();
        // if (CollectionUtils.isNotEmpty(followUsers)) {
        // List<Long> followUserIds = new ArrayList<Long>(followUsers.size());
        // for(FollowUserDTO fuDTO: followUsers){
        // followUserIds.add(fuDTO.getFollowUser());
        // }
        // taskTo.setFollowUsers(followUserIds);
        // }

        List<TagDto> tags = taskDto.getTags();
        if (CollectionUtils.isNotEmpty(tags)) {
            List<TagTo> tagTos = new ArrayList<TagTo>(tags.size());
            for (TagDto tag : tags) {
                TagTo tagTo = new TagTo();
                tagTo.setId(tag.getId());
                tagTo.setTaskId(tag.getTaskId());
                tagTo.setUserId(tag.getUserId());
                tagTo.setName(tag.getName());
                String color = tag.getColor();
                if (StringUtils.isEmpty(color)) {
                    color = "#59C203";
                }
                tagTo.setColor(color);
                tagTos.add(tagTo);
            }
            taskTo.setTags(tagTos);
        }

        taskTo.setStartTime(taskDto.getStartTime());
        taskTo.setEndTime(taskDto.getEndTime());
        taskTo.setTaskName(taskDto.getTaskName());
        taskTo.setDescription(taskDto.getDescription());
        taskTo.setAttachment(taskDto.getAttachment());
        taskTo.setStatus(taskDto.getStatus());
        taskTo.setCreateTime(taskDto.getCreateTime());
        taskTo.setChangeTime(taskDto.getChangeTime());

        return taskTo;
    }

}
