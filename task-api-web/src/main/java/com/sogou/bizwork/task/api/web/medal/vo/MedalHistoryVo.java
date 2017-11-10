package com.sogou.bizwork.task.api.web.medal.vo;

import com.sogou.bizwork.task.api.core.medal.bo.MedalHistory;

public class MedalHistoryVo {
	private String sid;
	private MedalHistory medalReward;
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public MedalHistory getMedalReward() {
		return medalReward;
	}
	public void setMedalReward(MedalHistory medalHistory) {
		this.medalReward = medalHistory;
	}
	
}
