package com.sogou.bizwork.task.api.web.activiti.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sogou.bizwork.task.api.core.activiti.dto.Repository;
import com.sogou.bizwork.task.api.core.activiti.dto.RepositoryDto;
import com.sogou.bizwork.task.api.core.activiti.po.RepositoryResult;
import com.sogou.bizwork.task.api.core.activiti.service.RepositoryService;
import com.sogou.bizwork.task.api.core.task.po.Result;
import com.sogou.bizwork.task.api.core.user.dao.UserMapper;
import com.sogou.bizwork.task.api.core.user.dto.UserDTO;
import com.sogou.bizwork.task.api.web.common.util.UserHolder;

@Controller
@RequestMapping("/svnService")
@SuppressWarnings("unchecked")
public class SvnServiceController {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private UserMapper userMapper;
	
	@RequestMapping("/getManagersByPaths.do")
	@ResponseBody
	public Result getManagersByPath(@RequestBody Map<String, Object> requestParams, HttpServletRequest request) {
		Result result = new Result();
		Long userId = UserHolder.getUserId();
		Set<String> managerIntersection = new HashSet<String>();
		List<Repository> repositories = new ArrayList<Repository>();
		RepositoryDto repositoryDto = new RepositoryDto();
		if (CollectionUtils.isEmpty(requestParams)) {
			result.setErrorCodeAndMsg(421, "params cannot be empty!");
			return result;
		}
		if (CollectionUtils.isEmpty((List<String>)requestParams.get("paths"))) {
			repositoryDto.setManagerIntersection(managerIntersection);
			repositoryDto.setRepositories(repositories);
			result.setData(repositoryDto);
			return result;
		}
		if (StringUtils.isBlank(requestParams.get("sid").toString())) {
			result.setErrorCodeAndMsg(421, "sid cannot be empty!");
			return result;
		}
		List<String> paths = (List<String>)requestParams.get("paths");
		Set<String> pathSet = new HashSet<String>(paths);
		if (pathSet.size() != paths.size()) {
			result.setErrorCodeAndMsg(421, "不能存在重复路径");
			return result;
		}
		pathSet.clear();
		Set<String> set = new HashSet<String>();
		//取出未离职人员的userName manager
		Set<String> unLeaveserNameSet = userMapper.getUnLeaveUserNames();
		if (!CollectionUtils.isEmpty(paths)) {  // svn 仓库路径
			boolean firstTime = true;
			boolean needJudgeSet = true;
			List<String> managers = null;

			for (String path : paths) {
				String originalPath = path;
				if (!path.contains(":/")) {   // 格式化path
					path += ":/";
				}
				if (!pathSet.add(path)) {
					continue;
				}
				managers = null;
				Map<String, String> params = new HashMap<String, String>();
				String url = "http://bizsvn.sogou-inc.com/svnadmin/if_accesspathinfo.php";
				UserDTO userDTO = userMapper.getUserByEmployeeId(Integer.parseInt(userId.toString()));
				if (userDTO != null && StringUtils.isNotBlank(userDTO.getUserName())) {
					params.put("username", userDTO.getUserName());
				}
				params.put("path", path);
				Map<String, Object> data = repositoryService.getManagersByPaths(url, params);  // 找谁审批,自己可以审批呢
				if (CollectionUtils.isEmpty(data)) {
					result.setErrorCodeAndMsg(521, "Query managers failed! Please try again lager!");
					return result;
				}
				if (data.get("errorCode") != null  ||
						data.get("isExist") != null) {
					if (data.get("errorcode") != null &&
							Long.parseLong(data.get("errorCode") + "") > 0) {
						result.setErrorCodeAndMsg(521, data.get("exception").toString());
						return result;
					} else {
						if ((data.get("isExist") + "").equals("1")) {
							if (StringUtils.isNotBlank(data.get("managers").toString())) {
								List<String> temp = (List<String>) data.get("managers");
								if (managers == null) {
									managers = new ArrayList<String>();
								}
								for (String ele : temp) {
									if (unLeaveserNameSet.contains(ele)) {    //离职人员不设管理员
										managers.add(ele);
									}
								}
							}
						}
					}
				}
				Repository repository = new Repository();
				repository.setPath(originalPath);  //
				repository.setManagers(managers);
				if (needJudgeSet && firstTime) { // 第一次迭代
					if (managers != null) {
						set.addAll(managers);  //
						managerIntersection.addAll(managers);
					}
					firstTime = false;
				} else if (needJudgeSet) {
					if (!CollectionUtils.isEmpty(managers)) {
						managers.remove(null);
						for (String manager : managers) {
							//if (!set.contains(manager))
							{    // 第一次的   后来有相同的管理员才会加
								managerIntersection.add(manager);
							}
						}

						set = new HashSet<String>(managerIntersection);   // 共有的leader
					//	managerIntersection.clear();
					} else {  // 为空
						set.clear();
						needJudgeSet = false;
					}
				}
				repositories.add(repository);    // 仓库信息
			}
			/* end for */
			if (pathSet.size() == 1 && !CollectionUtils.isEmpty(managers)) {  // 只有一个
				managerIntersection = new HashSet<String>(managers); //
			} else {
				//managerIntersection = set;  // 多个不断更新的
			}
		}
		repositoryDto.setManagerIntersection(managerIntersection);
		repositoryDto.setRepositories(repositories);
		result.setData(repositoryDto);
		return result;
	}
	

	@RequestMapping("/checkRepositoryName.do")
	@ResponseBody
	public Result checkSvnName(@RequestBody Map<String, String> requestParams, HttpServletRequest request) {
		Result result = new Result();
		if (CollectionUtils.isEmpty(requestParams)) {
			result.setErrorCodeAndMsg(421, "params cannot be empty!");
			return result;
		}
		
		String reponame = requestParams.get("repositoryName");
		if (StringUtils.isEmpty(reponame)) {
			result.setErrorCodeAndMsg(421, "仓库名称不能为空");
		} else {
			reponame = reponame.trim();
			if (StringUtils.isEmpty(reponame)) {
				result.setErrorCodeAndMsg(421, "仓库名称不能为空");
			}
			Pattern p = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-]*[a-zA-Z0-9(?!-)]]*$");
			Matcher m = p.matcher(reponame);
			if (!m.matches()) {
				result.setErrorCodeAndMsg(431, "仓库名称必须包含字母，以字母开头。纯字母、字母数字、字母数字-的组合，如bizwork-crm1，且不能以-结尾。");
				return result;
			}
		}
		boolean isExist = repositoryService.checkSvnName(reponame);
		result.setData(isExist);
		return result;
	}
	


	@RequestMapping("/createRepository.do")
	@ResponseBody
	public Result createRepository(HttpServletRequest request) {
		Result result = new Result();
		String businessKey = "bizwork_svnApplication__0000015A89ADF34D09149708CFAAA2DC";
		RepositoryResult repositoryResult = 
				repositoryService.applyRepository(businessKey);
		if (!repositoryResult.isSuccess()) {
			result.setErrorCodeAndMsg(repositoryResult.getErrorCode(),
					repositoryResult.getErrorMsg());
			return result;
		}
//		result.setData(isExist);
		result.setData("仓库创建成功");
		return result;
	}



	@RequestMapping("/getRepositories.do")
	@ResponseBody
	public Result getRepositories(HttpServletRequest request) {
		Result result = new Result();
		Long userId = UserHolder.getUserId();
		UserDTO userDTO = userMapper.getUserByEmployeeId(Integer.parseInt(userId + ""));
		String username = userDTO.getUserName();
		result.setData(repositoryService.getRepositories(username));
		return result;
	}
	
	
}
