namespace java com.sogou.bizwork.task.api.tag.service

include "../result/TagResult.thrift"
include "../to/TagTo.thrift"
include "../../common/exception/ApiTException.thrift"

service TagTService {

	/**
	 * 增加单个标签
	 */
	TagResult.TagResult addTag(1:TagTo.TagTo tagTo) throws (1:ApiTException.ApiTException ex);
	
	/**
	 * 增加多个个标签
	 */
	TagResult.TagResult addTags(1:list<TagTo.TagTo> tagTos) throws (1:ApiTException.ApiTException ex);
	
	/**
	 * 根据用户ID获取标签
	 */
	TagResult.TagResult getTags(1:i64 userId) throws (1:ApiTException.ApiTException ex);
	
	/**
	 * 根据任务ID和用户ID获取标签
	 */
	TagResult.TagResult getTagsByTaskIdAndUserId(1:i64 taskId,2:i64 userId) throws (1:ApiTException.ApiTException ex);
	
	
	/**
	 * 根据任务ID,用户ID和标签名称获取单个标签
	 */
	TagResult.TagResult getTagByTaskIdAndUserIdAndTagName(1:i64 taskId, 2:i64 userId, 3:string tagName) throws (1:ApiTException.ApiTException ex);
	
	/**
	 * 根据标签ID删除单个标签
	 */
	TagResult.TagResult deleteTag(1:i64 tagId) throws (1:ApiTException.ApiTException ex);
	
}