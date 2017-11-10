package com.sogou.bizwork.task.api.web.task.convertor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.sogou.bizwork.task.api.core.task.po.FollowUserVo;
import com.sogou.bizwork.task.api.core.taskfollow.dto.TaskFollowDTO;

/**
 * @description
 * @author liquancai
 * @date 2016年8月8日
 */
public class FollowUserConvertor {

    public static TaskFollowDTO convertFollowUserVo2Dto(FollowUserVo followUser) {
        TaskFollowDTO followUserDTO = new TaskFollowDTO();
        // BeanUtils.copy(followUser, followUserDTO);
        followUserDTO.setFollowUser(followUser.getId());
        followUserDTO.setType(followUser.getType());

        return followUserDTO;
    }

    public static List<TaskFollowDTO> convertFollowUserVos2Dtos(List<FollowUserVo> followUsers) {
        List<TaskFollowDTO> followUserDTO = new ArrayList<TaskFollowDTO>();
        if (CollectionUtils.isEmpty(followUsers))
            return Collections.emptyList();

        for (FollowUserVo followUser : followUsers) {
            followUserDTO.add(convertFollowUserVo2Dto(followUser));
        }

        return followUserDTO;
    }

    public static FollowUserVo convertTaskFollowDTO2Vo(TaskFollowDTO followUser) {
        FollowUserVo followUserVo = new FollowUserVo();
        followUserVo.setId(followUser.getFollowUser());
        followUserVo.setType(followUser.getType());
        return followUserVo;
    }

    public static List<FollowUserVo> convertTaskFollowDTOs2Vos(List<TaskFollowDTO> followUsers) {
        List<FollowUserVo> followUserVos = new ArrayList<FollowUserVo>();
        if (CollectionUtils.isEmpty(followUsers))
            return Collections.emptyList();

        for (TaskFollowDTO followUser : followUsers) {
            followUserVos.add(convertTaskFollowDTO2Vo(followUser));
        }

        return followUserVos;
    }
}
