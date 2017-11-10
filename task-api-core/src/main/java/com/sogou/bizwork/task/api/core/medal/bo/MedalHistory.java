package com.sogou.bizwork.task.api.core.medal.bo;

public class MedalHistory {
	private int medalId;
	private String acceptPeople;
	private int acceptPeopleId;
	private int givePeopleId;
	private String givePeople;
	private int medalTypeId;
	private String medalType;
	private String reason;
	private String createTime;
	
	
	public int getMedalTypeId() {
		return medalTypeId;
	}
	public void setMedalTypeId(int medalTypeId) {
		this.medalTypeId = medalTypeId;
	}
	public String getMedalType() {
		return medalType;
	}
	public void setMedalType(String medalType) {
		this.medalType = medalType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getMedalId() {
		return medalId;
	}
	public void setMedalId(int medalId) {
		this.medalId = medalId;
	}
	public String getAcceptPeople() {
		return acceptPeople;
	}
	public void setAcceptPeople(String acceptPeople) {
		this.acceptPeople = acceptPeople;
	}
	public int getAcceptPeopleId() {
		return acceptPeopleId;
	}
	public void setAcceptPeopleId(int acceptPeopleId) {
		this.acceptPeopleId = acceptPeopleId;
	}
	public int getGivePeopleId() {
		return givePeopleId;
	}
	public void setGivePeopleId(int givePeopleId) {
		this.givePeopleId = givePeopleId;
	}
	public String getGivePeople() {
		return givePeople;
	}
	public void setGivePeople(String givePeople) {
		this.givePeople = givePeople;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
