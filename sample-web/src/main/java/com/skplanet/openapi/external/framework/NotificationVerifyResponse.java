package com.skplanet.openapi.external.framework;

import com.skplanet.openapi.external.notification.NotiException;
import com.skplanet.openapi.external.notification.NotiVerifyResult;

public interface NotificationVerifyResponse {

	void onNotificationReceive(NotiVerifyResult notiVerifyResult);
	void onNitificationError(NotiException notiexception);
	
}
