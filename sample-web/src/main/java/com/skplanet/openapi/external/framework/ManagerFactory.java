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
	private Environment env = null;
	
	public ManagerFactory(String logPropertyPath) {
		this.logPropertyPath = logPropertyPath;
	}
	
	public ManagerFactory(Environment env, String logPropetyFilePath) {
		this.logPropertyPath = logPropetyFilePath;
		this.env = env;
	}
	
	@Override
	public OAuthManager getOAuthManager(OAuthClientInfo oauthClientInfo) {
		if (env == Environment.SANDBOX) {
			return new OAuthManagerImpl(true, oauthClientInfo, logPropertyPath);
		} else {
			return new OAuthManagerImpl(false, oauthClientInfo, logPropertyPath);			
		}
	}
	
	@Override
	public NotiManager getNotiManager() {
		if (env == Environment.SANDBOX) {
			return new NotiManagerImpl(true, logPropertyPath);
		} else {
			return new NotiManagerImpl(false, logPropertyPath);			
		}		
	}
	
	@Override
	public OpenApiManager getOpenApiManager() {
		if (env == Environment.SANDBOX) {
			return new OpenApiManagerImpl(true, logPropertyPath);
		} else {
			return new OpenApiManagerImpl(false, logPropertyPath);
		}
	}
	
}
