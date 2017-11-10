package com.sogou.bizwork.task.api.task.util;

import java.util.List;

import com.sogou.bizwork.task.api.common.ResultUtils;
import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.task.result.TaskResult;
import com.sogou.bizwork.task.api.task.to.TaskTo;

/**
 * 
 * @author liquancai
 * @description 2016年7月21日
 */
public class TaskResultUtil {
	public static TaskResult newResult(List<TaskTo> taskTo, BizErrorEnum be) {
		TaskResult result = new TaskResult();
        if (be != null) {
            result.setStatus(false);
            result.setErrorCode(ResultUtils.newErrorCode(be));
        } else {
            result.setStatus(true);
        }
        result.setTaskToList(taskTo);
        return result;
    }

    public static TaskResult newResult(BizErrorEnum be) {
        return newResult(null, be);
    }
}
