package com.sogou.bizwork.task.api.core.task.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.jsonplugin.JSONException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jsonplugin.JSONUtil;
import com.sogou.bizwork.task.api.common.util.JSONUtils;
import com.sogou.bizwork.task.api.core.activiti.po.TaskIdAndNewTaskId;
import com.sogou.bizwork.task.api.core.activiti.po.TaskIdAndTimeStamp;
import com.sogou.bizwork.task.api.core.scheduled.bo.ScheduledTask;
import com.sogou.bizwork.task.api.core.scheduled.bo.ScheduledTaskBo;
import com.sogou.bizwork.task.api.core.scheduled.dao.ScheduledTaskDao;
import com.sogou.bizwork.task.api.core.tag.dto.TagDto;
import com.sogou.bizwork.task.api.core.tag.service.TagService;
import com.sogou.bizwork.task.api.core.task.dto.TaskDTO;
import com.sogou.bizwork.task.api.core.task.po.FollowUserVo;
import com.sogou.bizwork.task.api.core.task.po.Result;
import com.sogou.bizwork.task.api.core.task.po.Scheduled;
import com.sogou.bizwork.task.api.core.task.po.TagVo;
import com.sogou.bizwork.task.api.core.task.po.TaskConvertor;
import com.sogou.bizwork.task.api.core.task.po.TaskVo;
import com.sogou.bizwork.task.api.core.task.service.ScheduledTaskService;
import com.sogou.bizwork.task.api.core.task.service.TaskService;

