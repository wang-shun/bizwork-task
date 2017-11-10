namespace java com.sogou.bizwork.task.api.common.to

/**
	用于分页查询
*/
enum OrderType{
	ASC,		//升序
	DESC		//降序
}

struct PaginationTo{
	1:i32 pageIndex;						//当前页索引
	2:i32 pageSize;							//当前页展示的结果数
	3:i32 pageStart;						//数据库查询起始记录
	4:optional string orderFieldStr;			//排序字段名称
	5:optional OrderType orderDirection;		//升序：'ASC'；降序：'DESC'
}