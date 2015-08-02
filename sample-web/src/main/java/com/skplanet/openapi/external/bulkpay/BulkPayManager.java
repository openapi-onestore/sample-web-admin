package com.skplanet.openapi.external.bulkpay;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.skplanet.openapi.external.bulkpay.BulkPayException.BulkPay;

public class BulkPayManager implements BulkPayInterface {
	
	private int threadPoolCount = 2;
	private ExecutorService jobExecutor = Executors.newFixedThreadPool(threadPoolCount);
	
	private String propertyPath = null;
	private String fileJobUrl = "http://172.21.60.141/v1/payment/fileJob";
	private String resultFileUrl = "http://172.21.60.141/v1/payment/job";
	private String txidInfoUrl = "http://172.21.60.141/v1/payment/transaction";	
	
	@Override
	public String createFilePayment(Map<String, String> paramMap) {
		
		BulkPayTransaction bulkPayTransaction = new BulkPayTransaction(paramMap);
		bulkPayTransaction.setCallUrl(fileJobUrl);
		Future<String> future = jobExecutor.submit(bulkPayTransaction);
		
		try {
			String result = future.get();
			
			if (result.equals("fail")) {
				return "{'result':'fail','reason':'Header or File path is not valid'}";
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "{'result':'fail','reason':'Transaction job execute failed'}";
		}
	}
	
	@Override
	public String getFilePaymentInfo(Map<String, String> paramMap) {
		// jobId, verifySign
		String jobId = paramMap.get("jobId");
		String verifySign = paramMap.get("verifySign");
		String accessToken = paramMap.get("accessToken");
		
		// authorization
		paramMap.clear();
		paramMap.put("accessToken", accessToken);

		String result = null;

		BulkPayResultTransaction bulkPayResultTransaction = new BulkPayResultTransaction(paramMap, jobId, verifySign);
		bulkPayResultTransaction.setCallUrl(resultFileUrl);
		Future<String> future = jobExecutor.submit(bulkPayResultTransaction);
		
		try {
			result = future.get();
		} catch (Exception e) {
			e.printStackTrace();
			result = "BulkPayResultTransaction error!";
		}
		
		return result;
	}

	@Override
	public String getPaymentTransactionDetail(Map<String, String> paramMap) {
		String txid = paramMap.get("tid");
		String accessToken = paramMap.get("accessToken");

		//authorization
		paramMap.clear();
		paramMap.put("accessToken", accessToken);

		String result = null;
		
		BulkPayTidInfoTransaction bulkPayTidInfoTransaction = new BulkPayTidInfoTransaction(paramMap, txid);
		bulkPayTidInfoTransaction.setCallUrl(txidInfoUrl);
		Future<String> future = jobExecutor.submit(bulkPayTidInfoTransaction);
		
		try {
			result = future.get();
		} catch (Exception e) {
			e.printStackTrace();
			result = "BulkPayTxidTransaction Error!";
		}
		
		return result;
	}
	
	@Override
	public String cancelPaymentTransaction(Map<String, String> paramMap) {
		
		return null;
	}
	
	public void setPropertyFile(String path) throws Exception {
		this.propertyPath = path;
		Properties props = new Properties();

		if (propertyPath == null) {
			throw new BulkPayException(BulkPay.BULK_PROPERTY_SETTING_ERROR,"Property path is null");
		}

		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(propertyPath);
			props.load(new BufferedInputStream(fis));
			fileJobUrl = props.getProperty("openapi.bulkjob_url");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BulkPayException(BulkPay.BULK_PROPERTY_SETTING_ERROR, "File creation is incorect!!");
		} finally {
			fis.close();
		}
		
	}

}
