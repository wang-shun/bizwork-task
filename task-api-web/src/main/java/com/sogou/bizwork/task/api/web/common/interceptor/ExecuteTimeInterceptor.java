package com.sogou.bizwork.task.api.web.common.interceptor;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sogou.bizwork.client.dto.LoginUserExample;
import com.sogou.bizwork.task.api.common.util.ExecuteTimeHolder;
import com.sogou.bizwork.task.api.constant.user.UserConstants;

/**
 * 打印action接口执行时间
 * 
 * @author qianlei
 */
public class ExecuteTimeInterceptor implements HandlerInterceptor {

    private static final Logger executeTimeLog = LoggerFactory.getLogger("EXECUTETIME");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ExecuteTimeHolder.setStartTime(System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        long endTime = System.currentTimeMillis();
        long startTime = ExecuteTimeHolder.getStartTime();
        long consumeTime = endTime - startTime;
        StringBuffer requestQueryString = new StringBuffer();
        Map paraMap = request.getParameterMap();
        Set keySet = paraMap.keySet();
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            String paraName = (String) it.next();
            String paraValue = ((String[]) paraMap.get(paraName))[0];
            requestQueryString.append(paraName + "=" + paraValue);
            if (it.hasNext()) {
                requestQueryString.append("&");
            }
        }
        String accountId = getUser(request) == null ? "" : getUser(request).getId().toString();

        executeTimeLog.info(request.getRequestURI() + "," + consumeTime + "," + requestQueryString.toString() + ","
                + accountId);
    }

    private LoginUserExample getUser(HttpServletRequest request) throws UnsupportedEncodingException {
        Object o = request.getSession().getAttribute(UserConstants.BIZWORK_TASK_USER_SID);
        if (o != null) {
            return (LoginUserExample) o;
        }
        return null;
    }
}
