package com.skplanet.openapi.external.oauth;

public interface OAuthInterface {

	void setClientInfo(OAuthClientInfo clientInfo);
	OAuthAccessToken createAccessToken() throws OAuthManagingException;
	
}
