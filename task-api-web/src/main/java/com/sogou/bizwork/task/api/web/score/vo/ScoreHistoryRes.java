package com.sogou.bizwork.task.api.web.score.vo;

import java.util.List;

import com.sogou.bizwork.task.api.core.score.bo.ScoreHistory;

public class ScoreHistoryRes {
	private List<ScoreHistory> list;
	private int totalNumber;
	public List<ScoreHistory> getList() {
		return list;
	}
	public void setList(List<ScoreHistory> list) {
		this.list = list;
	}
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
}
