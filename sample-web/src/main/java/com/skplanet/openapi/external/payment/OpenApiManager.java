package com.skplanet.openapi.external.payment;

import java.io.File;

import com.skplanet.openapi.vo.payment.FilePaymentResult;
import com.skplanet.openapi.vo.payment.TransactionDetail;
import com.skplanet.openapi.vo.refund.CancelRequest;
import com.skplanet.openapi.vo.refund.CancelResponse;

public interface OpenApiManager {
	
	public FilePaymentResult createFilePayment(File file) throws OpenApiException;
//	public FilePaymentResult createFilePayment(List<File> paymentFileList);
	public File getFilePaymentJobStatus(String jobId, String verifySign, String accessToken) throws OpenApiException;
	public TransactionDetail getPaymentTransactionDetail(String tid, String accessToken) throws OpenApiException;
	public CancelResponse cancelPaymentTransaction(CancelRequest cancelRequest, String accessToken) throws OpenApiException;
	
}
