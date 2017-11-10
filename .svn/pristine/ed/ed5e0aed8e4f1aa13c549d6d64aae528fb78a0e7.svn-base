namespace java com.sogou.bizwork.task.api.user.service

include "../result/UserResult.thrift"
include "../to/UserTo.thrift"
include "../../common/error/ErrorCode.thrift"
include "../../common/exception/ApiTException.thrift"

service UserTService{
		/**
		 *获取所有用户列表
		 */
 		UserResult.UserResult getUser()throws (1:ApiTException.ApiTException ex);
		
		/**
		 *根据用户Id获取用户名
		 */
		UserResult.UserResult  getUserById(1:i32 userId)throws (1:ApiTException.ApiTException ex);
		
		/**
		 *根据任务编号获取创建人
		 */
		UserResult.UserResult getCreaterByTaskId(1:i32 taskId)throws (1:ApiTException.ApiTException ex);
		
		/**
		 *根据任务编号获取关注人列表
		 */
		UserResult.UserResult getFollowerByTaskId(1:i32 taskId) throws (1:ApiTException.ApiTException ex);
}