include "../type/ScoreInfo.thrift"
include "../../common/exception/ApiExceptions.thrift"
include "../type/MedalInfo.thrift"

namespace java com.sogou.bizwork.task.api.achievement.service

service AchievementTService {
	ScoreInfo.ScoreInfo getScoreInfo(1:i32 employeeId) throws(1:ApiExceptions.ApiTException e),
	
	list<MedalInfo.MedalInfo> getMedalInfo(1:i32 employeeId) throws(1:ApiExceptions.ApiTException e),
}