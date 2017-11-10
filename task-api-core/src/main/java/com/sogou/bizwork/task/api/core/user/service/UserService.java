package com.sogou.bizwork.task.api.core.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.po.Subordinate;
import com.sogou.bizwork.task.api.core.user.po.UserInfo;

@Service("userService")
public interface UserService {

    public UserDTO getUserById(int userId);
    
    public List<String> getUserNamesByEmployeeIds(List<Long> ids);

    public List<UserDTO> getAllUsers();

    public List<UserDTO> getUsersByIds(List<Long> ids);
    
    public UserDTO getUserByEmployeeId(int employeeid);
    
    public List<Long> getEmployeeIdsByGroupIds(List<Long> groupIds);
    
    public Long getLeaderIdByEmployeeId(Long employeeId);

	public UserDTO getUserByUserName(String userName);

	public UserInfo getUserInfoByEmployeeId(long userId);

	public List<Subordinate> getSubordinates(List<String> grouppaths);

	public List<Integer> getEmployeeIdsByGrouppaths(List<String> grouppaths);

	public List<String> getGrouppathByEmployeeId(int employeeId);

	public List<String> getGrouppathLikeChineseName(String leader);


}
