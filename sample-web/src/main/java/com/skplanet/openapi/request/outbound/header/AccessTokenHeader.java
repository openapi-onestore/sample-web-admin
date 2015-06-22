package com.skplanet.openapi.request.outbound.header;

import com.skplanet.openapi.util.HttpHeader;

public class AccessTokenHeader implements HttpHeader {
	
	private String value;
	
	public AccessTokenHeader(String value){
		this.value = value;
	}
	
	public String getName() {
		return "access_token";
	}
	
	public String getValue() {
		return value;
	}
	
}