@Service("scheduledTaskService")
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

   private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ScheduledTaskServiceImpl.class);

	@Autowired
	private TaskService taskService;
	@Autowired
	private TagService tagService;

	@Autowired
	private ScheduledTaskDao scheduledTaskDao;


    // 任务状态
    private static final byte TASK_TODO = 0;
    @SuppressWarnings("unused")
	private static final byte TASK_DOING = 1;
    @SuppressWarnings("unused")
	private static final byte TASK_DONE = 2;

	@Override
	public Result addTask(TaskVo taskVo, long operateUserId) {
        Result result = new Result();
        taskVo.setStatus(TASK_TODO);
        taskVo.setCreateUser(operateUserId);
        try {
            TaskDTO taskDTO = TaskConvertor.convertTaskVo2Dto(taskVo);
            long newTaskId = taskService.addTask(taskDTO, taskVo.getCreateUser());
			result.setData(newTaskId);
            // ***********新增代码，用于实现 创建任务任务的时候可以添加标签**************
            // 获取刚才插入的task的ID,用于下面添加标签使用
            long createUser = taskDTO.getCreateUser();
            long chargeUser = taskDTO.getChargeUser();
            String startTime = taskDTO.getStartTime();
            String endTime = taskDTO.getEndTime();
            String taskName = taskDTO.getTaskName();
            List<TaskDTO> taskDto = taskService.queryTaskByFullInfo(createUser, chargeUser, startTime, endTime,
                    taskName);
            if (taskDto == null || taskDto.size() < 1) {
                result.setErrorMsg("查询任务失败，请确认信息填写正确");
            }
           // long taskId = newTaskId;
            List<TagVo> tagVos = taskVo.getTags();
            if (tagVos == null || tagVos.size() < 1) {
                // 该用户创建任务的时候没有添加任何标签
                // do nothing
            } else {
                // 该用户创建任务的时候添加某些标签
                // 得先获取前面添加任务的taskId
                for (TagVo tagVo : tagVos) {
                    TagDto tagDto = new TagDto();
                    tagDto.setUserId(operateUserId);
                    tagDto.setName(tagVo.getName());
                    tagDto.setColor(tagVo.getColor());
                    tagService.addTaskTagByName(newTaskId, tagDto);
                }
            }

            // ************************************************************

            // 新建任务，给所有人发送邮件
			try{
            String response = taskService.sendMailCreatNewTask(operateUserId, taskDTO);
            taskService.sendTaskToXiaoP(operateUserId, taskDTO);

            if (!response.equals(""))
                result.setErrorMsg("发送邮件失败");
           // result.setData(newTaskId);
            }
            catch (Exception ate){
				logger.error("send message erroe", ate);
			}
        } catch (Exception ate) {
          // logger.error("addTask for {} error", taskVo.getTaskId(), ate);
           ate.printStackTrace();
           result.setErrorMsg("addTask Error");
       }
		return result;
	}

	@Override
	public void addScheduledTask(TaskVo scheduledTask, long operateUserId) {
		Scheduled scheduled = scheduledTask.getScheduled();
		if (scheduled == null) return;
		ScheduledTaskBo scheduledTaskBo = new ScheduledTaskBo();
		try {
			ScheduledTask task = new ScheduledTask();
			task.convertTaskVoToTask(scheduledTask);
			scheduledTaskBo.setTask(JSONUtils.serializeObject(task));
//			scheduledTaskBo.setTask(JSONUtil.serialize(task));
		} catch (Exception e) {
			e.printStackTrace();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		scheduledTaskBo.setCreateUser((int)operateUserId);
		scheduledTaskBo.setTaskId(scheduledTask.getTaskId());
		scheduledTaskBo.setPeriodFrequency(scheduled.getPeriodFrequency());
		scheduledTaskBo.setPeriodSpecific(scheduled.getPeriodSpecific());
		scheduledTaskBo.setPeriodValid(scheduled.getPeriodValid());
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY,10);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		scheduledTaskBo.setUpdateTime(Timestamp.valueOf(format.format(calendar.getTimeInMillis())));
		scheduledTaskDao.addScheduledTask(scheduledTaskBo);
	}

	public static void main(String[] args) {
		String json = "{\"taskName\":\"周期任务检查\",\"chargeUser\":3812,\"followUsers\":[{\"id\":3812,\"type\":0,\"name\":null,\"displayName\":null}],\"tags\":[{\"id\":0,\"name\":\"你好\",\"color\":\"rgb(89, 194, 3)\"}],\"description\":null}";
		try {
			TaskVo taskVo = (TaskVo) JSONUtils.deserializeObject(json, TaskVo.class);
			List<FollowUserVo> followUserVo = taskVo.getFollowUsers();
			List<TagVo> tagVo = taskVo.getTags();
			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void updateScheduledTasks() {
		List<ScheduledTaskBo> validScheduledTaskBos = scheduledTaskDao.getValidTasks();  // 未完成的周期任务  status = 0
		Set<Long> needDelTasks = new HashSet<Long>();
		Set<Integer> needAddTasks = new HashSet<Integer>();
		Map<String, Object> params = new HashMap<String, Object>();
		Calendar cd = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<TaskIdAndTimeStamp> needUpdateDate = new ArrayList<TaskIdAndTimeStamp>();
		for (ScheduledTaskBo ele : validScheduledTaskBos) {
			int taskFre = ele.getPeriodFrequency();
			int specificDay = ele.getPeriodSpecific();
			int scheduledTaskId = ele.getId();
			long taskId = ele.getTaskId();
			TaskIdAndTimeStamp t1 = new TaskIdAndTimeStamp();
      if (ele.getUpdateTime() == null)continue;
			Date tempDate = new Date();
			tempDate.setTime(ele.getUpdateTime().getTime());
			Calendar tempCalendar = Calendar.getInstance();
			tempCalendar.setTime(tempDate);
			tempCalendar.set(Calendar.HOUR_OF_DAY, 10);
			tempCalendar.set(Calendar.MINUTE, 0);
			tempCalendar.set(Calendar.SECOND, 0);
			int dayGap = (int)((cd.getTimeInMillis() - tempCalendar.getTimeInMillis() + 600 * 1000 * 2) / 86400000);  // 加了十分钟转换成天数
			if (ele.getPeriodValid() == 3 && ele.getPeriodFrequency() == 1){    //new classmates
				switch (dayGap){
					case 1:	needDelTasks.add(ele.getTaskId());needAddTasks.add(ele.getId());  // second day
						break;
					case 2: needDelTasks.add(ele.getTaskId());needAddTasks.add(ele.getId());  //last day
						break;
					default:
						break;
				}

			}else {
				if (dayGap >= ele.getPeriodValid()) {      //it is time
					needDelTasks.add(taskId);
					switch (taskFre){
						case 1:
							needAddTasks.add(scheduledTaskId);
							t1.setTaskId(taskId);
							t1.setUpdateTime(Timestamp.valueOf(format.format(now.getTimeInMillis())));
							needUpdateDate.add(t1);
							break;
						case 2:
							int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
							int needAddWeek = specificDay % 7 + 1;
							if (dayOfWeek == needAddWeek){
								needAddTasks.add(scheduledTaskId);
								t1.setTaskId(taskId);
								t1.setUpdateTime(Timestamp.valueOf(format.format(now.getTimeInMillis())));
								needUpdateDate.add(t1);
							}
							break;
						case 3:
							int maxDateOfMath = cd.getActualMaximum(Calendar.DATE);
							if (specificDay < 0)
								specificDay = maxDateOfMath + specificDay + 1;
							if (cd.get(Calendar.DATE) == specificDay) {
								needAddTasks.add(scheduledTaskId);
								t1.setTaskId(ele.getTaskId());
								t1.setUpdateTime(Timestamp.valueOf(format.format(now.getTimeInMillis())));
								needUpdateDate.add(t1);
							}
							break;
						default:
							break;
					}
				}
			}
		}
		System.out.println("----------------------" + needDelTasks.size() + "---------" + needUpdateDate.size());
		if (CollectionUtils.isNotEmpty(needDelTasks)) {
			taskService.updateToDelByTaskIds(new ArrayList<Long>(needDelTasks));   // status = -1  add new task    delete old task
		}
		if (CollectionUtils.isNotEmpty(needUpdateDate)) {
			scheduledTaskDao.updateDate(needUpdateDate);  // base on taskID to update
		}
		if (CollectionUtils.isNotEmpty(needAddTasks)) {     //需要新建任务
			List<ScheduledTaskBo> scheduledTaskBos = scheduledTaskDao.getScheduledTasksByIds(new ArrayList<Integer>(needAddTasks));
			try {
				List<TaskIdAndNewTaskId> taskIds = new ArrayList<TaskIdAndNewTaskId>();
				for (ScheduledTaskBo scheduledTaskBo : scheduledTaskBos) {
					TaskVo taskVo = (TaskVo) JSONUtils.deserializeObject(scheduledTaskBo.getTask(),TaskVo.class);
					if (taskVo != null) {
						if (scheduledTaskBo.getPeriodValid() == 3 && scheduledTaskBo.getPeriodFrequency() == 1){
							int dayGAP = (int)((cd.getTimeInMillis() - scheduledTaskBo.getUpdateTime().getTime() + 600 * 1000 * 2) / 86400000);
							if (dayGAP == 1){
								taskVo.setTaskName("新人第二天任务");
							}
							else if (dayGAP == 2){
								taskVo.setTaskName("新人第三天任务");
								this.setTaskToDelete(scheduledTaskBo.getTaskId());
							}
						}
						Result result = addTask(taskVo, scheduledTaskBo.getCreateUser());
						long newTaskId = (Long) result.getData();
						System.out.println("newTaskId is -------------- " + newTaskId);
						TaskIdAndNewTaskId taskIdAndNewTaskId = new TaskIdAndNewTaskId();
						taskIdAndNewTaskId.setTaskId(scheduledTaskBo.getTaskId());
						taskIdAndNewTaskId.setNewTaskId(newTaskId);
						taskIds.add(taskIdAndNewTaskId);
						System.out.println(taskIds.size());
					}
				}
				if (CollectionUtils.isNotEmpty(taskIds)) {
					scheduledTaskDao.updateTaskIds(taskIds);
					System.out.println("update scheduled tasks-----------------");
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	@Override
	public ScheduledTaskBo getScheduledTaskByTaskId(long taskId) {
		return scheduledTaskDao.getTaskByTaskId(taskId);
	}

	@Override
	public void setTaskToDelete(long taskId) {
		List<Long> taskIds = new ArrayList<Long>();
		taskIds.add(taskId);
		scheduledTaskDao.updateToDelByTaskIds(taskIds);
	}

	@Override
	public void deleteTaskByTaskId(long taskId) {
		scheduledTaskDao.deleTaskByTaskId(taskId);
	}

}
