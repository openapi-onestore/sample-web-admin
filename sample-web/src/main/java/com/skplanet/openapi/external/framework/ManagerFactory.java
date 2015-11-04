package com.skplanet.openapi.external.framework;

import com.skplanet.openapi.external.notification.NotiManager;
import com.skplanet.openapi.external.notification.NotiManagerImpl;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuthManager;
import com.skplanet.openapi.external.oauth.OAuthManagerImpl;
import com.skplanet.openapi.external.payment.OpenApiManager;
import com.skplanet.openapi.external.payment.OpenApiManagerImpl;

public class ManagerFactory extends AbstractManagerFactory {

	private String logPropertyPath = null;
	
	public ManagerFactory(String logPropertyPath) {
		this.logPropertyPath = logPropertyPath;
	}
	
	@Override
	public OAuthManager getOAuthManager(OAuthClientInfo oauthClientInfo) {
		return new OAuthManagerImpl(oauthClientInfo, logPropertyPath);
	}
	
	@Override
	public NotiManager getNotiManager() {
		return new NotiManagerImpl(logPropertyPath);
	}
	
	@Override
	public OpenApiManager getOpenApiManager() {
		return new OpenApiManagerImpl(logPropertyPath);
	}
	
}
