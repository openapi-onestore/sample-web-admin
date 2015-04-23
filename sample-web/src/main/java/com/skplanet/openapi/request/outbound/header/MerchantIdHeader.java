package com.skplanet.openapi.request.outbound.header;

import com.skplanet.openapi.util.HttpHeader;

public class MerchantIdHeader implements HttpHeader{

	private String value;
	
	public MerchantIdHeader(String value){
		this.value = value;
	}
	
	public String getName() {
		return "merchantId";
	}

	public String getValue() {
		return value;
	}
	
}
