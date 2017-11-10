namespace java com.sogou.bizwork.task.api.user.result

include "../../common/error/ErrorCode.thrift"
include "../to/UserTo.thrift"

struct UserResult{
	1:bool status = true;
	2:optional ErrorCode.ErrorCode errorCode;
	3:optional list<UserTo.UserTo> userToList;
	4:i32 totalNumber;
}