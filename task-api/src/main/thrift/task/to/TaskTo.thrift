namespace java com.sogou.bizwork.task.api.task.to

include "../../tag/to/TagTo.thrift"

struct TaskTo{
    1:i64 id;
	2:i64 createUser;
	3:i64 chargeUser;
	4:list<i64> followUsers;
	5:string startTime;
	6:string endTime;
	7:string taskName;
	8:string description;
	9:string attachment;
	10:byte status;
	11:string createTime;
	12:string changeTime;
	13:list<TagTo.TagTo> tags;
}