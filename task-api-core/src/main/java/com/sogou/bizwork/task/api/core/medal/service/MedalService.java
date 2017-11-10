package com.sogou.bizwork.task.api.core.medal.service;

import java.util.List;

import com.sogou.bizwork.task.api.core.medal.bo.MedalHistory;
import com.sogou.bizwork.task.api.core.medal.bo.MedalType;
import com.sogou.bizwork.task.api.medal.bo.MedalInfo;

public interface MedalService {

	List<MedalType> getMedalTypes();
	
	void addMedal(MedalHistory medalHistory, int operatorId);

	List<MedalInfo> getMedalInfoByEmployeeId(int employeeId);
}
