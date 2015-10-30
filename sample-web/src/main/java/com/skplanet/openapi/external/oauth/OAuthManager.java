package com.skplanet.openapi.external.oauth;

import java.util.concurrent.ExecutorService;

public interface OAuthManager {

	void setClientInfo(OAuthClientInfo clientInfo);
	void setPropertyFile(String path) throws Exception;
	void setExecutorService(ExecutorService service);
	
}
