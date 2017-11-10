package com.sogou.bizwork.task.api.core.task.service;

import java.util.List;
import java.util.Map;

import com.sogou.bizwork.api.message.BriefTypeMessage;
import com.sogou.bizwork.task.api.core.activiti.po.UserAndLeader;
import com.sogou.bizwork.task.api.core.score.bo.ScoreHistory;
import com.sogou.bizwork.task.api.core.task.dto.TaskDTO;
import com.sogou.bizwork.task.api.core.task.po.AuditScore;
import com.sogou.bizwork.task.api.core.task.po.Task;
import com.sogou.bizwork.task.api.core.task.po.TaskVo;
import com.sogou.bizwork.task.api.core.taskfollow.dto.TaskFollowDTO;
import com.sogou.bizwork.task.api.core.user.dto.GroupInfoDto;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;


public interface TaskService {

    /**
     * @description 查询指定任务详情
     * @param taskId 任务的ID
     * @return 任务实体
     */
    public TaskDTO queryTaskInfo(long taskId);

    /**
     * @description 查询指定用户负责任务中某个标签下的任务
     * @param userId 负责人ID
     * @param viewType 视图类型，0:状态视图，1：标签视图
     * @param userType 查询用户负责类型：0：查询负责人，1：查询关注人
     * @return 任务实体列表
     */
    public List<TaskDTO> queryTasksWithUserType(long userId, Integer userType);

    /**
     * 查询demos
     * @param userId
     * @return
     */
    public List<TaskDTO> queryDemos(long userId);

    public void sendTaskToXiaoP(long operateUserId, TaskDTO taskDTO);

    /**
     * @description 添加新任务
     * @param taskDTO
     * @return
     */
    public long addTask(TaskDTO taskDTO, long operateUser);

    /**
     * @description 更新任务
     * @param taskId
     * @param taskDTO
     * @return
     */
    public boolean updateTask(TaskDTO taskDTO, long operateUser);

    public void updateChargeUser(long taskId, long chargeUserId, long operateUser);

    /**
     * @description 删除任务
     * @param taskId
     * @return
     */
    public boolean deleteTask(TaskDTO taskDTO, long operateUser);

    /**
     * 发送提醒邮件到任务负责人
     */
    public String sendMailAlert(long operateUserId, TaskDTO taskDTO);

    /**
     * 任务负责人发生变更，给所有人发送邮件
     */
    public String sendMailChargeUserChange(long operateUserId, TaskDTO taskDTO);

    /**
     * 新建任务，给所有人发送邮件
     */
    public String sendMailCreatNewTask(long operateUserId, TaskDTO taskDTO);

    /**
     *  任务状态发生变更，给所有人发送邮件
     */
    public String sendMailStatusChange(long operateUserId, TaskDTO taskDTO);

    /**
     *  删除任务，给所有人发送邮件
     */
    public String sendMailDeleteTask(long operateUserId, TaskDTO taskDTO);

    /**
     * 
     * @param taskDTO
     * @param operateUser
     * @return
     */
    public boolean deleteFollowUser(TaskDTO taskDTO, TaskFollowDTO operateUserDTO);

    public List<Long> getChargeUser();

    public List<TaskDTO> getDoneJob(Long chargeUser);

    public List<TaskDTO> getCommingJob(Long chargeUser);

    public List<TaskDTO> getTodoJob(Long chargeUser);

    /**
     * 发送提醒邮件到任务负责人
     */
    public String sendMailAlert(long id, List<TaskDTO> list1, List<TaskDTO> list2, List<TaskDTO> list3);

    public List<BriefTypeMessage> updateAllChargeBriefMsg();

    public List<BriefTypeMessage> getAllChargeBriefTypeTasks();

    public List<TaskDTO> queryTaskByFullInfo(long createUser, long chargeUser, String startTime, String endTime,
            String taskName);

    public void sendSvnApproveMail(String businessKey, long receiverUserId);

	public void updateToDelByTaskIds(List<Long> taskIds);

	String sendMail(long operateUserId, String mailBody, String mailSubject, String mailTo);
	
	public void sendEntryEmail(String mailBody, String mailSubject, String mailTo);

	public GroupInfoDto getGroupInfoByGroupname(String groupname);

	public List<AuditScore> getAuditScoresByEmployeeid(long employeeid);

	ScoreHistory getScoreHistory(TaskVo scheduledTask, int opearatorId);

	public void updateScoreIdByTaskId(long taskId, int scoreId);

	public void updateScoreStatusByTaskId(long taskId, int scoreStatus);
	
}
