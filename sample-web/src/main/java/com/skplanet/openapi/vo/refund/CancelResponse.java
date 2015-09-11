package com.skplanet.openapi.vo.refund;

public class CancelResponse {

	private String result;
	private String reasonCode;
	private String message;
	private RefundTransactionRequest refundTransactionRequest;
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getReasonCode() {
		return reasonCode;
	}
	
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
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
