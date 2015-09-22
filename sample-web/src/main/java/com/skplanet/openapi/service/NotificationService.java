package com.skplanet.openapi.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skplanet.openapi.external.notification.NotiReceive;
import com.skplanet.openapi.external.notification.NotiVerifyResult;
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
	public String processNoti(NotiReceive notificationResult) {
		// TODO Notification 처리
		// TODO 반드시 비동기로 처리되어야함.
		NotiVerifyResult notiVerifyResult = null;
		String result = null;
		
		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("notifyVersion", notificationResult.getNotifyVersion());
			paramMap.put("event", notificationResult.getEvent());
			paramMap.put("status", notificationResult.getStatus());
			paramMap.put("jobId", notificationResult.getJobId());
			paramMap.put("updateTime", notificationResult.getUpdateTime());
			paramMap.put("verifySign", notificationResult.getVerifySign());
			requestNotificationResult(paramMap);
			
			notiVerifyResult = payplanetClient.getNotificationVerifyResult(notificationResult, "bigcharging_notify_verification");
			System.out.println("Noti verify msg : " + notiVerifyResult.getResultCode() + " " + notiVerifyResult.getResultMsg());
			if (notiVerifyResult.getResultCode().equals("0000")) {
				Map<String, String> updateParam = new HashMap<String, String>();
				updateParam.put("jobId", notificationResult.getJobId());
				updateParam.put("verifyResult", notiVerifyResult.getResultMsg());
				requestNotificationVerify(updateParam);
			}
			result = notiVerifyResult.getResultCode();
		} catch (Exception e) {
			e.printStackTrace();
			result = "9999";
		}
		return result;
	}

	/**
	 * @param param
	 *            T Store Payment Transaction Notification Integration Guide
	 *            [Specification] Bulk Payment Notification Listener 참조
	 * 
	 * @return
	 * @throws Exception 
	 */
//	public String processBulkJob(NotificationResult notificationResult) throws Exception {
//		// TODO bulk job notification 처리를 한다.
//		
//		// TODO OpenAPI 서버에 Verify요청을 한다.
//		String result = null;
//		
//		
//		requestVerify(notificationResult, "bigcharging_notify_verification");
//		verifyParam.put("listenerType", listenerType);
//		
//		if (result == null)
//			result = "Fail";
//		
//		return result;
//	}
	
	public void requestNotificationVerify(Map<String, String> param) throws Exception {		
		payplanetClient.updateNotificationVerify(param);
	}
	
	public void requestNotificationResult(Map<String, String> param) throws Exception {
		payplanetClient.insertNotificationResult(param);
	}
	
}
