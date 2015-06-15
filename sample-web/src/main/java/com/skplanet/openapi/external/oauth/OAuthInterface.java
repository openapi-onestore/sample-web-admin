package com.skplanet.openapi.external.oauth;

public interface OAuthInterface {
	
	void setClientInfo(OAuthClientInfo clientInfo);
	boolean createOAuthAccessToken();
	boolean updateOAuthToken();
	boolean deleteOAuthToken();
	boolean verifyOAuthToken();
	OAuthAccessToken getOAuthToken() throws OAuthManagingException;
	OAuthVerifyResult getOAuthVerifyResult() throws OAuthManagingException;	
}
