package com.sogou.bizwork.task.api.web.session.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sogou.bizsession.filter.HttpServletRequestProxy;
import com.sogou.bizwork.task.api.web.common.request.MultiReadHttpServletRequest;

/**
 * 不使用BizSession提供的SessionFilter，原来的SessionFilter在每次访问的时候
 * 为了延长cookie时间，每次会再次向response中addCookie，造成response报文中每次都有Set Cookie这个Header
 * app拿到这个Header会刷新本地缓存的Cookie，影响性能
 *
 */
public class BizSessionFilter extends HttpServlet implements Filter {

    private static final long serialVersionUID = 1321783076006585090L;

    private final Log logger = LogFactory.getLog(this.getClass());

    private String webApp = "";

    private String sessionId = "sid";

    private String cookieDomain = "";

    private int cookieExpire = -1;

    private int sidLog = 1;// 1：不记录sid日志；2：记录

    private String cookiePath = "/";

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        MultiReadHttpServletRequest multiReadHttpServletRequest = new MultiReadHttpServletRequest(request);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (ServletInputStream) multiReadHttpServletRequest.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String sid = getSid(sb.toString());
        if (request.getMethod().equals("GET")) {
            sid = request.getParameter("sid");
        }

        // 如果sessionId不存在则创建
        if (sid == null || sid.length() == 0) {
            sid = this.webApp + "_" + java.util.UUID.randomUUID().toString();

            // sidLog为2，表示记录日志
            if (sidLog == 2) {
                logger.info("webApp:" + this.webApp + " new sid:" + sid);
            }
        }
        request.setAttribute("sid", sid);
        // 用HttpServletRequestWrapper代理request
        filterChain.doFilter(new HttpServletRequestProxy(sid, multiReadHttpServletRequest), servletResponse);
    }

    public String getSid(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        String str = json.replace("{", "").replace("}", "").replaceAll("\"", "");// 去掉大括号、双引号
        String[] ary1 = str.split(",");// 根据”,“号分隔字符串，获得数组对象
        for (String s : ary1) {// 循环取出数组中的对象
            String[] ary2 = s.split(":");// 根据”:“冒号分隔字符串，获得键-值数组对象
            if (ary2[0].trim().equals("sid") && ary2.length == 2) {
                return ary2[1].trim();
            }
        }
        return null;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.sessionId = filterConfig.getInitParameter("sessionId");
        this.cookieDomain = filterConfig.getInitParameter("cookieDomain");
        if (this.cookieDomain == null) {
            this.cookieDomain = "";
        }

        this.cookiePath = filterConfig.getInitParameter("cookiePath");
        if (this.cookiePath == null || this.cookiePath.length() == 0) {
            this.cookiePath = "/";
        }

        String expire = filterConfig.getInitParameter("cookieExpire");
        if (expire != null) {
            this.cookieExpire = Integer.parseInt(expire);
        }

        String sid = filterConfig.getInitParameter("sidLog");
        if (sid != null) {
            this.sidLog = Integer.parseInt(sid);
        }

        this.webApp = filterConfig.getInitParameter("webApp");
        if (this.webApp == null) {
            this.webApp = "defaultWebApp";
        }
    }

}
