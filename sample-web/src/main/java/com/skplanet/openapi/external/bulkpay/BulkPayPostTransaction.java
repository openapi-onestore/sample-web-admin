package com.skplanet.openapi.external.bulkpay;

import java.util.Map;
import java.util.concurrent.Callable;

public class BulkPayPostTransaction implements Callable<String> {

	private BulkPayHttpClient httpClient;
	private Map<String, String> paramMap;
	private String callUrl;
	private String jsonBody = null;
	private boolean isChunked = false;
	
	public BulkPayPostTransaction(Map<String, String> header) {
		this.paramMap = header;
	}
	
	@Override
	public String call() throws Exception {
		
		httpClient = new BulkPayHttpClient();
		httpClient.setHeaders(paramMap);
		
		String result = null;
		
		if (isChunked) {
			result = httpClient.postChunkedString(callUrl);
		}
		
		if (jsonBody != null) {
			result = httpClient.post(callUrl, jsonBody);
		}
		
		return result;
	}
	
	public void setCallUrl(String callingUrl) {
		this.callUrl = callingUrl;
		System.out.println("BulkPayGetTransaction" + this.callUrl);
	}

	public void setChunked(boolean isChunked) {
		this.isChunked = isChunked;
	}
	
	public void setJsonBody(String jsonBody) {
		this.jsonBody = jsonBody;
	}

	
	
}
