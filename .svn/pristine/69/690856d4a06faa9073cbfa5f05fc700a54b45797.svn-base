package com.sogou.bizwork.task.api.core.scheduled;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sogou.bizwork.task.api.core.activiti.service.EntryService;
import com.sogou.bizwork.task.api.core.bizservicetree.service.BizServiceTreeService;
import com.sogou.bizwork.task.api.core.task.dto.TaskDTO;
import com.sogou.bizwork.task.api.core.task.msg.service.BriefTaskMessage;
import com.sogou.bizwork.task.api.core.task.service.ScheduledTaskService;
import com.sogou.bizwork.task.api.core.task.service.TaskService;
import com.sogou.bizwork.task.api.zkclient.service.ServiceTreeZKClient;

@Component("scheduledTask")
public class ScheduledTask {
    private static final Logger logger = Logger.getLogger(ScheduledTask.class);

    @Autowired
    private TaskService taskService;
    @Autowired
    private ServiceTreeZKClient serviceTreeZKClient;
    @Autowired
    private BriefTaskMessage briefTaskMessage;
    @Autowired
    private ScheduledTaskService scheduledTaskService;
    @Autowired
    private BizServiceTreeService bizServiceTreeService;
    @Autowired
    private EntryService entryService;



    // 本机IP
    private static String ip = "";
    // 标记当时任务是否已经在执行
    private volatile boolean isRunning = false;  // 保证可见性和有序性，有可能被多个任务访问


    static {
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("get ip of local address error", e);
        }
    }

//    @Scheduled(fixedRate=20000)
    @Scheduled(cron = "0 10 10 ? * *") // 定时任务 second minute hour day month weekday year 每天10点10分
    public void sendProjectEmail() {
        if (isRunning)
            return;

        if (StringUtils.isEmpty(ip)) {
            logger.warn("ip of local address is empty");
            return;
        }
        try {
            boolean lockAviable = serviceTreeZKClient.getTaskLock(ip);  // 获得分布式锁
            if (!lockAviable) {
                logger.info("this task cannot get task running lock object");
                return;
            }
            isRunning = true;

            List<Long> chargeUser = new ArrayList<Long>();
             chargeUser = taskService.getChargeUser();
//            chargeUser.add(3151l);
//            chargeUser.add(3113l);
//            chargeUser.add(204516l);
//            chargeUser.add(115667l);
//            chargeUser.add(113329l);
//            chargeUser.add(205617l);
//            chargeUser.add(117368l);
//            chargeUser.add(206576l);
//            chargeUser.add(200758l);
//            chargeUser.add(207345l);

            for (long id : chargeUser) {
                List<TaskDTO> list1 = taskService.getDoneJob(id);// 已逾期  over deadline
                List<TaskDTO> list2 = taskService.getCommingJob(id);// 将进行 没做的或者还没做完的还有不到三天就到期了
                List<TaskDTO> list3 = taskService.getTodoJob(id);// 未跟进    没做的
                taskService.sendMailAlert(id, list1, list2, list3);  //  send mail
            }
        } catch (Exception e) {
            logger.error("bizwork-task scheduled failed!", e);
        } finally {
            isRunning = false;
        }
    }

//    @Scheduled(fixedRate=600000)
    @Scheduled(cron = "0 40/40 7-23 * * ?")  // 每天7点到晚上11点一个小时一次
    public void updateBriefMsgToBizwork() {
        if (isRunning)
            return;

        if (StringUtils.isEmpty(ip)) {
            logger.warn("ip of local address is empty");
            return;
        }
        try {
            boolean lockAviable = serviceTreeZKClient.getTaskLock(ip);
            if (!lockAviable) {
                logger.info("this task cannot get task running lock object");
                return;
            }
            isRunning = true;
            logger.info("begin push task to bizwork display");
            briefTaskMessage.updateTasksAndMessageToBizwork();  //这些任务推到bizwork 用于在bizwork首页display消息提醒
	    } catch (Exception e) {
	        logger.error("bizwork-task scheduled failed!", e);
	    } finally {
	        isRunning = false;
	    }
    }

//    @Scheduled(fixedRate=20000)
    @Scheduled(cron = "0 0 10 ? * *")  // 每天早上10点
    public void addScheduledTask() {
        if (isRunning)
            return;

        if (StringUtils.isEmpty(ip)) {
            logger.warn("ip of local address is empty");
            return;
        }
        try {
            boolean lockAviable = serviceTreeZKClient.getTaskLock(ip);
            if (!lockAviable) {
                logger.info("this task cannot get task running lock object");
                return;
            }
            isRunning = true;
            logger.info("--------->   begin check schedule task!!!!");
            scheduledTaskService.updateScheduledTasks();     //周期任务检查

	    } catch (Exception e) {
	        logger.error("bizwork-task scheduled failed!", e);
	    } finally {
	        isRunning = false;
	    }
    }

	@Scheduled(cron = "0 5/30 9-19 * * ?")    //朝九晚七，每小时的5分和35分触发 从9:5分开始,每half hour trigger
    // @Scheduled(cron = "0 0 */1 * * ?")     //新员工入职工作流
    public void addEntryProcess() {
        if (isRunning)
            return;
        if (StringUtils.isEmpty(ip)) {
            logger.warn("ip of local address is empty");
            return;
        }
        try {
            boolean lockAviable = serviceTreeZKClient.getTaskLock(ip);
            if (!lockAviable) {
                logger.info("this task cannot get task running lock object");
                return;
            }
            isRunning = true;
            entryService.updateEntryProcess();  //检查是否有新人;
        } catch (Exception e) {
            logger.error("bizwork-task scheduled failed!", e);
        } finally {
            isRunning = false;
        }
    }


//  @Scheduled(fixedRate=20000)
  @Scheduled(cron = "0 0 6 ? * *")    //每天早上6点更新服务树信息至bizwork-task数据库
  public void addServiceTree() {
      if (isRunning)
          return;

      if (StringUtils.isEmpty(ip)) {
          logger.warn("ip of local address is empty");
          return;
      }
      try {
          bizServiceTreeService.addServiceTreeInfo();
          boolean lockAviable = serviceTreeZKClient.getTaskLock(ip);
          if (!lockAviable) {
              logger.info("this task cannot get task running lock object");
              return;
          }
          isRunning = true;

          bizServiceTreeService.addServiceTreeInfo();

	    } catch (Exception e) {
	        logger.error("bizwork-task scheduled failed!", e);
	    } finally {
	        isRunning = false;
	    }
  }

}
