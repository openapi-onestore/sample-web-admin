package com.skplanet.openapi.external.oauth;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;

import com.skplanet.openapi.external.oauth.OAuthManagingException.OAuthManaging;
import com.skplanet.openapi.external.util.KvpPostRequest;

public class OAuthManagerImpl implements OAuthManager {
	
	private OAuthClientInfo clientInfo = null;
	private String propertyPath = null;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	// url default setting
	private String oauthAccessTokenUrl = "http://172.21.60.143:8080/oauth/service/accessToken";
	
	public OAuthManagerImpl(OAuthClientInfo oauthClientInfo) {
		this.clientInfo = oauthClientInfo;
	}
	
	public OAuthAccessToken createAccessToken() throws OAuthManagingException {
		
		if (clientInfo == null) {
			throw new OAuthManagingException(OAuthManaging.OAUTH_OBJECT_NULL, "Client info is null!!");
		}
		
		if (!clientInfo.validateClientInfo()) {
			throw new OAuthManagingException(OAuthManaging.INVALID_PARAMETER, "Client info is invalid!!");
		}
		
		Map<String, String> data = new HashMap<String, String>();
		data.put(clientInfo.GRANT_TYPE, clientInfo.getGrantType());
		
		KvpPostRequest kvpPostRequest = new KvpPostRequest();
		kvpPostRequest.setHeader(getOAuthHttpRequestHeader());
		kvpPostRequest.setCallUrl(oauthAccessTokenUrl);
		kvpPostRequest.setParameter(data);
		
		String response = null;
		OAuthAccessToken accessToken = null;
		
		try {
			System.out.println("Set call uri : " + oauthAccessTokenUrl);
			response = kvpPostRequest.executeRequest();
			accessToken = objectMapper.readValue(response, OAuthAccessToken.class);
			System.out.println("Post response : " + response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuthManagingException(OAuthManaging.OAUTH_HTTP_REQUEST_FAIL, "OAUTH http request is failure!!");			
		}
		
		return accessToken;
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new OAuthManagingException(OAuthManaging.UNKNOWN_ERROR, "File creation is incorect!!");
		} finally {
			fis.close();
		}
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
