namespace java com.sogou.bizwork.task.api.tasklog.result

include "../to/TaskLogTo.thrift"
include "../../common/error/ErrorCode.thrift"

struct TaskLogResult{
	1:bool status = true;
	2:optional ErrorCode.ErrorCode errorCode;
	3:optional list<TaskLogTo.TaskLogTo> taskLogToList;
	4:i32 totalNumber;
}