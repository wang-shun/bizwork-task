package com.sogou.bizwork.task.api.core.task.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sogou.bizwork.api.message.BriefTypeMessage;
import com.sogou.bizwork.task.api.core.task.dto.TaskDTO;
import com.sogou.bizwork.task.api.core.task.po.AuditScore;
import com.sogou.bizwork.task.api.core.user.dto.GroupInfoDto;

public interface TaskMapper {

    public TaskDTO queryTaskInfo(long taskId);

    public List<TaskDTO> queryChargeTasks(long userId);

    // 查询势力任务是否被删除过
    public List<TaskDTO> queryDemo(long userId);

    public List<TaskDTO> queryFollowTasks(@Param("userId") long userId, @Param("groupId") long groupId);

    public void addTask(TaskDTO taskDTO);

    public int updateTask(TaskDTO taskDTO);

    public void updateChargeUser(@Param("taskId") long taskId, @Param("chargeUserId") long chargeUserId);

    public List<Long> getChargeUser();// 得到t_task表中所有的charge_user的id

    public List<TaskDTO> getDoneJob(Long chargeUser);// 通过charge_user的id来得到这个人完成的（以逾期的工作）

    public List<TaskDTO> getCommingJob(Long chargeUser);// 通过charge_user的id来得到这个人进行中的任务（进行中的任务包括TODO
                                                        // 和DOING任务到截止任务3天的时间）

    public List<TaskDTO> getTodoJob(Long chargeUser);// 通过charge_user的id来得到这个人TODO的任务

    public BriefTypeMessage getChargeBriefTypeMessages(long employeeId);

    public BriefTypeMessage getChargeBriefTypeMessagesById(long taskId);

    public List<BriefTypeMessage> getAllChargeBriefTypeMessages();

    public List<TaskDTO> queryTaskByFullInfo(@Param("createUser") long createUser,
            @Param("chargeUser") long chargeUser, @Param("startTime") String startTime,
            @Param("endTime") String endTime, @Param("taskName") String taskName);

	public void updateToDelByTaskIds(List<Long> taskIds);

	public GroupInfoDto getGroupInfoByGroupname(String groupname);

	public List<AuditScore> getAuditScoresByEmployeeid(long employeeid);

	public void updateScoreIdByTaskId(@Param("taskId") long taskId, @Param("scoreId") int scoreId);

	public void updateScoreStatusByTaskId(@Param("taskId") long taskId, @Param("scoreStatus") int scoreStatus);

}