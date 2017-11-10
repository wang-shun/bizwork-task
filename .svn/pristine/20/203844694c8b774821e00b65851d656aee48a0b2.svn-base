package com.sogou.bizwork.task.api.web.user.convertor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.sogou.bizwork.task.api.core.user.dto.GroupDTO;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.web.user.vo.UserAndGroupPo;

public class UserConvertor {

    public static List<UserAndGroupPo> convectorUserDTOs2Pos(List<UserDTO> users) {
        if (CollectionUtils.isEmpty(users))
            return Collections.emptyList();
        List<UserAndGroupPo> userPoList = new ArrayList<UserAndGroupPo>();
        for (UserDTO user : users) {
            UserAndGroupPo userPo = convectorUserDTO2Po(user);
            userPoList.add(userPo);
        }
        return userPoList;
    }

    private static UserAndGroupPo convectorUserDTO2Po(UserDTO user) {
        if (null == user)
            return null;
        UserAndGroupPo userPo = new UserAndGroupPo();
        userPo.setName(user.getUserName());
        userPo.setLabel(user.getChineseName());
        userPo.setId(user.getEmployeeId());
        userPo.setType(0);
        return userPo;
    }

    public static List<UserAndGroupPo> convectorGroupDTOs2Pos(List<GroupDTO> groups) {
        if (CollectionUtils.isEmpty(groups))
            return Collections.emptyList();
        List<UserAndGroupPo> groupPoList = new ArrayList<UserAndGroupPo>();
        for (GroupDTO group : groups) {
            UserAndGroupPo userPo = convectorGroupDTO2Po(group);
            groupPoList.add(userPo);
        }
        return groupPoList;
    }

    private static UserAndGroupPo convectorGroupDTO2Po(GroupDTO group) {
        if (null == group)
            return null;
        UserAndGroupPo groupPo = new UserAndGroupPo();
        groupPo.setLabel(group.getGroupName());
        groupPo.setName(group.getGroupName());
        groupPo.setId(group.getId());
        groupPo.setType(1);
        return groupPo;
    }

}
