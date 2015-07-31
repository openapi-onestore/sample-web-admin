package com.skplanet.openapi.external.bulkpay;

import java.util.Map;
import java.util.concurrent.Callable;

public class BulkPayTidInfoTransaction implements Callable<String>{

	private BulkPayHttpClient httpClient;
	private Map<String, String> paramMap;
	private String callUrl;
	
	private String txid;
	
	public BulkPayTidInfoTransaction(Map<String, String> header, String txid) {
		this.paramMap = header;
		this.txid = txid;
	}
	
	@Override
	public String call() throws Exception {
		
		httpClient = new BulkPayHttpClient();
		httpClient.setHeaders(paramMap);
		String result = httpClient.get(callUrl);
		
		return result;
	}

	public void setCallUrl(String callingUrl) {
		this.callUrl = callingUrl.concat("/"+txid);
		System.out.println(this.callUrl);
	}
	
}
