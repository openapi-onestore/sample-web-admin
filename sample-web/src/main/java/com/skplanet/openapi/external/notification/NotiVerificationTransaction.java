package com.skplanet.openapi.external.notification;

import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.util.EntityUtils;

public class NotiVerificationTransaction implements Callable<String>{

	private String callUrl = null;
	private String param = null;	
	private Map<String, String> headers = null;

	private StatusLine statusLine;
	
	@Override
	public String call() throws Exception {
		String result = null;
				
		if (!validation()) {
			return null;
		}
		
		if (isParamEmpty()) {
			return null;
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(callUrl);

		if (headers != null) {
			addHeaders(httpPost);
		}
		
		HttpEntity httpEntity = null;
		httpEntity = new StringEntity(param, ContentType.APPLICATION_JSON);			
		
		httpPost.setEntity(httpEntity);
		CloseableHttpResponse response = httpclient.execute(httpPost);
		
		try {
			this.statusLine = response.getStatusLine();
			result = EntityUtils.toString(response.getEntity());
		} finally {
			response.close();
		}
		
		return result;
	}
	
	public void setHeader(Map<String, String> header) {
		this.headers = header;
	}
	
	public void setParam(String param) {
		this.param = param;
	}
	
	public void setCallUrl(String callUrl) {
		this.callUrl = callUrl;
	}
	
	private boolean validation() {
		if (callUrl == null)
			return false;
		return true;
	}
	
	private boolean isParamEmpty() {
		if (param == null)
			return true;
		return false;
	}
	
	public String getStatus() {
		if (statusLine != null) {
			return statusLine.toString();
		} else {
			return null;
		}
	}
	
	private void addHeaders(AbstractHttpMessage httpMessage) {
		for (String key : headers.keySet()) {
			System.out.println("add Headers : " + key + " value : " + headers.get(key));
			httpMessage.setHeader(key, headers.get(key));
		}
	}

}
