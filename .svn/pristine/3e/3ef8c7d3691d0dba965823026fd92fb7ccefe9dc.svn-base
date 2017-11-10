/**
 * Sogou-Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.sogou.bizwork.task.api.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sogou.bizwork.task.api.teleport.ApiUser;
import com.sogou.bizwork.task.api.teleport.ApiUserHolder;

/**
 * @title ServiceMonitorInterceptor
 * @description service拦截器，主要用来日志记录service层调用耗时 
 * @author tianzhen
 * @date 2014-8-27
 * @version 1.0
 */
@Service
public class ServiceMonitorInterceptor implements MethodInterceptor {

    private static Logger logger = Logger.getLogger("EXECUTETIME");

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept
     * .MethodInvocation)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String packageName = invocation.getMethod().getDeclaringClass().getPackage().getName();
        String className = invocation.getMethod().getDeclaringClass().getSimpleName();
        String methodName = className + "." + invocation.getMethod().getName();
        // 记录service调用前的时间点
        long beginTime = System.currentTimeMillis();
        try {
            Object res = invocation.proceed();
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            long elapseTime = System.currentTimeMillis() - beginTime;
            ApiUser user = ApiUserHolder.getApiUser();// current user
            String info = user == null ? "" : user.toString();

            // 记录package、method、耗时、以及user信息
            logger.info(packageName + "." + methodName + "|" + elapseTime + "|" + info);
        }
    }
}
