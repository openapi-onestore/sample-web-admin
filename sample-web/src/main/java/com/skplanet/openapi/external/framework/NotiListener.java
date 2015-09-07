package com.skplanet.openapi.external.framework;

import com.skplanet.openapi.external.notification.NotiReceive;

public interface NotiListener {

	void onResponse(String result, NotiReceive notiReceive);
	void onError(String error);
	
}
