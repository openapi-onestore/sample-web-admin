package com.skplanet.openapi.external.oauth;

public interface OAuthInterface {

	void setAccountInfo(OAuthAccountInfo accountInfo);
	void setClientInfo(OAuthClientInfo clientInfo);
	boolean createOAuthAccessToken();
	boolean verifyOAuthToken(String ipAddress);
	boolean createOAuthAccount();
	OAuthAccount getOAuthAccount() throws OAuthManagingException;
	OAuthAccessToken getOAuthToken() throws OAuthManagingException;
	OAuthVerifyResult getOAuthVerifyResult() throws OAuthManagingException;
}
