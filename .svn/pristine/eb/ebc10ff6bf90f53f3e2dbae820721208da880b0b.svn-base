package com.sogou.bizwork.task.api.core.tag.service;

import java.util.List;

import com.sogou.bizwork.task.api.core.tag.dto.TagDto;
import com.sogou.bizwork.task.api.core.tag.dto.TagOrderDto;

/**
 * 
 * @Title
 * @Description
 * @author fanjianjun
 * @date   2016年7月21日
 */
public interface TagService {

    // 添加单个标签
    public int addTag(TagDto tagDto);

    // 如果用户拥有相同名字的标签，那么直接添加task_tag表(如果存在相同记录，则不添加)，否则要先添加tag表相应标签
    public int addTaskTagByName(long taskId, TagDto tagDto);

    public int addTaskTag(long taskId, long tagId);

    // 批量添加标签
    public int addTags(List<TagDto> tagDtos);

    // 根据标签ID获取标签(不存在则返回null)
    public List<TagDto> getTagById(long tagId);

    // 获取该用户下所有未删除标签
    public List<TagDto> getTags(long userId);

    // 根据用户ID和任务ID获取所有标签
    public List<TagDto> getTagsByTaskIdAndUserId(long taskId, long userId);

    // 根据标签ID删除标签
    public int deleteTag(long tagId);

    // 根据任务ID和标签ID删除所有task_tag表内容
    public int deleteTagsByTaskIdAndTagId(long taskId, long tagId);

    // 删除任务时，删除任务相关的所有task_tag记录
    public void updateTaskTagStatusDel(long taskId);

    // 根据用户ID和标签名称获取标签
    public List<TagDto> getTagByUserIdAndTagName(long userId, String tagName);

    // 更新用户标签顺序
    public void updateTagOrder(TagOrderDto tagOrderDto);

    // 获取用户标签顺序
    public TagOrderDto getTagOrder(long userId, int type);

}
