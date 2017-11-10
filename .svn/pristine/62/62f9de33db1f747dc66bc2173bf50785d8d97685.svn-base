package com.sogou.bizwork.task.api.core.task.msg.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.sogou.adm.bizdev.bizflowapi.thrift.dto.query.TaskQueryTEntity;
import com.sogou.adm.bizdev.bizflowapi.thrift.dto.response.ResponseTask;
import com.sogou.adm.bizdev.bizflowapi.thrift.iface.task.TaskTService;
import com.sogou.bizwork.task.api.core.activiti.service.WorkflowService;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.api.message.BriefTypeMessage;
import com.sogou.bizwork.api.message.service.BizworkMessageTService;
import com.sogou.bizwork.task.api.common.util.MessageUtils;
import com.sogou.bizwork.task.api.core.message.service.MessageService;
import com.sogou.bizwork.task.api.core.task.msg.service.BriefTaskMessage;
import com.sogou.bizwork.task.api.core.task.service.TaskService;
import com.sogou.bizwork.task.api.core.taskfollow.service.TaskFollowService;

@Service("briefTaskMessage")
public class BriefTaskMessageImpl implements BriefTaskMessage {
    
    @Autowired
    private MessageService messageService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskFollowService taskFollowService;
    @Autowired
    private BizworkMessageTService.Iface bizworkMessageTService;
    @Autowired
    private WorkflowService workflowService;       // 封装了activiti SOA方法
    @Autowired
    private UserService userService;
    @Autowired
    private TaskTService.Iface taskTService;  // workflow soa xml 中
    
    
    @Override
    public void updateTasksAndMessageToBizwork() {

        List<BriefTypeMessage> btm1 = taskService.getAllChargeBriefTypeTasks();  //我负责的任务-待跟进
        List<BriefTypeMessage> btm2 = taskFollowService.getAllFollwBriefTasks();  //我关注的任务-待跟进  status = 0
        List<BriefTypeMessage> btm3 = messageService.getAllChargeBriefTypeMessages();  //我负责的任务-新进度
        List<BriefTypeMessage> btm4 = messageService.getAllFollowBriefBypeMessages();  //我关注的任务-新进度
        List<BriefTypeMessage> btm5 = new ArrayList<BriefTypeMessage>();       //all flow
        List<UserDTO> allUsers = userService.getAllUsers();
        List<BriefTypeMessage> btm = new ArrayList<BriefTypeMessage>();
        System.out.println("-------------------------------------------------------- this is update message!!!!!!");
        long startTime = System.currentTimeMillis();
        for (UserDTO user : allUsers){
            TaskQueryTEntity taskQueryTEntity = new TaskQueryTEntity();
            taskQueryTEntity.setUserId(user.getEmployeeId() + "");
            try {
                ResponseTask responseTask = taskTService.getTaskList("bizwork",taskQueryTEntity,0,-1);
                BriefTypeMessage briefTypeMessage = new BriefTypeMessage();
                briefTypeMessage.setMesTypeId((short) 10);
                briefTypeMessage.setNum(responseTask.getTotalNum());
                briefTypeMessage.setEmployeeId(user.getEmployeeId());
                btm5.add(briefTypeMessage);
              //  break;
            } catch (TException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-------------------------------------------------------------------- end");
        System.out.println(System.currentTimeMillis() - startTime);
        if (CollectionUtils.isNotEmpty(btm1)) {
            btm.addAll(btm1);
        }
        if (CollectionUtils.isNotEmpty(btm2)) {
            btm.addAll(btm2);
        }
        if (CollectionUtils.isNotEmpty(btm3)) {
            btm.addAll(btm3);
        }
        if (CollectionUtils.isNotEmpty(btm4)) {
            btm.addAll(btm4);
        }
        if (CollectionUtils.isNotEmpty(btm5)){
            btm.addAll(btm5);
        }
        if (CollectionUtils.isNotEmpty(btm)) {
            try {
                bizworkMessageTService.insertBriefMessage(btm);
            } catch (Exception e) {
                MessageUtils.printStackTrace(e);
            }
        }
    }
    
}
