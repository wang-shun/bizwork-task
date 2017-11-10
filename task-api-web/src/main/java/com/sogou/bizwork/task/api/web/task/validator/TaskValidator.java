package com.sogou.bizwork.task.api.web.task.validator;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.core.dto.Result;
import com.sogou.bizwork.task.api.web.task.vo.TaskVo;

/**
 * @description
 * @author liquancai
 * @date 2016年8月8日
 */
public class TaskValidator {

    public static void validateTask(Result result, com.sogou.bizwork.task.api.core.task.po.TaskVo taskVo) {
        result.setSuccess(Result.FAILED);

        if (taskVo.getStartTime() != null && taskVo.getEndTime() != null && taskVo.getStartTime() != ""
                && taskVo.getEndTime() != "") {

            // 验证开始时间和截止时间格式是否正确，以及开始时间 <=截止时间
            try {
                Timestamp startTimestamp;
                Timestamp endTimestamp;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (taskVo.getStartTime() == "") {
                    endTimestamp = new Timestamp(sdf.parse(taskVo.getEndTime()).getTime());
                } else if (taskVo.getEndTime() == "") {
                    startTimestamp = new Timestamp(sdf.parse(taskVo.getStartTime()).getTime());
                } else {
                    startTimestamp = new Timestamp(sdf.parse(taskVo.getStartTime()).getTime());
                    endTimestamp = new Timestamp(sdf.parse(taskVo.getEndTime()).getTime());
                    if (endTimestamp.before(startTimestamp)) {
                        result.setErrorMsg(BizErrorEnum.TASK_START_END_TIME_ERROR.getMessage());
                        return;
                    }
                }
            } catch (ParseException e) {
                result.setErrorMsg(BizErrorEnum.TASK_START_END_TIME_INVALID.getMessage());
                return;
            }
            // task_name
            if (taskVo.getTaskName() == null) {
                result.setErrorMsg(BizErrorEnum.TASK_NAME_NULL.getMessage());
                return;
            }

        }

        // 通过所有验证
        result.setSuccess(Result.SUCCESS);
    }
}
