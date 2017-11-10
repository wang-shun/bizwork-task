package com.sogou.bizwork.task.api.achievement.service.impl;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.achievement.service.*;
import com.sogou.bizwork.task.api.core.medal.service.MedalService;
import com.sogou.bizwork.task.api.core.score.bo.ScoreHistory;
import com.sogou.bizwork.task.api.core.score.bo.ScoreTask;
import com.sogou.bizwork.task.api.core.score.service.ScoreService;
import com.sogou.bizwork.task.api.exception.ApiTException;
import com.sogou.bizwork.task.api.medal.bo.MedalInfo;
import com.sogou.bizwork.task.api.score.bo.ScoreInfo;
import com.sogou.bizwork.task.api.score.bo.ScoreItem;

@Service("achievementTService")
public class AchievementTServiceImpl implements AchievementTService.Iface {
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private MedalService medalService;

	@Override
	public List<MedalInfo> getMedalInfo(int employeeId) throws ApiTException, TException {
		return medalService.getMedalInfoByEmployeeId(employeeId);
	}

	@Override
	public ScoreInfo getScoreInfo(int employeeId) throws ApiTException, TException {
		ScoreInfo scoreInfo = new ScoreInfo();
		List<ScoreTask> scoreTasks = scoreService.getScoreTaskByEmployeeId(employeeId);
		List<String> scoreHistory = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(scoreTasks)) {
			for (ScoreTask ele : scoreTasks) {
				String his = "积分类型：" + ele.getScoreType() + "，获得积分：" + ele.getScore()
				+ "，给分人：" + ele.getGivePeople();
				if (ele.getScoreFrom() == 0) {  //leader直接添加
					if (StringUtils.isNotBlank(ele.getReason())) {
						his += "，给分原因：" + ele.getReason();
					} else {
						his += "，给分原因：未填写"; 
					}
				} else if (ele.getScoreFrom() == 1) {    //获得积分任务
					his += "，给分原因：完成任务【" + ele.getTaskName() + "】";
				}
				his += "，" + ele.getUpdateTime().substring(0, ele.getUpdateTime().length() - 2);
				scoreHistory.add(his);
			}
		}
		List<ScoreItem> scoreItems = scoreService.getScoreItemsByEmployeeId(employeeId);
		if (CollectionUtils.isNotEmpty(scoreItems)) {
			int totalScore = 0;
			for (ScoreItem ele : scoreItems) {
				totalScore += ele.getScore();
			}
			scoreInfo.setTotalScore(totalScore);
			scoreInfo.setScoreList(scoreItems);
		}
		scoreInfo.setScoreHistory(scoreHistory);
		return scoreInfo;
	}

}
