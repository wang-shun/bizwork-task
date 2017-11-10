package com.sogou.bizwork.task.api.tag.convertor;

import java.util.ArrayList;
import java.util.List;

import com.sogou.bizwork.task.api.common.util.BeanUtils;
import com.sogou.bizwork.task.api.core.tag.dto.TagDto;
import com.sogou.bizwork.task.api.tag.to.TagTo;

/**
 * 
 * @Title
 * @Description
 * @author fanjianjun
 * @date   2016年7月20日
 */
public class TagConvertor {
    
    public static TagDto convertTo2Dto(TagTo tagTo){
    	TagDto tagDto = new TagDto();
    	if(tagTo != null)
    		BeanUtils.copy(tagTo, tagDto);
    	
    	return tagDto;
    }
    
    public static List<TagDto> convertTos2Dtos(List<TagTo> tagTos){
    	List<TagDto> tagDtos = new ArrayList<TagDto>();
    	if(tagTos != null){
    		for(TagTo tagTo : tagTos){
    			tagDtos.add(convertTo2Dto(tagTo));
    		}
    	}
    	
    	return tagDtos;
    }

    public static List<TagTo> convertDtos2Tos(List<TagDto> tags) {
        List<TagTo> tagTos = new ArrayList<TagTo>();

        if (tags == null) {
            return tagTos;
        }

        for (TagDto tagDto : tags) {
            tagTos.add(convertDto2To(tagDto));
        }

        return tagTos;
    }

    public static TagTo convertDto2To(TagDto tagDto) {
        TagTo tagTo = new TagTo();
        BeanUtils.copy(tagDto, tagTo);

        return tagTo;
    }

}
