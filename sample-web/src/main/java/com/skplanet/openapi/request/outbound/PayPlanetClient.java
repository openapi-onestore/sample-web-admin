package com.skplanet.openapi.request.outbound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skplanet.openapi.request.outbound.header.BatchFileVersionHeader;
import com.skplanet.openapi.request.outbound.header.MerchantIdHeader;
import com.skplanet.openapi.request.outbound.header.NotiUrlHeader;
import com.skplanet.openapi.request.outbound.header.ProcessingCountHeader;
import com.skplanet.openapi.util.HttpClient;
import com.skplanet.openapi.util.HttpHeader;

@Component("payPlanetClient")
public class PayPlanetClient {
	
	@Autowired
	private OutBoundRequestHandler<Map<String,String>,String> requestHandler;
	
	public String verify(Map<String,String> param) throws Exception {		
		String url = "http://{payplanet_domain}/openapi/v1/payment/notification/varify";
		HttpClient client = new HttpClient();
		String response = client.post(url,param);
		return response;
	}
	
	public String createBulkPayment(int processingCount, String path) throws Exception {
			
		List<HttpHeader> header = new ArrayList<HttpHeader>();
		header.add(new BatchFileVersionHeader("1"));
		header.add(new MerchantIdHeader("skplanet"));
		header.add(new NotiUrlHeader("http://[user_app_server_ip]/notification/noti_listener"));
		header.add(new ProcessingCountHeader(Integer.toString(processingCount)));
				
		HttpClient client = new HttpClient(header);
		String response = client.postChunkedString("http://{payplanet_domain}/openapi/v1/payment/bulkjob", path);
		
		return response;
		
	}
	
}
