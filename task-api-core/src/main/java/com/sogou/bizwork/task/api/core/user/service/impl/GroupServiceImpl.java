package com.sogou.bizwork.task.api.core.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sogou.biztech.starry.api.StarryTService;
import com.sogou.biztech.starry.entity.GroupEntity;
import com.sogou.bizwork.task.api.core.user.dao.UserMapper;
import com.sogou.bizwork.task.api.core.user.dto.GroupDTO;
import com.sogou.bizwork.task.api.core.user.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

    @Resource
    public StarryTService starryTService;
    @Resource
    public UserMapper userDao;

    @Override
    public List<GroupDTO> getAllGroups() {
        // GroupEntity.Builder gb = new GroupEntity.Builder();
        // List<GroupEntity> groupList =
        // starryTService.getGroupList(gb.build()).getGroupEntityList();
        // List<GroupDTO> groupDTOs = convertEntitysToGroups(groupList);
        List<GroupDTO> groupDTOs = userDao.getAllGroups();
        return groupDTOs;
    }

    @Override
    public GroupDTO getGroupById(int groupId) {
        // GroupEntity.Builder gb = new GroupEntity.Builder();
        // gb.setId(groupId);
        // List<GroupEntity> groupList =
        // starryTService.getGroupList(gb.build()).getGroupEntityList();
        // if (groupList != null && groupList.size() < 1) {
        // GroupDTO groupDTO = new GroupDTO();
        // groupDTO.setId(groupId);
        // return groupDTO;
        // } else {
        // return convertEntityToGroup(groupList.get(0));
        // }
        GroupDTO groupDTO = userDao.getGroupById(groupId);
        if (null == groupDTO) {
            groupDTO = new GroupDTO();
            groupDTO.setId(groupId);
        }
        return groupDTO;
    }

    public GroupDTO convertEntityToGroup(GroupEntity groupEntity) {
        if (groupEntity != null) {
            GroupDTO group = new GroupDTO();
            group.setId(groupEntity.getId());
            group.setGroupName(groupEntity.getGroupName());
            group.setChineseName(groupEntity.getChineseName());
            group.setParentGroup(groupEntity.getParentGroup());
            group.setLeader(groupEntity.getLeader());
            group.setLeaderEmail(groupEntity.getLeaderEmail());
            group.setGroupEmail(groupEntity.getGroupEmail());
            group.setGroupFunction(groupEntity.getGroupFunction());
            group.setGroupType(groupEntity.getGroupType());
            group.setGroupState(groupEntity.getGroupState());
            return group;
        }
        return new GroupDTO();
    }

    public List<GroupDTO> convertEntitysToGroups(List<GroupEntity> groupEntitys) {
        List<GroupDTO> groupDTOs = new ArrayList<GroupDTO>();
        if (groupEntitys.isEmpty())
            return groupDTOs;
        for (GroupEntity groupEntity : groupEntitys) {
            groupDTOs.add(convertEntityToGroup(groupEntity));
        }
        return groupDTOs;
    }

}
