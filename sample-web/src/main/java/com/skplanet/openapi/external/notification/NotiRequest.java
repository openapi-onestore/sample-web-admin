package com.skplanet.openapi.external.notification;

public class NotiRequest {

	private String listener_type;
	private String notify_version;
	private String status;
	private String reason_code;
	private String transaction_id;
	private String order_no;
	private String app_id;
	private String product_id;
	private String total_amount;
	private String pay_method1;
	private String pay_amount1;
	private String pay_method2;
	private String pay_amount2;
	private String product_name;
	private String update_time;
	private String verify_sign;
	
	public String getListener_type() {
		return listener_type;
	}
	public void setListener_type(String listener_type) {
		this.listener_type = listener_type;
	}
	public String getNotify_version() {
		return notify_version;
	}
	public void setNotify_version(String notify_version) {
		this.notify_version = notify_version;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason_code() {
		return reason_code;
	}
	public void setReason_code(String reason_code) {
		this.reason_code = reason_code;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getPay_method1() {
		return pay_method1;
	}
	public void setPay_method1(String pay_method1) {
		this.pay_method1 = pay_method1;
	}
	public String getPay_amount1() {
		return pay_amount1;
	}
	public void setPay_amount1(String pay_amount1) {
		this.pay_amount1 = pay_amount1;
	}
	public String getPay_method2() {
		return pay_method2;
	}
	public void setPay_method2(String pay_method2) {
		this.pay_method2 = pay_method2;
	}
	public String getPay_amount2() {
		return pay_amount2;
	}
	public void setPay_amount2(String pay_amount2) {
		this.pay_amount2 = pay_amount2;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getVerify_sign() {
		return verify_sign;
	}
	public void setVerify_sign(String verify_sign) {
		this.verify_sign = verify_sign;
	}
	
}
