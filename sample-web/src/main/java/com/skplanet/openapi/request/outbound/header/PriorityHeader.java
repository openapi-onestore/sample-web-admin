package com.skplanet.openapi.request.outbound.header;

import com.skplanet.openapi.util.HttpHeader;

public class PriorityHeader implements HttpHeader {

	private String value;
	
	public PriorityHeader(String value){
		this.value = value;
	}	
	
	@Override
	public String getName() {
		return "priority";
	}

	@Override
	public String getValue() {
		return value;
	}

	
	
}
