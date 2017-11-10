/**
 * Sogou-Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.sogou.bizwork.task.api.web.sso.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sogou.bizsession.filter.HttpSessionProxy;
import com.sogou.bizwork.cas.utils.StringUtils;
import com.sogou.bizwork.client.example.SessionManager;
import com.sogou.bizwork.client.filter.AbstractSingleLogoutFilter;
import com.sogou.bizwork.task.api.core.user.service.TicketIdToSessionIdMappingService;

/**
 * @title UserLogoutFilter
 * @description TODO 
 * @author tianzhen
 * @date 2016-8-1
 */
public class EunomiaSingleLogoutFilter extends AbstractSingleLogoutFilter {
    private SessionManager sessionManager;

    private TicketIdToSessionIdMappingService ticketIdToSessionIdMappingService;

    public static final String CONFIG_TICKETID_SESSION_ID_MAPPING_SERVICE = "ticketIdToSessionIdMappingServiceImpl";

    public static final String DEFAULT_TICKETID_SESSION_ID_MAPPING_SERVICE_NAME = "ticketIdToSessionIdMappingServiceImpl";

    public static final String CONFIG_SESSION_MANAGER = "sessionManagerName";

    public static final String DEFAULT_SESSION_MANAGER_NAME = "sessionManager";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);

        //this.sessionManager = initSessionManager(filterConfig);
        this.ticketIdToSessionIdMappingService = initTicketIdToSessionIdMappingService(filterConfig);
    }

    protected SessionManager initSessionManager(FilterConfig filterConfig) throws ServletException {
        String sessionManagerName = getPropertyFromInitParams(filterConfig, CONFIG_SESSION_MANAGER,
                DEFAULT_SESSION_MANAGER_NAME);
        if (StringUtils.isBlank(sessionManagerName)) {
            sessionManagerName = DEFAULT_SESSION_MANAGER_NAME;
        }
        WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig
                .getServletContext());
        SessionManager sessionManager = (SessionManager) appContext.getBean(sessionManagerName, SessionManager.class);
        if (null == sessionManager) {
            throw new ServletException("Unable to find session manager using bean name:" + sessionManagerName);
        }
        return sessionManager;
    }

    protected TicketIdToSessionIdMappingService initTicketIdToSessionIdMappingService(FilterConfig filterConfig)
            throws ServletException {
        String name = getPropertyFromInitParams(filterConfig, CONFIG_TICKETID_SESSION_ID_MAPPING_SERVICE,
                DEFAULT_TICKETID_SESSION_ID_MAPPING_SERVICE_NAME);
        if (StringUtils.isBlank(name)) {
            name = DEFAULT_TICKETID_SESSION_ID_MAPPING_SERVICE_NAME;
        }
        WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(filterConfig
                .getServletContext());
        TicketIdToSessionIdMappingService ticketIdToSessionIdMappingService = (TicketIdToSessionIdMappingService) appContext
                .getBean(name, TicketIdToSessionIdMappingService.class);
        if (null == ticketIdToSessionIdMappingService) {
            throw new ServletException("Unable to find ticket id to session id mapping service using bean name :"
                    + name);
        }
        return ticketIdToSessionIdMappingService;
    }

    //    @Override
    //    protected HttpSession getSessionByTicketId(String ticketId) {
    //        String sessionId = ticketIdToSessionIdMappingService.getSessionIdByTicketId(ticketId);
    //        HttpSessionProxy httpSessionProxy = new HttpSessionProxy(sessionId, request.getSession());
    //        return httpSessionProxy;
    //    }

    @Override
    protected HttpSession getSessionByTicketIdAndSessionId(String token, HttpServletRequest request) {
        //String sessionId = ticketIdToSessionIdMappingService.getSessionIdByTicketId(token);
        String sessionId = ticketIdToSessionIdMappingService.retriveSessionIdByTicketId(token);
        HttpSessionProxy httpSessionProxy = new HttpSessionProxy(sessionId, request.getSession());
        return httpSessionProxy;
    }

    @Override
    protected void recordSessionIdByTicketId(String ticketId, String sessionId) {
        //ticketIdToSessionIdMappingService.setSessionIdByTicketId(ticketId, sessionId);
        ticketIdToSessionIdMappingService.recordSessionIdByTicketId(ticketId, sessionId);

    }

}
