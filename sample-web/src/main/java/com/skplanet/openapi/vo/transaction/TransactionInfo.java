package com.skplanet.openapi.vo.transaction;

public class TransactionInfo {

	private String resultCode;
	private String resultMessage;
	
	private Payer payer;
	private PaymentTransactionInfo paymentTransactionInfo;
	
	public String getResultCode() {
		return resultCode;
	}
	
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public String getResultMessage() {
		return resultMessage;
	}
	
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	public Payer getPayer() {
		return payer;
	}
	
	public void setPayer(Payer payer) {
		this.payer = payer;
	}
	
	public PaymentTransactionInfo getPaymentTransactionInfo() {
		return paymentTransactionInfo;
	}
	
	public void setPaymentTransactionInfo(
			PaymentTransactionInfo paymentTransactionInfo) {
		this.paymentTransactionInfo = paymentTransactionInfo;
	}
	
}
