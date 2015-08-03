package com.skplanet.openapi.external.bulkpay;

import java.util.Map;
import java.util.concurrent.Callable;

public class BulkPayRefundTransaction implements Callable<String> {

	private BulkPayHttpClient httpClient;
	private Map<String, String> paramMap;
	private String callUrl;

	private String jsonString;
	
	public BulkPayRefundTransaction(Map<String,String> header, String refundJsonString) {
		this.paramMap = header;
		this.jsonString = refundJsonString;
	}
	
	
	@Override
	public String call() throws Exception {
		
		httpClient = new BulkPayHttpClient();
		httpClient.setHeaders(paramMap);
		String result = httpClient.post(callUrl, jsonString);
		
		return result;
	}
	
	public void setCallUrl(String callUrl) {
		this.callUrl = callUrl;
	}
	
}
