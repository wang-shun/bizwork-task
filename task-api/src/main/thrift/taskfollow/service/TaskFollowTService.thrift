namespace java com.sogou.bizwork.task.api.taskfollow.service

include "../result/TaskFollowResult.thrift"
include "../../common/exception/ApiTException.thrift"

service TaskFollowTService {

	TaskFollowResult.TaskFollowResult updateNewTaskFollowsByTaskId(1:list<i64> newTaskFollows, 2:i64 taskId) throws (1:ApiTException.ApiTException ex);
	
	
	
}