package com.skplanet.openapi.external.util;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class KvpPostRequest extends HttpRequest<Map<String, String>> implements Callable<String> {

	private final String delemeter = "&";
	private final String equal = "=";
	
	@Override
	public String call() throws Exception {
		String result = null;
		
		if (!validationUrl())
			return null;
		
		if (!validationParameter())
			return null;
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(callUrl);
		
		if (validationHeader())
			addHeaders(httpPost);
		
		HttpEntity httpEntity = null;
		httpEntity = new StringEntity(parameter, ContentType.APPLICATION_FORM_URLENCODED);
		
		httpPost.setEntity(httpEntity);
		CloseableHttpResponse response = httpClient.execute(httpPost);
		
		try {
			statusLine = response.getStatusLine();
			System.out.println("Http result code : " + statusLine.getStatusCode());
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity());
			}
		} finally {
			response.close();
			httpClient.close();
		}
		
		return result;
	}
	
	@Override
	public void setParameter(Map<String,String> paramMap) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> iterator = paramMap.keySet().iterator();
		
		while (iterator.hasNext()) {
			String key = iterator.next();
			sb.append(key).append(equal).append(paramMap.get(key));
			
			if (iterator.hasNext()) {
				sb.append(delemeter);
			}
		}
		this.parameter = sb.toString();
	}

}
