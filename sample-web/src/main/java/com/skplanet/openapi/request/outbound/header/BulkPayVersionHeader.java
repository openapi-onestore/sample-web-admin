package com.skplanet.openapi.request.outbound.header;

import com.skplanet.openapi.util.HttpHeader;

public class BulkPayVersionHeader implements HttpHeader{

	private String value;
	
	public BulkPayVersionHeader(String value){
		this.value = value;
	}
	
	public String getName() {
		return "verBulkPay";
	}

	public String getValue() {
		return value;
	}
	
}
