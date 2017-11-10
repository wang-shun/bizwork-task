package com.sogou.bizwork.task.api.tag.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.common.exception.ApiTException;
import com.sogou.bizwork.task.api.core.tag.dto.TagDto;
import com.sogou.bizwork.task.api.core.tag.service.TagService;
import com.sogou.bizwork.task.api.tag.convertor.TagConvertor;
import com.sogou.bizwork.task.api.tag.result.TagResult;
import com.sogou.bizwork.task.api.tag.service.TagTService;
import com.sogou.bizwork.task.api.tag.to.TagTo;
import com.sogou.bizwork.task.api.tag.validator.TagValidator;

@Service("tagTService")
public class TagTServiceImpl implements TagTService.Iface {

    private static Logger logger = Logger.getLogger(TagTServiceImpl.class);

    @Autowired
    private TagService tagService;

    @Override
    public TagResult addTag(TagTo tagTo) throws ApiTException, TException {
        TagDto tagDto = TagConvertor.convertTo2Dto(tagTo);
        TagResult tagResult = TagValidator.validateTagDto(tagDto);
        if (!tagResult.status) {
            return tagResult;
        }
        try {
            tagService.addTag(tagDto);
        } catch (Exception e) {
            logger.error("add tag error", e);
            ApiTException ate = new ApiTException();
            ate.setErrorCode(e.hashCode());
            ate.initCause(e);
            ate.setMessage(e.getMessage());
            ate.setStackTrace(e.getStackTrace());

            throw ate;
        }

        return tagResult;
    }

    @Override
    public TagResult addTags(List<TagTo> tagTos) throws ApiTException, TException {
        TagResult tagResult = new TagResult();
        List<TagDto> tagDtos = TagConvertor.convertTos2Dtos(tagTos);
        for (TagDto tagDto : tagDtos) {
            tagResult = TagValidator.validateTagDto(tagDto);
            if (!tagResult.status)
                return tagResult;
        }
        try {
            tagService.addTags(tagDtos);
        } catch (Exception e) {
            logger.error("add tag list error", e);
            ApiTException ate = new ApiTException();
            ate.setErrorCode(e.hashCode());
            ate.initCause(e);
            ate.setMessage(e.getMessage());
            ate.setStackTrace(e.getStackTrace());

            throw ate;
        }

        return tagResult;
    }

    @Override
    public TagResult getTags(long userId) throws ApiTException, TException {
        TagResult tagResult = new TagResult();
        List<TagDto> tagDtos = null;
        try {
            tagDtos = tagService.getTags(userId);
            tagResult.setTagTo(TagConvertor.convertDtos2Tos(tagDtos));
            tagResult.setTotalNumber(tagDtos.size());
        } catch (Exception e) {
            logger.error("get tag by tagId error", e);
            ApiTException ate = new ApiTException();
            ate.setErrorCode(e.hashCode());
            ate.initCause(e);
            ate.setMessage(e.getMessage());
            ate.setStackTrace(e.getStackTrace());

            throw ate;
        }

        return tagResult;
    }

    @Override
    public TagResult getTagsByTaskIdAndUserId(long taskId, long userId) throws ApiTException, TException {
        TagResult tagResult = new TagResult();
        List<TagDto> tagDtos = null;
        try {
            tagDtos = tagService.getTagsByTaskIdAndUserId(taskId, userId);
            tagResult.setTagTo(TagConvertor.convertDtos2Tos(tagDtos));
            tagResult.setTotalNumber(tagDtos.size());
        } catch (Exception e) {
            logger.error("get tags by userId and taskId error", e);
            ApiTException ate = new ApiTException();
            ate.setErrorCode(e.hashCode());
            ate.initCause(e);
            ate.setMessage(e.getMessage());
            ate.setStackTrace(e.getStackTrace());

            throw ate;
        }

        return tagResult;
    }

    @Override
    public TagResult deleteTag(long tagId) throws ApiTException, TException {
        TagResult result = new TagResult();
        try {
            tagService.deleteTag(tagId);
        } catch (Exception e) {
            logger.error("delete tag by tagId error", e);
            ApiTException ate = new ApiTException();
            ate.setErrorCode(e.hashCode());
            ate.initCause(e);
            ate.setMessage(e.getMessage());
            ate.setStackTrace(e.getStackTrace());

            throw ate;
        }

        return result;
    }

    @Override
    public TagResult getTagByTaskIdAndUserIdAndTagName(long taskId, long userId, String tagName) throws ApiTException,
            TException {
        TagResult tagResult = new TagResult();
        List<TagDto> tagDtos = null;
        try {
            tagDtos = tagService.getTagByUserIdAndTagName(userId, tagName);
            tagResult.setTagTo(TagConvertor.convertDtos2Tos(tagDtos));
            tagResult.setTotalNumber(tagDtos.size());
        } catch (Exception e) {
            logger.error("get tags by userId, taskId and tagName error", e);
            ApiTException ate = new ApiTException();
            ate.setErrorCode(e.hashCode());
            ate.initCause(e);
            ate.setMessage(e.getMessage());
            ate.setStackTrace(e.getStackTrace());

            throw ate;
        }

        return tagResult;
    }

}
