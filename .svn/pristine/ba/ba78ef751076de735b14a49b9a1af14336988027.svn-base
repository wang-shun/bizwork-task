//package com.sogou.bizwork.task.api.core.activiti.service.impl;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.googlecode.jsonplugin.JSONException;
//import com.googlecode.jsonplugin.JSONUtil;
//import com.sogou.biztech.starry.dao.UserMapper;
//import com.sogou.bizwork.task.api.core.activiti.po.SvnDistribution;
//import com.sogou.bizwork.task.api.core.activiti.service.SpecialBusinessService;
//import com.sogou.bizwork.task.api.core.activiti.vo.WorkflowFormVo;
//import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
//import com.sogou.bizwork.task.api.core.user.service.UserService;
//import com.sogou.bizwork.task.api.core.user.service.impl.UserServiceImpl;
//
//public class SpecialBusinessServiceImpl implements SpecialBusinessService {
//	@Autowired
//	private UserMapper userMapper;
//	
//	@Override
//	public Object getDataByTaskCode(String code, Object data) {
//		if (code.equals("102_2")) {
//			WorkflowFormVo workflowFormVo = (WorkflowFormVo) data;
//			String formData = workflowFormVo.getFormData();
//			if (StringUtils.isNotBlank(formData)) {
//				try {
//					List<String> candidateUserIds = new ArrayList<String>();
//					Object object = JSONUtil.deserialize(formData);
//					Map<String, Object> map = 
//							(HashMap<String, Object>) JSONUtil.deserialize(formData);
//					if (!org.springframework.util.CollectionUtils.isEmpty(map)) {
//						List<String> managers = (List<String>) map.get("managers");
//						for (String userName : managers) {
//							UserDTO userDTO = userService.getUserByUserName(userName);
//							if (userDTO != null && userDTO.getEmployeeId() != null) {
//								candidateUserIds.add(userDTO.getEmployeeId() + "");
//							}
//						}
//						return candidateUserIds;
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		} else if (code.equals("102_3")) {
//			
//		}
//		return null;
//	}
//}
