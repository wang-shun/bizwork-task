package com.sogou.bizwork.task.api.web.activiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.bizwork.task.api.core.bizservicetree.service.BizServiceTreeService;
import com.sogou.bizwork.task.api.core.task.po.Result;
import com.sogou.bizwork.task.api.web.common.util.UserHolder;

@Controller
@RequestMapping("/serviceTree")
public class ServiceTreeController {
	
	@Autowired
	private BizServiceTreeService bizServiceTreeService;
	
	@RequestMapping("/getGroupAndSvnList.do")
	@ResponseBody
	public Result getManagersByPath() {
		Result result = new Result();
		int employeeId = (int) UserHolder.getUserId();
//		result.setData(bizServiceTreeService.getServiceTreeInfos());
//		result.setData(bizServiceTreeService.getServiceTreeInfoByEmployeeId(204516));
		result.setData(bizServiceTreeService.getServiceTreeInfos(employeeId));
		return result;
	}
}
