package com.sogou.bizwork.task.api.core.score.bo;

public class ScoreVo {
	private int totalScore;
	private String acceptPeople;
	private RecentScore recentScore;
	private String imgUrl;
	
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public String getAcceptPeople() {
		return acceptPeople;
	}
	public void setAcceptPeople(String acceptPeople) {
		this.acceptPeople = acceptPeople;
	}
	public RecentScore getRecentScore() {
		return recentScore;
	}
	public void setRecentScore(RecentScore recentScore) {
		this.recentScore = recentScore;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
}
