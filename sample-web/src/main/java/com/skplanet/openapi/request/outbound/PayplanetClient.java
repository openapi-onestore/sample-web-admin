package com.skplanet.openapi.request.outbound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.skplanet.openapi.dao.FilePaymentDAO;
import com.skplanet.openapi.dao.NotificationDAO;
import com.skplanet.openapi.external.notification.NotiException;
import com.skplanet.openapi.external.notification.NotiManagerImpl;
import com.skplanet.openapi.external.notification.NotiReceive;
import com.skplanet.openapi.external.notification.NotiVerifyResult;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuthManagerImpl;
import com.skplanet.openapi.external.payment.OpenApiManagerImpl;
import com.skplanet.openapi.vo.NotificationResult;
import com.skplanet.openapi.vo.payment.FilePaymentHeader;
import com.skplanet.openapi.vo.payment.FilePaymentResult;
import com.skplanet.openapi.vo.payment.Payer;
import com.skplanet.openapi.vo.payment.PaymentMethods;
import com.skplanet.openapi.vo.payment.PaymentTransactionInfo;
import com.skplanet.openapi.vo.payment.TransactionDetail;
import com.skplanet.openapi.vo.refund.CancelRequest;
import com.skplanet.openapi.vo.refund.CancelResponse;
import com.skplanet.openapi.vo.refund.Links;
import com.skplanet.openapi.vo.refund.RefundTransactionRequest;

@Component("payplanetClient")
public class PayplanetClient {
	
	private static final Logger logger = LoggerFactory.getLogger(PayplanetClient.class);
	
	@Autowired
	private FilePaymentDAO filePaymentDAO;
	
	@Autowired
	private NotificationDAO notificationDAO;
	
	private OAuthManagerImpl oauthManager = new OAuthManagerImpl();
	private OpenApiManagerImpl openApiManager = new OpenApiManagerImpl();
	private NotiManagerImpl notiManagerImpl = new NotiManagerImpl();
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Value("${notification.verify_url}") private String verifyUrl;
	@Value("${openapi.notification_url}") private String notificationUrl;
	
