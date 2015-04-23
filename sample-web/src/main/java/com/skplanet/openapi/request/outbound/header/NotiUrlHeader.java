package com.skplanet.openapi.request.outbound.header;

import com.skplanet.openapi.util.HttpHeader;

public class NotiUrlHeader implements HttpHeader{

	private String value;
	
	public NotiUrlHeader(String value){
		this.value = value;
	}
	
	public String getName() {
		return "notiUrl";
	}

	public String getValue() {
		return value;
	}
	
}
