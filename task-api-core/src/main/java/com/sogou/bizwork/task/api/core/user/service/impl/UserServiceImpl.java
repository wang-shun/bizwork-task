package com.sogou.bizwork.task.api.core.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sogou.biztech.starry.api.StarryTService;
import com.sogou.biztech.starry.entity.UserEntity;
import com.sogou.bizwork.task.api.core.user.dao.UserMapper;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.po.Subordinate;
import com.sogou.bizwork.task.api.core.user.po.UserInfo;
import com.sogou.bizwork.task.api.core.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    public StarryTService starryTService;
    @Resource
    public UserMapper userDao;
    @Override
    public List<UserDTO> getAllUsers() {
        // UserEntity.Builder ub = new UserEntity.Builder();
        // List<UserEntity> userList =
        // starryTService.getUser(ub.build()).getUserEntityList();
        // List<UserDTO> userDTOs = convertEntitysToUsers(userList);
        List<UserDTO> userDTOs = userDao.getAllUsers();
        return userDTOs;
    }

    @Override
    public UserDTO getUserById(int userId) {
//         UserEntity.Builder ub = new UserEntity.Builder();
//         ub.setId(userId);
//         List<UserEntity> userList =
//         starryTService.getUser(ub.build()).getUserEntityList();
//         if (userList != null && userList.size() < 1) {
//         UserDTO userDTO = new UserDTO();
//         userDTO.setId(userId);
//         return userDTO;
//         } else {
//         return convertEntityToUser(userList.get(0));
//         }
//    	List<BriefTypeMessage> brMessages = new ArrayList<BriefTypeMessage>();
//    	BriefTypeMessage testMsg = new BriefTypeMessage();
//    	testMsg.setEmployeeId(204516);
//    	testMsg.setNum(1);
//    	testMsg.setMesTypeId((short) 1);
//    	 try {
// 			bizworkMessageTService.insertBriefMessage(brMessages);
// 		} catch (ApiTException e) {
// 			e.printStackTrace();
// 		} catch (TException e) {
// 			e.printStackTrace();
// 		}
        UserDTO userDTO = userDao.getUserById(userId);
        if (null == userDTO) {
            userDTO = new UserDTO();
            userDTO.setId(userId);
        }
        return userDTO;
    }

    @Override
    public List<UserDTO> getUsersByIds(List<Long> ids) {
        List<UserDTO> userDTOs = new ArrayList<UserDTO>();
        if (ids == null || ids.size() < 1)
            return userDTOs;
        List<Integer> list = new ArrayList<Integer>();
        for (Long temp : ids) {
            list.add(temp.intValue());
        }
        // List<UserEntity> userList =
        // starryTService.getUserByIdList(list).getUserEntityList();
        // return convertEntitysToUsers(userList);
        userDTOs = userDao.getUsersByIds(list);
        return userDTOs;
    }

    /*
     * UserEntity转为User类型 User 用于操作数据库 UserEntity用于接口交互
     */
    public UserDTO convertEntityToUser(UserEntity userEntity) {
        if (userEntity != null) {
            UserDTO user = new UserDTO();
            user.setId(userEntity.getId());
            user.setUserName(userEntity.getUserName());
            user.setEmail(userEntity.getEmail());
            user.setChineseName(userEntity.getChineseName());
            user.setGroupName(userEntity.getGroupName());
            user.setGroupId(userEntity.getGroupId());
            user.setBirthday(userEntity.getBirthday());
            user.setTelephone(userEntity.getTelephone());
            user.setMobile(userEntity.getMobile());
            user.setRole(userEntity.getRole());
            user.setState(userEntity.getState());
            user.setLevel(userEntity.getLevel());
            user.setJob(userEntity.getJob());
            user.setPhoto(userEntity.getPhoto());
            return user;
        }
        return new UserDTO();
    }

    public List<UserDTO> convertEntitysToUsers(List<UserEntity> userEntitys) {
        List<UserDTO> userDTOs = new ArrayList<UserDTO>();
        if (userEntitys.isEmpty())
            return userDTOs;
        for (UserEntity userEntity : userEntitys) {
            userDTOs.add(convertEntityToUser(userEntity));
        }
        return userDTOs;
    }

    
    @Override
    public UserDTO getUserByEmployeeId(int employeeid) {
        UserDTO userDTO = userDao.getUserByEmployeeId(employeeid);
        if (null == userDTO) {
            userDTO = new UserDTO();
            userDTO.setId(employeeid);
        }
        return userDTO;
    }

    @Override
    public List<Long> getEmployeeIdsByGroupIds(List<Long> groupIds) {
        return userDao.getEmployeeIdsByGroupIds(groupIds);
    }

	@Override
	public List<String> getUserNamesByEmployeeIds(List<Long> ids) {
		return userDao.getUserNamesByEmployeeIds(ids);
	}

	@Override
	public Long getLeaderIdByEmployeeId(Long employeeId) {
		return userDao.getLeaderIdByEmployeeId(employeeId);
	}

	@Override
	public UserDTO getUserByUserName(String userName) {
		return userDao.getUserByUserName(userName);
	}

	@Override
	public UserInfo getUserInfoByEmployeeId(long employeeId) {
		return userDao.getUserInfoByEmployeeId(employeeId);
	}

	@Override
	public List<Subordinate> getSubordinates(List<String> groupauths) {
		return userDao.getSubordinates(groupauths);
	}

	@Override
	public List<Integer> getEmployeeIdsByGrouppaths(List<String> grouppaths) {
		return userDao.getEmployeeIdsByGrouppaths(grouppaths);
	}

	@Override
	public List<String> getGrouppathByEmployeeId(int employeeId) {
		return userDao.getGrouppathByEmployeeId(employeeId);
	}

	@Override
	public List<String> getGrouppathLikeChineseName(String leader) {
		return userDao.getGrouppathLikeChineseName(leader);
	}

}
