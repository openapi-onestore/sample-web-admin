package com.skplanet.openapi.external.bulkpay;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import com.skplanet.openapi.external.bulkpay.BulkPayException.BulkPay;

public class BulkPayManager implements BulkPayInterface {

	private int threadPoolCount = 1;
	private ExecutorService jobExecutor = Executors.newFixedThreadPool(
			threadPoolCount, new ThreadFactory() {
				@Override
				public Thread newThread(Runnable runnable) {
					Thread bulkPayThread = Executors.defaultThreadFactory()
							.newThread(runnable);
					bulkPayThread.setName("bulkPayManager");
					bulkPayThread.setDaemon(true);
					return bulkPayThread;
				}
			});

	// Property values, uri is default setting
	private String propertyPath = null;
	private String fileJobUrl = "http://172.21.60.141/v1/payment/fileJob";
	private String resultFileUrl = "http://172.21.60.141/v1/payment/job";
	private String txidInfoUrl = "http://172.21.60.141/v1/payment/transaction";
	private String refundUrl = "http://172.21.60.141/v1/payment/refund";

	@Override
	public String createFilePayment(Map<String, String> paramMap)
			throws BulkPayException {

		BulkPayPostTransaction bulkPayPostTransaction = new BulkPayPostTransaction(
				paramMap);
		bulkPayPostTransaction.setCallUrl(fileJobUrl);
		bulkPayPostTransaction.setChunked(true);

		Future<String> future = jobExecutor.submit(bulkPayPostTransaction);

		try {
			String result = future.get();

			if (result.equals("fail")) {
				throw new BulkPayException(
						BulkPay.BULK_PAY_FILE_NOT_FOUND_ERROR,
						"{'reason':'Header or File path is not valid'}");
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BulkPayException(BulkPay.BULK_PAY_JOB_EXECUTE_ERROR,
					"{'reason':'Transaction job execute failed'}");
		}

	}

	@Override
	public String getFilePaymentInfo(Map<String, String> paramMap)
			throws BulkPayException {
		// jobId, verifySign
		String jobId = paramMap.get("jobId");
		String verifySign = paramMap.get("verifySign");
		String accessToken = paramMap.get("accessToken");

		// authorization
		paramMap.clear();
		paramMap.put("accessToken", accessToken);

		String result = null;

		BulkPayGetTransaction bulkPayGetTransaction = new BulkPayGetTransaction(
				paramMap);
		bulkPayGetTransaction.setCallUrl(resultFileUrl.concat("/" + jobId
				+ "?signCode=" + verifySign));

		Future<String> future = jobExecutor.submit(bulkPayGetTransaction);

		try {
			result = future.get();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BulkPayException(BulkPay.BULK_PAY_JOB_EXECUTE_ERROR,
					"{'reason':'Transaction job execute failed'}");
		}

		return result;
	}

	@Override
	public String getPaymentTransactionDetail(Map<String, String> paramMap)
			throws BulkPayException {
		String txid = paramMap.get("tid");
		String accessToken = paramMap.get("accessToken");

		// authorization
		paramMap.clear();
		paramMap.put("accessToken", accessToken);

		BulkPayGetTransaction bulkPayGetTransaction = new BulkPayGetTransaction(
				paramMap);
		bulkPayGetTransaction.setCallUrl(txidInfoUrl.concat("/" + txid));

		Future<String> future = jobExecutor.submit(bulkPayGetTransaction);

		String result = null;

		try {
			result = future.get();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BulkPayException(BulkPay.BULK_PAY_JOB_EXECUTE_ERROR,
					"{'reason':'Transaction job execute failed'}");
		}

		return result;
	}
	
	@Override
	public String cancelPaymentTransaction(Map<String, String> paramMap)
			throws BulkPayException {
		String jsonString = paramMap.get("jsonString");
		String accessToken = paramMap.get("accessToken");

		// authorization
		paramMap.clear();
		paramMap.put("accessToken", accessToken);

		BulkPayPostTransaction bulkPayPostTransaction = new BulkPayPostTransaction(paramMap);
		bulkPayPostTransaction.setCallUrl(refundUrl);
		bulkPayPostTransaction.setJsonBody(jsonString);

		Future<String> future = jobExecutor.submit(bulkPayPostTransaction);

		String result = null;

		try {
			result = future.get();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BulkPayException(BulkPay.BULK_PAY_JOB_EXECUTE_ERROR,
					"{'reason':'Transaction job execute failed'}");
		}
		
		return result;
	}
	
	public void setPropertyFile(String path) throws Exception {
		this.propertyPath = path;
		Properties props = new Properties();
		
		if (propertyPath == null) {
			throw new BulkPayException(BulkPay.BULK_PROPERTY_SETTING_ERROR,
					"Property path is null");
		}
		
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(propertyPath);
			props.load(new BufferedInputStream(fis));

			fileJobUrl = props.getProperty("openapi.bulkjob_url");
			resultFileUrl = props.getProperty("openapi.result_file_url");
			txidInfoUrl = props.getProperty("openapi.txid_info_url");
			refundUrl = props.getProperty("openapi.refund_url");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BulkPayException(BulkPay.BULK_PROPERTY_SETTING_ERROR,
					"File creation is incorect!!");
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
