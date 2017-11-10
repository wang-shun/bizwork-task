package com.sogou.bizwork.task.api.core.user.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.hibernate.annotations.Parameter;
import org.springframework.stereotype.Repository;

import com.sogou.bizwork.task.api.core.activiti.po.UserAndLeader;
import com.sogou.bizwork.task.api.core.user.dto.GroupDTO;
import com.sogou.bizwork.task.api.core.user.dto.GroupInfoDto;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.po.ChineseNameAndGroup;
import com.sogou.bizwork.task.api.core.user.po.Subordinate;
import com.sogou.bizwork.task.api.core.user.po.UserInfo;

@Repository("userMapper")
public interface UserMapper {

    public List<UserDTO> getAllUsers();

    public List<UserDTO> getUsersByIds(List<Integer> userIds);

    public UserDTO getUserById(int id);
    
    public List<String> getUserNamesByEmployeeIds(List<Long> ids);

    public List<GroupDTO> getAllGroups();

    public GroupDTO getGroupById(int id);
    
    public UserDTO getUserByEmployeeId(int employeeId);
    
    public UserDTO getUserByUserName(String userName);
    
    public List<Long> getEmployeeIdsByGroupIds(List<Long> groupIds);

	public Long getLeaderIdByEmployeeId(Long employeeId);
	
	public List<ChineseNameAndGroup> getChineseNameAndGroup(List<Long> employeeIds);
	
	public int getGroupLeaderCount(Long employeeId);
	
	public List<UserAndLeader> getUserAndLeader();

	public void updateActiveTime(List<UserAndLeader> userAndLeaders);

	public GroupInfoDto getGroupInfoByEmployeeId(int employeeId);

	public UserInfo getUserInfoByEmployeeId(long employeeId);
	
	public List<Subordinate> getSubordinates(List<String> groupauths);

	public Set<String> getUnLeaveUserNames();

	public List<Integer> getEmployeeIdsByGrouppaths(List<String> grouppaths);

	public List<String> getGrouppathByEmployeeId(int leaderId);

	public List<String> getGrouppathLikeChineseName(String leader);
	
	
}
