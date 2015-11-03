package com.skplanet.openapi.external.oauth;

import java.util.concurrent.ExecutorService;

public interface OAuthManager {

	public void setClientInfo(OAuthClientInfo clientInfo);
	public void setPropertyFile(String path) throws Exception;
	public void setExecutorService(ExecutorService service);
	public OAuthAccessToken createAccessToken() throws OAuthManagingException;
	
}
