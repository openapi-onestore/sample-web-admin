package com.skplanet.openapi.external.bulkpay;

import java.io.File;

public interface OpenApiManager {
	
	public Object createFilePayment(File file);
//	public Object createFilePayment(List<Object> file);
	public Object getFilePaymentJobStatus(String jobId);
	public Object cancelPaymentTransaction(String txId);
	public Object getPaymentTransaction(String txId);
	
}
