package com.skplanet.openapi.request.outbound.header;

import com.skplanet.openapi.util.HttpHeader;

public class BatchFileVersionHeader implements HttpHeader{

	private String value;
	
	public BatchFileVersionHeader(String value){
		this.value = value;
	}
	
	public String getName() {
		return "batchFileVersion";
	}

	public String getValue() {
		return value;
	}
	
}
