package com.skplanet.openapi.external.notification;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.codehaus.jackson.map.ObjectMapper;

import com.skplanet.openapi.external.notification.NotiException.Noti;

public class NotiManagerImpl implements NotiManager {

	private int threadPoolCount = 1;
	private ExecutorService jobExecutor = Executors.newFixedThreadPool(threadPoolCount, new ThreadFactory() {
		@Override
		public Thread newThread(Runnable runnable) {
			Thread notiManagerThread = Executors.defaultThreadFactory().newThread(runnable);
			notiManagerThread.setName("notiManager");
			notiManagerThread.setDaemon(true);
			return notiManagerThread;
		}
	});
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private String propertyPath = null;
	private String verifyUrl = "http://172.21.60.142/openapi/v1/payment/notification/verify";
	
	@Override
	public NotiReceive receiveNotificationFromServer(String notificationResult) throws NotiException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		NotiReceive notiReceive = null;
		
		try {
			notiReceive = objectMapper.readValue(objectMapper.writeValueAsString(notificationResult),NotiReceive.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotiException(Noti.PARAMETER_PARSING_ERROR, "Json Parsing Error");
		};
		
		return notiReceive;
	}
	
	@Override
	public NotiVerifyResult requestNotificationVerification(NotiReceive notiReceive, String listenerType, String accessToken) throws NotiException {
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("listenerType", listenerType);
		paramMap.put("notifyVersion", notiReceive.getNotifyVersion());
		paramMap.put("event", notiReceive.getEvent());
		paramMap.put("status", notiReceive.getStatus());
		paramMap.put("jobId", notiReceive.getJobId());
		paramMap.put("updateTime", notiReceive.getUpdateTime());
		paramMap.put("verifySign", notiReceive.getVerifySign());
		
		NotiVerificationTransaction notiVerificationTransaction = new NotiVerificationTransaction();
		
		try {
			notiVerificationTransaction.setParam(objectMapper.writeValueAsString(paramMap));
		} catch (Exception exception) {
			
		}
		
		paramMap.clear();
		paramMap.put("Authorization", "Bearer "+accessToken);
		notiVerificationTransaction.setHeader(paramMap);
		notiVerificationTransaction.setCallUrl(verifyUrl);
		
		Future<String> future = jobExecutor.submit(notiVerificationTransaction);
		String result = null;
		NotiVerifyResult notiVerifyResult = null;
		
		try {
			result = future.get();
			System.out.println("Notification verify Result" + result);
			notiVerifyResult = objectMapper.readValue(result, NotiVerifyResult.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotiException(Noti.NOTI_JOB_EXECUTE_FAIL_ERROR, "NotificationVerification reuqest execute error");
		}
		
		return notiVerifyResult;
	}
	
	@Override
	public void setPropertyFile(String path) throws Exception {
		this.propertyPath = path;
		Properties props = new Properties();

		if (propertyPath == null) {
			throw new NotiException(Noti.NOTI_PROPERTY_SETTING_ERROR,"Property path is null");
		}
		
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(propertyPath);
			props.load(new BufferedInputStream(fis));
			
			verifyUrl = props.getProperty("notification.verify_url");
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotiException(Noti.NOTI_PROPERTY_SETTING_ERROR, "File creation is incorect!!");
		} finally {
			fis.close();
		}
	}
	
	public void setExecutorService(ExecutorService service) {
		if (jobExecutor != null) {
			this.jobExecutor.shutdown();			
		}
		this.jobExecutor = service;
	}
	
}
