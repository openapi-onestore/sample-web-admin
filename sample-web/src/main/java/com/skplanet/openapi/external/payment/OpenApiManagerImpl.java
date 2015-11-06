package com.skplanet.openapi.external.payment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skplanet.openapi.external.payment.OpenApiException.OpenApi;
import com.skplanet.openapi.external.util.FilePostRequest;
import com.skplanet.openapi.external.util.GetFileRequest;
import com.skplanet.openapi.external.util.GetInsRequest;
import com.skplanet.openapi.external.util.GetRequest;
import com.skplanet.openapi.external.util.JsonPostRequest;
import com.skplanet.openapi.vo.payment.FilePaymentHeader;
import com.skplanet.openapi.vo.payment.FilePaymentResult;
import com.skplanet.openapi.vo.payment.TransactionDetail;
import com.skplanet.openapi.vo.refund.CancelRequest;
import com.skplanet.openapi.vo.refund.CancelResponse;

public class OpenApiManagerImpl implements OpenApiManager {

	private static final Logger logger = LoggerFactory
			.getLogger(OpenApiManagerImpl.class);
	private ObjectMapper objectMapper = new ObjectMapper();

	// Property values, uri is default setting
	private String fileJobUrl = "http://172.21.60.141/v1/payment/fileJob";
	private String resultFileUrl = "http://172.21.60.141/v1/payment/job";
	private String txidInfoUrl = "http://172.21.60.141/v1/payment/transaction";
	private String refundUrl = "http://172.21.60.141/v1/payment/refund";

	public OpenApiManagerImpl() { }

	public OpenApiManagerImpl(String logPath) {
		if (logPath != null) {
			if (logPath.length() > 0) {
				PropertyConfigurator.configure(logPath);				
			}
		}
	}

