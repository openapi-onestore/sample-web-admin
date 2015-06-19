package com.skplanet.openapi.external.oauth;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;

import com.skplanet.openapi.external.oauth.OAuthManagingException.OAuthManaging;
import com.skplanet.openapi.util.HttpHeader;

public class OAuthManager implements OAuthInterface {

	private int threadPoolCount = 1;
	private ExecutorService jobExecutor = Executors.newFixedThreadPool(threadPoolCount);
	
	private OAuthAccountInfo accountInfo = null;
	private OAuthClientInfo clientInfo = null;
	
	private OAuthAccount oauthAccount = null;
	private OAuthAccessToken oauth = null;
	private OAuthVerifyResult oauthVerifyResult = null;
	
	private String propertyPath = null;
	
	// default setting
	private String oauthAccountCreateUrl = "http://172.21.60.143/oauth/admin/management/account/creation";
	private String oauthAccessTokenUrl = "http://172.21.60.143/oauth/service/accessToken";
	private String oauthVerifyUrl = "http://172.21.60.143/oauth/internal/v1/validation";
	
	@Override
	public void setAccountInfo(OAuthAccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}
	
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
	public boolean createOAuthAccount() {
		
		if (accountInfo == null) 
			return false;
		
		if (!accountInfo.validateAccountInfo()) 
			return false;
		
		String response = null;
		OAuthHttpRequest httpRequest = new OAuthHttpRequest();
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			String accountJsonString = objectMapper.writeValueAsString(accountInfo);
			httpRequest.setCallUrl(oauthAccountCreateUrl);
			httpRequest.setParam(accountJsonString);
			
			Future<String> future = jobExecutor.submit(httpRequest);
			response = future.get();
			
			System.out.println("Post response : " + response);
			if (response != null) {
				objectMapper = new ObjectMapper();
				oauthAccount = objectMapper.readValue(response, OAuthAccount.class);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		
		return false;
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
	public OAuthAccessToken getOAuthToken() throws OAuthManagingException {
		if (oauth == null)
			throw new OAuthManagingException(OAuthManaging.OAUTH_OBJECT_NULL, "OAuth object is null");
		return this.oauth;
	}

	@Override
	public OAuthVerifyResult getOAuthVerifyResult() throws OAuthManagingException {
		if (oauthVerifyResult == null)
			throw new OAuthManagingException(OAuthManaging.OAUTH_OBJECT_NULL, "OAuth object is null");
		return oauthVerifyResult;
	}
	
	@Override
	public OAuthAccount getOAuthAccount() throws OAuthManagingException {
		if (oauthAccount == null)
			throw new OAuthManagingException(OAuthManaging.OAUTH_OBJECT_NULL, "OAuth object is null");
		return oauthAccount;
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

	public void setPropertyFile(String path) throws Exception {
		this.propertyPath = path;
		Properties props = new Properties();

		if (propertyPath == null)
			return;
		
		try {
			FileInputStream fis = new FileInputStream(propertyPath);
			props.load(new BufferedInputStream(fis));
			
			oauthAccountCreateUrl = props.getProperty("oauth.account_create_url");
			oauthAccessTokenUrl = props.getProperty("token_create_url");
			oauthVerifyUrl = props.getProperty("token_verify_url");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
