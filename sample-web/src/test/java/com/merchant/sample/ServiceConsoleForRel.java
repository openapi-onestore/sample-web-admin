package com.merchant.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import com.skplanet.openapi.external.framework.ManagerProducer;
import com.skplanet.openapi.external.oauth.OAuthAccessToken;
import com.skplanet.openapi.external.oauth.OAuthClientInfo;
import com.skplanet.openapi.external.oauth.OAuthManager;
import com.skplanet.openapi.external.payment.OpenApiException;
import com.skplanet.openapi.external.payment.OpenApiManager;
import com.skplanet.openapi.vo.payment.TransactionDetail;
import com.skplanet.openapi.vo.refund.CancelRequest;
import com.skplanet.openapi.vo.refund.CancelResponse;
import com.skplanet.openapi.vo.refund.RefundTransactionRequest;

public class ServiceConsoleForRel {

	public static void main(String[] args) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String accessToken = null;
		String path = null;
		String logPath = null;
		
		try {
			path = new ClassPathResource("properties/rel_config.properties").getFile().getAbsolutePath();
			logPath = new ClassPathResource("properties/log4j.properties").getFile().getAbsolutePath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// Get New AccessToken
		OAuthClientInfo oauthClientInfo = new OAuthClientInfo(
				"VTFqvgcLbyyFdG2wZtEzBgbRB++RAp4WLWURY7g8Rvg=", "dxHw4bH8HU0f0+pmVsuvvfATdD8OXr85nL1ZTkmdVig=", "client_credentials");
		OAuthManager oauthManager = ManagerProducer.getFactory(logPath).getOAuthManager(oauthClientInfo);
		
		try {
			oauthManager.setPropertyFile(path);
			OAuthAccessToken accessTokenObj = oauthManager.createAccessToken();
			accessToken = accessTokenObj.getAccessToken();
			System.out.println("AccessToken >>>" + accessToken);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Create File Payment
		OpenApiManager service = ManagerProducer.getFactory(logPath).getOpenApiManager();
		try {
			service.setPropertyFile(path);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		
		try {
			
			File resFile = new File("d:/samplefolder/sample-web/resFileinMerchant.txt");
			if (!resFile.createNewFile()) {
				System.exit(-1);
			}
			
			service.getFilePaymentJobStatus("20", resFile, accessToken);
			System.out.println("Path >>> " + resFile.getAbsolutePath());
			printFile(resFile);
			resFile.delete();			
			
		} catch (OpenApiException | IOException e) {
			e.printStackTrace();
		}
		
		boolean switchBool = false;
		
		if ( switchBool ) {
			String txid = "TSTORE0004_20151105183704751841158519958";
			
			// Get Payment Transaction Details
			TransactionDetail txDetail;
			try {
				txDetail = service.getPaymentTransactionDetail(txid, accessToken);
				System.out.println("Transaction detail result >>>" + txDetail.getResultCode() + "|" + txDetail.getResultMsg() + "|" + txDetail.getPayer().getAuthkey());			
				System.out.println(objectMapper.writeValueAsString(txDetail));
			} catch (OpenApiException | IOException e) {
				e.printStackTrace();
			}
			
			// Cancel Payment Transaction
			CancelRequest cancelRequest = new CancelRequest();
			RefundTransactionRequest refundTxReq = new RefundTransactionRequest();
			refundTxReq.setAmount(1000);
			refundTxReq.setTid(txid);
			cancelRequest.setRefundTransactionRequest(refundTxReq);
					
			CancelResponse cancelRes;
			try {
				cancelRes = service.cancelPaymentTransaction(cancelRequest, accessToken);
				System.out.println("Cancel response result >>> " + cancelRes.getResultCode() + "|" + cancelRes.getResultMsg());			
			} catch (OpenApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

		
	}
	
	public static void printFile(File file) {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line = null;
			while((line = br.readLine()) != null) {
				System.out.println(">>> " + line);
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
