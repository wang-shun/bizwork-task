namespace java com.sogou.bizwork.task.api.exception

exception ApiTException {
	1:i32 errorCode = 500,
	2:optional string errorMsg,
	3:bool needI18N = true,//是否需要国际化
	4:optional list<string> arguments,//异常参数
}