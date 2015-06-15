package com.skplanet.openapi.external.oauth;

import com.skplanet.openapi.util.HttpHeader;

public class OAuthHttpRequestHeader implements HttpHeader{

	private String headerName;
	private String headerValue;
	
	public OAuthHttpRequestHeader(String name, String value) {
		this.headerName = name;
		this.headerValue = value;
	}
	
	@Override
	public String getName() {
		return headerName;
	}

	@Override
	public String getValue() {
		return headerValue;
	}

}
