package com.skplanet.openapi.external.payment;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;

public class OpenApiPostTransaction implements Callable<String> {

	private OpenApiHttpClient httpClient;
	private Map<String, String> paramMap;
	private File paymentFile;
	private String callUrl;
	private String jsonBody = null;
	private boolean isChunked = false;
	
	public OpenApiPostTransaction(File paymentFile) {
		this.paymentFile = paymentFile;
	}
	
	public OpenApiPostTransaction(String body) {
		this.jsonBody = body;
	}
	
	@Override
	public String call() throws Exception {
		
		httpClient = new OpenApiHttpClient();
		httpClient.setHeaders(paramMap);
		
		String result = null;
		
		if (isChunked) {
			result = httpClient.postChunkedString(callUrl, this.paymentFile);
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
	
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	
}
