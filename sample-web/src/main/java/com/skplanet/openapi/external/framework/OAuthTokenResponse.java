package com.skplanet.openapi.external.framework;

import com.skplanet.openapi.external.oauth.OAuthAccessToken;
import com.skplanet.openapi.external.oauth.OAuthManagingException;

public interface OAuthTokenResponse {

	void onOAuthTokenReceive(OAuthAccessToken oauthAccessToken);
	void onOAuthTokenError(OAuthManagingException oauthManagingException);
	
}
