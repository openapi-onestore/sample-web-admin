package com.skplanet.openapi.external.payment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.codehaus.jackson.map.ObjectMapper;

import com.skplanet.openapi.external.payment.OpenApiException.OpenApi;
import com.skplanet.openapi.vo.payment.FilePaymentHeader;
import com.skplanet.openapi.vo.payment.FilePaymentResult;
import com.skplanet.openapi.vo.payment.TransactionDetail;
import com.skplanet.openapi.vo.refund.CancelRequest;
import com.skplanet.openapi.vo.refund.CancelResponse;

public class OpenApiManagerImpl implements OpenApiManager{

	private ObjectMapper objectMapper = new ObjectMapper();
	private int threadPoolCount = 1;
	private ExecutorService jobExecutor = Executors.newFixedThreadPool(
			threadPoolCount, new ThreadFactory() {
				@Override
				public Thread newThread(Runnable runnable) {
					Thread bulkPayThread = Executors.defaultThreadFactory()
							.newThread(runnable);
					bulkPayThread.setName("OpenApiManager");
					bulkPayThread.setDaemon(true);
					return bulkPayThread;
				}
			});
	
	// Property values, uri is default setting
	private String propertyPath = null;
	private String fileWritePath = "D:/samplefolder/bulkfile";
	private String fileJobUrl = "http://172.21.60.141/v1/payment/fileJob";
	private String resultFileUrl = "http://172.21.60.141/v1/payment/job";
	private String txidInfoUrl = "http://172.21.60.141/v1/payment/transaction";
	private String refundUrl = "http://172.21.60.141/v1/payment/refund";
	
	@Override
	public FilePaymentResult createFilePayment(FilePaymentHeader filePaymentHeader, File file, String accessToken) throws OpenApiException {
		
		if (file == null || !file.canRead()) {
			throw new OpenApiException(OpenApi.OPENAPI_FILE_NOT_FOUND_ERROR, "{'reason':'File not found.'}");
		}
		
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("verBulkPay", filePaymentHeader.getVerBulkPay());
		headerMap.put("bid", filePaymentHeader.getBid());
		headerMap.put("notiUrl", filePaymentHeader.getNotiUrl());
		headerMap.put("postbackUrl", filePaymentHeader.getPostbackUrl());
		headerMap.put("cntTotalTrans", filePaymentHeader.getCntTotalTrans());
		headerMap.put("priority", filePaymentHeader.getPrioity());
//		headerMap.put("Authorization", "Bearer " + accessToken);
		headerMap.put("accessToken", accessToken);
		
		System.out.println(headerMap);
		
		OpenApiPostTransaction openApiPostTransaction = new OpenApiPostTransaction(file);
		openApiPostTransaction.setParamMap(headerMap);
		openApiPostTransaction.setCallUrl(fileJobUrl);
		openApiPostTransaction.setChunked(true);
		
		Future<String> future = jobExecutor.submit(openApiPostTransaction);
		
		try {
			String result = future.get();
			FilePaymentResult filePaymentResult = objectMapper.readValue(result, FilePaymentResult.class);
			return filePaymentResult;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenApiException(OpenApi.OPENAPI_JOB_EXECUTE_ERROR, e.getMessage());
		}
		
	}
	
	@Override
	public File getFilePaymentJobStatus(String jobId, String verifySign, String accessToken) throws OpenApiException {
		
		// authorization
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("accessToken", accessToken);
		
		File resultFile = null;
		
		OpenApiGetFileTransaction openApiGetFileTransaction = new OpenApiGetFileTransaction(paramMap);
		openApiGetFileTransaction.setCallUrl(resultFileUrl.concat("/" + jobId + "?signCode=" + verifySign));
		openApiGetFileTransaction.setFileWritePath(fileWritePath);

		Future<File> future = jobExecutor.submit(openApiGetFileTransaction);
		
		try {
			resultFile = future.get();
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenApiException(OpenApi.OPENAPI_JOB_EXECUTE_ERROR, e.getMessage());
		}
		
		return resultFile;
	}
	
	@Override
	public TransactionDetail getPaymentTransactionDetail(String tid, String accessToken) throws OpenApiException {
		
		// authorization
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("accessToken", accessToken);
		
		OpenApiGetTransaction openApiGetTransaction = new OpenApiGetTransaction(paramMap);
		openApiGetTransaction.setCallUrl(txidInfoUrl.concat("/" + tid));
		
		Future<String> future = jobExecutor.submit(openApiGetTransaction);
		
		String result = null;
		TransactionDetail transactionDetail = null;
		
		try {
			result = future.get();
			transactionDetail = objectMapper.readValue(result, TransactionDetail.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenApiException(OpenApi.OPENAPI_JOB_EXECUTE_ERROR, e.getMessage());
		}
		
		return transactionDetail;
	}
	
	@Override
	public CancelResponse cancelPaymentTransaction(CancelRequest cancelRequest, String accessToken) throws OpenApiException {		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("accessToken", accessToken);
		
		CancelResponse cancelResponse = null;
		OpenApiPostTransaction openApiPostTransaction = null;
		
		try {
			String cancelJsonString = objectMapper.writeValueAsString(cancelRequest);
			openApiPostTransaction = new OpenApiPostTransaction(cancelJsonString);
			openApiPostTransaction.setCallUrl(refundUrl);
			
			Future<String> future = jobExecutor.submit(openApiPostTransaction);
			String result = future.get();
			
			cancelResponse = objectMapper.readValue(result, CancelResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenApiException(OpenApi.OPENAPI_JOB_EXECUTE_ERROR, e.getMessage());
		}
		
		return cancelResponse;
	}
	
	public void setPropertyFile(String path) throws Exception {
		this.propertyPath = path;
		Properties props = new Properties();
		
		if (propertyPath == null) {
			throw new OpenApiException(OpenApi.OPENAPI_PROPERTY_SETTING_ERROR, "Property path is null");
		}
		
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(propertyPath);
			props.load(new BufferedInputStream(fis));

			fileWritePath = props.getProperty("openapi.file_write_path");
			fileJobUrl = props.getProperty("openapi.file_payment_url");
			resultFileUrl = props.getProperty("openapi.result_file_url");
			txidInfoUrl = props.getProperty("openapi.txid_info_url");
			refundUrl = props.getProperty("openapi.refund_url");
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenApiException(OpenApi.OPENAPI_PROPERTY_SETTING_ERROR, "File creation is incorect!!");
		} finally {
			fis.close();
		}
	}
	
	public void setExecutorService(ExecutorService service) {
		if (jobExecutor != null) {
			this.jobExecutor.shutdown();			
		}
		this.jobExecutor = service;
	}	
	
}
