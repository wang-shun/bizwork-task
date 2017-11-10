package com.sogou.bizwork.task.api.core.task.po;

import java.util.ArrayList;
import java.util.List;

import com.sogou.bizwork.task.api.common.util.BeanUtils;
import com.sogou.bizwork.task.api.core.tag.dto.TagDto;
import com.sogou.bizwork.task.api.core.tag.dto.TagOrderDto;
import com.sogou.bizwork.task.api.tag.to.TagTo;

public class TagConvertor {

    public static TagVo convertTo2Vo(TagTo tagTo) {
        TagVo tagVo = new TagVo();
        if (tagTo != null)
            BeanUtils.copy(tagTo, tagVo);

        return tagVo;
    }

    public static List<TagVo> convertTos2Vos(List<TagTo> tagTos) {
        List<TagVo> tagVos = new ArrayList<TagVo>();
        if (tagTos != null) {
            for (TagTo tagTo : tagTos) {
                tagVos.add(convertTo2Vo(tagTo));
            }
        }

        return tagVos;
    }

    public static TagVo convertDto2Vo(TagDto tagDto) {
        TagVo tagVo = new TagVo();
        if (tagDto != null)
            BeanUtils.copy(tagDto, tagVo);
        return tagVo;
    }

    public static List<TagVo> convertDtos2Vos(List<TagDto> tagDtos) {
        List<TagVo> tagVos = new ArrayList<TagVo>();
        if (tagDtos != null) {
            for (TagDto tagDto : tagDtos) {
                tagVos.add(convertDto2Vo(tagDto));
            }
        }
        return tagVos;
    }

    public static TagOrderDto convertTagOrderVo2Dto(TagOrderVo tagOrderVo) {
        TagOrderDto tagOrderDto = new TagOrderDto();
        if (tagOrderVo != null)
            BeanUtils.copy(tagOrderVo, tagOrderDto);
        return tagOrderDto;
    }
}
