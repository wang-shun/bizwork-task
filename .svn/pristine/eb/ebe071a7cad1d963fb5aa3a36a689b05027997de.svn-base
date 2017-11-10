package com.sogou.bizwork.task.api.core.bizservicetree.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sogou.bizdev.teleport.common.http.JsonUtils;
import com.sogou.bizservicetree.api.result.ServiceInstanceResult;
import com.sogou.bizservicetree.api.service.ServiceInstanceManagerTService;
import com.sogou.bizwork.task.api.core.bizservicetree.GroupAndSvn;
import com.sogou.bizwork.task.api.core.bizservicetree.ServiceTreeInfo;
import com.sogou.bizwork.task.api.core.bizservicetree.dao.ServiceTreeInfoDao;
import com.sogou.bizwork.task.api.core.bizservicetree.service.BizServiceTreeService;
import com.sogou.bizwork.task.api.core.user.dao.UserMapper;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.core.util.http.HttpTool;

@Service("bizServiceTreeService")
public class BizServiceTreeServiceImpl implements BizServiceTreeService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ServiceInstanceManagerTService.Iface serviceInstanceManagerTService;
	@Autowired
	private ServiceTreeInfoDao serviceTreeInfoDao;

	@Override
	public Map<String, Set<String>> getServiceTreeInfoByEmployeeId(int employeeId) {
		UserDTO userDTO = userMapper.getUserByEmployeeId(employeeId);
		UserDTO leaderDTO = userMapper.getUserByEmployeeId(userDTO.getLeaderId());
		Map<String, Set<String>> groupAndSvns = new TreeMap<String, Set<String>>();
		List<ServiceTreeInfo> serviceTreeInfos = serviceTreeInfoDao.getServiceTreeInfos();
		for (ServiceTreeInfo serviceTreeInfo : serviceTreeInfos) {
			String groupName = userDTO.getGroupName();
			if (userDTO.getJob() == 2) { // qa
				if (groupName.equals(serviceTreeInfo.getQaGroup())
						|| leaderDTO.getUserName().equals(serviceTreeInfo.getQaLeader())) {
					addGroupAndSvn(groupName, serviceTreeInfo.getServiceName(), groupAndSvns);
				}
			} else if (userDTO.getJob() == 3) { // op
				if (groupName.equals(serviceTreeInfo.getOpGroup())
						|| leaderDTO.getUserName().equals(serviceTreeInfo.getOpLeader())) {
					addGroupAndSvn(groupName, serviceTreeInfo.getServiceName(), groupAndSvns);
				}
			} else if (userDTO.getJob() == 1 || userDTO.getJob() == 4) {
				// dev
				if (groupName.equals(serviceTreeInfo.getDevGroup())
						|| leaderDTO.getUserName().equals(serviceTreeInfo.getDevLeader())) {
					addGroupAndSvn(groupName, serviceTreeInfo.getServiceName(), groupAndSvns);
				}
			}
		}

		return groupAndSvns;
	}

	private void addGroupAndSvn(String groupName, String svn, Map<String, Set<String>> groupAndSvnsMap) {
		if (StringUtils.isEmpty(svn) || StringUtils.isEmpty(groupName))
			return;
		
		Set<String> svns = groupAndSvnsMap.get(groupName);
		if (svns == null) {
			svns = new HashSet<String>();
			groupAndSvnsMap.put(groupName, svns);
		}
		svns.add(svn);
	}

	@Override
	public void addServiceTreeInfo() {
		List<ServiceTreeInfo> serviceTreeInfos = new ArrayList<ServiceTreeInfo>();
		try {
			List<ServiceInstanceResult> serviceInstances = serviceInstanceManagerTService
					.getServiceInstanceResultByDirectoryPath("/onlineapp");

			for (ServiceInstanceResult instanceResult : serviceInstances) {
				serviceTreeInfos.add(this.getSvnServiceTree(instanceResult));
			}
			serviceTreeInfoDao.clearServiceTreeInfo();
			serviceTreeInfoDao.addServiceTreeInfo(serviceTreeInfos);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private ServiceTreeInfo getSvnServiceTree(ServiceInstanceResult instanceResult) {
		ServiceTreeInfo serviceTreeInfo = new ServiceTreeInfo();
		serviceTreeInfo.setDept(instanceResult.getDept());
		serviceTreeInfo.setDescription(instanceResult.getDescription());
		serviceTreeInfo.setDevCharger(instanceResult.getDevCharger());
		serviceTreeInfo.setDevGroup(instanceResult.getDevGroup());
		serviceTreeInfo.setDevLeader(instanceResult.getDevLeader());
		if (instanceResult.isIs404()) {
			serviceTreeInfo.setIs404(1);
		} else {
			serviceTreeInfo.setIs404(0);
		}
		serviceTreeInfo.setOpCharger(instanceResult.getOpCharger());
		serviceTreeInfo.setOpGroup(instanceResult.getOpGroup());
		serviceTreeInfo.setOpLeader(instanceResult.getOpLeader());
		serviceTreeInfo.setQaCharger(instanceResult.getQaCharger());
		serviceTreeInfo.setQaGroup(instanceResult.getQaGroup());
		serviceTreeInfo.setQaLeader(instanceResult.getQaLeader());
		serviceTreeInfo.setServiceName(instanceResult.getServiceName());
		serviceTreeInfo.setServiceStatus(instanceResult.getServiceStatus());
		return serviceTreeInfo;
	}

	@Override
	public List<GroupAndSvn> getServiceTreeInfos(int employeeId) {
		Map<String, Set<String>> groupAndSvns = new TreeMap<String, Set<String>>();
		List<ServiceTreeInfo> serviceTreeInfos = serviceTreeInfoDao.getServiceTreeInfos();
		List<GroupAndSvn> leaderGroupAndSvns = new ArrayList<GroupAndSvn>();
		UserDTO userDTO = userMapper.getUserByEmployeeId(employeeId);
		UserDTO leaderDTO = userMapper.getUserByEmployeeId(userDTO.getLeaderId());
		Set<String> leaderGroupSet = new HashSet<String>();
		for (ServiceTreeInfo serviceTreeInfo : serviceTreeInfos) {
			if (leaderDTO.getUserName().equals(serviceTreeInfo.getDevLeader())
					|| userDTO.getUserName().equals(serviceTreeInfo.getDevLeader())) {
				leaderGroupSet.add(serviceTreeInfo.getDevGroup());
			} else if (leaderDTO.getUserName().equals(serviceTreeInfo.getOpLeader())
					|| userDTO.getUserName().equals(serviceTreeInfo.getOpLeader())) {
				leaderGroupSet.add(serviceTreeInfo.getOpGroup());
			} else if (leaderDTO.getUserName().equals(serviceTreeInfo.getQaLeader())
					|| userDTO.getUserName().equals(serviceTreeInfo.getQaLeader())) {
				leaderGroupSet.add(serviceTreeInfo.getQaGroup());
			}
			addGroupAndSvn(serviceTreeInfo.getQaGroup(), serviceTreeInfo.getServiceName(), groupAndSvns);
			addGroupAndSvn(serviceTreeInfo.getDevGroup(), serviceTreeInfo.getServiceName(), groupAndSvns);
			addGroupAndSvn(serviceTreeInfo.getOpGroup(), serviceTreeInfo.getServiceName(), groupAndSvns);
		}
		for (Entry<String, Set<String>> entry : groupAndSvns.entrySet()) {
			GroupAndSvn groupAndSvn = new GroupAndSvn();
			groupAndSvn.setGroup(entry.getKey());
			Set<String> svnSet = entry.getValue();
			String url = "http://bizpms.sogou-inc.com/outter/querySvnByServiceName.json";
			String svnStr = "";
			int count = 0;
			for (String ele : svnSet) {
				if (count == 0) {
					count++;
				} else {
					svnStr += ",";
				}
				svnStr += ele;
			}
//			url += svnStr;
			String ret = HttpTool.sendSvnServicePostRequest(null, url, "serviceNames=" + svnStr);
//			String ret = HttpTool.sendSvnServiceGetRequest(null, url,  "serviceNames=" + svnStr);
			if (StringUtils.isNotBlank(ret)) {
				Set<String> svn = new HashSet<String>();
				String temp = "";
				try {
					Map<String, Object> mapRet = (Map<String, Object>) JSONUtil.deserialize(ret);
					List<Map<String, String>> svnList = (List<Map<String, String>>) mapRet.get("data");
					if (CollectionUtils.isNotEmpty(svnList)) {
						for (Map<String, String> ele : svnList) {
							String svnUrl = ele.get("svnUrl");
							if (StringUtils.isNotBlank(svnUrl)) {
								temp = svnUrl;
								if (svnUrl.indexOf("/trunk") > 0) {
									svnUrl = svnUrl.substring(32, svnUrl.indexOf("/trunk"));
								} else {
									svnUrl = svnUrl.substring(32, svnUrl.length());
								}
								svn.add(svnUrl);
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
					e.printStackTrace();
				}
				groupAndSvn.setSvns(svn);
				if (leaderGroupSet.contains(entry.getKey())) {
					leaderGroupAndSvns.add(0, groupAndSvn);
				} else {
					leaderGroupAndSvns.add(groupAndSvn);
				}
			}
		}
		return leaderGroupAndSvns;
	}

}