	public PayplanetClient() {
		System.out.println("Initiation for Manager");
		try {
			String path = new ClassPathResource("properties/config.properties").getFile().getAbsolutePath();
			oauthManager.setPropertyFile(path);
			openApiManager.setPropertyFile(path);
			notiManagerImpl.setPropertyFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public FilePaymentResult createFilePayment(int processingCount, File requestFile) throws Exception {		
		logger.debug("createBulkPayment() called");
		
		String accessToken = null;
		
		OAuthClientInfo oauthClientInfo = new OAuthClientInfo();
		oauthClientInfo.setClientId("pfnWKkKJzSZipMTVKg2FIsj1FQory8Hh/mqAy1/CTI8=");
		oauthClientInfo.setClientSecret("jizYJ/CY1lJdgmSy6c3vvzFHxUHddz36DX5ubMcsgCI=");
		oauthClientInfo.setGrantType("client_credentials");
		oauthManager.setClientInfo(oauthClientInfo);
		accessToken = oauthManager.createAccessToken().getAccessToken();
		
		FilePaymentResult filePaymentResult = openApiManager.createFilePayment(getFilepaymentHeader(processingCount), requestFile, accessToken);
		
		return filePaymentResult;
	}
	
	public void insertFilePaymentRequest(Map<String,String> param) throws Exception {
		logger.debug("insertFilePaymentRequest() called");
		filePaymentDAO.insertFilePaymentRequest(param);
	}
	
	public List<Map<String, String>> selectFilePaymentRequest() throws Exception {
		logger.debug("selectFilePaymentRequest() called");
		List<Map<String, String>> result = filePaymentDAO.selectFilePaymentRequest();
		return result;
	}
	
	public void insertNotificationResult(Map<String,String> param) throws Exception {
		logger.debug("insertNotificationResult() called");
		notificationDAO.addNotificationResult(param);
	}
	
	public void updateNotificationVerify(Map<String,String> param) throws Exception {
		logger.debug("updateNotificationVerify() called");
		filePaymentDAO.updateNotifiVerified(param);
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
	
	public String getFilePaymentResultFile(Map<String, String> param) throws Exception {
		logger.debug("getFilePaymentResultFile() called");		
		String accessToken = getAccessTokenFromOauthManager();
		String jobId = param.get("jobId");
		
		File resultFile = openApiManager.getFilePaymentJobStatus(jobId, accessToken);
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(resultFile));
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		stringBuffer.append(resultFile.getName()).append("\n");
		
		while((temp = bufferedReader.readLine()) != null) {
			stringBuffer.append(temp).append("\n");
		}
		
		bufferedReader.close();
		
		return stringBuffer.toString();
	}
	
	public String getTidInformation(String tid) throws Exception {
		logger.debug("getTidInformation() called");		
		String accessToken = getAccessTokenFromOauthManager();
		TransactionDetail transactionDetail = openApiManager.getPaymentTransactionDetail(tid, accessToken);
		
		return objectMapper.writeValueAsString(transactionDetail);
	}
	
	public String getRefundInformation(Map<String, String> param) throws Exception {
		logger.debug("getRefundInformation() called");		
		String accessToken = getAccessTokenFromOauthManager();
		String jsonTransactionString = param.get("transactionInfo");
		
		CancelRequest cancelRequest = getRefundTransactionJsonString(jsonTransactionString);
		CancelResponse cancelResponse = openApiManager.cancelPaymentTransaction(cancelRequest, accessToken);
		
		return objectMapper.writeValueAsString(cancelResponse);
	}
	
	public NotiReceive getNotiReceive(String notification) throws Exception{
		NotiReceive notiReceive = null;
		
		try {
			notiReceive = notiManagerImpl.receiveNotificationFromServer(notification);
		} catch (NotiException e) {
			e.printStackTrace();
		}
		return notiReceive;
	}
	
	public NotiVerifyResult getNotificationVerifyResult(NotiReceive notiReceive, String listenerType) {
		NotiVerifyResult notiVerifyResult = null;
		try {
			String accessToken = getAccessTokenFromOauthManager();
			notiVerifyResult = notiManagerImpl.requestNotificationVerification(notiReceive, listenerType, accessToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notiVerifyResult;
	}
	
	private FilePaymentHeader getFilepaymentHeader(int processingCount) {
		FilePaymentHeader filePaymentHeader = new FilePaymentHeader();
		filePaymentHeader.setVerBulkPay("1");
		filePaymentHeader.setBid("skplanet");
		filePaymentHeader.setNotiUrl(notificationUrl);
		filePaymentHeader.setCntTotalTrans(Integer.toString(processingCount));
		filePaymentHeader.setPrioity("Instant");
		return filePaymentHeader;
	}
	
	private String getAccessTokenFromOauthManager() throws Exception {
		String accessToken = null;
		
		OAuthClientInfo oauthClientInfo = new OAuthClientInfo();
		oauthClientInfo.setClientId("pfnWKkKJzSZipMTVKg2FIsj1FQory8Hh/mqAy1/CTI8=");
		oauthClientInfo.setClientSecret("jizYJ/CY1lJdgmSy6c3vvzFHxUHddz36DX5ubMcsgCI=");
		oauthClientInfo.setGrantType("client_credentials");
		
		oauthManager.setClientInfo(oauthClientInfo);
		accessToken = oauthManager.createAccessToken().getAccessToken();
		
		return accessToken;
	}
	
	private CancelRequest getRefundTransactionJsonString(String jsonTransactionInfo) throws Exception {
		//tid = "{\"resultCode\":\"0000\",\"resultMsg\":\"SUCCESS\",\"payer\":{\"mdn\":\"01040448500\",\"carrier\":\"SKT\"},\"paymentTransactionInfo\":{\"status\":\"complite\",\"reason\":\"1\",\"message\":\"transaction was succeed\",\"lastestUpdate\":\"20150731175443\",\"tid\":\"TSTORE0004_20150731175443017804817193658\",\"amount\":1000,\"description\":\"transaction was succeed\",\"goods\":[{\"appId\":\"OA00654485\",\"productId\":\"0910009018\"}],\"paymentMethods\":[{\"paymentMethod\":\"11\",\"amount\":1000}]}}";
		System.out.println(jsonTransactionInfo);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		TransactionDetail transactionDetail = objectMapper.readValue(jsonTransactionInfo, TransactionDetail.class);
		System.out.println(objectMapper.writeValueAsString(transactionDetail));
		
		// for refund request body Payer, RefundTransactionRequest, Links
		PaymentTransactionInfo paymentTransactionInfo = transactionDetail.getPaymentTransactionInfo();
		
		if (paymentTransactionInfo == null) {
			throw new Exception();
		}
		
		Payer payer = transactionDetail.getPayer();
		PaymentMethods[] paymentMethods = paymentTransactionInfo.getPaymentMethods();
		
		// for making FilePaymentRefund
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
		
		return cancelRequest;
	}
	
}
