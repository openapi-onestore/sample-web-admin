package com.merchant.openapi.console;

import java.io.File;

import com.skplanet.openapi.external.oauth.OAuthAccessToken;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuthManagerImpl;
import com.skplanet.openapi.external.oauth.OAuthManagingException;
import com.skplanet.openapi.external.payment.OpenApiException;
import com.skplanet.openapi.external.payment.OpenApiManager;
import com.skplanet.openapi.external.payment.OpenApiManagerImpl;
import com.skplanet.openapi.vo.payment.FilePaymentHeader;
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
//		String accessToken = getAccessToken();	//TODO interface is changed by jwkim
		String accessToken = "056c7b3022e026567f23e5901f3245e4";
		
		OpenApiManager openApiManager = new OpenApiManagerImpl();
		File file = new File("D:/samplefolder/bulk_job.txt");
		FilePaymentResult filePaymentResult = null;
		
		FilePaymentHeader filePaymentHeader = new FilePaymentHeader();
		filePaymentHeader.setBid("Tstore");
		filePaymentHeader.setNotiUrl("");
		filePaymentHeader.setCntTotalTrans("2");
		filePaymentHeader.setPostbackUrl("");
		filePaymentHeader.setPrioity("Instant");
		filePaymentHeader.setVerBulkPay("1");
		
		try {
			filePaymentResult = openApiManager.createFilePayment(filePaymentHeader, file, accessToken);
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
