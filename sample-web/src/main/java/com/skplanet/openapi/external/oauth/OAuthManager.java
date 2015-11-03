package com.skplanet.openapi.external.oauth;


public interface OAuthManager {

	public void setClientInfo(OAuthClientInfo clientInfo);
	public void setPropertyFile(String path) throws Exception;
	public OAuthAccessToken createAccessToken() throws OAuthManagingException;
	
}
