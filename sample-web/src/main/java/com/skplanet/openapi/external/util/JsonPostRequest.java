package com.skplanet.openapi.external.util;

import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class JsonPostRequest extends HttpRequest<String> implements Callable<String> {

	@Override
	public String call() throws Exception {
		String result = null;
		
		if (!validationUrl()) {
			return null;
		}
		
		if (!validationParameter()) {
			return null;
		}
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(callUrl);
		
		if (validationHeader()) {
			addHeaders(httpPost);
		}
		
		HttpEntity httpEntity = null;
		httpEntity = new StringEntity(getParameter(), ContentType.APPLICATION_JSON);
		
		httpPost.setEntity(httpEntity);
		CloseableHttpResponse response = httpClient.execute(httpPost);
		
		try {
			this.statusLine = response.getStatusLine();
			result = EntityUtils.toString(response.getEntity());
		} finally {
			response.close();
			httpClient.close();
		}
		
		return result;
	}

	@Override
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

}
