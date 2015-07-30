package com.skplanet.openapi.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.openapi.request.outbound.PayplanetClient;

@Service("NotificationService")
public class NotificationService {

	@Autowired
	private PayplanetClient payplanetClient;
	
	/**
	 * 
	 * @param param
	 * @return // TODO return 타입을 처리 방식에 맞게 변경
	 */
	public String processNoti(Map<String, String> param) {
		// TODO Notification 처리
		// TODO 반드시 비동기로 처리되어야함.
		
		if (param.containsKey("event")) {
			try {
				// TODO 각 Event 타입에 따른 처리 로직을 추상화 할지는 알아서 결정하면 됨.
				if (param.get("event").equals("singlePayment")) {
					return processSinglePayment(param);
				} else if (param.get("event").equals("bulkJob")) {
					return processBulkJob(param);
				} else {
					// invalid the value of event
					return "Fail";
				}
			} catch (Exception e) {
				//TODO
				e.printStackTrace();
				return "Fail";
			}
		} else {
			// invalid param
			return "Fail";
		}
	}

	/**
	 * @param param
	 *            T Store Payment Transaction Notification Integration Guide
	 *            [Specification] Bulk Payment Notification Listener 참조
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String processBulkJob(Map<String, String> param) throws Exception {
		// TODO bulk job notification 처리를 한다.
		
		// TODO OpenAPI 서버에 Verify요청을 한다.
		String result = requestVerify(param, "bigcharging_notify_verification");
		
		if (result == null)
			result = "Fail";
		
		return result;
	}
	
	/**
	 * 
	 * @param param
	 *            T Store Payment Transaction Notification Integration Guide
	 *            [Specification] Single Payment Notification Listener 참조
	 * @return
	 * @throws Exception 
	 */
	public String processSinglePayment(Map<String, String> param) throws Exception {
		// TODO single payment job notification 처리를 한다.
		
		// TODO OpenAPI 서버에 Verify요청을 한다.
		String result = requestVerify(param, "singlepay_notify_verification");
		
		if (result == null)
			result = "Fail";
		
		return result;
	}
	
	public String requestVerify(Map<String, String> param, String listenerType) throws Exception {
		// TODO 응답 Params를 지정한다. 대부분 Notification 메시지를 그대로 사용하며, 추가적으로
		// "listener_type"만 추가한다.
		Map<String, String> verifyParam = new HashMap<String, String>();
		verifyParam.putAll(param);
		verifyParam.put("listenerType", listenerType);
		
		// TODO OpenAPI 서버에 Verify요청을 한다.
		String result = payplanetClient.verify(verifyParam);
		
		return result;
	}
	
	public void requestNotificationVerify(Map<String, String> param) throws Exception {		
		payplanetClient.updateNotificationVerify(param);
	}
	
	public void requestNotificationResult(Map<String, String> param) throws Exception {
		payplanetClient.insertNotificationResult(param);
	}
	
}
