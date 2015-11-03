package com.skplanet.openapi.external.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GetRequest extends HttpRequest<String, String> {
	
	@Override
	public String executeRequest() throws Exception {
		String result = null;
		
		if (!validationUrl()) {
			return null;
		}
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(callUrl);
		
		if (validationHeader()) {
			addHeaders(httpGet);
		}
		
		CloseableHttpResponse response = httpClient.execute(httpGet);
		try {
			statusLine = response.getStatusLine();
			result = EntityUtils.toString(response.getEntity());
		} finally {
			response.close();
			httpClient.close();
		}
		
		return result;
	}
	
	@Override
	public void setParameter(String param) {
		if (callUrl != null) {
			callUrl.concat(param);
		}
	}

}
