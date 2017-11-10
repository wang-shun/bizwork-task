package com.sogou.bizwork.task.api.web.score.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.bizwork.task.api.core.dto.Result;
import com.sogou.bizwork.task.api.core.score.bo.ScoreAndPeople;
import com.sogou.bizwork.task.api.core.score.bo.ScoreDetail;
import com.sogou.bizwork.task.api.core.score.bo.ScoreFilter;
import com.sogou.bizwork.task.api.core.score.bo.ScoreHistory;
import com.sogou.bizwork.task.api.core.score.bo.ScoreStatus;
import com.sogou.bizwork.task.api.core.score.bo.ScoreType;
import com.sogou.bizwork.task.api.core.score.bo.ScoreVo;
import com.sogou.bizwork.task.api.core.score.service.ScoreService;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.service.UserService;
import com.sogou.bizwork.task.api.web.common.util.UserHolder;
import com.sogou.bizwork.task.api.web.score.vo.ScoreFilterVo;
import com.sogou.bizwork.task.api.web.score.vo.ScoreHistoryRes;
import com.sogou.bizwork.task.api.web.score.vo.ScoreHistoryVo;

@Controller
@RequestMapping("/score")
public class ScoreController {
	
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private UserService userService;
	
	@RequestMapping("/getScoreHistory.do")
	@ResponseBody
	public Result getScoreHistory(@RequestBody ScoreFilterVo scoreFilterVo) {
		Result result = new Result();
		
		ScoreFilter scoreFilter = scoreFilterVo.getScoreFilter();
		ScoreHistoryRes scoreHistoryRes = new ScoreHistoryRes();
		scoreFilter.setStartNo((scoreFilter.getPageNo() - 1) * scoreFilter.getPageSize());
		scoreFilter.setEndNo(scoreFilter.getPageSize());
		int num = scoreService.getScoreHistoryCount(scoreFilter);
		scoreHistoryRes.setTotalNumber(num);
    	List<String> grouppaths = userService.getGrouppathLikeChineseName(scoreFilter.getLeader());
    	if (CollectionUtils.isNotEmpty(grouppaths)) {
    		List<Integer> employeeIds = userService.getEmployeeIdsByGrouppaths(grouppaths);
    		scoreFilter.setEmployeeIds(employeeIds);
    	}
//		List<Subordinate> subordinates = userService.getSubordinatesByName(userInfo.getEmployeeId(), userInfo.getGroupauth());
		List<ScoreHistory> scoreHistories = scoreService.getScoreHistory(scoreFilter, Integer.valueOf(UserHolder.getUserId() + ""));
		for (ScoreHistory ele : scoreHistories) {
			ele.setCanEdit(true);
		}
		scoreHistoryRes.setList(scoreHistories);
		result.setData(scoreHistoryRes);
		return result;
	}
	
	@RequestMapping("/getScoreTypes.do")
	@ResponseBody
	public Result getScoreTypes() {
		Result result = new Result();
		List<ScoreType> scoreHistories = scoreService.getScoreTypes();
		result.setData(scoreHistories);
		return result;
	}
	
	@RequestMapping("/addScore.do")
	@ResponseBody
	public Result addScore(@RequestBody ScoreHistoryVo scoreHistoryVo) {
		Result result = new Result();
		long givePeopleId = UserHolder.getUserId();
		UserDTO givePeopleDto = userService.getUserByEmployeeId(Integer.parseInt(givePeopleId + ""));
		ScoreHistory scoreHistory = scoreHistoryVo.getScoreReward();
		scoreHistory.setGivePeopleId(givePeopleDto.getEmployeeId());
		scoreHistory.setGivePeople(givePeopleDto.getChineseName());
		UserDTO acceptPeopleDto = userService.getUserByEmployeeId(scoreHistory.getAcceptPeopleId());
		scoreHistory.setAcceptPeople(acceptPeopleDto.getChineseName());
		scoreHistory.setGroup(acceptPeopleDto.getGroupName());
		UserDTO leaderDto = userService.getUserByEmployeeId(acceptPeopleDto.getLeaderId());
		scoreHistory.setLeader(leaderDto.getChineseName());
		scoreHistory.setLeaderId(leaderDto.getLeaderId());
		scoreHistory.setScoreStatus("已获得积分");
		scoreHistory.setStatus(2);
		scoreService.addScore(scoreHistory);
		return result;
	}
	

	
	@RequestMapping("/confirmScore.do")
	@ResponseBody
	public Result confirmScore(@RequestBody Map<String, String> params) {
		Result result = new Result();
		int scoreId = Integer.valueOf(params.get("scoreId"));
		scoreService.updateScoreIdByScoreId(scoreId, ScoreStatus.AGREE_SCORE);
		return result;
	}


	
	@RequestMapping("/getScoreRanking.do")
	@ResponseBody
	public Result getScoreRanking() {
		Result result = new Result();
		List<ScoreAndPeople> scoreAndPeoples = scoreService.getScoreAndPeoples();
		Map<String, Object> res = new LinkedHashMap<String, Object>();
		if (CollectionUtils.isNotEmpty(scoreAndPeoples)) {
			List<String> acceptPeople = new ArrayList<String>();
			List<Integer> score = new ArrayList<Integer>();
			for (ScoreAndPeople ele : scoreAndPeoples) {
				acceptPeople.add(ele.getAcceptPeople());
				score.add(ele.getScore());
			}
			res.put("acceptPeople", acceptPeople);
			res.put("score", score);
		}
		result.setData(res);
		return result;
	}


	
	@RequestMapping("/getScoreDetail.do")
	@ResponseBody
	public Result getScoreDetail(@RequestBody Map<String, String> params) {
		Result result = new Result();
		String chineseName = params.get("name");
		List<ScoreDetail> scoreDetails = scoreService.getScoreDetail(chineseName);
		int totalScore = 0;
		for (ScoreDetail ele : scoreDetails) {
			ele.setDateTime(ele.getDateTime().substring(0, ele.getDateTime().length() - 2));
			totalScore += ele.getScore();
		}
		ScoreDetail scoreDetail = new ScoreDetail();
		scoreDetail.setDateTime("总计");
		scoreDetail.setScore(totalScore);
		scoreDetails.add(0, scoreDetail);
		result.setData(scoreDetails);
		return result;
	}


	
	@RequestMapping("/getScoreList.do")
	@ResponseBody
	public Result getScoreList() {
		Result result = new Result();
		List<ScoreVo> scoreVos = scoreService.getScoreList();
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("scoreList", scoreVos);
		result.setData(res);
		return result;
	}
}
