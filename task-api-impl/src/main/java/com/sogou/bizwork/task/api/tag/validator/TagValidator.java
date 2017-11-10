package com.sogou.bizwork.task.api.tag.validator;

import org.apache.commons.lang.StringUtils;

import com.sogou.bizwork.task.api.constant.common.BizErrorEnum;
import com.sogou.bizwork.task.api.core.tag.dto.TagDto;
import com.sogou.bizwork.task.api.tag.result.TagResult;
import com.sogou.bizwork.task.api.tag.util.TagResultUtil;

/**
 * api tag validator
 * @author fanjianjun
 *
 */
public class TagValidator {
	private static final byte TAG_LIVE = 0;
	private static final byte TAG_DELETE = -1;
	
    public static TagResult validateTagDto(TagDto tagDto) {
        if (StringUtils.isBlank(tagDto.getName())) {
            return TagResultUtil.newResult(BizErrorEnum.TAG_NAME_NULL);
        }
        if(tagDto.getStatus() != TAG_LIVE && tagDto.getStatus() != TAG_DELETE){
        	return TagResultUtil.newResult(BizErrorEnum.TAG_STATUS_ERROR);
        }
        
        return new TagResult();

    }

    public static TagResult validateTagStatus(int status) {
        if (status == 0 || status == -1) {
            return new TagResult();
        }
        return TagResultUtil.newResult(BizErrorEnum.TAG_STATUS_ERROR);
    }
}
