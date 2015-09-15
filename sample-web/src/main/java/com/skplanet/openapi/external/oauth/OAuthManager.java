package com.skplanet.openapi.external.oauth;

public interface OAuthManager {

	void setClientInfo(OAuthClientInfo clientInfo);
	OAuthAccessToken createAccessToken() throws OAuthManagingException;
	void setPropertyFile(String path) throws Exception;
	
}
