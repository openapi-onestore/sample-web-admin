package com.skplanet.openapi.external.notification;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.codehaus.jackson.map.ObjectMapper;

import com.skplanet.openapi.external.notification.NotiException.Noti;

public class NotiManager implements NotiInterface {

	private int threadPoolCount = 1;
	private ExecutorService jobExecutor = Executors.newFixedThreadPool(threadPoolCount);
	private String verifyUrl = "http://172.21.60.142/openapi/v1/payment/notification/verify";
	
	@Override
	public NotiReceive receiveNotificationFromServer(String result) throws NotiException {
		
		String[] splitResult = result.split("&");
		Map<String, String> paramMap = new HashMap<String, String>();
		
		for (String results : splitResult) {
			String[] temp = results.split("=");
			paramMap.put(temp[0], temp[1]);
		}
		
		ObjectMapper om = new ObjectMapper();
		NotiReceive notiReceive = null;
		
		try {
			notiReceive = om.readValue(om.writeValueAsString(paramMap),NotiReceive.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotiException(Noti.PARAMETER_PARSING_ERROR, "Json Parsing Error");
		};
		
		return notiReceive;
	}
	
	@Override
	public String requestNotificationVerification(Map<String, String> params) throws NotiException {
		
		NotiVerificationTransaction notiVerificationTransaction = new NotiVerificationTransaction();
		notiVerificationTransaction.setParamMap(params);
		notiVerificationTransaction.setCallUrl(verifyUrl);
		
		Future<String> future = jobExecutor.submit(notiVerificationTransaction);
		String result = null;
		
		try {
			result = future.get();
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotiException(Noti.UNKNOWN_ERROR, "future execute error");
		} 
		
		return result;
	}
	
}
