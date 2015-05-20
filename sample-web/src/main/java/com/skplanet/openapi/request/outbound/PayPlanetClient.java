package com.skplanet.openapi.request.outbound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.skplanet.openapi.request.outbound.header.BatchFileVersionHeader;
import com.skplanet.openapi.request.outbound.header.MerchantIdHeader;
import com.skplanet.openapi.request.outbound.header.NotiUrlHeader;
import com.skplanet.openapi.request.outbound.header.ProcessingCountHeader;
import com.skplanet.openapi.util.HttpClient;
import com.skplanet.openapi.util.HttpHeader;
import com.skplanet.openapi.vo.ClientInfo;
import com.skplanet.openapi.vo.OAuth;
import com.sun.security.ntlm.Client;

@Component("payPlanetClient")
public class PayPlanetClient {
	
	private static final Logger logger = LoggerFactory.getLogger(PayPlanetClient.class);
	
	@Autowired
	private HttpClient httpClient;
	
	@Value("${openapi.verify_url}") private String verifyUrl;
	@Value("${openapi.notification_url}") private String notificationUrl;
	@Value("${openapi.bulkjob_url}") private String bulkjobUrl;

	@Value("${oauth.token_url}") private String oauthUrl;
	@Value("${oauth.token_verify_url") private String oauthVerifyUrl;
	
//	@Autowired
//	private OutBoundRequestHandler<Map<String,String>,String> outRequestHandler;
	
	public String verify(Map<String,String> param) throws Exception {		
		//HttpClient client = new HttpClient();
		String response = httpClient.post(verifyUrl,param);
		return response;
	}
	
	public String createBulkPayment(int processingCount, String path) throws Exception {		
		// TODO: Header create
		List<HttpHeader> header = new ArrayList<HttpHeader>();
		header.add(new BatchFileVersionHeader("1"));
		header.add(new MerchantIdHeader("skplanet"));
		header.add(new NotiUrlHeader(notificationUrl));
		header.add(new ProcessingCountHeader(Integer.toString(processingCount)));
		
		logger.debug("Path : " + path + " bulkjob url : " + bulkjobUrl);
		
		//HttpClient client = new HttpClient(header);
		httpClient.setHeaders(header);
		String response = httpClient.postChunkedString(bulkjobUrl, path);
		
		return response;
	}
	
	public OAuth createOAuthToken(ClientInfo clientInfo) {
		
		Map<String, String> data = new HashMap<String, String>();
		data.put(ClientInfo.CLIENT_ID, clientInfo.getClientId());
		data.put(ClientInfo.CLIENT_SECRET, clientInfo.getClientSecret());
		data.put(ClientInfo.GRANT_TYPE, clientInfo.getGrantType());
		
		String response = null;
		OAuth oauth = null;
		
		try {
			response = httpClient.post(oauthUrl, data);
			System.out.println("Post response : " + response);
			if (response != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				oauth = objectMapper.readValue(response, OAuth.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return oauth;
		
	}
	
}
