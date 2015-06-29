package com.skplanet.openapi.external.notification;

import java.util.Map;

public interface NotiInterface {

	NotiReceive receiveNotificationFromServer(String result) throws NotiException;
	String requestNotificationVerification(Map<String,String> params) throws NotiException;
	
}