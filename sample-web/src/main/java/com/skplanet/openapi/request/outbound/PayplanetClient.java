package com.skplanet.openapi.request.outbound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.skplanet.openapi.dao.BulkJobDAO;
import com.skplanet.openapi.dao.NotificationDAO;
import com.skplanet.openapi.external.bulkpay.BulkPayManager;
import com.skplanet.openapi.external.notification.NotiManager;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuthManager;
import com.skplanet.openapi.vo.NotificationResult;
import com.skplanet.openapi.vo.refund.RefundTransactionRequest;
import com.skplanet.openapi.vo.transaction.TransactionInfo;

@Component("payplanetClient")
public class PayplanetClient {
	
	private static final Logger logger = LoggerFactory.getLogger(PayplanetClient.class);
	
	@Autowired
	private BulkJobDAO bulkJobDao;
	
	@Autowired
	private NotificationDAO notificationDAO;
	
	private OAuthManager oauthManager = new OAuthManager();
	private BulkPayManager bulkPayManager = new BulkPayManager();
	private NotiManager notiManager = new NotiManager();
	
	@Value("${openapi.notification_verify_url}") private String verifyUrl;
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
		logger.debug("selectBulkJobRequest() called");
		List<Map<String, String>> result = bulkJobDao.selectBulkJobRequest();
		return result;
	}
	
	public void insertNotificationResult(Map<String,String> param) throws Exception {
		logger.debug("insertNotificationResult() called");
		notificationDAO.addNotificationResult(param);
	}
	
	public void updateNotificationVerify(Map<String,String> param) throws Exception {
		logger.debug("updateNotificationVerify() called");
		bulkJobDao.updateNotifiVerified(param);
	}
	
	public NotificationResult selectNotificationResult(Map<String,String> param) throws Exception {
		logger.debug("selectNotificationResult() called");
		
		NotificationResult notificationResult = new NotificationResult();
		List<Map<String, String>> list = notificationDAO.selectNotificationResult(param);
		
		if (list.size() <= 0) {
			throw new Exception();
		}
		
		Map<String, String> tempMap = list.get(0);
		notificationResult.setJobId(tempMap.get("jobId"));
		notificationResult.setEvent(tempMap.get("event"));
		notificationResult.setStatus(tempMap.get("status"));
		notificationResult.setVerifySign(tempMap.get("verifySign"));
		notificationResult.setUpdateDate(tempMap.get("updateDate"));
		notificationResult.setNotifyVersion(tempMap.get("notiVersion"));
		
		return notificationResult;
	}
	
	public String getBulkJobResultFile(Map<String, String> param) throws Exception {
		logger.debug("getBulkJobResultFile() called");		
		String accessToken = getAccessTokenFromOauthManager();
		param.put("accessToken", accessToken);
		
		return bulkPayManager.getFilePaymentInfo(param);
	}
	
	public String getTidInformation(Map<String, String> param) throws Exception {
		logger.debug("getTidInformation() called");		
		String accessToken = getAccessTokenFromOauthManager();
		param.put("accessToken", accessToken);
		
		return bulkPayManager.getPaymentTransactionDetail(param);
	}
	
	public String getRefundInformation(Map<String, String> param) throws Exception {
		logger.debug("getRefundInformation() called");
		
		
		
		String accessToken = getAccessTokenFromOauthManager();
		param.put("accessToken", accessToken);
		
		return bulkPayManager.cancelPaymentTransaction(param);
	}
	
	private Map<String,String> getBulkPayParamMap(int processingCount, String path, String token) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("verBulkPay", "1");
		paramMap.put("bid", "skplanet");
		paramMap.put("notiUrl", notificationUrl);
		paramMap.put("cntTotalTrans", Integer.toString(processingCount));
		paramMap.put("priority", "Instant");
		paramMap.put("accessToken", token);
		paramMap.put("filePath", path);
		return paramMap;
	}
	
	private String getAccessTokenFromOauthManager() throws Exception {
		String accessToken = null;
		
		OAuthClientInfo oauthClientInfo = new OAuthClientInfo();
		oauthClientInfo.setClientId("84xK38rx9iCrFRJVOynsRA0MT0o3LTs83OqDLEJf5g0=");
		oauthClientInfo.setClientSecret("GS1qrhoHMJWpmS6QwLNaG5NcFWFqzh5TrmY5476a2nA=");
		oauthClientInfo.setGrantType("client_credentials");
		
		oauthManager.setClientInfo(oauthClientInfo);
		if (oauthManager.createOAuthAccessToken()) {
			accessToken = oauthManager.getOAuthToken().getAccessToken();
		}
		return accessToken;
	}
	
	private RefundTransactionRequest getRefundTransaction(String tid) throws Exception {
		
		ObjectMapper ojectMapper = new ObjectMapper();
		TransactionInfo transactionInfo = ojectMapper.readValue(tid, TransactionInfo.class);
		
		
		
		
		return null;
	}
}
