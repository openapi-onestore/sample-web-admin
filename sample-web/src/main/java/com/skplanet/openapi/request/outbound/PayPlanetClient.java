package com.skplanet.openapi.request.outbound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skplanet.openapi.request.outbound.header.BatchFileVersionHeader;
import com.skplanet.openapi.request.outbound.header.MerchantIdHeader;
import com.skplanet.openapi.request.outbound.header.NotiUrlHeader;
import com.skplanet.openapi.request.outbound.header.ProcessingCountHeader;
import com.skplanet.openapi.service.BulkJobService;
import com.skplanet.openapi.util.HttpClient;
import com.skplanet.openapi.util.HttpHeader;

@Component("payPlanetClient")
public class PayPlanetClient {
	
	private static final Logger logger = LoggerFactory.getLogger(PayPlanetClient.class);
	
//	@Autowired
//	private OutBoundRequestHandler<Map<String,String>,String> outRequestHandler;
	
	public String verify(Map<String,String> param) throws Exception {		
		String url = "http://{payplanet_domain}/openapi/v1/payment/notification/verify";
		HttpClient client = new HttpClient();
		String response = client.post(url,param);
		return response;
	}
	
	public String createBulkPayment(int processingCount, String path) throws Exception {
		
		List<HttpHeader> header = new ArrayList<HttpHeader>();
		header.add(new BatchFileVersionHeader("1"));
		header.add(new MerchantIdHeader("skplanet"));
		header.add(new NotiUrlHeader("http://localhost:8080/sample-web/openapi/notification/noti_listener"));
		header.add(new ProcessingCountHeader(Integer.toString(processingCount)));
		
		logger.debug("Path : " + path);
		
		HttpClient client = new HttpClient(header);
		String response = client.postChunkedString("http://localhost:8080/SpringMVC/rest/bulkjob/get", path);
		
		return response;
		
	}
	
}
