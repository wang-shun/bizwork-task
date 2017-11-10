package com.sogou.bizwork.task.api.core.medal.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sogou.bizwork.task.api.core.medal.bo.MedalHistory;
import com.sogou.bizwork.task.api.core.medal.bo.MedalType;
import com.sogou.bizwork.task.api.medal.bo.MedalInfo;

@Repository("medalDao")
public interface MedalDao {

	List<MedalType> getMedalTypes();

	void addMedal(MedalHistory medalHistory);

	List<MedalInfo> getMedalInfoByEmployeeId(int employeeId);
	
}
