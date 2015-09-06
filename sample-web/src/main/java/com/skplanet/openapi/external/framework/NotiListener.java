package com.skplanet.openapi.external.framework;

public interface NotiListener {

	void onResponse(String result);
	void onError(String error);
	
}
