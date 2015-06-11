package com.skplanet.openapi.external.oauth;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.codehaus.jackson.map.ObjectMapper;

public class OAuthManager implements OAuthInterface {

	private int threadPoolCount = 2;
	private ExecutorService jobExecutor = Executors.newFixedThreadPool(threadPoolCount);
	private OAuthClientInfo clientInfo = null;
	private OAuth oauth = null;
	private OAuthVerifyResult oauthVerifyResult = null;
	
	private String oauthTokenUrl = "http://10.200.226.71:8080/oauth/service/token";
	private String oauthVerifyUrl = "http://10.200.226.71:8080/oauth/internal/v1/validation";
	private String oauthVerifyApiId = "GetFilePaymentInfo";
	
	@Override
	public void setClientInfo(OAuthClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}
	
	@Override
	public boolean createOAuthToken() throws Exception {
		
		if (clientInfo == null)
			return false;
		
		if (!clientInfo.validateClientInfo())
			return false;
		
		Map<String, String> data = new HashMap<String, String>();
		data.put(OAuthClientInfo.CLIENT_ID, clientInfo.getClientId());
		data.put(OAuthClientInfo.CLIENT_SECRET, clientInfo.getClientSecret());
		data.put(OAuthClientInfo.GRANT_TYPE, clientInfo.getGrantType());
		
		String response = null;
		OAuthHttpRequest httpRequest = new OAuthHttpRequest();
		
		try {
			httpRequest.setCallUrl(oauthTokenUrl);
			httpRequest.setParamMap(data);
			
			Future<String> future = jobExecutor.submit(httpRequest);
			response = future.get();
			
			System.out.println("Post response : " + response);
			if (response != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				oauth = objectMapper.readValue(response, OAuth.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean updateOAuthToken() {
		return false;
	}

	@Override
	public boolean deleteOAuthToken() {
		return false;
	}

	@Override
	public OAuth getOAuthToken() throws Exception {
		if (oauth == null)
			throw new Exception();
		return this.oauth;
	}

	@Override
	public boolean verifyOAuthToken() throws Exception {
		
		if (oauth == null)
			return false;
				
		Map<String, String> verifyData = new HashMap<String, String>();
		verifyData.put("ipAddress", oauthVerifyUrl);
		verifyData.put("apiId", oauthVerifyApiId);
		verifyData.put("accessToken", oauth.getAccessToken());
		
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthHttpRequest httpRequest = new OAuthHttpRequest();
		
		try {
			String requestJson = null;
			requestJson = objectMapper.writeValueAsString(verifyData);

			httpRequest.setCallUrl(oauthVerifyUrl);
			httpRequest.setParam(requestJson);
			
			Future<String> future = jobExecutor.submit(httpRequest);
			String response = future.get();			
			
			if (response != null) {
				oauthVerifyResult = objectMapper.readValue(response, OAuthVerifyResult.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public OAuthVerifyResult getOAuthVerifyResult() throws Exception {
		if (oauthVerifyResult == null)
			throw new Exception();
		return oauthVerifyResult;
	}
	


}