	@Override
	public FilePaymentResult createFilePayment(
			FilePaymentHeader filePaymentHeader, File file, String accessToken)
			throws OpenApiException {
		if (file == null || !file.canRead()) {
			throw new OpenApiException(OpenApi.OPENAPI_FILE_NOT_FOUND_ERROR,
					"{'reason':'File not found.'}");
		}

		String result = null;
		FilePaymentResult filePaymentResult = null;

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("verBulkPay", filePaymentHeader.getVerBulkPay());
		headerMap.put("bid", filePaymentHeader.getBid());
		headerMap.put("notiUrl", filePaymentHeader.getNotiUrl());
		headerMap.put("cntTotalTrans", String.valueOf(filePaymentHeader.getCntTotalTrans()));
		headerMap.put("priority", filePaymentHeader.getPriority());
		headerMap.put("Authorization", "Bearer " + accessToken);

		System.out.println(headerMap);

		FilePostRequest filePostRequest = new FilePostRequest();
		filePostRequest.setHeader(headerMap);
		filePostRequest.setCallUrl(fileJobUrl);
		filePostRequest.setParameter(file);

		try {
			result = filePostRequest.executeRequest();
			filePaymentResult = objectMapper.readValue(result,
					FilePaymentResult.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenApiException(OpenApi.OPENAPI_JOB_EXECUTE_ERROR,
					e.getMessage());
		}

		return filePaymentResult;
	}

	@Override
	public void getFilePaymentJobStatus(String jobId, File targetFile,
			String accessToken) throws OpenApiException {
		if (jobId == null || targetFile == null) {
			throw new OpenApiException(OpenApi.OPENAPI_FILE_NOT_FOUND_ERROR,
					"{'reason':'Job ID or targetFile is null.'}");
		}

		// authorization
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("Authorization", "Bearer " + accessToken);

		GetFileRequest getFileRequest = new GetFileRequest();
		getFileRequest.setCallUrl(resultFileUrl.concat("/" + jobId));
		getFileRequest.setHeader(paramMap);
		getFileRequest.setParameter(targetFile);

		try {
			getFileRequest.executeRequest();
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenApiException(OpenApi.OPENAPI_JOB_EXECUTE_ERROR,
					e.getMessage());
		}
	}

	@Override
	public InputStream getFilePaymentJobStatus(String jobId, String accessToken)
			throws OpenApiException {
		if (jobId == null) {
			throw new OpenApiException(OpenApi.OPENAPI_FILE_NOT_FOUND_ERROR,
					"{'reason':'Job ID or targetFile is null.'}");
		}

		// authorization
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("Authorization", "Bearer " + accessToken);

		GetInsRequest getInsRequest = new GetInsRequest();
		getInsRequest.setCallUrl(resultFileUrl.concat("/" + jobId));
		getInsRequest.setHeader(paramMap);
		InputStream inputStream = null;

		try {
			inputStream = getInsRequest.executeRequest();
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenApiException(OpenApi.OPENAPI_JOB_EXECUTE_ERROR,
					e.getMessage());
		}
		return inputStream;
	}

	@Override
	public TransactionDetail getPaymentTransactionDetail(String tid,
			String accessToken) throws OpenApiException {
		// authorization
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("Authorization", "Bearer " + accessToken);

		GetRequest getRequest = new GetRequest();

		getRequest.setHeader(paramMap);
		getRequest.setCallUrl(txidInfoUrl.concat("/" + tid));

		String result = null;
		TransactionDetail transactionDetail = null;

		try {
			result = getRequest.executeRequest();
			transactionDetail = objectMapper.readValue(result,
					TransactionDetail.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenApiException(OpenApi.OPENAPI_JOB_EXECUTE_ERROR,
					e.getMessage());
		}
		return transactionDetail;
	}

	@Override
	public CancelResponse cancelPaymentTransaction(CancelRequest cancelRequest,
			String accessToken) throws OpenApiException {
		Map<String, String> paramMap = new HashMap<String, String>();
		logger.info("[CancelPayment] access Token -> " + accessToken);
		paramMap.put("Authorization", "Bearer " + accessToken);

		JsonPostRequest jsonPostRequest = new JsonPostRequest();
		jsonPostRequest.setHeader(paramMap);
		jsonPostRequest.setCallUrl(refundUrl);

		String result = null;
		CancelResponse cancelResponse = null;
		
		try {
			jsonPostRequest.setParameter(objectMapper
					.writeValueAsString(cancelRequest));
			result = jsonPostRequest.executeRequest();
			logger.info("[CancelPayment] Response -> " + result);
			cancelResponse = objectMapper.readValue(result,
					CancelResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenApiException(OpenApi.OPENAPI_JOB_EXECUTE_ERROR,
					e.getMessage());
		}
		
		return cancelResponse;
	}

	@Override
	public void setPropertyFile(String path) throws Exception {
		Properties props = new Properties();
		StringBuilder stringBuilder = new StringBuilder();

		if (path == null) {
			throw new OpenApiException(OpenApi.OPENAPI_PROPERTY_SETTING_ERROR,
					"{'reason':'Property path is null.'}");
		}

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(path);
			props.load(new BufferedInputStream(fis));

			String propsFileJobUrl = props.getProperty("openapi.file_payment_url");
			String propsResultFileUrl = props.getProperty("openapi.file_payment_info_url");
			String propsTxidInfoUrl = props.getProperty("openapi.payment_transaction_detail_url");
			String propsRefundUrl = props.getProperty("openapi.payment_cancel_url");
			
			if (propsFileJobUrl != null)
				fileJobUrl = propsFileJobUrl;
			
			if (propsResultFileUrl != null)
				resultFileUrl = propsResultFileUrl;
			
			if (propsTxidInfoUrl != null)
				txidInfoUrl = propsTxidInfoUrl;
			
			if (propsRefundUrl != null)
				refundUrl = propsRefundUrl;
			
			stringBuilder.append("[setPropertyFile] ").append(fileJobUrl)
					.append("|").append(resultFileUrl).append("|")
					.append(txidInfoUrl).append("|").append(refundUrl);
			logger.info(stringBuilder.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenApiException(OpenApi.OPENAPI_PROPERTY_SETTING_ERROR,
					"{'reason':'File creation is incorect!!'}");
		} finally {
			fis.close();
		}
	}

}
