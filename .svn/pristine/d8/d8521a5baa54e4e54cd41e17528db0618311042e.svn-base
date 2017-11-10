package com.sogou.bizwork.task.api.core.score.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sogou.bizwork.task.api.core.score.bo.ScoreAndPeople;
import com.sogou.bizwork.task.api.core.score.bo.ScoreDetail;
import com.sogou.bizwork.task.api.core.score.bo.ScoreFilter;
import com.sogou.bizwork.task.api.core.score.bo.ScoreHistory;
import com.sogou.bizwork.task.api.core.score.bo.ScoreListBo;
import com.sogou.bizwork.task.api.core.score.bo.ScoreTask;
import com.sogou.bizwork.task.api.core.score.bo.ScoreType;
import com.sogou.bizwork.task.api.score.bo.ScoreItem;

@Repository("scoreDao")
public interface ScoreDao {

	List<ScoreHistory> getScoreHistory(ScoreFilter scoreFilter);

	List<ScoreType> getScoreTypes();

	void addScore(ScoreHistory scoreHistory);

	void deleteScoreByScoreId(int scoreId);

	int getScoreHistoryCount(ScoreFilter scoreFilter);

	int updateScoreIdByScoreId(@Param("scoreId") int scoreId, @Param("scoreStatus") int scoreStatus);

	List<ScoreTask> getScoreTaskByEmployeeId(int employeeId);

	List<ScoreItem> getScoreItemsByEmployeeId(int employeeId);

	void setScoreToDelete(long taskId);

	List<ScoreAndPeople> getScoreAndPeoples();

	List<ScoreDetail> getScoreDetail(String chineseName);

	List<ScoreListBo> getScoreList();

	ScoreHistory getScoreByScoreId(int scoreId);

}
