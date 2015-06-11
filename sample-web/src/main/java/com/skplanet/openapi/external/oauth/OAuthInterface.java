package com.skplanet.openapi.external.oauth;

public interface OAuthInterface {

	void setClientInfo(OAuthClientInfo clientInfo);
	boolean createOAuthToken() throws Exception;
	boolean updateOAuthToken();
	boolean deleteOAuthToken();
	boolean verifyOAuthToken() throws Exception;
	OAuth getOAuthToken() throws Exception;
	OAuthVerifyResult getOAuthVerifyResult() throws Exception;	
}
