package com.skplanet.openapi.external.bulkpay;

import java.util.Map;

public interface BulkPayInterface {

	String createFilePayment(Map<String, String> paramMap) throws BulkPayException;
	String getFilePaymentInfo(Map<String, String> paramMap) throws BulkPayException;
	String getPaymentTransactionDetail(Map<String, String> paramMap) throws BulkPayException;
	String cancelPaymentTransaction(Map<String, String> paramMap) throws BulkPayException;
	
}
