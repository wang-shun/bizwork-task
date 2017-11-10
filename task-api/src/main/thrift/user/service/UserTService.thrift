namespace java com.sogou.bizwork.task.api.user.service

include "../result/UserResult.thrift"
include "../to/UserTo.thrift"
include "../../common/error/ErrorCode.thrift"
include "../../common/exception/ApiTException.thrift"

service UserTService{
		/**
		 *��ȡ�����û��б�
		 */
 		UserResult.UserResult getUser()throws (1:ApiTException.ApiTException ex);
		
		/**
		 *�����û�Id��ȡ�û���
		 */
		UserResult.UserResult  getUserById(1:i32 userId)throws (1:ApiTException.ApiTException ex);
		
		/**
		 *���������Ż�ȡ������
		 */
		UserResult.UserResult getCreaterByTaskId(1:i32 taskId)throws (1:ApiTException.ApiTException ex);
		
		/**
		 *���������Ż�ȡ��ע���б�
		 */
		UserResult.UserResult getFollowerByTaskId(1:i32 taskId) throws (1:ApiTException.ApiTException ex);
}