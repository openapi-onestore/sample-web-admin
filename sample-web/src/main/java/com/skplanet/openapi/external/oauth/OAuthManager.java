package com.skplanet.openapi.external.oauth;


public interface OAuthManager {

	public OAuthAccessToken createAccessToken() throws OAuthManagingException;
	
}
