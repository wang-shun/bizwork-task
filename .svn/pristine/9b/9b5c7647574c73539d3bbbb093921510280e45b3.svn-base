/**
 * Sogou-Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.sogou.bizwork.task.api.teleport.factorybean;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.transport.THttpClient;

import com.google.gson.Gson;
import com.sogou.bizdev.teleport.consumer.factorybean.thrift.ThriftHttpClusterProxyFactoryBean;
import com.sogou.bizwork.task.api.teleport.ApiUser;
import com.sogou.bizwork.task.api.teleport.ApiUserHolder;
import com.sogou.bizwork.task.api.teleport.Constants;

/**
 * @title CustomThriftHttpClusterProxyFactoryBean
 * @description TODO 
 * @author qianlei
 * @date 2014-8-15
 * @version 1.0
 */
public class CustomThriftHttpClusterProxyFactoryBean extends ThriftHttpClusterProxyFactoryBean {

    private Gson gson = new Gson();

    protected void onPrepareTHttpTransport(THttpClient thttpClient) {
        /**
         * 设置header
         */
        Map<String, String> headers = new HashMap<String, String>();
        ApiUser apiUser = ApiUserHolder.getApiUser();
        if (apiUser != null) {
            headers.put(Constants.HTTP_HEAD_APIUSER_KEY, gson.toJson(apiUser));
            thttpClient.setCustomHeaders(headers);
        }
    }

}
