package com.skplanet.openapi.request.outbound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuth;
import com.skplanet.openapi.external.oauth.OAuthVerifyResult;
import com.skplanet.openapi.request.outbound.header.BatchFileVersionHeader;
import com.skplanet.openapi.request.outbound.header.MerchantIdHeader;
import com.skplanet.openapi.request.outbound.header.NotiUrlHeader;
import com.skplanet.openapi.request.outbound.header.ProcessingCountHeader;
import com.skplanet.openapi.util.HttpClient;
import com.skplanet.openapi.util.HttpHeader;
import com.skplanet.openapi.util.HttpRequest;

@Component("payplanetClient")
public class PayplanetClient {
	
	private static final Logger logger = LoggerFactory.getLogger(PayplanetClient.class);
	
	private ExecutorService jobExecutor = Executors.newFixedThreadPool(2);
	
	@Autowired
	private HttpClient httpClient;
	
	@Autowired
	private HttpRequest httpRequest;
	
	@Value("${openapi.verify_url}") private String verifyUrl;
	@Value("${openapi.notification_url}") private String notificationUrl;
	@Value("${openapi.bulkjob_url}") private String bulkjobUrl;

	@Value("${oauth.token_url}") private String oauthUrl;
	@Value("${oauth.token_verify_url}") private String oauthVerifyUrl;
	
	@Value("${oauth.token_verify_ip}")private String oauthVerifyIp;
	@Value("${oauth.token_verify_api_id}")private String oauthApiId;
	
//	@Autowired
//	private OutBoundRequestHandler<Map<String,String>,String> outRequestHandler;
	
	public String verify(Map<String,String> param) throws Exception {
		httpRequest.setCallUrl(verifyUrl);
		httpRequest.setParamMap(param);
		Future<String> future = jobExecutor.submit(httpRequest);
		
		String response = future.get();
		return response;
	}
	
	public String createBulkPayment(int processingCount, String path) throws Exception {		
		// TODO: Header create
		List<HttpHeader> header = new ArrayList<HttpHeader>();
		header.add(new BatchFileVersionHeader("1"));
		header.add(new MerchantIdHeader("skplanet"));
		header.add(new NotiUrlHeader(notificationUrl));
		header.add(new ProcessingCountHeader(Integer.toString(processingCount)));
				
		httpRequest.setHeader(header);
		
		//HttpClient client = new HttpClient(header);
		httpClient.setHeaders(header);
		String response = httpClient.postChunkedString(bulkjobUrl, path);
		
		return response;
	}
	
}
