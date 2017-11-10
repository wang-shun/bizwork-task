package com.sogou.bizwork.task.api.core.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sogou.bizwork.task.api.common.util.MD5Util;
import com.sogou.bizwork.task.api.core.activiti.bo.ActInfo;
import com.sogou.bizwork.task.api.core.activiti.dao.WorkflowDao;
import com.sogou.bizwork.task.api.core.activiti.po.RepositoryResult;
import com.sogou.bizwork.task.api.core.activiti.po.ServiceResult;
import com.sogou.bizwork.task.api.core.activiti.po.SvnRepositoryErrorEnum;
import com.sogou.bizwork.task.api.core.activiti.service.RepositoryService;
import com.sogou.bizwork.task.api.core.user.dao.UserMapper;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;


@Service
@SuppressWarnings({ "unchecked", "deprecation" })
public class RepositoryServiceImpl implements RepositoryService {
	@Autowired
	private WorkflowDao workflowDao;
	@Autowired
	private UserMapper userMapper;
	
	@Value("${macStr}")
	private String macStr;
	//线上
//	private static final String  macStr = "svnadmin_bizwork_)!Hk:SD*P";
	
	//开发
//	private static final String  macStr = "svnadmin_bizwork_)!Hk:SD*P_test";
	
    @Override
    public ServiceResult isRepositoryExist(String username, String reponame) {
        ServiceResult result = new ServiceResult();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

	/* 申请仓库 */
	@Override
    public RepositoryResult applyRepository(String businessKey) {
    	RepositoryResult repositoryResult = new RepositoryResult();
    	String repositoryUrl = "http://bizsvn.sogou-inc.com/svnadmin/if_repositorycreate.php?";
    	ActInfo actInfo = workflowDao.getActivitiInfo(businessKey);  // get flow message
    	String formDataStr = actInfo.getFormData();
    	String extendedParams1 = "";
    	try {
    		Map<String, Object> map = (Map<String, Object>)
					JSONUtil.deserialize(formDataStr);
			Map<String, String> params = new HashMap<String, String>();
			if (!CollectionUtils.isEmpty(map)) {
				String reponame = (String) map.get("repositoryName"); // get res
				String repochinesename = (String) map.get("description");
					params.put("reponame", reponame);
					params.put("repochinesename", repochinesename);
				boolean is404 = (Boolean) map.get("need404");
				String manager = "";
				if (is404) {
					params.put("is404", "1");
					manager = "fangxiuli";
				} else {
					params.put("is404", "0");
				    manager = (String) map.get("manager");
				}
				params.put("repotype", "fsfs");
				params.put("accesspathcreate", "1");
				params.put("repostructuretype", "single");
				if (StringUtils.isNotBlank(manager)) {
					String[] manarges = manager.split(",");
					if (manarges != null) {
						for (String m : manarges) {
								extendedParams1 += "&adminusers[]=" + m;
						}
					}
				}
				
			}
			long employeeId = actInfo.getEmployeeId();
			UserDTO userDTO = userMapper.getUserByEmployeeId((int)employeeId);
			if (userDTO != null && StringUtils.isNotBlank(userDTO.getUserName())) {
				params.put("username", userDTO.getUserName());
			}
			String data = sendSvnServicePostRequest(params, repositoryUrl, extendedParams1);
//			String data = sendSvnServiceGetRequest(params, repositoryUrl, extendedParams);
			if (data.contains("errorCode")) {
				Map<String, Object> resultMap = 
						(Map<String, Object>) JSONUtil.deserialize(data);
				int errorCode = Integer.parseInt(resultMap.get("errorCode").toString());
				String errMsg = SvnRepositoryErrorEnum.getMessage(errorCode);
						;
				if (StringUtils.isNotBlank(errMsg)) {
					repositoryResult.setError(errorCode ,errMsg);
				} else {
					repositoryResult.setError(1999, "未知错误");
				}
				return repositoryResult;
			}
		//	distributePermission(businessKey,Integer.MAX_VALUE);
		} catch (JSONException e) {
			e.printStackTrace();
		}

        return repositoryResult;
    }

    @Override
    public ServiceResult createPath() {
        return null;
    }

    @Override
    public ServiceResult grantPermission() {
        return null;
    }
    
	@SuppressWarnings("resource")
	public String sendSvnServicePostRequest(Map<String, String> params, String url, String extendedParams) {
    	String result = "";
    	HttpPost httpPost = new HttpPost(url);
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	for (Map.Entry<String, String> entry : params.entrySet()) {
    		String name = entry.getKey();
    		String value = entry.getValue();
    		nameValuePairs.add(new BasicNameValuePair(name, value));
    		if (name.equals("username")) {
    			String mac = MD5Util.MD5(value + macStr).toLowerCase();
    			nameValuePairs.add(new BasicNameValuePair("mac", mac));
    		}
    	}
    	if (StringUtils.isNotBlank(extendedParams)) {
    		String[] pairs = extendedParams.split("&");
    		for (String pair : pairs) {
    			if (StringUtils.isNotBlank(pair)) {
    				String[] temp = pair.split("=");
    				if (temp.length == 2) {
    					nameValuePairs.add(new BasicNameValuePair(temp[0], temp[1]));
    				}
    			}
    		}
    	}
    	try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
    		HttpResponse response = new DefaultHttpClient().execute(httpPost);
    		HttpEntity entity = response.getEntity();
    		result = EntityUtils.toString(entity).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    

	@Override
	public Map<String, Object> getManagersByPaths(String url, Map<String, String> params) {
//		String paramsStr = sendSvnServiceGetRequest(params, url, "");
		String paramsStr = sendSvnServicePostRequest(params, url, "");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = (Map<String, Object>)JSONUtil.deserialize(paramsStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean checkSvnName(String reponame) {
		String url = "http://bizsvn.sogou-inc.com/svnadmin/if_repositorycheck.php";
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", "chenxisi0848");
//		if (!reponame.contains(":/")) {
//			reponame += ":/";
//		}
//		String[] arr = reponame.split(":/");
//		reponame =arr[0];
		params.put("reponame", reponame);
//		String res = sendSvnServiceGetRequest(params, url, "");
		String res = sendSvnServicePostRequest(params, url, "");
//		res = res.substring(4, res.length());
		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>) JSONUtil.deserialize(res);
			if (!CollectionUtils.isEmpty(map)) {
				long isExist = (Long)map.get("isExist");
				if (isExist == 0) {
					return false;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return true;
	}
	@Override
	public RepositoryResult distributePermission(String businessKey, long employeeId) {

		RepositoryResult repositoryResult = new RepositoryResult();
		
		List<Map<String, Object>> permissionResults = new ArrayList<Map<String,Object>>();
		String pathUrl = "http://bizsvn.sogou-inc.com/svnadmin/if_permissionassign.php";
		ActInfo actInfo = workflowDao.getActivitiInfo(businessKey);
		if (employeeId < 0  || employeeId == Integer.MAX_VALUE) {
			employeeId = actInfo.getEmployeeId();    //如果employeeId < 0， 那么就传入申请人。
		}
		UserDTO userDTO = userMapper.getUserByEmployeeId((int)employeeId);
		
		String formData = actInfo.getFormData();   //权限分配的formdata
    	String extendedParams1 = "";
		
		Map<String, String> params = new HashMap<String, String>();
		try {
			Map<String, Object> formParams = 
					(Map<String, Object>) JSONUtil.deserialize(formData);
			
			if (userDTO != null) {
				String userName = userDTO.getUserName();
				//  根据新需求，只要404管理员同意，不管是否是仓库管理员都可以成功分配权限
	            //  因此，userDTO直接取出仓库管理员，以管理员身份进行审批
				if (actInfo.getFlowTypeId() == 102) {      //权限分配类型中取出管理员
					List<String> managers = (List<String>) formParams.get("managers");    
					if (!CollectionUtils.isEmpty(managers)) {
						userName = managers.get(0);
					}
				} else if (actInfo.getFlowTypeId() == 101) {    //仓库申请类型中取出管理员
					String managers = (String) formParams.get("manager");
					if (StringUtils.isNotBlank(managers)) {
						String[] managerArr = managers.split(",");
						if (!ArrayUtils.isEmpty(managerArr)) {
							userName = managerArr[0];
						}
					}
				}
				if (StringUtils.isNotBlank(userName)) {
					params.put("username", userName);
				}
			}
			
			List<Map<String, Object>> repositories =  new ArrayList<Map<String,Object>>();
			if (actInfo.getFlowTypeId() == 101) {  // 权限分配
				String repository = (String) formParams.get("repositoryName");
				if (StringUtils.isNotBlank(repository)) {
					if (!repository.contains(":/")) {
						repository += ":/";
					}
					extendedParams1 += "accesspaths[]=" + repository;
				}
			} else if (actInfo.getFlowTypeId() == 102) {
				repositories = (List<Map<String, Object>>) formParams.get("repositories");

			}
			if (!CollectionUtils.isEmpty(repositories)) {
				int count = 0;
				for (Map<String, Object> pathAndManagers : repositories) {   //accesspaths
					String repository = (String) pathAndManagers.get("path");
					if (StringUtils.isNotBlank(repository)) {
						if (!repository.contains(":/")) {
							repository += ":/";
						}
						if (count == 0) {
							count++;
						} else {
							extendedParams1 += "&";
						}
						extendedParams1 += "accesspaths[]=" + repository;
					}
				}
//				extendedParams1 += "&accesspaths[]=bizsvn:/";
			}
			List<Map<String, Object>> persons =
					(List<Map<String, Object>>) formParams.get("persons");
			if (!CollectionUtils.isEmpty(persons)) {
				String originExtendedParams1 = extendedParams1;
				
				boolean needAddEmployeeReadOnlyPermission = true;    //是否需要增加操作人的只读权限，如果persons中未指定该人，则添加该操作人的只读权限
				for (Map<String, Object> person : persons) {
					if (person.get("email").equals(userDTO.getUserName())) {
						needAddEmployeeReadOnlyPermission = false;
						break;
					}
				}
				if (needAddEmployeeReadOnlyPermission) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("email", userDTO.getUserName());    //操作人的username
					map.put("permission", "2");    //添加操作人的只读权限
					persons.add(map);
				}
				for (Map<String, Object> person : persons) {
					String username = (String) person.get("email");
					String permission = person.get("permission") + "";
					if (StringUtils.isNotBlank(username) &&
							StringUtils.isNotBlank(permission)) {    //users
						extendedParams1 = originExtendedParams1 + "&users[]=" + username;
						if (permission.equals("1")) {    //读写权限
							params.put("permission", "Read-Write");
						} else if (permission.equals("2")) {    //只读权限
							params.put("permission", "Read");
						}
						
						String res = sendSvnServicePostRequest(params, pathUrl, extendedParams1);
						Map<String, Object> tempMap = (Map<String, Object>) JSONUtil.deserialize(res);
						List<String> succesList = (List<String>) tempMap.get("doneList");
						List<String> noPermissionList = (List<String>) tempMap.get("noPermList");
						List<String> failedList = (List<String>) tempMap.get("failedList");
						
						Map<String, Object> permissionResult = new TreeMap<String, Object>();
						permissionResult.put("username", username);
						if (!CollectionUtils.isEmpty(succesList)) {
							permissionResult.put("成功添加路径", succesList);
						}
						if (!CollectionUtils.isEmpty(noPermissionList)) {
							permissionResult.put("无权限失败路径（请确保表单中申请人为仓库管理员）", noPermissionList);
						}
						if (!CollectionUtils.isEmpty(failedList)) {
							permissionResult.put("未知错误失败路径", failedList);
						}
						permissionResults.add(permissionResult);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			repositoryResult.setData(JSONUtil.serialize(permissionResults));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return repositoryResult;
	}

	@Override
	public List<String> getRepositories(String username) {
		String pathUrl = "http://bizsvn.sogou-inc.com/svnadmin/if_accesspathinfo.php";
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
//		String res = sendSvnServiceGetRequest(params, pathUrl, "");
		String res = sendSvnServicePostRequest(params, pathUrl, "");
		Map<String, List<String>> resMap = new HashMap<String, List<String>>();
		try {
			resMap = 
					(Map<String, List<String>>) JSONUtil.deserialize(res);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return resMap.get("paths");
	}
}
