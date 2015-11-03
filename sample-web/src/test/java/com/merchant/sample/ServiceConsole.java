package com.merchant.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.skplanet.openapi.external.oauth.OAuthAccessToken;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuthManager;
import com.skplanet.openapi.external.oauth.OAuthManagerImpl;
import com.skplanet.openapi.external.oauth.OAuthManagingException;
import com.skplanet.openapi.external.payment.OpenApiException;
import com.skplanet.openapi.external.payment.OpenApiManager;
import com.skplanet.openapi.external.payment.OpenApiManagerImpl;
import com.skplanet.openapi.vo.payment.FilePaymentHeader;
import com.skplanet.openapi.vo.payment.FilePaymentResult;
import com.skplanet.openapi.vo.payment.TransactionDetail;
import com.skplanet.openapi.vo.refund.CancelRequest;
import com.skplanet.openapi.vo.refund.CancelResponse;
import com.skplanet.openapi.vo.refund.RefundTransactionRequest;

public class ServiceConsole {
	
	public static void main(String ... v) {
		String accessToken = null;
		
		// Get New AccessToken
		OAuthClientInfo oauthClientInfo = new OAuthClientInfo(
				"84xK38rx9iCrFRJVOynsRA0MT0o3LTs83OqDLEJf5g0=", "GS1qrhoHMJWpmS6QwLNaG5NcFWFqzh5TrmY5476a2nA=", "client_credentials");
		OAuthManager oauthManager = new OAuthManagerImpl(oauthClientInfo);
		try {
			OAuthAccessToken accessTokenObj = oauthManager.createAccessToken();
			accessToken = accessTokenObj.getAccessToken();
			System.out.println("AccessToken >" + accessToken);
			
		} catch (OAuthManagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Create File Payment
		OpenApiManager service = new OpenApiManagerImpl();
		FilePaymentHeader filePaymentHeader = new FilePaymentHeader();
		filePaymentHeader.setVerBulkPay("1");
		filePaymentHeader.setBid("skplanet");
		filePaymentHeader.setNotiUrl("");
		filePaymentHeader.setCntTotalTrans(Integer.toString(1));
		filePaymentHeader.setPriority("Instant");
		
		File file = new File("res/sample.pay");
		
		System.out.println(">>>" + file.getAbsolutePath() + ">>>" + file.exists());
		String jobId = null;
		try {
			
			FilePaymentResult result = 
					service.createFilePayment(filePaymentHeader, file, accessToken);
			
			System.out.println(result.getJobId() + "|" + result.getResultCode() + 
					"|" + result.getResultMsg() + "|" + result.getWaitingJobs());
			jobId = result.getJobId();
			
			// Get File Payment Job Result
			Thread.sleep(2000);
			File resFile = service.getFilePaymentJobStatus(jobId, accessToken);
			System.out.println("Path :" + resFile.getAbsolutePath());
			printFile(resFile);
			
			// Get Payment Transaction Details
			TransactionDetail txDetail = service.getPaymentTransactionDetail("tx_my00001111", accessToken);
			System.out.println(txDetail.getResultCode() + "|" + txDetail.getResultMsg() + 
					"|" + txDetail.getPayer());
						
			// Cancel Payment Transaction
			CancelRequest cancelRequest = new CancelRequest();
			RefundTransactionRequest refundTxReq = new RefundTransactionRequest();
			refundTxReq.setAmount(1000);
			refundTxReq.setTid("tx_my00001111");
			cancelRequest.setRefundTransactionRequest(refundTxReq);
						
			CancelResponse cancelRes = service.getCancelPaymentTransaction(cancelRequest, accessToken);
			System.out.println(">>>" + cancelRes.getResultCode() + "|" + cancelRes.getResultMsg());
			
			// Verify Transaction Notification
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printFile(File file) {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line = null;
			while((line = br.readLine()) != null) {
				System.out.println(">>>" + line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
