package com.skplanet.openapi.external.framework;

import com.skplanet.openapi.external.oauth.OAuthAccessToken;

public interface OAuthListener {

	void onResponse(String result);
	void onResponse(OAuthAccessToken oauthAccessToken);
	void onError(String error);

}
