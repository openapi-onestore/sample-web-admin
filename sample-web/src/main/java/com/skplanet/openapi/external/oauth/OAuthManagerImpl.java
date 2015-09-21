package com.skplanet.openapi.external.oauth;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;

import com.skplanet.openapi.external.oauth.OAuthManagingException.OAuthManaging;

public class OAuthManagerImpl implements OAuthManager {

	private int threadPoolCount = 1;
	private ExecutorService jobExecutor = Executors.newFixedThreadPool(threadPoolCount, new ThreadFactory() {
		@Override
		public Thread newThread(Runnable runnable) {
			Thread oauthManagerThread = Executors.defaultThreadFactory().newThread(runnable);
			oauthManagerThread.setName("oauthManager");
			oauthManagerThread.setDaemon(true);
			return oauthManagerThread;
		}
	});
	
	private OAuthClientInfo clientInfo = null;
	private OAuthAccessToken oauth = null;
	
	private String propertyPath = null;
	
	// url default setting
	private String oauthAccessTokenUrl = "http://172.21.60.143:8080/oauth/service/accessToken";
	private String clientId = "/0Ny9V9Kp06EHEhqfE3anmLpV9OeuvRYY3OSqiSkzMwcp2O5K0sYAAjguN8sfPRF";
	private String clientSecret = "UkCq4QydghHHV+bRGfGJ1dIF9mcX66Hq61fDdylRP2Q=";
	
	public OAuthManagerImpl() {
		OAuthClientInfo oauthClientInfo = new OAuthClientInfo();
		oauthClientInfo.setClientId(this.clientId);
		oauthClientInfo.setClientSecret(this.clientSecret);
		this.clientInfo = oauthClientInfo;
	}
	
	public OAuthManagerImpl(String clientId, String clientSecret) {
		OAuthClientInfo oauthClientInfo = new OAuthClientInfo();
		oauthClientInfo.setClientId(clientId);
		oauthClientInfo.setClientSecret(clientSecret);
		this.clientInfo = oauthClientInfo;
	}
	
	@Override
	public OAuthAccessToken createAccessToken() throws OAuthManagingException {
		
		if (clientInfo == null) {
			throw new OAuthManagingException(OAuthManaging.OAUTH_OBJECT_NULL, "Client info is null!!");
		}

		if (!clientInfo.validateClientInfo()) {
			throw new OAuthManagingException(OAuthManaging.INVALID_PARAMETER, "Client info is invalid!!");
		}
		
		Map<String, String> data = new HashMap<String, String>();
		data.put(clientInfo.GRANT_TYPE, clientInfo.getGrantType());
		
		String response = null;
		
		OAuthHttpRequest httpRequest = new OAuthHttpRequest();
		
		try {
			System.out.println("Set Call url " + oauthAccessTokenUrl);
			httpRequest.setCallUrl(oauthAccessTokenUrl);
			httpRequest.setParamMap(data);
			httpRequest.setHeader(getOAuthHttpRequestHeader());
			
			Future<String> future = jobExecutor.submit(httpRequest);
			response = future.get();
			
			System.out.println("Post response : " + response);
			if (response != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				oauth = objectMapper.readValue(response, OAuthAccessToken.class);
			} else {
				throw new OAuthManagingException(OAuthManaging.OAUTH_HTTP_REQUEST_FAIL, "OAUTH http request is failure!! Status");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuthManagingException(OAuthManaging.OAUTH_HTTP_REQUEST_FAIL, "OAUTH http request is failure!!");
		}
		
		return oauth;
	}	
	
	@Override
	public void setClientInfo(OAuthClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}
	
	@Override
	public void setPropertyFile(String path) throws Exception {
		this.propertyPath = path;
		Properties props = new Properties();

		if (propertyPath == null) {
			throw new OAuthManagingException(OAuthManaging.OAUTH_OBJECT_NULL,"Property path is null");
		}

		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(propertyPath);
			props.load(new BufferedInputStream(fis));
			
			oauthAccessTokenUrl = props.getProperty("oauth.token_create_url");
			clientId = props.getProperty("oauth.client_id");
			clientSecret = props.getProperty("oauth.client_secret");
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuthManagingException(OAuthManaging.UNKNOWN_ERROR, "File creation is incorect!!");
		} finally {
			fis.close();
		}
	}
	
	public void setExecutorService(ExecutorService service) {
		if (jobExecutor != null) {
			this.jobExecutor.shutdown();			
		}
		this.jobExecutor = service;
	}
	
	private Map<String, String> getOAuthHttpRequestHeader() {
		StringBuilder sb = new StringBuilder();		
		System.out.println(clientInfo.getAuthString());
		
		byte[] basicStringBase64 = Base64.encodeBase64(clientInfo.getAuthString().getBytes());
		sb.append("BASIC ").append(new String(basicStringBase64));
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization", sb.toString());
		System.out.println("Authorization : " + sb.toString());
		
		return headerMap;
	}	
	
}
