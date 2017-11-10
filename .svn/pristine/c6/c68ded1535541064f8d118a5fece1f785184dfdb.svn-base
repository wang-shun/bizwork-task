package com.sogou.bizwork.task.api.core.bizservicetree;

import org.apache.thrift.transport.THttpClient;

import com.sogou.bizdev.teleport.consumer.factorybean.thrift.ThriftHttpClusterProxyFactoryBean;

public class BizserviceThriftFactoryBean extends ThriftHttpClusterProxyFactoryBean{
	
    private String apiToken;

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    protected void onPrepareTHttpTransport(THttpClient thttpClient) {
        thttpClient.setCustomHeader("ApiToken", "bizpms:ad3bb823");
        super.onPrepareTHttpTransport(thttpClient);
    }
}
