package com.skplanet.openapi.external.oauth;

public interface OAuthManager {

	void setClientInfo(OAuthClientInfo clientInfo);
	void setPropertyFile(String path) throws Exception;
	
}
