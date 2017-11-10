namespace java com.sogou.bizwork.task.api.tasklog.service

include "../result/TaskLogResult.thrift"
include "../to/TaskLogTo.thrift"
include "../../common/error/ErrorCode.thrift"
include "../../common/exception/ApiTException.thrift"

service TaskLogTService{
	/**
	 *@description 根据taskId获取操作日志
	 *@param taskId 任务ID
	 *@param pagination 标记页数，用于分页查询
	 *@return TaskLogResult 返回TaskLogResult
	 */
	TaskLogResult.TaskLogResult getTaskLogsByTaskId(1:i64 taskId) throws (1:ApiTException.ApiTException ex);
	
	/**
	 *@description 删除操作日志
	 *@param taskId 任务ID
	 *@return 返回TaskLogResult
	 */
	TaskLogResult.TaskLogResult deleteTaskLog(1:i64 taskId) throws (1:ApiTException.ApiTException ex);
}