package com.skplanet.openapi.external.framework;

import com.skplanet.openapi.external.payment.OpenApiException;
import com.skplanet.openapi.vo.refund.CancelResponse;

public interface FilePaymentCancelResponse {

	void onFilePaymentCancelReceived(CancelResponse cancelResponse);
	void onFilePaymentCancelError(OpenApiException openApiException);
	
}
