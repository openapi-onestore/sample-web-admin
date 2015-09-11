package com.skplanet.openapi.vo.payment;

public class FilePaymentHeader {
	
	private String verBulkPay;
	private String bid;
	private String notiUrl;
	private String postbackUrl;
	private String cntTotalTrans;
	private String prioity;
	
	public String getVerBulkPay() {
		return verBulkPay;
	}
	public void setVerBulkPay(String verBulkPay) {
		this.verBulkPay = verBulkPay;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getNotiUrl() {
		return notiUrl;
	}
	public void setNotiUrl(String notiUrl) {
		this.notiUrl = notiUrl;
	}
	public String getPostbackUrl() {
		return postbackUrl;
	}
	public void setPostbackUrl(String postbackUrl) {
		this.postbackUrl = postbackUrl;
	}
	public String getCntTotalTrans() {
		return cntTotalTrans;
	}
	public void setCntTotalTrans(String cntTotalTrans) {
		this.cntTotalTrans = cntTotalTrans;
	}
	public String getPrioity() {
		return prioity;
	}
	public void setPrioity(String prioity) {
		this.prioity = prioity;
	}
	
}
