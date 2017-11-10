namespace java com.sogou.bizwork.task.api.task.service

include "../result/TaskResult.thrift"
include "../to/TaskTo.thrift"
include "../../common/error/ErrorCode.thrift"
include "../../common/exception/ApiTException.thrift"
include "../../common/to/PaginationTo.thrift"

service TaskTService{
	/**
	 * @description 查询指定用户的任务
	 * @param userId 负责人ID
	 * @param viewType 视图类型，0：状态视图，1：标签视图
	 * @param userType 条件用户是负责人还是关注人,0:负责人，2：关注人
	 * @return 
	 */
	TaskResult.TaskResult queryUserTaskByStatusOrTag(1:i64 userId, 2:i32 viewType, 3:i32 userType) throws (1:ApiTException.ApiTException ex);
	
	/**
	 * @description 查询指定任务详情
	 * @param userId 任务ID
	 * @return 
	 */
	TaskResult.TaskResult queryTaskInfo(1:i64 taskId) throws (1:ApiTException.ApiTException ex);
	
	/**
	 * @description 添加任务
	 * @param taskTo 
	 * @param operateUser 操作用户ID
	 * @return
	 */
	TaskResult.TaskResult addTask(1:TaskTo.TaskTo taskTo, 2:i64 operateUser) throws (1:ApiTException.ApiTException ex);
	
	/**
	 * @description 更新指定任务ID的任务详情
	 * @param taskTo 更新后的任务
	 * @param operateUser 操作用户ID
	 * @return 
	 */
	TaskResult.TaskResult updateTask(1:TaskTo.TaskTo taskTo, 2:i64 operateUser) throws (1:ApiTException.ApiTException ex);
	
	/**
	 * @description 删除任务
	 * @param taskTo 任务
	 * @param operateUser 操作用户ID
	 * @return
	 */
	TaskResult.TaskResult deleteTask(1:TaskTo.TaskTo taskTo, 2:i64 operateUser) throws (1:ApiTException.ApiTException ex);
	
}