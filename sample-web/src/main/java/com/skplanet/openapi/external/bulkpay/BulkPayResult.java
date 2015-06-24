package com.skplanet.openapi.external.bulkpay;

public interface BulkPayResult {
	
	void onResponse(String result);
	void onError(String result);
	
}
