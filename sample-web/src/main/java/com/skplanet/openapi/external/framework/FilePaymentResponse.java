package com.skplanet.openapi.external.framework;

import com.skplanet.openapi.external.payment.OpenApiException;
import com.skplanet.openapi.vo.payment.FilePaymentResult;

public interface FilePaymentResponse {
	
	void onFilePaymentReceive(FilePaymentResult filePaymentResult);
	void onFilePaymentError(OpenApiException openApiException);

}
