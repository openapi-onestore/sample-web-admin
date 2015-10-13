package com.skplanet.openapi.external.framework;

import com.skplanet.openapi.external.payment.OpenApiException;
import com.skplanet.openapi.vo.payment.TransactionDetail;

public interface FilePaymentTrResponse {

	void onFilePaymentTrReceive(TransactionDetail transactionDetail);
	void onFilePaymentTrError(OpenApiException openApiException);
	
}
