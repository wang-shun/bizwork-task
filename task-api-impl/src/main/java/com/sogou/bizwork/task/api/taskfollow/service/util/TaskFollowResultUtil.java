package com.sogou.bizwork.task.api.taskfollow.service.util;

import com.sogou.bizwork.task.api.common.ResultUtils;
import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.taskfollow.result.TaskFollowResult;

public class TaskFollowResultUtil {
	public static TaskFollowResult newResult(BizErrorEnum be) {
		TaskFollowResult result = new TaskFollowResult();
        if (be != null) {
            result.setStatus(false);
            result.setErrorCode(ResultUtils.newErrorCode(be));
        } else {
            result.setStatus(true);
        }
        return result;
    }
}
