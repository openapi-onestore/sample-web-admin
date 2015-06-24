package com.skplanet.openapi.external.bulkpay;

import java.util.Map;
import java.util.concurrent.Callable;

public class BulkPayTransaction implements Callable<String>{
	
	private BulkPayHttpClient httpClient;
	private Map<String, String> paramMap;
	private String callUrl;
	
	public BulkPayTransaction(Map<String, String> map) {
		this.paramMap = map;
	}
	
	@Override
	public String call() throws Exception {
		
		httpClient = new BulkPayHttpClient();
		httpClient.setHeaders(paramMap);
		String result = httpClient.postChunkedString(callUrl);
		
		return result;
	}
	
	public void setCallUrl(String callUrl) {
		this.callUrl = callUrl;
	}
	
}
