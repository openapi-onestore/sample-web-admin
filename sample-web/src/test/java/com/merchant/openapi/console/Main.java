package com.merchant.openapi.console;

import java.io.File;

import com.skplanet.openapi.external.oauth.OAuthAccessToken;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuthManagerImpl;
import com.skplanet.openapi.external.oauth.OAuthManagingException;
import com.skplanet.openapi.external.payment.OpenApiException;
import com.skplanet.openapi.external.payment.OpenApiManager;
import com.skplanet.openapi.external.payment.OpenApiManagerImpl;
import com.skplanet.openapi.vo.payment.FilePaymentResult;

public class Main { 
	
	public static OAuthClientInfo oauthClientInfo = null;
	
	public static void init() {
		oauthClientInfo = new OAuthClientInfo();
		oauthClientInfo.setClientId("84xK38rx9iCrFRJVOynsRA0MT0o3LTs83OqDLEJf5g0=");
		oauthClientInfo.setClientSecret("GS1qrhoHMJWpmS6QwLNaG5NcFWFqzh5TrmY5476a2nA=");
		oauthClientInfo.setGrantType("client_credentials");
	}
	
	public static String getAccessToken() {
		OAuthManagerImpl oauthManager = new OAuthManagerImpl(oauthClientInfo.getClientId(), oauthClientInfo.getClientSecret());
		OAuthAccessToken accessToken = null;
		try {
			accessToken = oauthManager.createAccessToken();
		} catch (OAuthManagingException e) {
			e.printStackTrace();
		}
		
		return accessToken.getAccessToken();
	}
	
	public static FilePaymentResult createFilePayment() {
//		String accessToken = getAccessToken();	//TODO to be 
		String accessToken = "AAA";
		
		OpenApiManager openApi = new OpenApiManagerImpl();
		File file = new File("....");
		FilePaymentResult filePaymentResult = null;
		try {
			filePaymentResult = openApi.createFilePayment(file);
		} catch (OpenApiException e) {
			e.printStackTrace();
		}
		return filePaymentResult;
	}
	
	public static void getFileTxDetails() {
		;
	}
	
	public static void cancelPaymentTx() {
		;
	}
	
	public static void getPaymentTx() {
		;
	}
	
	public static void handleNotifcation() {
		;
	}
	
	public static void main(String ... v) {
		System.out.println("Hello JW");
	}
}
