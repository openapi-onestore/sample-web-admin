package com.skplanet.openapi.external.bulkpay;


public class BulkPayException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public enum BulkPay {
		BULK_PROPERTY_SETTING_ERROR,
		BULK_PAY_JOB_EXECUTE_ERROR,
		BULK_PAY_FILE_NOT_FOUND_ERROR,
		UNKNOWN_ERROR;
		
		private String msg;
		private BulkPay() {
			msg = "Undefined";
		}
		private BulkPay(String custMsg) {
			this.msg = custMsg;
		}
		public String getMsg() {
			return msg;
		}
	}
	
	private BulkPay status = BulkPay.UNKNOWN_ERROR;
	
	public BulkPayException(String msg) {
		super(msg);
	}
	
	public BulkPayException(BulkPay control) {
		this.setStatus(control);
	}
	
	public BulkPayException(BulkPay control, String msg) {
		super(msg);
		this.setStatus(control);
	}
	
	public BulkPay getStatus() {
		return status;
	}

	public void setStatus(BulkPay status) {
		this.status = status;
	}
}
