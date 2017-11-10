package com.sogou.bizwork.task.api.web.user.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.biztech.starry.api.StarryTService;
import com.sogou.bizwork.task.api.core.dto.Result;
import com.sogou.bizwork.task.api.core.user.dto.GroupDTO;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.po.Subordinate;
import com.sogou.bizwork.task.api.core.user.po.UserInfo;
import com.sogou.bizwork.task.api.core.user.po.UserJobType;
import com.sogou.bizwork.task.api.core.user.service.GroupService;
import com.sogou.bizwork.task.api.core.user.service.UserService;
import com.sogou.bizwork.task.api.web.common.util.UserHolder;
import com.sogou.bizwork.task.api.web.user.convertor.UserConvertor;
import com.sogou.bizwork.task.api.web.user.vo.UserAndGroupPo;

/**
 * 
 * @author yaojun
 *
 */
@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Resource
    public StarryTService starryTService;

    /**
     * 获取所有用户信息
     * @return
     */
    @RequestMapping("getAll.do")
    @ResponseBody
    public Result getAll() {
        Result result = new Result();
        try {
            List<UserDTO> userDTOs = userService.getAllUsers();
            List<GroupDTO> groupDTOs = groupService.getAllGroups();
            if (CollectionUtils.isEmpty(userDTOs) && CollectionUtils.isEmpty(groupDTOs)) {
                result.setErrorMsg("无用户或者组信息");
                return result;
            }
            List<UserAndGroupPo> userPo = UserConvertor.convectorUserDTOs2Pos(userDTOs);
            List<UserAndGroupPo> groupPo = UserConvertor.convectorGroupDTOs2Pos(groupDTOs);
            userPo.addAll(groupPo);
            result.setData(userPo);

        } catch (Exception e) {
            logger.error("获取所有用户异常,", e);
            result.setErrorMsg("获取所有用户异常");
        }
        return result;
    }


    @RequestMapping("getUserInfo.do")
    @ResponseBody
    public Result getUserInfo() {
    	Result result = new Result();
    	UserInfo userInfo = userService.getUserInfoByEmployeeId(UserHolder.getUserId());
    	userInfo.setUserLastName(userInfo.getChineseName().substring(userInfo.getChineseName().length() - 1));
		userInfo.setTitle(UserJobType.parse(userInfo.getJob()).getText());
    	userInfo.setTitle("开发工程师");
//    	if (StringUtils.isNotBlank(userInfo.getEmail()) && StringUtils.isNotBlank(userInfo.getGroupemail())
//    			&& userInfo.getEmail().equals(userInfo.getGroupemail())) {
    	List<String> groupauths = userService.getGrouppathByEmployeeId(userInfo.getEmployeeId());
    	if (CollectionUtils.isNotEmpty(groupauths)) {
    		List<Subordinate> subordinates = userService.getSubordinates(groupauths);
        	userInfo.setSubordinates(subordinates);
    	}
    	userInfo.setIsLeader(true);
//    	}
    	result.setData(userInfo);
    	return result;
    }

}
