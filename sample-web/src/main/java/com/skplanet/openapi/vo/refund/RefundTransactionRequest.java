package com.skplanet.openapi.vo.refund;

import com.skplanet.openapi.vo.transaction.PaymentMethods;

public class RefundTransactionRequest {

	private String tid;
	private String fulRefund;
	private String amount;
	private String note;

	private PaymentMethods paymentMethods;

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getFulRefund() {
		return fulRefund;
	}

	public void setFulRefund(String fulRefund) {
		this.fulRefund = fulRefund;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public PaymentMethods getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(PaymentMethods paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

}
