package com.skplanet.openapi.external.framework;

import com.skplanet.openapi.external.notification.NotiException;
import com.skplanet.openapi.external.notification.NotiReceive;

public interface NotificationResponse {

	void onNotificationReceive(NotiReceive notiReceive);
	void onNitificationError(NotiException notiexception);
	
}
