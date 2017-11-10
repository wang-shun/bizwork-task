namespace java com.sogou.bizwork.task.api.tag.result

include "../../common/error/ErrorCode.thrift"
include "../to/TagTo.thrift"

struct TagResult {
	1:bool status = true;
	2:optional ErrorCode.ErrorCode errorCode;
	3:optional list<TagTo.TagTo> tagTo;
	4:i32 totalNumber;
	5:i32 totalPage;
}