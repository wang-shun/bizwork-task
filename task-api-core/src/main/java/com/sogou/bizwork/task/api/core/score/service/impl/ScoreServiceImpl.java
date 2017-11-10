package com.sogou.bizwork.task.api.core.score.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.core.score.bo.RecentScore;
import com.sogou.bizwork.task.api.core.score.bo.ScoreAndPeople;
import com.sogou.bizwork.task.api.core.score.bo.ScoreDetail;
import com.sogou.bizwork.task.api.core.score.bo.ScoreFilter;
import com.sogou.bizwork.task.api.core.score.bo.ScoreHistory;
import com.sogou.bizwork.task.api.core.score.bo.ScoreListBo;
import com.sogou.bizwork.task.api.core.score.bo.ScoreTask;
import com.sogou.bizwork.task.api.core.score.bo.ScoreType;
import com.sogou.bizwork.task.api.core.score.bo.ScoreVo;
import com.sogou.bizwork.task.api.core.score.dao.ScoreDao;
import com.sogou.bizwork.task.api.core.score.service.ScoreService;
import com.sogou.bizwork.task.api.score.bo.ScoreItem;

@Service("scoreService")
public class ScoreServiceImpl implements ScoreService{
	@Autowired
	private ScoreDao scoreDao;
	
	@Override
	public List<ScoreHistory> getScoreHistory(ScoreFilter scoreFilter, int operatorId) {
		List<ScoreHistory> scoreHistories = scoreDao.getScoreHistory(scoreFilter);
		for (ScoreHistory ele : scoreHistories) {
			ele.setTime(ele.getTime().substring(0, ele.getTime().length() - 2));
			if (ele.getStatus() == 1 && operatorId == ele.getGivePeopleId()) {
				ele.setNeedScoreConfirm(true);
			}
		}
		return scoreHistories;
	}

	@Override
	public List<ScoreType> getScoreTypes() {
		return scoreDao.getScoreTypes();
	}

	@Override
	public void addScore(ScoreHistory scoreHistory) {
		scoreDao.addScore(scoreHistory);
	}

	@Override
	public void deleteScoreByScoreId(int scoreId) {
		scoreDao.deleteScoreByScoreId(scoreId);
	}

	@Override
	public int getScoreHistoryCount(ScoreFilter scoreFilter) {
		return scoreDao.getScoreHistoryCount(scoreFilter);
	}

	@Override
	public int updateScoreIdByScoreId(int scoreId, int scoreStatus) {
		return scoreDao.updateScoreIdByScoreId(scoreId, scoreStatus);
	}

	@Override
	public List<ScoreTask> getScoreTaskByEmployeeId(int employeeId) {
		return scoreDao.getScoreTaskByEmployeeId(employeeId);
	}

	@Override
	public List<ScoreItem> getScoreItemsByEmployeeId(int employeeId) {
		return scoreDao.getScoreItemsByEmployeeId(employeeId);
	}

	@Override
	public void setScoreToDelete(long taskId) {
		scoreDao.setScoreToDelete(taskId);
		
	}

	@Override
	public List<ScoreAndPeople> getScoreAndPeoples() {
		return scoreDao.getScoreAndPeoples();
	}

	@Override
	public List<ScoreDetail> getScoreDetail(String chineseName) {
		return scoreDao.getScoreDetail(chineseName);
	}

	@Override
	public List<ScoreVo> getScoreList() {
		List<ScoreVo> scoreVos = new ArrayList<ScoreVo>();
		List<ScoreListBo> scoreListBos = scoreDao.getScoreList();
		Map<Integer, String> imgUrlMap = new HashMap<Integer, String>();
		imgUrlMap.put(0, "http://img.store.sogou/app/a/100200010/17b1281b-36ba-4006-b967-7b5d6bd5a4fe-20170629152044.jpg");
		imgUrlMap.put(1, "http://img.store.sogou/app/a/100200010/43aea26a-219b-4580-b12e-5d33f6fe4c22-20170629152044.jpg");
		imgUrlMap.put(2, "http://img.store.sogou/app/a/100200010/a5590333-f537-41a8-bab2-ef6187adbdd5-20170629152044.jpg");
		if (CollectionUtils.isNotEmpty(scoreListBos)) {
			int i = 0;
			for (ScoreListBo ele : scoreListBos) {
				ScoreVo scoreVo = new ScoreVo();
				scoreVo.setAcceptPeople(ele.getAcceptPeople());
				scoreVo.setTotalScore(ele.getTotalScore());
				RecentScore recentScore = new RecentScore();
				recentScore.setDateTime(ele.getUpdateTime().substring(0, 19));
				recentScore.setReason(ele.getReason());
				recentScore.setScore(ele.getScore());
				scoreVo.setRecentScore(recentScore);
				scoreVo.setImgUrl(imgUrlMap.get(i++));
				scoreVos.add(scoreVo);
			}
		}
		return scoreVos;
	}

	@Override
	public ScoreHistory getScoreByScoreId(int scoreId) {
		return scoreDao.getScoreByScoreId(scoreId);
	}
	
}
