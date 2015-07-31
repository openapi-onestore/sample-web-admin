package com.skplanet.openapi.vo.refund;

public class Links {

	private Payer payer;
	private RefundTransactionRequest refundTransactionRequest;
	private Links links;
	
	public Payer getPayer() {
		return payer;
	}
	
	public void setPayer(Payer payer) {
		this.payer = payer;
	}
	
	public RefundTransactionRequest getRefundTransactionRequest() {
		return refundTransactionRequest;
	}
	
	public void setRefundTransactionRequest(
			RefundTransactionRequest refundTransactionRequest) {
		this.refundTransactionRequest = refundTransactionRequest;
	}
	
	public Links getLinks() {
		return links;
	}
	
	public void setLinks(Links links) {
		this.links = links;
	}
	
}
