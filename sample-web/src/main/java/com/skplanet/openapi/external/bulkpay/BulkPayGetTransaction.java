package com.skplanet.openapi.external.bulkpay;

import java.util.Map;
import java.util.concurrent.Callable;

public class BulkPayGetTransaction implements Callable<String> {

	private BulkPayHttpClient httpClient;
	private Map<String, String> paramMap;
	private String callUrl;
	
	public BulkPayGetTransaction(Map<String, String> header) {
		this.paramMap = header;
	}
	
	@Override
	public String call() throws Exception {
		
		httpClient = new BulkPayHttpClient();
		httpClient.setHeaders(paramMap);
		String result = httpClient.get(callUrl);
		
		return result;
	}
	
	public void setCallUrl(String callingUrl) {
		this.callUrl = callingUrl;
		System.out.println("BulkPayGetTransaction" + this.callUrl);
	}
	
}