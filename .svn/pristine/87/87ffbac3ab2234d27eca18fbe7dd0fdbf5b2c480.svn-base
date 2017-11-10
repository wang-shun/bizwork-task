package com.sogou.bizwork.task.api.web.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.bizwork.task.api.constant.user.UserConstants;
import com.sogou.bizwork.task.api.teleport.ApiUserHolder;
import com.sogou.bizwork.task.api.web.common.util.UserHolder;
import com.sogou.bizwork.task.api.web.user.vo.UserInfo;

public class UserHolderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfo user = (UserInfo) request.getSession().getAttribute(UserConstants.BIZWORK_TASK_USER_SID);
        if (user != null) {
            UserHolder.setUser(user);
            //            ApiUser apiUser = new ApiUser();
            //            apiUser.setAccountId(user.getAccountId());
            //            apiUser.setAgentUserId(user.getAgentUserId());
            //            apiUser.setAdminUserId(user.getAdminUserId());
            //            apiUser.setOperator(user.getOperator());
            //            apiUser.setFrom(user.getFrom());
            //            apiUser.setIp(user.getIp());
            //            apiUser.setUserRoleId(user.getUserRole().getRoleId());
            //            ApiUserHolder.setApiUser(apiUser);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        UserHolder.remove();
        ApiUserHolder.remove();
    }

}
