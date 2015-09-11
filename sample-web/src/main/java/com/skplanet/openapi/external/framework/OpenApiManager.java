package com.skplanet.openapi.external.framework;

import java.util.HashMap;
import java.util.Map;

import com.skplanet.openapi.external.bulkpay.BulkPayException;
import com.skplanet.openapi.external.bulkpay.BulkPayManager;
import com.skplanet.openapi.external.notification.NotiException;
import com.skplanet.openapi.external.notification.NotiManager;
import com.skplanet.openapi.external.oauth.OAuthAccessToken;
import com.skplanet.openapi.external.oauth.OAuthManager;
import com.skplanet.openapi.external.oauth.OAuthManagingException;

public class OpenApiManager {

	private BulkPayListener bulkPayListener;
	private NotiListener notiListener;
	private OAuthListener oauthListener;
	
	private BulkPayManager bulkPayManager;
	private NotiManager notiManager;
	private OAuthManager oauthManager;

	/**
	 * BulkPay Manager API
	 */
	public void requestFilePayment(String filePath) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("filePath", filePath);
		
		try {
			String result = bulkPayManager.createFilePayment(paramMap);
			bulkPayListener.onResponse(result);		
		} catch (BulkPayException e) {
			e.printStackTrace();
			bulkPayListener.onError(e.getMessage());
		}
	}
	
	/**
	 * Noti Manager API
	 */
	public void requestNotiVerification(Map<String, String> paramMap, String listenerType) {
		paramMap.put("listenerType", listenerType);
		try {
			String result = notiManager.requestNotificationVerification(paramMap);
			notiListener.onResponse(result, null);
		} catch (NotiException e) {
			e.printStackTrace();
			notiListener.onError(e.getMessage());
		}
	}
	
	/**
	 * OAuth Manager API
	 */
	public void getOAuthAccessToken() {
		try {
			OAuthAccessToken accessToken = oauthManager.createAccessToken();
			oauthListener.onResponse(accessToken);
		} catch (OAuthManagingException e) {
			e.printStackTrace();
			oauthListener.onError(e.getMessage());
		}
	}
	
	
	
	
	public void setBulkPayListener(BulkPayListener bulkPayListener) {
		this.bulkPayListener = bulkPayListener;
	}
	
	public void setNotiListener(NotiListener notiListener) {
		this.notiListener = notiListener;
	}
	
	public void setOauthListener(OAuthListener oauthListener) {
		this.oauthListener = oauthListener;
	}
	
	public void setBulkPayManager(BulkPayManager bulkPayManager) {
		this.bulkPayManager = bulkPayManager;
	}
	
	public void setNotiManager(NotiManager notiManager) {
		this.notiManager = notiManager;
	}
	
	public void setOauthManager(OAuthManager oauthManager) {
		this.oauthManager = oauthManager;
	}
}
