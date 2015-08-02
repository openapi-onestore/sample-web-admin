package com.skplanet.openapi.vo.transaction;

public class PaymentTransactionInfo {
	
	private String status;
	private String reason;
	private String message;
	private String lastestUpdate;
	private String tid;
	private String amount;
	private String description;
	
	private Goods goods;
	private PaymentMethods paymentMethods;
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getLastestUpdate() {
		return lastestUpdate;
	}
	
	public void setLastestUpdate(String lastestUpdate) {
		this.lastestUpdate = lastestUpdate;
	}
	
	public String getTid() {
		return tid;
	}
	
	public void setTid(String tid) {
		this.tid = tid;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Goods getGoods() {
		return goods;
	}
	
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	
	public PaymentMethods getPaymentMethod() {
		return paymentMethods;
	}
	
	public void setPaymentMethod(PaymentMethods paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

}
