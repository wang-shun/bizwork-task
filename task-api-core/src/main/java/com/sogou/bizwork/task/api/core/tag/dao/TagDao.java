package com.sogou.bizwork.task.api.core.tag.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sogou.bizwork.task.api.core.tag.dto.TagDto;
import com.sogou.bizwork.task.api.core.tag.dto.TagOrderDto;

@Repository
public interface TagDao {

    public int addTag(TagDto tagDto);

    public int addTaskTag(@Param("taskId") long taskId, @Param("tagId") long tagId);

    public int addTags(List<TagDto> tagDtos);

    public List<TagDto> getTagById(long tagId);

    public List<TagDto> getTags(@Param("userId") long userId);

    public List<Long> getTagsByTaskId(@Param("taskId") long taskId);

    public List<TagDto> getTagsByTaskIdAndUserId(@Param("taskId") long taskId, @Param("userId") long userId);

    public List<TagDto> getTagByUserIdAndTagName(@Param("userId") long userId, @Param("tagName") String tagName);

    public void updateTaskTagStatusDel(@Param("taskId") long taskId);

    public int getTagBytaskIdAndTagId(@Param("taskId") long taskId, @Param("tagId") long tagId);

    public int deleteTag(@Param("tagId") long tagId);

    public int deleteTagsByTaskIdAndTagId(@Param("taskId") long taskId, @Param("tagId") long tagId);

    public int getTagIdCount(@Param("tagId") long tagId);

    public void updateTagOrder(TagOrderDto tagOrderDto);

    public TagOrderDto getTagOrder(@Param("userId") long userId, @Param("type") int type);
}
