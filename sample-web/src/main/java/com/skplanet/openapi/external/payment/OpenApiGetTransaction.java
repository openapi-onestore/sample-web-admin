package com.skplanet.openapi.external.payment;

import java.util.Map;
import java.util.concurrent.Callable;

public class OpenApiGetTransaction implements Callable<String>{
	
	private OpenApiHttpClient httpClient;
	private Map<String, String> paramMap;
	private String callUrl;
	
	public OpenApiGetTransaction(Map<String, String> header) {
		this.paramMap = header;
	}
	
	@Override
	public String call() throws Exception {
		
		httpClient = new OpenApiHttpClient();
		httpClient.setHeaders(paramMap);
		
		String result = httpClient.get(callUrl);
				
		return result;
	}
	
	public void setCallUrl(String callingUrl) {
		this.callUrl = callingUrl;
		System.out.println("OpenApiGetTransaction" + this.callUrl);
	}
	
}
