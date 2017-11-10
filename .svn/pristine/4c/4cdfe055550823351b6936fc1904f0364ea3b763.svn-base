/**
 * Sogou-Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.sogou.bizwork.task.api.web.session.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sogou.bizwork.client.utils.PathPatternMatcher;
import com.sogou.bizwork.task.api.constant.user.UserConstants;
import com.sogou.bizwork.task.api.core.dto.Result;

/**
 * @title SessionCheckFilter
 * @description TODO 
 * @author tianzhen
 * @date 2016-7-29
 */
public class SessionCheckFilter implements Filter {
    private static final long serialVersionUID = -3920403250590755433L;
    private Logger logger = Logger.getLogger(getClass());

    private static final String CONFIG_EXCLUDE_PATH = "excludePath";
    private static final String PARAM_TICKET = "ticket";
    private static final String PARAM_SID = "sid";
    private static final int STATUS_UN_LOGIN = 103;
    private List<String> excludePath;

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String excludePathv = filterConfig.getInitParameter(CONFIG_EXCLUDE_PATH);
        excludePath = new ArrayList<String>();
        if (!StringUtils.isBlank(excludePathv)) {
            String[] paths = excludePathv.split(";");
            excludePath.addAll(Arrays.asList(paths));
        }

    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String path = request.getServletPath();
        PrintWriter writer = null;
        try {
            if (StringUtils.isEmpty(path) || PathPatternMatcher.urlPathMatch(excludePath, path)) {
                filterChain.doFilter(request, response);
                return;
            }
            Object obj = session.getAttribute(UserConstants.BIZWORK_TASK_USER_SID);
            if (obj == null) {
                Gson gson = new Gson();
                Result result = new Result();
                result.setData(session.getId());
                result.setErrorCode(STATUS_UN_LOGIN);

                writer = response.getWriter();
                writer.print(gson.toJson(result));
            } else {
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
