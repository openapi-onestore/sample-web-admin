package com.skplanet.openapi.external.oauth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;

import com.skplanet.openapi.external.oauth.OAuthManagingException.OAuthManaging;
import com.skplanet.openapi.util.HttpHeader;

public class OAuthManager implements OAuthInterface {

	private int threadPoolCount = 2;
	private ExecutorService jobExecutor = Executors.newFixedThreadPool(threadPoolCount);
	private OAuthClientInfo clientInfo = null;
	private OAuthAccessToken oauth = null;
	private OAuthVerifyResult oauthVerifyResult = null;
	
	private final String oauthAccessTokenUrl = "http://172.21.60.143/oauth/service/accessToken";
	private final String oauthVerifyUrl = "http://172.21.60.143/oauth/internal/v1/validation";
	
	@Override
	public void setClientInfo(OAuthClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}
	
	@Override
	public boolean createOAuthAccessToken() {
		
		if (clientInfo == null)
			return false;
		
		if (!clientInfo.validateClientInfo())
			return false;
		
		Map<String, String> data = new HashMap<String, String>();
		data.put(OAuthClientInfo.GRANT_TYPE, clientInfo.getGrantType());
		
		String response = null;
		
		OAuthHttpRequest httpRequest = new OAuthHttpRequest();
		List<HttpHeader> authHeader = new ArrayList<HttpHeader>();
		authHeader.add(getOAuthHttpRequestHeader());
		
		try {
			httpRequest.setCallUrl(oauthAccessTokenUrl);
			httpRequest.setParamMap(data);
			httpRequest.setHeader(authHeader);
			
			Future<String> future = jobExecutor.submit(httpRequest);
			response = future.get();
			
			System.out.println("Post response : " + response);
			if (response != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				oauth = objectMapper.readValue(response, OAuthAccessToken.class);
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
	public OAuthAccessToken getOAuthToken() throws OAuthManagingException {
		if (oauth == null)
			throw new OAuthManagingException(OAuthManaging.OAUTH_OBJECT_NULL, "OAuth object is null");
		return this.oauth;
	}

	@Override
	public boolean verifyOAuthToken() {
		
		if (oauth == null)
			return false;
		
		Map<String, String> verifyData = new HashMap<String, String>();
		verifyData.put("ipAddress", "10.202.33.51");
		verifyData.put("apiId", oauth.getScope().split(" ")[0]);
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
	public OAuthVerifyResult getOAuthVerifyResult() throws OAuthManagingException {
		if (oauthVerifyResult == null)
			throw new OAuthManagingException(OAuthManaging.OAUTH_OBJECT_NULL, "OAuth object is null");
		return oauthVerifyResult;
	}

	private OAuthHttpRequestHeader getOAuthHttpRequestHeader() {
		StringBuilder sb = new StringBuilder();		
		
		System.out.println(clientInfo.getAuthString());
		
		byte[] basicStringBase64 = Base64.encodeBase64(clientInfo.getAuthString().getBytes());
		sb.append("BASIC ").append(new String(basicStringBase64));
		OAuthHttpRequestHeader oAuthHttpRequestHeader = new OAuthHttpRequestHeader("Authorization", sb.toString());
		System.out.println(oAuthHttpRequestHeader.getName() + " " + oAuthHttpRequestHeader.getValue());
		return oAuthHttpRequestHeader;
	}
	
}
