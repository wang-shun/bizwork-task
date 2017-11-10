package com.sogou.bizwork.task.api.task.validator;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.task.result.TaskResult;
import com.sogou.bizwork.task.api.task.to.TaskTo;
import com.sogou.bizwork.task.api.task.util.TaskResultUtil;

/**
 * 
 * @author liquancai
 * @date 2016年7月21日
 */
public class TaskValidator {

    public static TaskResult validateTaskDto(TaskTo taskTo) {
        String startTime = taskTo.startTime;
        String endTime = taskTo.endTime;
        String taskName = taskTo.taskName;

        if (startTime != null && endTime != null && !startTime.equals("") && !endTime.equals("")) {
            try {
                Timestamp startTimestamp;
                Timestamp endTimestamp;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (!startTime.equals("")) {
                    startTimestamp = new Timestamp(sdf.parse(startTime).getTime());
                } else if (!endTime.equals("")) {
                    endTimestamp = new Timestamp(sdf.parse(endTime).getTime());
                } else {
                    startTimestamp = new Timestamp(sdf.parse(startTime).getTime());
                    endTimestamp = new Timestamp(sdf.parse(endTime).getTime());
                    if (endTimestamp.before(startTimestamp)) {
                        return TaskResultUtil.newResult(BizErrorEnum.TASK_START_END_TIME_ERROR);
                    }
                }
            } catch (ParseException e) {
                return TaskResultUtil.newResult(BizErrorEnum.TASK_START_END_TIME_INVALID);
            }
        }

        if (StringUtils.isBlank(taskName)) {
            return TaskResultUtil.newResult(BizErrorEnum.TASK_NAME_NULL);
        }

        return new TaskResult();
    }

    public static TaskResult validateTaskStatus(Byte status) {
        if (status == (byte) 0 || status == (byte) 1 || status == (byte) 2 || status == (byte) -1) {
            return new TaskResult();
        } else {
            return TaskResultUtil.newResult(BizErrorEnum.TASK_STATUS_ERROR);
        }
    }
}
