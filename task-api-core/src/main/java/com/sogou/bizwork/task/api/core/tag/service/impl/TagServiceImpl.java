package com.sogou.bizwork.task.api.core.tag.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.core.tag.dao.TagDao;
import com.sogou.bizwork.task.api.core.tag.dto.TagDto;
import com.sogou.bizwork.task.api.core.tag.dto.TagOrderDto;
import com.sogou.bizwork.task.api.core.tag.service.TagService;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public int addTag(TagDto tagDto) {
        return tagDao.addTag(tagDto);
    }

    @Override
    public int addTaskTag(long taskId, long tagId) {
        // 如果已经存在相同记录
        int count = tagDao.getTagBytaskIdAndTagId(taskId, tagId);
        if (count == 0)
            return tagDao.addTaskTag(taskId, tagId);
        return 1;
    }

    @Override
    public int addTags(List<TagDto> tagDtos) {
        return tagDao.addTags(tagDtos);
    }

    @Override
    public List<TagDto> getTags(long userId) {
        return tagDao.getTags(userId);
    }

    @Override
    public List<TagDto> getTagsByTaskIdAndUserId(long taskId, long userId) {
        return tagDao.getTagsByTaskIdAndUserId(taskId, userId);
    }

    @Override
    public int deleteTag(long tagId) {
        return tagDao.deleteTag(tagId);
    }

    @Override
    public int deleteTagsByTaskIdAndTagId(long taskId, long tagId) {
        tagDao.deleteTagsByTaskIdAndTagId(taskId, tagId);
        int count = tagDao.getTagIdCount(tagId);
        if (count == 0) {
            // 删除相应标签
            tagDao.deleteTag(tagId);
        }
        return 0;
    }

    @Override
    public void updateTaskTagStatusDel(long taskId) {
        List<Long> tags = tagDao.getTagsByTaskId(taskId);
        tagDao.updateTaskTagStatusDel(taskId);
        for (long tagId : tags) {
            int count = tagDao.getTagIdCount(tagId);
            if (count == 0) {
                // 删除相应标签
                tagDao.deleteTag(tagId);
            }
        }
    }

    @Override
    public List<TagDto> getTagByUserIdAndTagName(long userId, String tagName) {
        return tagDao.getTagByUserIdAndTagName(userId, tagName);
    }

    @Override
    public List<TagDto> getTagById(long tagId) {
        return tagDao.getTagById(tagId);
    }

    @Override
    public int addTaskTagByName(long taskId, TagDto tagDto) {
        List<TagDto> tagDtos = getTagByUserIdAndTagName(tagDto.getUserId(), tagDto.getName());
        if (tagDtos.size() > 0) {
            addTaskTag(taskId, tagDtos.get(0).getId());
        } else {
            addTag(tagDto);
            tagDtos = getTagByUserIdAndTagName(tagDto.getUserId(), tagDto.getName());
            addTaskTag(taskId, tagDtos.get(0).getId());
        }
        return 0;
    }

    /**
     * 更新用户标签顺序
     */
    @Override
    public void updateTagOrder(TagOrderDto tagOrderDto) {
        tagDao.updateTagOrder(tagOrderDto);
    }

    /**
     * 获取用户标签顺序
     */
    @Override
    public TagOrderDto getTagOrder(long userId, int type) {
        return tagDao.getTagOrder(userId, type);
    }

}
