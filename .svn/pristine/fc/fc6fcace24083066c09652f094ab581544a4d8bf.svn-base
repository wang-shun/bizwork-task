namespace java com.sogou.bizwork.task.api.task.result

include "../../common/error/ErrorCode.thrift"
include "../to/TaskTo.thrift"

struct TaskResult{
	1:bool status = true;
	2:optional ErrorCode.ErrorCode errorCode;
	3:optional list<TaskTo.TaskTo> taskToList;
	4:i32 totalNumber;
}