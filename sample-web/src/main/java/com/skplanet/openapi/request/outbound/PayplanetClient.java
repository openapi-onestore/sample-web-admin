package com.skplanet.openapi.request.outbound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.skplanet.openapi.dao.BulkJobDAO;
import com.skplanet.openapi.external.bulkpay.BulkPayManager;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuthManager;
import com.skplanet.openapi.util.HttpClient;
import com.skplanet.openapi.util.HttpRequest;

@Component("payplanetClient")
public class PayplanetClient {
	
	private static final Logger logger = LoggerFactory.getLogger(PayplanetClient.class);
	
	private ExecutorService jobExecutor = Executors.newFixedThreadPool(2);
	
	@Autowired
	private HttpClient httpClient;
	
	@Autowired
	private HttpRequest httpRequest;
	
	@Autowired
	private BulkJobDAO bulkJobDao;
	
	private OAuthManager oauthManager = new OAuthManager();
	private BulkPayManager BulkPayManager = new BulkPayManager();
	
	@Value("${openapi.verify_url}") private String verifyUrl;
	@Value("${openapi.notification_url}") private String notificationUrl;
	@Value("${openapi.bulkjob_url}") private String bulkjobUrl;
	
//	@Autowired
//	private OutBoundRequestHandler<Map<String,String>,String> outRequestHandler;
	
	public String verify(Map<String,String> param) throws Exception {
		logger.debug("verify() called");
		httpRequest.setCallUrl(verifyUrl);
		httpRequest.setParamMap(param);
		Future<String> future = jobExecutor.submit(httpRequest);
		
		String response = future.get();
		return response;
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
		String response = BulkPayManager.createFilePayment(paramMap);
		
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
