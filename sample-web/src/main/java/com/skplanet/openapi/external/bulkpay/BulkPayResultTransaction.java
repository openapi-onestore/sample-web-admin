package com.skplanet.openapi.external.bulkpay;

import java.util.Map;
import java.util.concurrent.Callable;

public class BulkPayResultTransaction implements Callable<String> {

	private BulkPayHttpClient httpClient;
	private Map<String, String> paramMap;
	private String callUrl;
	
	private String jobId, verifySign;
	
	public BulkPayResultTransaction(Map<String, String> header, String jobId, String verifySign) {
		this.paramMap = header;
		this.jobId = jobId;
		this.verifySign = verifySign;
	}
	
	@Override
	public String call() throws Exception {
		
		httpClient = new BulkPayHttpClient();
		httpClient.setHeaders(paramMap);
		String result = httpClient.get(callUrl);
		
		return result;
	}

	public void setCallUrl(String callingUrl) {
		this.callUrl = callingUrl.concat("/"+jobId+"?signCode="+verifySign);
		System.out.println(this.callUrl);
	}
	
}
