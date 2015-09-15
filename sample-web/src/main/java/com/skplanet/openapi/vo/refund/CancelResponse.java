package com.skplanet.openapi.vo.refund;

public class CancelResponse {

	private String resultCode;
	private String resultMsg;
	private String message;
	private RefundTransactionRequest refundTransactionRequest;
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public RefundTransactionRequest getRefundTransactionRequest() {
		return refundTransactionRequest;
	}
	
	public void setRefundTransactionRequest(
			RefundTransactionRequest refundTransactionRequest) {
		this.refundTransactionRequest = refundTransactionRequest;
	}
	
}
