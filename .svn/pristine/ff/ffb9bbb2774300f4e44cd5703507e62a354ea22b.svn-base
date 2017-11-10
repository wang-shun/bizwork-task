package com.sogou.bizwork.task.api.tag.util;

import java.util.List;

import com.sogou.bizwork.task.api.common.ResultUtils;
import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.tag.result.TagResult;
import com.sogou.bizwork.task.api.tag.to.TagTo;

public class TagResultUtil {

    public static TagResult newResult(List<TagTo> tagTo, BizErrorEnum be) {
        TagResult result = new TagResult();
        if (be != null) {
            result.setStatus(false);
            result.setErrorCode(ResultUtils.newErrorCode(be));
        } else {
            result.setStatus(true);
        }
        result.setTagTo(tagTo);
        return result;
    }

    public static TagResult newResult(BizErrorEnum be) {
        return newResult(null, be);
    }
}
