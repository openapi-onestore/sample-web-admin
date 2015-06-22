package com.skplanet.openapi.request.outbound.header;

import com.skplanet.openapi.util.HttpHeader;

public class ProcessingCountHeader implements HttpHeader{

	private String value;
	
	public ProcessingCountHeader(String value){
		this.value = value;
	}
	
	public String getName() {
		return "cntTotalTrans";
	}

	public String getValue() {
		return value;
	}
	
}
