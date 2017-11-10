/**
 * Sogou-Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.sogou.bizwork.task.api.web.sso.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sogou.bizwork.cas.utils.StringUtils;
import com.sogou.bizwork.client.filter.BizworkSSOFilter;
import com.sogou.bizwork.client.filter.ExampleBizworkSingleSignOnFilter;
import com.sogou.bizwork.client.utils.PathPatternMatcher;
import com.sogou.bizwork.task.api.constant.user.UserConstants;

/**
 * @title SingleSignOnFilter
 * @description TODO 
 * @author tianzhen
 * @date 2016-8-3
 */
public class EunomiaSingleSignOnFilter extends BizworkSSOFilter {
    private static final Logger logger = Logger.getLogger(ExampleBizworkSingleSignOnFilter.class);

    public static final String CONFIG_EXCLUDE_PATH = "excludePath";
    private List<String> excludePath;

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        String excludePathv = filterConfig.getInitParameter(CONFIG_EXCLUDE_PATH);

        excludePath = new ArrayList<String>();
        if (!StringUtils.isBlank(excludePathv)) {
            String[] paths = excludePathv.split(";");
            excludePath.addAll(Arrays.asList(paths));
        }
    }

    /**
     * @return whether the SSO/SLO process can be by-passed
     */
    protected boolean needSSO(ServletRequest request, ServletResponse response) {
        //判断是否登陆过，已登录返回false，否则返回true代表走SSO
        HttpServletRequest hrequest = (HttpServletRequest) request;
        HttpServletResponse hresponse = (HttpServletResponse) response;
        String path = hrequest.getServletPath();

        if ("".equals(path) || PathPatternMatcher.urlPathMatch(excludePath, path)) {
            return false;
        }
        Object obj = hrequest.getSession().getAttribute(UserConstants.BIZWORK_TASK_USER_SID);
        /**
         * 这里根据系统的登陆session情况来扩展，判断是否需要使用登陆
         */
        if (obj == null) {
            boolean needSSO = super.needSSO(hrequest, hresponse);
            if (needSSO) {
                logger.info("SSO begin");
            }
            return needSSO;
        }
        return false;
    }
}
