package com.sogou.bizwork.task.api.core.util.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpTool {
    @SuppressWarnings("resource")
    public static String sendSvnServicePostRequest(Map<String, String> params, String url, String extendedParams) {
        String result = "";
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (!org.springframework.util.CollectionUtils.isEmpty(params)) {
	        for (Map.Entry<String, String> entry : params.entrySet()) {
	            String name = entry.getKey();
	            String value = entry.getValue();
	            nameValuePairs.add(new BasicNameValuePair(name, value));
	        }
        }
        if (StringUtils.isNotBlank(extendedParams)) {
            String[] pairs = extendedParams.split("&");
            for (String pair : pairs) {
                if (StringUtils.isNotBlank(pair)) {
                    String[] temp = pair.split("=");
                    if (temp.length == 2) {
                        nameValuePairs.add(new BasicNameValuePair(temp[0], temp[1]));
                    }
                }
            }
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = new DefaultHttpClient().execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity).toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    

    public static String sendSvnServiceGetRequest(Map<String, String> params, String url, String extendedParams) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(3000);
        if (!url.contains("?")) {
            url += "?";
        }
        int count = 0;
        if (!org.springframework.util.CollectionUtils.isEmpty(params)) {
	        for (Map.Entry<String, String> entry : params.entrySet()) {
	            String paramsName = entry.getKey();
	            String paramsValue = entry.getValue();
	            if (++count > 1) {
	                url += "&";
	            }
	            url += paramsName + "=" + paramsValue;
	        }
        }
        
        
        url += extendedParams;
        GetMethod getMethod = new GetMethod(url);
        getMethod.addRequestHeader("Content-Type", "text/html; charset=UTF-8");  
        // 响应状态的判断
        int status = 0;
        try {
            status = httpClient.executeMethod(getMethod);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 200 ok 请求成功，否则请求失败
        if (status != HttpStatus.SC_OK) {
            
        }
        // 请求成功，使用string获取响应数据
        // String info = null;
        String response = null;
        try {
            // info = new String(getMethod.getResponseBodyAsString());
            // 请求成功，使用 byte数组来获取响应数据
            response = getMethod.getResponseBodyAsString();
            // 编码要和 服务端响应的一致
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}