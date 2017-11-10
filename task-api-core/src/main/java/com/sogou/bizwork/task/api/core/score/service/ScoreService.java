package com.sogou.bizwork.task.api.core.score.service;

import java.util.List;

import com.sogou.bizwork.task.api.core.score.bo.ScoreAndPeople;
import com.sogou.bizwork.task.api.core.score.bo.ScoreDetail;
import com.sogou.bizwork.task.api.core.score.bo.ScoreFilter;
import com.sogou.bizwork.task.api.core.score.bo.ScoreHistory;
import com.sogou.bizwork.task.api.core.score.bo.ScoreTask;
import com.sogou.bizwork.task.api.core.score.bo.ScoreType;
import com.sogou.bizwork.task.api.core.score.bo.ScoreVo;
import com.sogou.bizwork.task.api.score.bo.ScoreItem;


public interface ScoreService {
	
	List<ScoreHistory> getScoreHistory(ScoreFilter scoreFilter, int operatorId);

	List<ScoreType> getScoreTypes();

	void addScore(ScoreHistory scoreHistory);

	void deleteScoreByScoreId(int scoreId);

	int getScoreHistoryCount(ScoreFilter scoreFilter);

	int updateScoreIdByScoreId(int scoreId, int scoreStatus);

	List<ScoreTask> getScoreTaskByEmployeeId(int employeeId);

	List<ScoreItem> getScoreItemsByEmployeeId(int employeeId);

	void setScoreToDelete(long taskId);

	List<ScoreAndPeople> getScoreAndPeoples();

	List<ScoreDetail> getScoreDetail(String chineseName);

	List<ScoreVo> getScoreList();

	ScoreHistory getScoreByScoreId(int scoreId);
	
	
}
