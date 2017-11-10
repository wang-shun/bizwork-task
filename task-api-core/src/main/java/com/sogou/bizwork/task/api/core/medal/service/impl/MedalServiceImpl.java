package com.sogou.bizwork.task.api.core.medal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.core.medal.bo.MedalHistory;
import com.sogou.bizwork.task.api.core.medal.bo.MedalType;
import com.sogou.bizwork.task.api.core.medal.dao.MedalDao;
import com.sogou.bizwork.task.api.core.medal.service.MedalService;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.user.service.UserService;
import com.sogou.bizwork.task.api.medal.bo.MedalInfo;

@Service("medalService")
public class MedalServiceImpl implements MedalService{
	@Autowired
	private MedalDao medalDao;
	@Autowired
	private UserService userService;
	
	@Override
	public List<MedalType> getMedalTypes() {
		return medalDao.getMedalTypes();
	}

	@Override
	public void addMedal(MedalHistory medalHistory, int operatorId) {
		UserDTO operatorDto = userService.getUserByEmployeeId(operatorId);
		medalHistory.setGivePeople(operatorDto.getChineseName());
		medalHistory.setGivePeopleId(operatorId);
		UserDTO userDTO = userService.getUserByEmployeeId(medalHistory.getAcceptPeopleId());
		medalHistory.setAcceptPeople(userDTO.getChineseName());
		medalDao.addMedal(medalHistory);
	}

	@Override
	public List<MedalInfo> getMedalInfoByEmployeeId(int employeeId) {
		return medalDao.getMedalInfoByEmployeeId(employeeId);
	}

}
