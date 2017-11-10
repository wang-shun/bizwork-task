package com.sogou.bizwork.task.api.task.util;

import com.sogou.bizwork.task.api.common.ResultUtils;
import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.task.result.FileUploadResult;

/**
 * 
 * @author yaojun
 *
 */
public class FileUploadResultUtil {
	
	public static FileUploadResult newResult(BizErrorEnum be) {
		FileUploadResult result = new FileUploadResult();
        if (be != null) {
            result.setStatus(false);
            result.setErrorCode(ResultUtils.newErrorCode(be));
        } else {
            result.setStatus(true);
        }
        return result;
    }

}
