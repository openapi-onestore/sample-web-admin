package com.skplanet.openapi.request.outbound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.skplanet.openapi.dao.BulkJobDAO;
import com.skplanet.openapi.external.bulkpay.BulkPayManager;
import com.skplanet.openapi.external.notification.NotiManager;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuthManager;

@Component("payplanetClient")
public class PayplanetClient {
	
	private static final Logger logger = LoggerFactory.getLogger(PayplanetClient.class);
	
	@Autowired
	private BulkJobDAO bulkJobDao;
	
	private OAuthManager oauthManager = new OAuthManager();
	private BulkPayManager bulkPayManager = new BulkPayManager();
	private NotiManager notiManager = new NotiManager();
	
	@Value("${openapi.verify_url}") private String verifyUrl;
	@Value("${openapi.notification_url}") private String notificationUrl;
	@Value("${openapi.bulkjob_url}") private String bulkjobUrl;
	
//	@Autowired
//	private OutBoundRequestHandler<Map<String,String>,String> outRequestHandler;
	
	public String verify(Map<String,String> param) throws Exception {
		logger.debug("verify() called");
		
		String result = notiManager.requestNotificationVerification(param);
		
		return result;
	}
	
	public String createBulkPayment(int processingCount, String path) throws Exception {		
		logger.debug("createBulkPayment() called");
		
		String accessToken = null;
		
		OAuthClientInfo oauthClientInfo = new OAuthClientInfo();
		oauthClientInfo.setClientId("84xK38rx9iCrFRJVOynsRA0MT0o3LTs83OqDLEJf5g0=");
		oauthClientInfo.setClientSecret("GS1qrhoHMJWpmS6QwLNaG5NcFWFqzh5TrmY5476a2nA=");
		oauthClientInfo.setGrantType("client_credentials");
		
		oauthManager.setClientInfo(oauthClientInfo);
		if (oauthManager.createOAuthAccessToken()) {
			accessToken = oauthManager.getOAuthToken().getAccessToken();
		}
		
		Map<String,String> paramMap = getBulkPayParamMap(processingCount, path, accessToken);
		String response = bulkPayManager.createFilePayment(paramMap);
		
		return response;		
	}
	
	public void insertBulkPaymentRequest(Map<String,String> param) throws Exception {
		logger.debug("insertBulkPaymentRequest() called");
		bulkJobDao.addBulkJobRequest(param);
	}
	
	public List<Map<String, String>> selectBulkJobRequest() throws Exception {
		logger.debug("selectBulkJobRequest(); called");
		List<Map<String, String>> result = bulkJobDao.selectBulkJobRequest();
		return result;
	}
	
	private Map<String,String> getBulkPayParamMap(int processingCount, String path, String token) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("verBulkPay", "1");
		paramMap.put("bid", "skplanet");
		paramMap.put("notiUrl", notificationUrl);
		paramMap.put("cntTotalTrans", Integer.toString(processingCount));
		paramMap.put("priority", "Instant");
		paramMap.put("access_token", token);
		paramMap.put("filePath", path);
		return paramMap;
	}
}
