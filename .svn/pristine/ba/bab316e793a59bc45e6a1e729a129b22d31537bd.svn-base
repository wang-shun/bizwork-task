package com.sogou.bizwork.task.api.web.medal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.bizwork.task.api.core.dto.Result;
import com.sogou.bizwork.task.api.core.medal.service.MedalService;
import com.sogou.bizwork.task.api.web.common.util.UserHolder;
import com.sogou.bizwork.task.api.web.medal.vo.MedalHistoryVo;

@Controller
@RequestMapping("/medal")
public class MedalController {
	
	@Autowired
	private MedalService medalService;
	
	@RequestMapping("/getMedalTypes.do")
	@ResponseBody
	public Result queryScoreTypes() {
		Result result = new Result();
		result.setData(medalService.getMedalTypes());
		return result;
	}
	
	@RequestMapping("/addMedal.do")
	@ResponseBody
	public Result addMedal(@RequestBody MedalHistoryVo medalHistoryVo) {
		Result result = new Result();
		int operatorId = Integer.valueOf(UserHolder.getUserId() + "");
		medalService.addMedal(medalHistoryVo.getMedalReward(), operatorId);
		return result;
	}
	
	
}
