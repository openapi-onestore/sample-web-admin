package com.skplanet.openapi.external.framework;

import java.io.File;

import com.skplanet.openapi.external.payment.OpenApiException;

public interface FilePaymentJobResponse {
	
	void onFilePaymentJobReceive(File jobFile);
	void onFilePaymentJobError(OpenApiException openApiException);
	
}
