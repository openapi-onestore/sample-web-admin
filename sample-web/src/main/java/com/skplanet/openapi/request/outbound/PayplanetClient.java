package com.skplanet.openapi.request.outbound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.skplanet.openapi.dao.BulkJobDAO;
import com.skplanet.openapi.dao.NotificationDAO;
import com.skplanet.openapi.external.notification.NotiManagerImpl;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuthManagerImpl;
import com.skplanet.openapi.external.payment.OpenApiManagerImpl;
import com.skplanet.openapi.vo.NotificationResult;
import com.skplanet.openapi.vo.payment.FilePaymentResult;
import com.skplanet.openapi.vo.payment.Payer;
import com.skplanet.openapi.vo.payment.PaymentMethods;
import com.skplanet.openapi.vo.payment.PaymentTransactionInfo;
import com.skplanet.openapi.vo.payment.TransactionDetail;
import com.skplanet.openapi.vo.refund.CancelRequest;
import com.skplanet.openapi.vo.refund.Links;
import com.skplanet.openapi.vo.refund.RefundTransactionRequest;

@Component("payplanetClient")
public class PayplanetClient {
	
	private static final Logger logger = LoggerFactory.getLogger(PayplanetClient.class);
	
	@Autowired
	private BulkJobDAO bulkJobDao;
	
	@Autowired
	private NotificationDAO notificationDAO;
	
	private OAuthManagerImpl oauthManager = new OAuthManagerImpl();
	private OpenApiManagerImpl openApiManager = new OpenApiManagerImpl();
	private NotiManagerImpl notiManagerImpl = new NotiManagerImpl();
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Value("${notification.verify_url}") private String verifyUrl;
	@Value("${openapi.notification_url}") private String notificationUrl;
	
//	@Autowired
//	private OutBoundRequestHandler<Map<String,String>,String> outRequestHandler;
	
	public String verify(Map<String,String> param) throws Exception {
		logger.debug("verify() called");
		String result = notiManagerImpl.requestNotificationVerification(param);
		
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
		accessToken = oauthManager.createAccessToken().getAccessToken();
		
		Map<String,String> paramMap = getBulkPayParamMap(processingCount, path, accessToken);
		FilePaymentResult filePaymentResult = openApiManager.createFilePayment(new File(path));
				
		return objectMapper.writeValueAsString(filePaymentResult);
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
		String jobId = param.get("jobId");
		String verifySign = param.get("verifySign");
		
		File resultFile = openApiManager.getFilePaymentJobStatus(jobId, verifySign, accessToken);
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(resultFile));
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		
		while((temp = bufferedReader.readLine()) != null) {
			stringBuffer.append(temp);
		}
		
		bufferedReader.close();
		
		return stringBuffer.toString();
	}
	
	public String getTidInformation(Map<String, String> param) throws Exception {
		logger.debug("getTidInformation() called");		
		String accessToken = getAccessTokenFromOauthManager();
		String tid = param.get("tid");
		
		TransactionDetail transactionDetail = openApiManager.getPaymentTransactionDetail(tid, accessToken);
		
		return objectMapper.writeValueAsString(transactionDetail);
	}
	
	public String getRefundInformation(Map<String, String> param) throws Exception {
		logger.debug("getRefundInformation() called");		
		String accessToken = getAccessTokenFromOauthManager();
		String jsonString = param.get("jsonString");
		String tid = param.get("tid");
		
		param.put("jsonString", getRefundTransactionJsonString(param.get("tid")));
		return null;
		//return openApiManager.cancelPaymentTransaction(param);
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
		accessToken = oauthManager.createAccessToken().getAccessToken();
		
		return accessToken;
	}
	
	private String getRefundTransactionJsonString(String tid) throws Exception {
		tid = "{\"resultCode\":\"0000\",\"resultMsg\":\"SUCCESS\",\"payer\":{\"mdn\":\"01040448500\",\"carrier\":\"SKT\"},\"paymentTransactionInfo\":{\"status\":\"complite\",\"reason\":\"1\",\"message\":\"transaction was succeed\",\"lastestUpdate\":\"20150731175443\",\"tid\":\"TSTORE0004_20150731175443017804817193658\",\"amount\":1000,\"description\":\"transaction was succeed\",\"goods\":[{\"appId\":\"OA00654485\",\"productId\":\"0910009018\"}],\"paymentMethods\":[{\"paymentMethod\":\"11\",\"amount\":1000}]}}";
		System.out.println(tid);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		TransactionDetail transactionDetail = objectMapper.readValue(tid, TransactionDetail.class);
		System.out.println(objectMapper.writeValueAsString(transactionDetail));
		
		// for refund request body Payer, RefundTransactionRequest, Links
		PaymentTransactionInfo paymentTransactionInfo = transactionDetail.getPaymentTransactionInfo();
		Payer payer = transactionDetail.getPayer();
		PaymentMethods[] paymentMethods = paymentTransactionInfo.getPaymentMethods();
		
		// for making BulkJobRefund
		RefundTransactionRequest refundTransactionRequest = new RefundTransactionRequest();
		refundTransactionRequest.setTid(paymentTransactionInfo.getTid());
		refundTransactionRequest.setFullRefund(true);
		refundTransactionRequest.setAmount(paymentMethods[0].getAmount());
		refundTransactionRequest.setPaymentMethods(paymentMethods);
		refundTransactionRequest.setNote("Test refund request");
		
		payer.setAuthkey("AuthKey");
		
		Links links = new Links();
		links.setName("refundurl");
		links.setUrl("http://");
		
		CancelRequest cancelRequest = new CancelRequest();
		cancelRequest.setPayer(transactionDetail.getPayer());
		cancelRequest.setRefundTransactionRequest(refundTransactionRequest);
		cancelRequest.setLinks(links);
		System.out.println(objectMapper.writeValueAsString(cancelRequest));
		
		return objectMapper.writeValueAsString(cancelRequest);
	}
}
